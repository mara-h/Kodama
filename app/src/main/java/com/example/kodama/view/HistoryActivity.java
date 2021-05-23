package com.example.kodama.view;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kodama.R;
import com.example.kodama.controllers.ClickListener;
import com.example.kodama.controllers.RecyclerViewAdapter;
import com.example.kodama.controllers.StorageArrayController;
import com.example.kodama.models.PlantCard;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {

    private StorageArrayController storageArrayController = new StorageArrayController();
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private ArrayList<PlantCard> plantList;
    private static final String PLANT_NAME = "plant_name";
    private boolean fromRetake = false;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        getWindow().getDecorView().setSystemUiVisibility(flags);
        final View decorView = getWindow().getDecorView();
        decorView
                .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
                {

                    @Override
                    public void onSystemUiVisibilityChange(int visibility)
                    {
                        if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                        {
                            decorView.setSystemUiVisibility(flags);
                        }
                    }
                });
        setContentView(R.layout.history);

        SharedPreferences sharedPreferences = this.getSharedPreferences("PLANTS", Context.MODE_PRIVATE);
        ImageButton historyButton = (ImageButton) findViewById(R.id.historyButton);
        ImageButton helpButton = (ImageButton) findViewById(R.id.helpButton);
        ImageButton aboutUsButton = (ImageButton) findViewById(R.id.aboutUsButton);
        ImageButton homeButton = (ImageButton) findViewById(R.id.homeButton);
        historyButton.bringToFront();
        helpButton.bringToFront();
        aboutUsButton.bringToFront();
        homeButton.bringToFront();

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HistoryActivity.this, HomeActivity.class));
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HistoryActivity.this, HistoryActivity.class));
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HistoryActivity.this, HelpActivity.class));
            }
        });
        aboutUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HistoryActivity.this, AboutUsActivity.class));
            }
        });


        plantList = new ArrayList<>();
        preparePlantsList(sharedPreferences);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerViewAdapter = new RecyclerViewAdapter(plantList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        recyclerViewAdapter.setOnItemClickListener(new ClickListener<PlantCard>(){
            @Override
            public void onItemClick(PlantCard data) {
                Intent sendNameIntent = new Intent(HistoryActivity.this, PlantPageActivity.class);
                sendNameIntent.putExtra(PLANT_NAME, data.getName());
                startActivity(sendNameIntent);
            }
        });
        recyclerView.setAdapter(recyclerViewAdapter);
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


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void preparePlantsList(SharedPreferences sharedPreferences){
        plantList = storageArrayController.getStoredData(sharedPreferences);
    }


}
