package com.example.kodama.models;

//import com.example.kodama.controllers.Searcheable;

import java.io.Serializable;

import ir.mirrajabi.searchdialog.core.Searchable;

public class PlantCard implements Searchable, Serializable {

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

    @Override
    public String getTitle() {
        return null;
    }
}