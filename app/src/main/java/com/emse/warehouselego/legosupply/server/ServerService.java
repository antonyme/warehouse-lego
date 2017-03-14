package com.emse.warehouselego.legosupply.server;


import com.emse.warehouselego.legosupply.server.model.ClientOrder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public interface ServerService {
    @GET("clientOrders")
    Call<List<ClientOrder>> clientOrders();

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://lego-server.herokuapp.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();
}
