package com.emse.warehouselego.legosupply;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Thomas Mons on 08/03/2017.
 */

public class ClientActivity extends ActionBarActivity {
    private RecyclerView recyclerView;

    private int nbreBleu = 10;
    private int nbreRouge = 10;
    private int nbreJaune = 10;
    private int nbreVert = 10;
    private int nbreNoir = 10;
    private int nbreBlanc = 10;

    private List<ObjectClient> products = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        setTitle("Page Client");



        ajouterProduits();

        recyclerView = (RecyclerView) findViewById(R.id.listClient);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        recyclerView.setAdapter(new AdapterClient(products));
    }

    private void ajouterProduits() {
        products.add(new ObjectClient("Bleu. En Stock : "+String.valueOf(nbreBleu),"http://static.fnac-static.com/multimedia/Images/FR/NR/24/97/55/5609252/1540-1/tsp20131226170027/Mini-boite-repas-a-8-plots-Lego-Bleu.jpg"));
        products.add(new ObjectClient("Rouge. En Stock : "+String.valueOf(nbreRouge),"http://static.fnac-static.com/multimedia/Images/FR/NR/e9/24/35/3482857/1540-1/tsp20131226170027/Mini-boite-repas-a-8-plots-Lego-Rouge.jpg"));
        products.add(new ObjectClient("Jaune. En Stock : "+String.valueOf(nbreJaune),"http://i2.cdscdn.com/pdt2/4/2/3/1/700x700/auc5706773400423/rw/lego-brique-de-rangements-empilable-jaune-8-plots.jpg"));
        products.add(new ObjectClient("Vert. En Stock : "+String.valueOf(nbreVert),"http://media.uaredesign.com/catalog/product/cache/1/image/9df78eab33525d08d6e5fb8d27136e95/b/o/boite-rangement-brick-8-lego-vert-1_1.jpg"));
        products.add(new ObjectClient("Noir. En Stock : "+String.valueOf(nbreNoir),"http://www.jouet-et-cie.com/produits/40041731/40041731_zoom.jpg"));
        products.add(new ObjectClient("Blanc. En Stock : "+String.valueOf(nbreBlanc),"https://www.charlieetrose.com/9714-thickbox_default/boite-de-rangement-empilable-lego-blanc-50cm.jpg"));
    }

     public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_client, menu);
         return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Intent pagecommandes = new Intent(ClientActivity.this,CommandesActivity.class);
        startActivity(pagecommandes);
        return super.onOptionsItemSelected(item);
    }

}

