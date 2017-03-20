package com.emse.warehouselego.legosupply.server.model;

import android.os.Parcel;
import android.os.Parcelable;

public class StockEntry implements Parcelable {
    @Override
    public String toString() {
        return "StockEntry{" +
                "id='" + id + '\'' +
                ", color='" + color + '\'' +
                '}';
    }

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

    protected StockEntry(Parcel in) {
        id = in.readString();
        color = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(color);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<StockEntry> CREATOR = new Parcelable.Creator<StockEntry>() {
        @Override
        public StockEntry createFromParcel(Parcel in) {
            return new StockEntry(in);
        }

        @Override
        public StockEntry[] newArray(int size) {
            return new StockEntry[size];
        }
    };
}