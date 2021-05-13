package com.example.kodama.models;

import java.io.Serializable;

public class PlantCard implements Serializable {

    private String title;
    private int image;

    public PlantCard(String title) {
        this.title = title;
    }

    public String getName() {
        return title;
    }

    public void setName(String title) {
        this.title = title;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}