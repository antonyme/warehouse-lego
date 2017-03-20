package com.emse.warehouselego.legosupply;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.emse.warehouselego.legosupply.supplier.SupplierActivity;
import com.emse.warehouselego.legosupply.warehouse.WarehouseActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button clientButton = (Button) findViewById(R.id.clientBtn);
        clientButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, ClientActivity.class));
            }
        });
        Button warehouseButton = (Button) findViewById(R.id.warehouseBtn);
        warehouseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WarehouseActivity.class));
            }
        });
        Button supplierButton = (Button) findViewById(R.id.supplierBtn);
        supplierButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SupplierActivity.class));
            }
        });
    }
}

