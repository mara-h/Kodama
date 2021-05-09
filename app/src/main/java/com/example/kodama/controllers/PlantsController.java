package com.example.kodama.controllers;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.kodama.models.Plants;
import com.example.kodama.view.RetakePhotoActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


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




}
