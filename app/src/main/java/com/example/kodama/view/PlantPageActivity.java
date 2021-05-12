package com.example.kodama.view;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kodama.R;
import com.example.kodama.controllers.PlantsController;
import com.example.kodama.models.Plants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;


public class PlantPageActivity extends AppCompatActivity {

    private String textToFind;
    private static final String PLANT_NAME = "plant_name";
    private String rename = "moarte";

    private PlantsController plantsController = new PlantsController();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView plantName = (TextView) findViewById(R.id.plantName);

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        getWindow().getDecorView().setSystemUiVisibility(flags);
        final View decorView = getWindow().getDecorView();
        decorView
                .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            decorView.setSystemUiVisibility(flags);
                        }
                    }
                });
        setContentView(R.layout.plant_page);

        textToFind = getIntent().getStringExtra(PLANT_NAME).toString();

        Plants plant = new Plants();
               plant = plantsController.searchPlantFirebase(textToFind, PlantPageActivity.this);




        // Toast.makeText(getApplicationContext(),textToFind, Toast.LENGTH_SHORT).show();

//        databaseReference = FirebaseDatabase.getInstance().getReference("Plants");
//        databaseReference.addListenerForSingleValueEvent(valueEventListener);

//        Query query = FirebaseDatabase.getInstance().getReference("Plants").orderByChild("Name").equalTo(textToFind);
//        query.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    String plantNameDb = snapshot.child(textToFind).child("Name").getValue(String.class);
//                    String plantsScientificNameDb = snapshot.child(textToFind).child("Scientific name").getValue(String.class);
//                    String plantsScientificNameDb2 = snapshot.child(textToFind).child("Scientific name").getValue(String.class);
//                    // Toast.makeText(getApplicationContext(),plantNameDb, Toast.LENGTH_SHORT).show();
//
//                }
//                else {
//                    Toast.makeText(getApplicationContext(),"MORI IN PACE", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull @NotNull DatabaseError error) {
//                Log.e("firebase", "loadPost:onCancelled", error.toException());
//            }
//        });
//
//         //plantName.setText(rename);
//        // Toast.makeText(getApplicationContext(),rename, Toast.LENGTH_LONG).show();
    }
    public void onWindowFocusChanged(boolean hasFocus){
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();
        if(hasFocus){
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    |View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    |View.SYSTEM_UI_FLAG_FULLSCREEN
                    |View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
    }
}