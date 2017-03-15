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

    @POST("stockOut")
    Call<StockEntry> stockOut(@Body StockEntry stockEntry);

    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://lego-server.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
