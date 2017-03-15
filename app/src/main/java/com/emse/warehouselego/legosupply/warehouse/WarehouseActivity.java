package com.emse.warehouselego.legosupply.warehouse;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.emse.warehouselego.legosupply.NFCUtil;
import com.emse.warehouselego.legosupply.R;
import com.emse.warehouselego.legosupply.server.ServerService;
import com.emse.warehouselego.legosupply.server.model.ClientOrder;
import com.emse.warehouselego.legosupply.server.model.OrderItem;
import com.emse.warehouselego.legosupply.server.model.StockEntry;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WarehouseActivity extends ListActivity {
    Context context;
    public static final String ACTION_GET_ORDERS = "orders.get";
    public static final String ACTION_TAG_DATA = "tag.data";
    WarehouseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("LegoSupply", "Hello");
        setContentView(R.layout.activity_warehouse);
        adapter = new WarehouseAdapter(this, new ArrayList<OrderItem>());
        setListAdapter(adapter);
        context = this;

        Button button = (Button) findViewById(R.id.fetchBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getOrders();
            }
        });
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
                    Log.i("LegoSupply", "Read tag: (id:" + id + ", text:" + text + ")");
//                    sendStockOut(new StockEntry(id, text));
//                    Intent newIntent = new Intent(ACTION_TAG_DATA);
//                    newIntent.putExtra("tagId", id);
//                    newIntent.putExtra("tagText", text.substring(3));
//                    LocalBroadcastManager.getInstance(context).sendBroadcast(newIntent);
//                    Log.i("LegoSupply", "broadcast sent!");
                }
            }
            else {
                Log.e("LegoSupply", "Read tag: no tag data");
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_GET_ORDERS);
        filter.addAction(ACTION_TAG_DATA);

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
    }

    private void getOrders() {
        ServerService serverService = ServerService.retrofit.create(ServerService.class);
        final Call<List<ClientOrder>> call = serverService.clientOrders();

        call.enqueue(new Callback<List<ClientOrder>>() {
            @Override
            public void onResponse(Call<List<ClientOrder>> call, Response<List<ClientOrder>> response) {
                List<OrderItem> orders = new ArrayList<>();
                if(response.isSuccessful() && response.body().size()>0) {
                    orders = response.body().get(0).getToPrepare();
                }

                Intent intent = new Intent(ACTION_GET_ORDERS);
                intent.putParcelableArrayListExtra("orders", (ArrayList<? extends Parcelable>) orders);

                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
            @Override
            public void onFailure(Call<List<ClientOrder>> call, Throwable t) {
                Log.e("LegoSupply", t.getMessage());
            }
        });
    }

    private void sendStockOut(StockEntry stockEntry) {
        ServerService serverService = ServerService.retrofit.create(ServerService.class);
        final Call<StockEntry> call = serverService.stockOut(stockEntry);

        call.enqueue(new Callback<StockEntry>() {
            @Override
            public void onResponse(Call<StockEntry> call, Response<StockEntry> response) {
                Log.i("LegoSupply", String.valueOf(response.code()));
            }

            @Override
            public void onFailure(Call<StockEntry> call, Throwable t) {

            }
        });
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i("LegoSupply", "In Broadcast receiver");
            if(intent.getAction().contentEquals(ACTION_GET_ORDERS)) {
                adapter.clear();
                List<OrderItem> orders = intent.getExtras().getParcelableArrayList("orders");
                if(orders != null) {
                    adapter.addAll(orders);
                }
            }
            else if(intent.getAction().contentEquals(ACTION_TAG_DATA)) {
                String id = intent.getStringExtra("tagId");
                String text = intent.getStringExtra("tagText");
                Log.i("LegoSupply", "Tag data received: (id:" + id + ", text:" + text + ")");
            }
        }
    };

}
