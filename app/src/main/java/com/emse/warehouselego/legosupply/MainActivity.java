package com.emse.warehouselego.legosupply;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.emse.warehouselego.legosupply.client.ClientActivity;
import com.emse.warehouselego.legosupply.supplier.SupplierActivity;
import com.emse.warehouselego.legosupply.warehouse.WarehouseActivity;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Integer chosenRole = 0;
    private Spinner spinner;
    private EditText clientName;
    private Button startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        spinner = (Spinner) findViewById(R.id.ma_spinner);
        clientName = (EditText) findViewById(R.id.ma_client_name);
        startBtn = (Button) findViewById(R.id.ma_start);

        ArrayAdapter<CharSequence> spinAdapter = ArrayAdapter.createFromResource(this,
                R.array.roles, android.R.layout.simple_spinner_item);
        spinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(spinAdapter);
        spinner.setOnItemSelectedListener(this);

        startBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (chosenRole) {
                    case 0: //client
                        startActivity(new Intent(MainActivity.this, ClientActivity.class));
                        break;
                    case 1: //warehouse
                        startActivity(new Intent(MainActivity.this, WarehouseActivity.class));
                        break;
                    case 2: //supplier
                        startActivity(new Intent(MainActivity.this, SupplierActivity.class));
                        break;


                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        chosenRole = position;
        if(chosenRole == 0) {
            clientName.setVisibility(View.VISIBLE);
        }
        else {
            clientName.setVisibility(View.GONE);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

