package com.emse.warehouselego.legosupply.supplier;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.emse.warehouselego.legosupply.R;
import com.emse.warehouselego.legosupply.server.model.StockGroup;

import java.util.ArrayList;

class SupplierAdapter extends ArrayAdapter<StockGroup> {
    private final Context context;
    private final ArrayList<StockGroup> values;

    SupplierAdapter(Context context, ArrayList<StockGroup> values) {
        super(context, R.layout.sp_row, values);
        this.context = context;
        this.values = values;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.sp_row, parent, false);
        ImageView image = (ImageView) rowView.findViewById(R.id.sp_icon);
        TextView title = (TextView) rowView.findViewById(R.id.sp_title);
        TextView quantity = (TextView) rowView.findViewById(R.id.sp_quantity);
        title.setText(values.get(position).getColor());
        quantity.setText(context.getResources().getString(R.string.order_quantity,
                values.get(position).getQuantity()));

        return rowView;
    }
}
