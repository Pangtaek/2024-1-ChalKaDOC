package com.example.chalkadoc.common;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chalkadoc.R;

public class HospitalInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partnership_info);

        // 전달된 데이터를 받기 위해 Intent에서 Extra를 가져옵니다.
        String item = getIntent().getStringExtra("item");

        // item 값을 사용하여 UI를 업데이트 합니다.
        // TextView textView = findViewById(R.id.textView);
        // textView.setText(item);
    }
}