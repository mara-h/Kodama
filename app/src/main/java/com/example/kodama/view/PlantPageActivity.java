package com.example.kodama.view;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.kodama.R;
import com.example.kodama.controllers.FirebaseCallback;
import com.example.kodama.controllers.PlantsController;
import com.example.kodama.models.Plants;



public class PlantPageActivity extends AppCompatActivity {

    private String textToFind;
    private static final String PLANT_NAME = "plant_name";
    private String rename = "moarte";
    TextView plantLink;
    private PlantsController plantsController = new PlantsController();


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
                .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {

                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                            decorView.setSystemUiVisibility(flags);
                        }
                    }
                });
        setContentView(R.layout.plant_page);

        TextView plantName = (TextView) findViewById(R.id.plantNamePage);
        TextView plantScientificName = (TextView) findViewById(R.id.plantScientificNamePage);
        TextView plantDescription = (TextView) findViewById(R.id.plantDescriptionPage);
        plantLink = (TextView) findViewById(R.id.plantLinkPage);
        ImageButton plantBackButton = (ImageButton) findViewById(R.id.plantBackButton);

        textToFind = getIntent().getStringExtra(PLANT_NAME).toString();

        plantsController.readData(textToFind, new FirebaseCallback() {
                   @Override
                   public void onCallback(Plants plant) {
                       Log.d("TAG", plant.getName());
                       plantName.setText("Plant name: "+ plant.getName());
                       plantScientificName.setText("Scientific name: "+ plant.getScientificName());
                       plantDescription.setMovementMethod(new ScrollingMovementMethod());
                       plantDescription.setText("Description: " + plant.getDescription());
                       String text = "Find out more here";
                       SpannableString ss = new SpannableString(text);
                       UnderlineSpan underlineSpan = new UnderlineSpan();
                       ClickableSpan clickableSpan = new ClickableSpan() {
                           @Override
                           public void updateDrawState(@NonNull TextPaint ds) {
                               super.updateDrawState(ds);
                               ds.setUnderlineText(true);
                           }

                           @Override
                           public void onClick(@NonNull View view) {

                           }
                       };
                       ss.setSpan(clickableSpan,14, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                       ss.setSpan(underlineSpan, 14, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                       plantLink.setText(ss);
                       plantLink.setMovementMethod(LinkMovementMethod.getInstance());

                   }
               });
               //rename = plant.getName();
               //Log.e("plant get name log caca", plant.getName());
            System.out.println("AICIIIIIIIII");

          //System.out.println(plant.getName());
        //  Toast.makeText(PlantPageActivity.this, rename, Toast.LENGTH_SHORT).show();

        //plantName.setText(plant.getName());


        String text = "Find out more here";
//        SpannableString ss = new SpannableString(text);
//        UnderlineSpan underlineSpan = new UnderlineSpan();
//        ClickableSpan clickableSpan = new ClickableSpan() {
//            @Override
//            public void updateDrawState(@NonNull TextPaint ds) {
//                super.updateDrawState(ds);
//                ds.setUnderlineText(true);
//            }
//
//            @Override
//            public void onClick(@NonNull View view) {
//
//            }
//        };
//        ss.setSpan(clickableSpan,14, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        ss.setSpan(underlineSpan, 14, 18, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        plantLink.setText(ss);
//        plantLink.setMovementMethod(LinkMovementMethod.getInstance());

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