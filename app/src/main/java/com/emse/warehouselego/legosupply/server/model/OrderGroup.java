package com.emse.warehouselego.legosupply.server.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderGroup extends StockGroup implements Parcelable {
    Integer stock;

    public OrderGroup(String color, Integer quantity, Integer stock) {
        super(color,quantity);
        this.stock = stock;
    }

    protected OrderGroup(Parcel in) {
        super(in);
        stock = in.readByte() == 0x00 ? null : in.readInt();
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
        if(getQuantity() > stock) {
            setQuantity(stock);
        }
    }

    @Override
    public void setQuantity(Integer quantity) {
        if(quantity > stock) {
            super.setQuantity(stock);
        }
        else {
            super.setQuantity(quantity);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        if (stock == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeInt(stock);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<OrderGroup> CREATOR = new Parcelable.Creator<OrderGroup>() {
        @Override
        public OrderGroup createFromParcel(Parcel in) {
            return new OrderGroup(in);
        }

        @Override
        public OrderGroup[] newArray(int size) {
            return new OrderGroup[size];
        }
    };
}
