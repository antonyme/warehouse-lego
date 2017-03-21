package com.emse.warehouselego.legosupply.server;


import com.emse.warehouselego.legosupply.server.model.ClientOrder;
import com.emse.warehouselego.legosupply.server.model.StockEntry;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ServerService {
    @GET("clientOrders")
    Call<List<ClientOrder>> clientOrders();

    @POST("newClientOrder")
    Call<Void> newClientOrder(@Body ClientOrder clientOrder);

    @POST("stockOut")
    Call<Void> stockOut(@Body StockEntry stockEntry);

    @POST("stockIn")
    Call<Void> stockIn(@Body StockEntry stockEntry);

    @GET("stock")
    Call<List<StockEntry>> stock();

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://lego-server.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
