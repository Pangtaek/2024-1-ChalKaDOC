package com.example.chalkadoc.common;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.chalkadoc.R;
import com.example.chalkadoc.navigation.AssociationFragment;
import com.example.chalkadoc.navigation.HomeFragment;
import com.example.chalkadoc.navigation.MyPageFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private HomeFragment homeFragment;
    private AssociationFragment associationFragment;
    private MyPageFragment myPageFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        init();

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
                                .replace(R.id.container, associationFragment)
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
        if (item.getItemId() == R.id.association)
            result = 2;
        if (item.getItemId() == R.id.mypage)
            result = 3;

        return result;
    }

    // 초기화 메소드
    private void init(){
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        homeFragment = new HomeFragment();
        associationFragment = new AssociationFragment();
        myPageFragment = new MyPageFragment();

        // FragmentManager를 통해 Fragment 트랜잭션을 시작합니다.
        getSupportFragmentManager().beginTransaction()
                // R.id.container에 해당하는 View 영역에 새로운 Fragment로 교체합니다.
                .replace(R.id.container, homeFragment)
                // 트랜잭션을 커밋하여 변경 사항을 적용합니다.
                .commit();
        ;
    }
}