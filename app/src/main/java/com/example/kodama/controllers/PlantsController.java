package com.example.kodama.controllers;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.kodama.models.Plants;
import com.example.kodama.view.RetakePhotoActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class PlantsController {

    Plants plantInfo;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference("plantInfo");

   // firebaseDatabase = FirebaseDatabase.getInstance();
  //  databaseReference = firebaseDatabase.getReference("plantInfo");


    private void addDataToFireBase(String name, RetakePhotoActivity recognitionActivity) {
        plantInfo.setPlantName(name);
        //altele daca mai e nevoie

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.setValue(plantInfo);
                //after adding this data we are showing toast message
                Toast.makeText(recognitionActivity, "data added", Toast.LENGTH_SHORT).show();

            }

            public void onCancelled(@NonNull DatabaseError error) {
                //if the data is not added or it is cancelled then we are displaying a failure toast message
                Toast.makeText(recognitionActivity, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }




}
