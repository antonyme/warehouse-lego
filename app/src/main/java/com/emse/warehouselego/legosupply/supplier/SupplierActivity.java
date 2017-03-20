package com.emse.warehouselego.legosupply.supplier;

import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcV;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.emse.warehouselego.legosupply.NFCUtil;
import com.emse.warehouselego.legosupply.R;
import com.emse.warehouselego.legosupply.Util;
import com.emse.warehouselego.legosupply.server.ServerService;
import com.emse.warehouselego.legosupply.server.model.StockGroup;
import com.emse.warehouselego.legosupply.server.model.StockEntry;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SupplierActivity extends ListActivity {
    Context context;
    String logTag;
    public static final String ACTION_GET_STOCK = "orders.get";
    SupplierAdapter adapter;
    TextView headerTxt;
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter[] intentFiltersArray;
    String[][] techListsArray;
    private ScheduledExecutorService getStockScheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logTag = getResources().getString(R.string.app_name) + "/"
                + getResources().getString(R.string.title_activity_supplier);
        Log.i(logTag, "Hello Supplier");
        context = this;
        //set view
        setContentView(R.layout.activity_supplier);
        ListView lv = getListView();
        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.sp_header, lv, false);
        lv.addHeaderView(header, null, false);
        headerTxt = (TextView) findViewById(R.id.sp_header_txt);
        //set adapteur
        adapter = new SupplierAdapter(this, new ArrayList<StockGroup>());
        setListAdapter(adapter);
        // schedule a task to update stock list
        getStockScheduler = Executors.newScheduledThreadPool(2);
        getStockScheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                getStock();}
        }, 0, 5, TimeUnit.SECONDS);
        //set NFC
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if(nfcAdapter == null || !nfcAdapter.isEnabled()) {
            Log.e(logTag, "No NFC Adapter found");
        }
        Intent intent = new Intent(this, getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        IntentFilter ndef = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndef.addDataType("text/plain");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            e.printStackTrace();
        }
        intentFiltersArray = new IntentFilter[]{ndef};
        techListsArray = new String[][]{new String[]{NfcV.class.getName()}};
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null) {
            Parcelable[] rawMessages =
                    intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if(rawMessages != null && tag != null) {
                String id = NFCUtil.bytesToHex(tag.getId());
                String text = NFCUtil.readTag(rawMessages);
                if(text != null) {
                    Log.i(logTag, "Read tag: (id:" + id + ", text:" + text.substring(3) + ")");
                    sendStockIn(new StockEntry(id, text.substring(3)));
                }
            }
            else {
                Log.e(logTag, "Read tag: no tag data");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_GET_STOCK);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, filter);

        nfcAdapter.enableForegroundDispatch(this, pendingIntent, intentFiltersArray, techListsArray);
        Log.i(logTag, "resume");
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
        nfcAdapter.disableForegroundDispatch(this);
        Log.i(logTag, "pause");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        getStockScheduler.shutdown();
        Log.i(logTag, "destroy");
    }

    private void getStock() {
        ServerService serverService = ServerService.retrofit.create(ServerService.class);
        final Call<List<StockEntry>> call = serverService.stock();

        call.enqueue(new Callback<List<StockEntry>>() {
            @Override
            public void onResponse(Call<List<StockEntry>> call, Response<List<StockEntry>> response) {
                List<StockGroup> stockGroups = new ArrayList<>();
                if(response.isSuccessful()) {
                    if(response.body().size()>0) {
                        stockGroups = Util.stockEntriesToGroup(response.body());
                    }
                    else {
                        headerTxt.setText(R.string.empty_stock_list);
                    }
                }

                Intent intent = new Intent(ACTION_GET_STOCK);
                intent.putParcelableArrayListExtra("stockGroups",
                        (ArrayList<? extends Parcelable>) stockGroups);

                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
            @Override
            public void onFailure(Call<List<StockEntry>> call, Throwable t) {
                Log.e(logTag, "getStock failed: " + t.getMessage());
            }
        });
    }

    private void sendStockIn(StockEntry stockEntry) {
        ServerService serverService = ServerService.retrofit.create(ServerService.class);
        final Call<Void> call = serverService.stockIn(stockEntry);
        Log.i(logTag, "Send stockIn " + stockEntry.toString());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i(logTag, "stockIn -> " + String.valueOf(response.code()));
                CharSequence text;
                switch (response.code()) {
                    case 200:
                        text = "OK! Inventaire mis à jour!";
                        getStock();
                        break;
                    default:
                        text = "Oups, petit pb de connexion, réessaye";
                }
                Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(logTag, "sendStockIn failed: " + t.getMessage());
            }
        });
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().contentEquals(ACTION_GET_STOCK)) {
                adapter.clear();
                List<StockGroup> stockGroups = intent.getExtras().getParcelableArrayList("stockGroups");
                if(stockGroups != null) {
                    adapter.addAll(stockGroups);
                }
            }
        }
    };
}
