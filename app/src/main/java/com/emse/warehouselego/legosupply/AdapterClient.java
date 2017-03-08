package com.emse.warehouselego.legosupply;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Thomas Mons on 08/03/2017.
 */

public class AdapterClient extends RecyclerView.Adapter<ViewHolderClient> {

    List<ObjectClient> list;

    //ajouter un constructeur prenant en entrée une liste
    public AdapterClient(List<ObjectClient> list) {
        this.list = list;
    }

    //cette fonction permet de créer les viewHolder
    //et par la même indiquer la vue à inflater (à partir des layout xml)
    @Override
    public ViewHolderClient onCreateViewHolder(ViewGroup viewGroup, int itemType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cell_cards,viewGroup,false);
        return new ViewHolderClient(view);

    }

    //c'est ici que nous allons remplir notre cellule avec le texte/image de chaque MyObjects
    @Override
    public void onBindViewHolder(ViewHolderClient myViewHolder, int position) {
        ObjectClient myObject = list.get(position);
        myViewHolder.bind(myObject);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

}

