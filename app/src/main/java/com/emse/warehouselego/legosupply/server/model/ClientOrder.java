package com.emse.warehouselego.legosupply.server.model;

import java.util.List;

public class ClientOrder {
    String clientName;
    List<OrderItem> toPrepare;
    List<OrderItem> prepared;

    public List<OrderItem> getToPrepare() {
        return toPrepare;
    }
}
