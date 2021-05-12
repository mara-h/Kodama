package com.example.kodama.controllers;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.kodama.models.Plants;
import com.example.kodama.view.PlantPageActivity;
import com.example.kodama.view.RetakePhotoActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
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


//    Plants plantInfo;
//    private String name, description, link, scientificName;
//    public Plants searchPlantFirebase(String textToFind, PlantPageActivity plantPageActivity) {
//        plantInfo = new Plants ();
//        Query query = FirebaseDatabase.getInstance().getReference("Plants").orderByChild("Name").equalTo(textToFind);
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    name = snapshot.child(textToFind).child("Name").getValue(String.class);
//                    Log.d("snapshot", snapshot.child(textToFind).child("Name").getValue(String.class));
//                    Log.d("snapshot-name", name);
//
//                    description = snapshot.child(textToFind).child("Description").getValue(String.class);
//                    scientificName = snapshot.child(textToFind).child("Scientific name").getValue(String.class);
//                    link = snapshot.child(textToFind).child("Link").getValue(String.class);
//
//
//                    plantInfo.setName(name);
//                    plantInfo.setDescription(description);
//                    plantInfo.setScientificName(scientificName);
//                    plantInfo.setLink(link);
//                    System.out.println(plantInfo.getName());
//
//
//
//
////                    plantInfo.setName(snapshot.child(textToFind).child("Name").getValue(String.class));
////                    plantInfo.setScientificName(snapshot.child(textToFind).child("Scientific name").getValue(String.class));
////                    plantInfo.setDescription(snapshot.child(textToFind).child("Description").getValue(String.class));
////                    plantInfo.setLink(snapshot.child(textToFind).child("Link").getValue(String.class));
//                }
//                else {
//                    Toast.makeText(plantPageActivity,"plant not found", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//                Log.e("firebase", "loadPost:onCancelled", error.toException());
//            }
//        });
//
//        //plantInfo = new Plants(name, description, link, scientificName);
//        return plantInfo;
//        //plantName.setText(rename);
//        // Toast.makeText(getApplicationContext(),rename, Toast.LENGTH_LONG).show();
//    }

    }






