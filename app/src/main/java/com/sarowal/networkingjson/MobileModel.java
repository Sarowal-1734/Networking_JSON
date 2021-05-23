package com.sarowal.networkingjson;

import android.graphics.Bitmap;

public class MobileModel {
    private String model;
    private String price;
    private Bitmap image;

    public Bitmap getImageBitmap() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
