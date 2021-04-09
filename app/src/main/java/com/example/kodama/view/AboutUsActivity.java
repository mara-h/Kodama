package com.example.kodama.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.example.kodama.R;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_us);

        ImageButton historyButton = (ImageButton) findViewById(R.id.historyButton);
        ImageButton helpButton = (ImageButton) findViewById(R.id.helpButton);
        ImageButton aboutUsButton = (ImageButton) findViewById(R.id.aboutUsButton);
        ImageButton homeButton = (ImageButton) findViewById(R.id.homeButton);



        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AboutUsActivity.this, HomeActivity.class));
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AboutUsActivity.this, HistoryActivity.class));
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AboutUsActivity.this, HelpActivity.class));
            }
        });

        aboutUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AboutUsActivity.this, AboutUsActivity.class));
            }
        });

        TextView aboutUsText = (TextView) findViewById(R.id.aboutUsText);
        TextView aboutUsTextHeading = (TextView) findViewById(R.id.aboutUsTextHeading);
        aboutUsTextHeading.setText("Our mission\n");
        aboutUsText.setText("    We are Mara & Oana, students in our third year of studies toward a bachelor’s degree in Computer Science. We decided to develop this app for our Computer Engeneering class in the hope that we would create something special that could be of use for many people. \n" +
                "    Our main goal is to help everyone interact with nature so we created an app which allows you to identify plants by photographing them. \n" +
                "    Have you ever stumbled upon flowers, trees and other plants without knowing their name? Surfing on the internet to find the answer may take hours, but with Kodama the plants can be recognized instantly. \n" +
                "    We hope you enjoy our application as much as we enjoy offering it to you. ");



        aboutUsText.setMovementMethod(new ScrollingMovementMethod());

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
