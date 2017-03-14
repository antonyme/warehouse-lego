package com.emse.warehouselego.legosupply.warehouse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.emse.warehouselego.legosupply.R;
import com.emse.warehouselego.legosupply.server.model.OrderItem;

import java.util.ArrayList;

public class WarehouseAdapter extends ArrayAdapter<OrderItem> {
    private final Context context;
    private final ArrayList<OrderItem> values;

    public WarehouseAdapter(Context context, ArrayList<OrderItem> values) {
        super(context, R.layout.warehouse_row, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.warehouse_row, parent, false);
        ImageView image = (ImageView) rowView.findViewById(R.id.wr_icon);
        TextView title = (TextView) rowView.findViewById(R.id.wr_title);
        TextView quantity = (TextView) rowView.findViewById(R.id.wr_quantity);
        title.setText(values.get(position).getColor());
        quantity.setText(values.get(position).getQuantity().toString());

        return rowView;
    }
}
