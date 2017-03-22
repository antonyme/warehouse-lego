package com.emse.warehouselego.legosupply.client;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.emse.warehouselego.legosupply.R;
import com.emse.warehouselego.legosupply.Util;
import com.emse.warehouselego.legosupply.server.ServerService;
import com.emse.warehouselego.legosupply.server.model.ClientOrder;
import com.emse.warehouselego.legosupply.server.model.OrderGroup;
import com.emse.warehouselego.legosupply.server.model.StockEntry;
import com.emse.warehouselego.legosupply.server.model.StockGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientActivity extends ListActivity {
    Context context;
    String logTag;
    public static final String ACTION_GET_STOCK = "stock.get";
    ClientAdapter adapter;
    TextView headerTxt;
    String clientName;
    private ScheduledExecutorService getStockScheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        logTag = getResources().getString(R.string.app_name) + "/"
                + getResources().getString(R.string.title_activity_client);
        Log.i(logTag, "Hello Client");
        context = this;
        clientName = getIntent().getStringExtra("clientName");
        //set view
        setContentView(R.layout.activity_client);
        ListView lv = getListView();
        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.cl_header, lv, false);
        lv.addHeaderView(header, null, false);
        headerTxt = (TextView) findViewById(R.id.cl_header_txt);
        ImageButton headerBuy = (ImageButton) findViewById(R.id.cl_buy);
        headerBuy.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                sendNewClientOrder(adapter.getOrder());
            }
        });
        //set adapteur
        adapter = new ClientAdapter(this, new ArrayList<OrderGroup>());
        setListAdapter(adapter);
        // schedule a task to update stock list
        getStockScheduler = Executors.newScheduledThreadPool(2);
        getStockScheduler.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                getStock();}
        }, 0, 5, TimeUnit.SECONDS);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_GET_STOCK);
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver, filter);
        Log.i(logTag, "resume");
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver);
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
        final Call<List<StockGroup>> call = serverService.stockGroups();

        call.enqueue(new Callback<List<StockGroup>>() {
            @Override
            public void onResponse(Call<List<StockGroup>> call, Response<List<StockGroup>> response) {
                List<StockGroup> stockGroups = new ArrayList<>();
                if(response.isSuccessful()) {
                    if(response.body().size()>0) {
                        headerTxt.setText(getResources().getString(R.string.order_title, clientName));
                        stockGroups = response.body();
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
            public void onFailure(Call<List<StockGroup>> call, Throwable t) {
                Log.e(logTag, "getStock failed: " + t.getMessage());
            }
        });
    }

    private void sendNewClientOrder(List<StockGroup> stockGroups) {
        ServerService serverService = ServerService.retrofit.create(ServerService.class);
        ClientOrder clientOrder = new ClientOrder();
        clientOrder.setClientName(clientName);
        clientOrder.setToPrepare(stockGroups);
        final Call<Void> call = serverService.newClientOrder(clientOrder);
        Log.i(logTag, "Send newClientOrder " + clientOrder.toString());

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Log.i(logTag, "newClientOrder -> " + String.valueOf(response.code()));
                CharSequence text;
                switch (response.code()) {
                    case 200:
                        text = "OK! Commande reçu!";
                        adapter.resetQuantities();
                        getStock();
                        break;
                    case 201:
                        text = "Commande refusée!";
                        getStock();
                    default:
                        text = "Oups, petit pb de connexion, réessaye";
                }
                Toast toast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e(logTag, "newClientOrder failed: " + t.getMessage());
            }
        });
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().contentEquals(ACTION_GET_STOCK)) {
                List<StockGroup> stockGroups = intent.getExtras().getParcelableArrayList("stockGroups");
                if(stockGroups != null) {
                    adapter.updateStock(stockGroups);
                }
            }
        }
    };
}
