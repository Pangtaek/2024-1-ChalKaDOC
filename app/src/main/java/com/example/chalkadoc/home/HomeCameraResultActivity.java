package com.example.chalkadoc.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chalkadoc.R;
import com.example.chalkadoc.partnership.PartnershipActivity;

public class HomeCameraResultActivity extends AppCompatActivity {
    private TextView tv_detailResult;
    private TextView tv_goToMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_camera_result);

        init();

        tv_detailResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeCameraResultActivity.this, HomeCameraDetailResultActivity.class);
                startActivity(intent);
                finish();
            }
        });

        tv_goToMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeCameraResultActivity.this, PartnershipActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void init(){
        tv_detailResult = findViewById(R.id.tv_detailResult);
        tv_goToMap = findViewById(R.id.tv_goToMap);
    }
}