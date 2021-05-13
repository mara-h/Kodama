package com.example.kodama.controllers;

import android.content.SharedPreferences;
import android.util.Log;

import com.example.kodama.models.PlantCard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class StorageArrayController {
     private ArrayList<PlantCard> storedData = new ArrayList<>();

    public void saveToStorage(SharedPreferences sharedPreferences, ArrayList<PlantCard> plantList, PlantCard plant) {
        plantList.add(plant);
        try {
            sharedPreferences.edit().putString("plants", ObjectSerializer.serialize(plantList)).apply(); // apply
            Log.d("serialize", ObjectSerializer.serialize(plantList));
            Log.d("serialize", ObjectSerializer.serialize(plantList));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String, ?> map = sharedPreferences.getAll();
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            Log.d("map values", entry.getKey() + ": " + entry.getValue().toString());

        }
    }

    public ArrayList<PlantCard> getStoredData(SharedPreferences sharedPreferences){

        try {
            storedData = (ArrayList<PlantCard>) ObjectSerializer.deserialize(sharedPreferences.getString("plants", ObjectSerializer.serialize(new ArrayList<PlantCard>())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return storedData;
    }
}
