package com.example.chalkadoc.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chalkadoc.R;

public class HomeCameraTakePictureActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_camera_take_picture);

        Button cameraCheckStart = findViewById(R.id.cameraCheckStart);

        cameraCheckStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeCameraTakePictureActivity.this, HomeCameraResultActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}
