package com.emse.warehouselego.legosupply.warehouse;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.emse.warehouselego.legosupply.R;
import com.emse.warehouselego.legosupply.server.model.OrderItem;

import java.util.ArrayList;

class WarehouseAdapter extends ArrayAdapter<OrderItem> {
    private final Context context;
    private final ArrayList<OrderItem> values;

    WarehouseAdapter(Context context, ArrayList<OrderItem> values) {
        super(context, R.layout.wr_row, values);
        this.context = context;
        this.values = values;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.wr_row, parent, false);
        ImageView image = (ImageView) rowView.findViewById(R.id.wr_icon);
        TextView title = (TextView) rowView.findViewById(R.id.wr_title);
        TextView quantity = (TextView) rowView.findViewById(R.id.wr_quantity);
        title.setText(values.get(position).getColor());
        quantity.setText(context.getResources().getString(R.string.order_quantity,
                values.get(position).getQuantity()));

        return rowView;
    }
}
