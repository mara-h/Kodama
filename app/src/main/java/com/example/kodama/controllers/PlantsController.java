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
import com.google.firebase.database.annotations.NotNull;


public class PlantsController {

    Plants plantInfo;
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference = firebaseDatabase.getReference();

   // firebaseDatabase = FirebaseDatabase.getInstance();
  //  databaseReference = firebaseDatabase.getReference("plantInfo");



    public void addUserIdToFireBase(String plantName, String userId, RetakePhotoActivity retakePhotoActivity) {

        Query query = firebaseDatabase.getReference().orderByChild(plantName).limitToFirst(1);



       // plantInfo.setUserId(userId);

        Toast.makeText(retakePhotoActivity,query.toString() , Toast.LENGTH_SHORT).show();

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                databaseReference.setValue(plantInfo);
                //after adding this data we are showing toast message
                Toast.makeText(retakePhotoActivity, "data added", Toast.LENGTH_SHORT).show();

            }

            public void onCancelled(@NonNull DatabaseError error) {
                //if the data is not added or it is cancelled then we are displaying a failure toast message
                Toast.makeText(retakePhotoActivity, "Fail to add data " + error, Toast.LENGTH_SHORT).show();
            }
        });
    }



    public Plants searchPlantFirebase(String textToFind, PlantPageActivity plantPageActivity) {
        plantInfo = new Plants ();
        Query query = FirebaseDatabase.getInstance().getReference("Plants").orderByChild("Name").equalTo(textToFind);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    plantInfo.setName(snapshot.child(textToFind).child("Name").getValue(String.class));
                    plantInfo.setScientificName(snapshot.child(textToFind).child("Scientific name").getValue(String.class));
                    plantInfo.setDescription(snapshot.child(textToFind).child("Description").getValue(String.class));
                    plantInfo.setLink(snapshot.child(textToFind).child("Link").getValue(String.class));

//                    String plantNameDb = snapshot.child(textToFind).child("Name").getValue(String.class);
//                    String plantsScientificNameDb = snapshot.child(textToFind).child("Scientific name").getValue(String.class);
//                    String plantsScientificNameDb2 = snapshot.child(textToFind).child("Scientific name").getValue(String.class);
                    // Toast.makeText(getApplicationContext(),plantNameDb, Toast.LENGTH_SHORT).show();

                }
                else {
                    Toast.makeText(plantPageActivity,"plant not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Log.e("firebase", "loadPost:onCancelled", error.toException());
            }
        });
        return plantInfo;
        //plantName.setText(rename);
        // Toast.makeText(getApplicationContext(),rename, Toast.LENGTH_LONG).show();
    }

    }






