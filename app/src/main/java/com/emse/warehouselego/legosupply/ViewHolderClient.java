package com.emse.warehouselego.legosupply;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.net.URL;

import static java.lang.System.load;

/**
 * Created by Thomas Mons on 08/03/2017.
 */

public class ViewHolderClient extends RecyclerView.ViewHolder{

    private TextView textViewView;
    private ImageView imageView;
    private Button info;
    private String zoomBrique;
    //WebView myWebView;



    public ViewHolderClient(final View itemView) {
        super(itemView);


        textViewView = (TextView) itemView.findViewById(R.id.text);
        imageView = (ImageView) itemView.findViewById(R.id.image);
        info = (Button) itemView.findViewById(R.id.boutonInfo);
        //myWebView = (WebView) itemView.findViewById(R.id.zoomWebView);

       info.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Uri uri = Uri.parse(zoomBrique);
               Intent intent = new Intent(Intent.ACTION_VIEW,uri);
               itemView.getContext().startActivity(intent);
           }
       });
    }

    public final class OkOnClickListener implements
            DialogInterface.OnClickListener {
        public void onClick(DialogInterface dialog, int which) {
        }
    }


    public void bind(ObjectClient myObject){
        textViewView.setText(myObject.getText());
        Picasso.with(imageView.getContext()).load(myObject.getImageUrl()).centerCrop().fit().into(imageView);
        zoomBrique = myObject.getImageUrl();
    }

}



