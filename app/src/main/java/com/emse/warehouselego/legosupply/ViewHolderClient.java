package com.emse.warehouselego.legosupply;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

/**
 * Created by Thomas Mons on 08/03/2017.
 */

public class ViewHolderClient extends RecyclerView.ViewHolder{

    private TextView textViewView;
    private ImageView imageView;
    private Button commander;
    EditText et;


    //itemView est la vue correspondante à 1 cellule
    public ViewHolderClient(final View itemView) {
        super(itemView);

        //c'est ici que l'on fait nos findView

        textViewView = (TextView) itemView.findViewById(R.id.text);
        imageView = (ImageView) itemView.findViewById(R.id.image);
        commander = (Button) itemView.findViewById(R.id.boutonCommander);


        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //new AlertDialog.Builder(itemView.getContext())
                //.setMessage("Test")
                //.show();
                //Toast toast = new Toast(itemView.getContext());
                //toast.setView(imageView);
                //toast.show();
            }
        });

        commander.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                LayoutInflater factory = LayoutInflater.from(itemView.getContext());
                final View alertDialogView = factory.inflate(R.layout.dialogcommand, null);
                et = (EditText) alertDialogView.findViewById(R.id.editTextDialogCommand);
                AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
                builder.setView(alertDialogView);
                builder.setPositiveButton("Test", new OkOnClickListener());
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }

    public final class OkOnClickListener implements
            DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
            String chaine = et.getText().toString();
            new AlertDialog.Builder(itemView.getContext())
                    .setMessage(String.valueOf(chaine))
                    .show();
        }
    }
    //puis ajouter une fonction pour remplir la cellule en fonction d'un MyObject
    public void bind(ObjectClient myObject){
        textViewView.setText(myObject.getText());
        Picasso.with(imageView.getContext()).load(myObject.getImageUrl()).centerCrop().fit().into(imageView);
    }
}

