package com.example.kodama.view;

import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.kodama.R;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.help);

        ImageButton historyButton = (ImageButton) findViewById(R.id.historyButton);
        ImageButton helpButton = (ImageButton) findViewById(R.id.helpButton);
        ImageButton aboutUsButton = (ImageButton) findViewById(R.id.aboutUsButton);
        ImageButton homeButton = (ImageButton) findViewById(R.id.homeButton);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HelpActivity.this, HomeActivity.class));
            }
        });

        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HelpActivity.this, HistoryActivity.class));
            }
        });

        helpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HelpActivity.this, HelpActivity.class));
            }
        });
        aboutUsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HelpActivity.this, AboutUsActivity.class));
            }
        });

        TextView textDetails = (TextView) findViewById(R.id.textDetails);
        textDetails.setText("Welcome!\n" +
                "Kodama helps you identify plants easily just by taking photos.\nHere are a few tricks to help you get started:\n" +
                "- Take centered photos of leafs or flowers\n" + "- Make sure that the plant is in focus\n" + "- Dont't snap plants that are far out of frame\n" +
                "- Don't have other objects (pot, hands, etc.) \n" + "- Have fun discovering new plants!\n");
        textDetails.setMovementMethod(new ScrollingMovementMethod());
    }
}