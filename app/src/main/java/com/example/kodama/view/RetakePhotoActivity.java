package com.example.kodama.view;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.kodama.R;

import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.label.TensorLabel;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

public class RetakePhotoActivity extends Activity {

    private static final String IMAGE_FILE_LOCATION = "image_file_location";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_retake_photo);


        ImageButton retakeButton = (ImageButton) findViewById(R.id.btn_retakepicture);
        ImageButton useButton = (ImageButton) findViewById(R.id.btn_usepicture);
        ImageButton rechooseButton = (ImageButton) findViewById(R.id.btn_rechoose);

        ImageView imageView = findViewById(R.id.pictureView);
        File imageFile = new File(getIntent().getStringExtra(IMAGE_FILE_LOCATION));

        retakeButton.setOnClickListener(new View.OnClickListener() {
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

        rechooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 12);
            }
        });

        imageView.setImageBitmap(BitmapFactory.decodeFile(imageFile.getAbsolutePath()));
    }

   //





}