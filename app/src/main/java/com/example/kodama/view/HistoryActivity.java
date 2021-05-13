package com.example.kodama.view;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kodama.R;
import com.example.kodama.controllers.ClickListener;
import com.example.kodama.controllers.RecyclerViewAdapter;
import com.example.kodama.controllers.StorageArrayController;
import com.example.kodama.models.PlantCard;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    private StorageArrayController storageArrayController = new StorageArrayController();
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<PlantCard> plantList;
    private ArrayList<String> plantListString = new ArrayList<>();
    private static final String PLANT_NAME = "plant_name";
    private Dialog dialog;



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
        ImageButton searchButton = (ImageButton) findViewById(R.id.search_button_bun);

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
        convertToStringArray(plantList);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerViewAdapter = new RecyclerViewAdapter(plantList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        searchButton.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                dialog = new Dialog(HistoryActivity.this);
                                                dialog.setContentView(R.layout.dialog_searcheable_spinner);
                                                dialog.getWindow().setLayout(850, 800);
                                                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                                                dialog.show();
                                                ListView listView = dialog.findViewById(R.id.list_view);
                                                EditText editText = dialog.findViewById(R.id.edit_text);
                                                ArrayAdapter<String> adapter = new ArrayAdapter<>(HistoryActivity.this, android.R.layout.simple_list_item_1, plantListString);
                                                editText.addTextChangedListener(new TextWatcher() {
                                                    @Override
                                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                    }

                                                    @Override
                                                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                                                        adapter.getFilter().filter(s);
                                                    }

                                                    @Override
                                                    public void afterTextChanged(Editable s) {

                                                    }
                                                });
                                                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                    @Override
                                                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                                        Intent sendNameIntent = new Intent(HistoryActivity.this, PlantPageActivity.class);
                                                        sendNameIntent.putExtra(PLANT_NAME, adapter.getItem(position));
                                                        startActivity(sendNameIntent);
                                                        dialog.dismiss();
                                                    }
                                                });
                                            }
                                        });

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

    private void convertToStringArray(List<PlantCard> plantList) {
        for(int i = 0; i < plantList.size(); i++ ){
            plantListString.add(plantList.get(i).getName());
            Log.e("moarte", plantListString.get(i));
        }
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


    private void preparePlantsList(SharedPreferences sharedPreferences){
        plantList = storageArrayController.getStoredData(sharedPreferences);
    }
}
