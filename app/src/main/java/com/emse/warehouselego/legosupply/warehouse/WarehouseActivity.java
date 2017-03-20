package com.emse.warehouselego.legosupply.warehouse;

import android.app.ListActivity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.NfcV;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.emse.warehouselego.legosupply.NFCUtil;
import com.emse.warehouselego.legosupply.R;
import com.emse.warehouselego.legosupply.server.ServerService;
import com.emse.warehouselego.legosupply.server.model.ClientOrder;
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

public class WarehouseActivity extends ListActivity {
    Context context;
    String logTag;
    public static final String ACTION_GET_ORDERS = "orders.get";
    WarehouseAdapter adapter;
    TextView headerTxt;
    NfcAdapter nfcAdapter;
    PendingIntent pendingIntent;
    IntentFilter[] intentFiltersArray;
    String[][] techListsArray;
    private ScheduledExecutorService getOrderScheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logTag = getResources().getString(R.string.app_name) + "/"
                + getResources().getString(R.string.title_activity_warehouse);
        Log.i(logTag, "Hello Warehouse");
        context = this;
        //set view
        setContentView(R.layout.activity_warehouse);
        ListView lv = getListView();
        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.wr_header, lv, false);
        lv.addHeaderView(header, null, false);
        headerTxt = (TextView) findViewById(R.id.wr_header_txt);
        //set adapteur
        adapter = new WarehouseAdapter(this, new ArrayList<StockGroup>());
        setListAdapter(adapter);
        // schedule a task to update order list
        getOrderScheduler = Executors.newScheduledThreadPool(2);
        getOrderScheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {getOrders();}
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
        Log.i(logTag, "OK");
        if (intent != null) {
            Parcelable[] rawMessages =
                    intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            if(rawMessages != null && tag != null) {
                String id = NFCUtil.bytesToHex(tag.getId());
                String text = NFCUtil.readTag(rawMessages);
                if(text != null) {
                    Log.i(logTag, "Read tag: (id:" + id + ", text:" + text.substring(3) + ")");
                    sendStockOut(new StockEntry(id, text.substring(3)));
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
        filter.addAction(ACTION_GET_ORDERS);
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
        getOrderScheduler.shutdown();
        Log.i(logTag, "destroy");
    }

    private void getOrders() {
        ServerService serverService = ServerService.retrofit.create(ServerService.class);
        final Call<List<ClientOrder>> call = serverService.clientOrders();

        call.enqueue(new Callback<List<ClientOrder>>() {
            @Override
            public void onResponse(Call<List<ClientOrder>> call, Response<List<ClientOrder>> response) {
                List<StockGroup> orders = new ArrayList<>();
                if(response.isSuccessful()) {
                    if(response.body().size()>0) {
                        orders = response.body().get(0).getToPrepare();
                        headerTxt.setText(getResources().getString(R.string.order_list,
                                response.body().get(0).getClientName()));
                    }
                    else {
                        headerTxt.setText(R.string.empty_order_list);
                    }
                }

                Intent intent = new Intent(ACTION_GET_ORDERS);
                intent.putParcelableArrayListExtra("orders", (ArrayList<? extends Parcelable>) orders);

                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
            @Override
            public void onFailure(Call<List<ClientOrder>> call, Throwable t) {
                Log.e(logTag, "getOrders failed: " + t.getMessage());
            }
        });
    }

    private void sendStockOut(StockEntry stockEntry) {
        ServerService serverService = ServerService.retrofit.create(ServerService.class);
        final Call<Void> call = serverService.stockOut(stockEntry);
        Log.i(logTag, "Send stockOut " + stockEntry.toString());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i(logTag, "stockOut -> " + String.valueOf(response.code()));
                CharSequence text;
                switch (response.code()) {
                    case 200:
                        text = "OK! Inventaire et commande mis à jour!";
                        getOrders();
                        break;
                    case 201:
                        text = "Ce LEGO n'est pas utile à la commande, repose le!";
                        break;
                    case 202:
                        text = "Heu... Ce LEGO ne devrait pas être là, non?";
                        break;
                    default:
                        text = "Oups, petit pb de connexion, réessaye";
                }
                Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(logTag, "sendStockOut failed: " + t.getMessage());
            }
        });
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().contentEquals(ACTION_GET_ORDERS)) {
                adapter.clear();
                List<StockGroup> orders = intent.getExtras().getParcelableArrayList("orders");
                if(orders != null) {
                    adapter.addAll(orders);
                }
            }
        }
    };

}
