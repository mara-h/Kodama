package com.example.kodama.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.kodama.R;

import java.io.File;

public class RetakePhotoActivity extends Activity {

    private static final String IMAGE_FILE_LOCATION = "image_file_location";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_retake_photo);


        Button retryButton = (Button) findViewById(R.id.btn_retakepicture);
        Button useButton = (Button) findViewById(R.id.btn_usepicture);

        ImageView imageView = findViewById(R.id.pictureView);
        File imageFile = new File(getIntent().getStringExtra(IMAGE_FILE_LOCATION));

        retryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RetakePhotoActivity.this, CameraActivity.class));
            }
        });

        useButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent viewPictureIntent = new Intent(RetakePhotoActivity.this, RecognitionActivity.class);
                viewPictureIntent.putExtra(IMAGE_FILE_LOCATION,  imageFile);
            }
        });

        imageView.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
    }



}