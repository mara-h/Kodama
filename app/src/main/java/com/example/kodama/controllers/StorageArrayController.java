package com.example.kodama.controllers;

import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.example.kodama.models.PlantCard;
import com.example.kodama.models.Plants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.function.Predicate;

public class StorageArrayController {
     private ArrayList<PlantCard> storedData = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void saveToStorage(SharedPreferences sharedPreferences, ArrayList<PlantCard> plantList, PlantCard plant) {
        if(verifyIfExists(plant,plantList)) {
            Predicate<PlantCard> condition = p->p.getName().matches(plant.getName());
            plantList.removeIf(condition);
        }
        plantList.add(plant);
        try {
            sharedPreferences.edit().putString("plants", ObjectSerializer.serialize(plantList)).apply(); // apply
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private boolean verifyIfExists(PlantCard plant, ArrayList<PlantCard> plantList) {
        return plantList.stream().filter(o->o.getName().matches(plant.getName())).findFirst().isPresent();
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
