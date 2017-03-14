package com.emse.warehouselego.legosupply.warehouse;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.emse.warehouselego.legosupply.R;
import com.emse.warehouselego.legosupply.server.ServerService;
import com.emse.warehouselego.legosupply.server.model.ClientOrder;
import com.emse.warehouselego.legosupply.server.model.OrderItem;
import com.emse.warehouselego.legosupply.warehouse.WarehouseAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WarehouseActivity extends ListActivity {
    Context context;
    public static final String ACTION_GET_ORDERS = "orders.get";
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
    protected void onResume() {
        super.onResume();

        IntentFilter orderGetFilter = new IntentFilter(ACTION_GET_ORDERS);
        LocalBroadcastManager.getInstance(this).registerReceiver(onOrdersReceived, orderGetFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(onOrdersReceived);
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

    private BroadcastReceiver onOrdersReceived = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            if(intent.getAction().contentEquals(ACTION_GET_ORDERS)) {
                adapter.clear();
                List<OrderItem> orders = intent.getExtras().getParcelableArrayList("orders");
                if(orders != null) {
                    adapter.addAll(orders);
                }
            }
        }
    };

}
