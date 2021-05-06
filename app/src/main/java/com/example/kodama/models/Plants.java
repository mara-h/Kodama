package com.example.kodama.models;

public class Plants {

    private String plantName;
    //ce info mai trebuie

    public Plants() {}

    public Plants (String plantName) {
        this.plantName = plantName;
    }

    public String getPlantName() {
        return plantName;
    }

    //setter?
    public void setPlantName(String plantName) {
        this.plantName = plantName;
    }
}
