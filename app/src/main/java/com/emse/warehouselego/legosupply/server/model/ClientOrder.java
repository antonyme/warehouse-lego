package com.emse.warehouselego.legosupply.server.model;

import java.util.List;

public class ClientOrder {
    private String clientName;
    private List<OrderItem> toPrepare;
    private List<OrderItem> prepared;

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public List<OrderItem> getToPrepare() {
        return toPrepare;
    }

    public void setToPrepare(List<OrderItem> toPrepare) {
        this.toPrepare = toPrepare;
    }

    public List<OrderItem> getPrepared() {
        return prepared;
    }

    public void setPrepared(List<OrderItem> prepared) {
        this.prepared = prepared;
    }
}
