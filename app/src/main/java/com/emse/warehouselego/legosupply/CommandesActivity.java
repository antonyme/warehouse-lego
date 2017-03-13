package com.emse.warehouselego.legosupply;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

/**
 * Created by Thomas Mons on 08/03/2017.
 */

public class CommandesActivity extends Activity {
    private Button lancerCommande;

    private EditText etBleu;
    private EditText etRouge;
    private EditText etJaune;
    private EditText etVert;
    private EditText etNoir;
    private EditText etBlanc;

    private RadioButton boutonBleu;
    private RadioButton boutonRouge;
    private RadioButton boutonJaune;
    private RadioButton boutonVert;
    private RadioButton boutonNoir;
    private RadioButton boutonBlanc;


    private boolean isCheckedBleu = false;
    private boolean isCheckedRouge = false;
    private boolean isCheckedJaune = false;
    private boolean isCheckedVert = false;
    private boolean isCheckedNoir = false;
    private boolean isCheckedBlanc = false;

    private String resultatBleu;
    private String resultatRouge;
    private String resultatJaune;
    private String resultatVert;
    private String resultatNoir;
    private String resultatBlanc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commandes);

        lancerCommande = (Button) findViewById(R.id.lancerCommande);

        etBleu=(EditText) findViewById(R.id.editTextBleu);
        etRouge=(EditText) findViewById(R.id.editTextRouge);
        etJaune=(EditText) findViewById(R.id.editTextJaune);
        etVert=(EditText) findViewById(R.id.editTextVert);
        etNoir=(EditText) findViewById(R.id.editTextNoir);
        etBlanc=(EditText) findViewById(R.id.editTextBlanc);

        boutonBleu = (RadioButton) findViewById(R.id.radioButtonBleu);
        boutonBleu.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isCheckedBleu==false) {
                    etBleu.setVisibility(View.VISIBLE);
                    isCheckedBleu=true;

                }
                else {
                    etBleu.setVisibility(View.INVISIBLE);
                    boutonBleu.setChecked(false);
                    isCheckedBleu=false;
                }
            }
        });


        boutonRouge = (RadioButton) findViewById(R.id.radioButtonRouge);
        boutonRouge.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isCheckedRouge==false) {
                    etRouge.setVisibility(View.VISIBLE);
                    isCheckedRouge=true;
                }
                else {
                    etRouge.setVisibility(View.INVISIBLE);
                    boutonRouge.setChecked(false);
                    isCheckedRouge=false;
                }
            }
        });


        boutonJaune = (RadioButton) findViewById(R.id.radioButtonJaune);
        boutonJaune.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isCheckedJaune==false) {
                    etJaune.setVisibility(View.VISIBLE);
                    isCheckedJaune=true;
                }
                else {
                    etJaune.setVisibility(View.INVISIBLE);
                    boutonJaune.setChecked(false);
                    isCheckedJaune=false;
                }
            }
        });


        boutonVert = (RadioButton) findViewById(R.id.radioButtonVert);
        boutonVert.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isCheckedVert==false) {
                    etVert.setVisibility(View.VISIBLE);
                    isCheckedVert=true;
                }
                else {
                    etVert.setVisibility(View.INVISIBLE);
                    boutonVert.setChecked(false);
                    isCheckedVert=false;
                }
            }
        });


        boutonNoir = (RadioButton) findViewById(R.id.radioButtonNoir);
        boutonNoir.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isCheckedNoir==false) {
                    etNoir.setVisibility(View.VISIBLE);
                    isCheckedNoir=true;
                }
                else {
                    etNoir.setVisibility(View.INVISIBLE);
                    boutonNoir.setChecked(false);
                    isCheckedNoir=false;
                }
            }
        });


        boutonBlanc = (RadioButton) findViewById(R.id.radioButtonBlanc);
        boutonBlanc.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (isCheckedBlanc==false) {
                    etBlanc.setVisibility(View.VISIBLE);
                    isCheckedBlanc=true;
                }
                else {
                    etBlanc.setVisibility(View.INVISIBLE);
                    boutonBlanc.setChecked(false);
                    isCheckedBlanc=false;
                }
            }
        });

        lancerCommande.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resultatBleu = etBleu.getText().toString();
                resultatRouge = etRouge.getText().toString();
                resultatJaune = etJaune.getText().toString();
                resultatVert = etVert.getText().toString();
                resultatNoir = etNoir.getText().toString();
                resultatBlanc = etBlanc.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder((CommandesActivity.this));
                builder.setPositiveButton("OK", new OkOnClickListener());
                builder.setNegativeButton("Annuler", new CancelOnClickListener());
                builder.setMessage("Résumé Commande\n Bleu : "+String.valueOf(resultatBleu)+"\nRouge : "+String.valueOf(resultatRouge)+"\nJaune : "
                        +String.valueOf(resultatJaune)+"\nVert : "+String.valueOf(resultatVert)+"\nNoir : "+String.valueOf(resultatNoir)+"\nBlanc : "+String.valueOf(resultatBlanc));
                builder.show();
            }
        });
    }

    public final class OkOnClickListener implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
            new AlertDialog.Builder(CommandesActivity.this).setMessage("Commande envoyée").show();
        }
    }

    public final class CancelOnClickListener implements DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {

        }
    }
}
