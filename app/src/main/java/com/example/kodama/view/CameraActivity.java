package com.example.kodama.view;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.kodama.R;
import com.example.kodama.controllers.Camera2BasicFragment;

public abstract class CameraActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        if (null == savedInstanceState) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, Camera2BasicFragment.newInstance())
                    .commit();
        }
    }
}