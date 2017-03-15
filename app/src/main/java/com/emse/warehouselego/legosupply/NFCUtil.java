package com.emse.warehouselego.legosupply;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcV;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.emse.warehouselego.legosupply.warehouse.WarehouseActivity;

import java.io.IOException;
import java.util.Arrays;

public class NFCUtil {
    final private static char[] hexArray = "0123456789ABCDEF".toCharArray();

    public static String readTag(Parcelable[] rawMessages) {
        if (rawMessages != null && rawMessages.length >= 1) {
            Log.i("LegoSupply", "rawMessage size: " + rawMessages.length);
            NdefMessage messages = (NdefMessage) rawMessages[0];
            if (messages.getRecords().length >= 1) {
                NdefRecord record = messages.getRecords()[0];
                if(record.getTnf() == NdefRecord.TNF_WELL_KNOWN
                        && Arrays.equals(record.getType(), NdefRecord.RTD_TEXT)) {
                    return new String(record.getPayload());
                }
                else {
                    Log.e("LegoSupply", "Read tag: wrong tnf or type: " + record.toString());
                }
            }
            else {
                Log.e("LegoSupply", "Read tag: no records");
            }
        }
        else {
            Log.e("LegoSupply", "Read tag: empty NDEFMessage");
        }
        return null;
    }

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 3 - 1];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 3] = hexArray[v >>> 4];
            hexChars[j * 3 + 1] = hexArray[v & 0x0F];
            if(j < bytes.length - 1) hexChars[j * 3 + 2] = ':';
        }
        return new String(hexChars);
    }

    public static void processNfcIntent(Intent intent, LocalBroadcastManager lbm){
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if(tag != null){
            // set up read command buffer
            byte blockNo = 0; // block address
            byte[] id = tag.getId();
            byte[] readCmd = new byte[3 + id.length];
            readCmd[0] = 0x20; // set "address" flag (only send command to this tag)
            readCmd[1] = 0x20; // ISO 15693 Single Block Read command byte
            System.arraycopy(id, 0, readCmd, 2, id.length); // copy ID
            readCmd[2 + id.length] = blockNo; // 1 byte payload: block address

            NfcV tech = NfcV.get(tag);
            if (tech != null) {
                // send read command
                try {
                    tech.connect();
                    byte[] data = tech.transceive(readCmd);
                    // inform WarehouseActivity
                    Intent intent1 = new Intent(WarehouseActivity.ACTION_TAG_DATA);
                    intent1.putExtra("tagData", data);
                    lbm.sendBroadcast(intent1);
                    Log.i("LegoSupply", "Tag data received (NFCUtil): " + Arrays.toString(data));
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        tech.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
