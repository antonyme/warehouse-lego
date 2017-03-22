package com.emse.warehouselego.legosupply.server.model;

import android.os.Parcel;
import android.os.Parcelable;

public class StockGroup implements Parcelable {
    private String color;
    private Integer quantity;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public StockGroup(String color, Integer quantity) {
        this.color = color;
        this.quantity = quantity;
    }

    public StockGroup(OrderGroup orderGroup) {
        this.color = orderGroup.getColor();
        this.quantity = orderGroup.getQuantity();
    }

    protected StockGroup(Parcel in) {
        color = in.readString();
        quantity = in.readByte() == 0x00 ? null : in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(color);
        if (quantity == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(quantity);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<StockGroup> CREATOR = new Parcelable.Creator<StockGroup>() {
        @Override
        public StockGroup createFromParcel(Parcel in) {
            return new StockGroup(in);
        }

        @Override
        public StockGroup[] newArray(int size) {
            return new StockGroup[size];
        }
    };
}
