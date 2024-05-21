package com.example.chalkadoc.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.chalkadoc.R;

public class HomeCameraActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home_camera);

        Button cameraCheckbtn = findViewById(R.id.cameraCheckbtn);

        cameraCheckbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // 이동 페이지 추후 추가 예정
                Intent intent = new Intent(HomeCameraActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
    }
}