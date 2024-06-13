package com.example.chalkadoc.navigation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chalkadoc.R;
import com.example.chalkadoc.home.HomeCameraActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment;
    private PartnershipFragment partnershipFragment;
    private MyPageFragment myPageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        init();
        // Firebase 스토리지의 촬영 사진 폴더 안에 사진이 없으면 토스트 메시지 생성
        checkIfPhotosExist();



        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (getSelectedMenu(item)) {
                    case 0:
                        Log.d("Error", "시나리오에 없는 메뉴버튼을 눌렀음");
                        return false;

                    case 1:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, homeFragment)
                                .commit();
                        return true;

                    case 2:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, partnershipFragment)
                                .commit();
                        return true;

                    case 3:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, myPageFragment)
                                .commit();
                        return true;
                }
                return false;
            }
        });
    }

    private int getSelectedMenu(@NonNull MenuItem item) {
        int result = 0;

        if (item.getItemId() == R.id.home)
            result = 1;
        if (item.getItemId() == R.id.partnership)
            result = 2;
        if (item.getItemId() == R.id.mypage)
            result = 3;

        return result;
    }

    private void init(){
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        homeFragment = new HomeFragment();
        partnershipFragment = new PartnershipFragment();
        myPageFragment = new MyPageFragment();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, homeFragment)
                .commit();
    }

    private void checkIfPhotosExist() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference photosRef = storage.getReference().child("촬영사진");
        photosRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                // Firebase storage의 /촬영사진 경로에 사진이 있는 경우
                if (!listResult.getItems().isEmpty()) {

                }
                // Firebase storage의 /촬영사진 경로에 사진이 없는 경우 -> 촬영하도록 페이지 변경
                else {
                    Intent intent = new Intent(MainActivity.this, HomeCameraActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
