package com.emse.warehouselego.legosupply.client;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.emse.warehouselego.legosupply.R;
import com.emse.warehouselego.legosupply.server.model.OrderGroup;
import com.emse.warehouselego.legosupply.server.model.StockGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

class ClientAdapter extends ArrayAdapter<OrderGroup> {
    private final Context context;
    private final ArrayList<OrderGroup> values;

    ClientAdapter(Context context, ArrayList<OrderGroup> values) {
        super(context, R.layout.cl_row, values);
        this.context = context;
        this.values = values;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.cl_row, parent, false);
        TextView title = (TextView) rowView.findViewById(R.id.cl_title);
        TextView stockQty = (TextView) rowView.findViewById(R.id.cl_stock_qty);
        TextView orderQty = (TextView) rowView.findViewById(R.id.cl_order_qty);
        ImageView image = (ImageView) rowView.findViewById(R.id.cl_icon);
        ImageButton button = (ImageButton) rowView.findViewById(R.id.cl_add);

        title.setText(values.get(position).getColor());
        stockQty.setText(context.getResources().getString(R.string.order_quantity,
                values.get(position).getStock()));
        orderQty.setText(values.get(position).getQuantity().toString());
        int imageID = context.getResources()
                .getIdentifier("ic_lego_" + values.get(position).getColor(), "mipmap",
                        context.getPackageName());
        image.setImageResource(imageID);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                OrderGroup toUpdate = values.get(position);
                toUpdate.setQuantity(toUpdate.getQuantity() + 1);
                notifyDataSetChanged();
            }
        });

        return rowView;
    }

    void updateStock(List<StockGroup> stock) {
        for(Iterator<OrderGroup> iterator = values.iterator(); iterator.hasNext();) {
            OrderGroup orderGroup = iterator.next();
            if(!findAndUpdate(orderGroup, stock)) {
                iterator.remove();
            }
        }
        for(StockGroup stockGroup :  stock) {
            OrderGroup orderGroup = new OrderGroup(stockGroup.getColor(), 0, stockGroup.getQuantity());
            values.add(orderGroup);
        }
        notifyDataSetChanged();
    }

    private boolean findAndUpdate(OrderGroup orderGroup, List<StockGroup> stock) {
        for(StockGroup stockGroup : stock) {
            if(stockGroup.getColor().equalsIgnoreCase(orderGroup.getColor())) {
                orderGroup.setStock(stockGroup.getQuantity());
                stock.remove(stockGroup);
                return true;
            }
        }
        return false;
    }

    ArrayList<StockGroup> getOrder() {
        ArrayList<StockGroup> res = new ArrayList<>();
        for(OrderGroup orderGroup : values) {
            if(orderGroup.getQuantity() != 0) {
                    res.add(new StockGroup(orderGroup));
            }
        }
        return res;
    }

    void resetQuantities() {
        for(OrderGroup orderGroup : values) {
            orderGroup.setQuantity(0);
        }
    }
}
