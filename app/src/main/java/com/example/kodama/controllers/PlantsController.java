package com.example.kodama.controllers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.kodama.models.Plants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;


public class PlantsController {
    Plants plantInfo = new Plants();

    public void readData(String textToFind, FirebaseCallback firebaseCallback) {
        Query query = FirebaseDatabase.getInstance().getReference("Plants").orderByChild("Name").equalTo(textToFind);
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    plantInfo.setName(snapshot.child(textToFind).child("Name").getValue(String.class));
                    plantInfo.setScientificName(snapshot.child(textToFind).child("Scientific name").getValue(String.class));
                    plantInfo.setDescription(snapshot.child(textToFind).child("Description").getValue(String.class));
                    plantInfo.setLink(snapshot.child(textToFind).child("Link").getValue(String.class));
                    firebaseCallback.onCallback(plantInfo);
                } else {
                    Log.e("TAG", "plant not found");
                }
            }

            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.e("firebase", "loadPost:onCancelled", error.toException());
            }

        });
    }

}






