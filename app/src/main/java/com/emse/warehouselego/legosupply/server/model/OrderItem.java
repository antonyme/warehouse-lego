package com.emse.warehouselego.legosupply.server.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderItem implements Parcelable {
    String color;
    Integer quantity;

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


    protected OrderItem(Parcel in) {
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
    public static final Parcelable.Creator<OrderItem> CREATOR = new Parcelable.Creator<OrderItem>() {
        @Override
        public OrderItem createFromParcel(Parcel in) {
            return new OrderItem(in);
        }

        @Override
        public OrderItem[] newArray(int size) {
            return new OrderItem[size];
        }
    };
}
