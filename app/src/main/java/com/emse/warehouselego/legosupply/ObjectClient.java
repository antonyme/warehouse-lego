package com.emse.warehouselego.legosupply;

import android.view.View;

/**
 * Created by Thomas Mons on 08/03/2017.
 */

public class ObjectClient {
    private String text;
    private String imageUrl;

    public ObjectClient(String text, String imageUrl) {
        this.text = text;
        this.imageUrl = imageUrl;
    }

    //getters & setters
    public String getText() {

        return text;
    }

    public void setText(String text) {

        this.text = text;
    }

    public String getImageUrl() {

        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {

        this.imageUrl = imageUrl;
    }
}

