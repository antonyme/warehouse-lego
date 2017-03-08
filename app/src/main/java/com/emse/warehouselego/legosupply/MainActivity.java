package com.emse.warehouselego.legosupply;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button boutonClient = (Button) findViewById(R.id.boutonClient);
        boutonClient.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent pageclient = new Intent(MainActivity.this, ClientActivity.class);
                startActivity(pageclient);
            }
        });
    }
}

