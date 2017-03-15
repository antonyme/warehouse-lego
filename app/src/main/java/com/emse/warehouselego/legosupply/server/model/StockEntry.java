package com.emse.warehouselego.legosupply.server.model;

public class StockEntry {
    private String id;
    private String color;

    public StockEntry(String id, String color) {
        this.id = id;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}
