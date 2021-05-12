package com.example.kodama.view;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.kodama.R;
import com.example.kodama.controllers.FirebaseCallback;
import com.example.kodama.controllers.PlantsController;
import com.example.kodama.models.Plants;



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




    //   Plants plant = plantsController.searchPlantFirebase(textToFind, PlantPageActivity.this);

        plantsController.readData(textToFind, new FirebaseCallback() {
                   @Override
                   public void onCallback(Plants plant) {
                       Log.d("TAG", plant.getName());
                   }
               });
               //rename = plant.getName();
               //Log.e("plant get name log caca", plant.getName());
            System.out.println("AICIIIIIIIII");
          //System.out.println(plant.getName());
        //  Toast.makeText(PlantPageActivity.this, rename, Toast.LENGTH_SHORT).show();

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