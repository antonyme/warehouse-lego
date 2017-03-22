package com.emse.warehouselego.legosupply.server.model;

import java.util.ArrayList;
import java.util.List;

public class ClientOrder {
    private String clientName;
    private List<StockGroup> toPrepare;
    private List<StockGroup> prepared;

    public ClientOrder() {
        prepared = new ArrayList<>();
        toPrepare = new ArrayList<>();
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public List<StockGroup> getToPrepare() {
        return toPrepare;
    }

    public void setToPrepare(List<StockGroup> toPrepare) {
        this.toPrepare = toPrepare;
    }

    public List<StockGroup> getPrepared() {
        return prepared;
    }

    public void setPrepared(List<StockGroup> prepared) {
        this.prepared = prepared;
    }
}
