package com.example.chalkadoc.navigation;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chalkadoc.R;
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
                        return true; // 각 case가 실행된 후에 switch 문을 종료하고자 return true를 추가합니다.

                    case 2:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, partnershipFragment)
                                .commit();
                        return true; // 각 case가 실행된 후에 switch 문을 종료하고자 return true를 추가합니다.

                    case 3:
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container, myPageFragment)
                                .commit();
                        return true; // 각 case가 실행된 후에 switch 문을 종료하고자 return true를 추가합니다.
                }
                return false; // default case, 만약 위의 case 중 어떤 것도 매치되지 않을 경우 false를 반환합니다.
            }
        });
    }


    // bottomNavigationView에서 선택한 메뉴를 반환한다.
    // 0 = error     1 = home    2 = association     3 = mypage
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

    // 초기화 메소드
    private void init(){
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        homeFragment = new HomeFragment();
        partnershipFragment = new PartnershipFragment();
        myPageFragment = new MyPageFragment();

        // FragmentManager를 통해 Fragment 트랜잭션을 시작합니다.
        getSupportFragmentManager().beginTransaction()
                // R.id.container에 해당하는 View 영역에 새로운 Fragment로 교체합니다.
                .replace(R.id.container, homeFragment)
                // 트랜잭션을 커밋하여 변경 사항을 적용합니다.
                .commit();
        ;
    }

    // Firebase 스토리지의 촬영 사진 폴더 안에 사진이 없으면 토스트 메시지 생성하는 메소드
    private void checkIfPhotosExist() {
        // FirebaseStorage 인스턴스 생성
        FirebaseStorage storage = FirebaseStorage.getInstance();
        // 촬영 사진이 있는 경로 설정
        StorageReference photosRef = storage.getReference().child("촬영사진");
        // 사진이 있는지 확인
        photosRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                // 촬영 사진 폴더 안에 파일이 있으면
                if (!listResult.getItems().isEmpty()) {
                    // 사진이 있음을 나타내는 처리를 수행
                    Toast.makeText(MainActivity.this, "촬영된 사진이 있습니다.", Toast.LENGTH_SHORT).show();
                } else {
                    // 사진이 없음을 나타내는 처리를 수행
                    Toast.makeText(MainActivity.this, "촬영된 사진이 없습니다.", Toast.LENGTH_SHORT).show();

                    // 앞으로 사진 촤영 페이지로 이동하는 기능 만들면됨
                }
            }
        });
    }
}