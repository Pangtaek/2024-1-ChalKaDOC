package com.example.chalkadoc.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.chalkadoc.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class PartnershipActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ListView listView;
    private View dragHandle;
    private LinearLayout.LayoutParams mapParams;
    private LinearLayout.LayoutParams listViewParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partnership);

        // Map Fragment 초기화
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // ListView 초기화 및 데이터 설정
        listView = findViewById(R.id.listView_list);
        String[] items = {"Item 1", "Item 2", "Item 3", "Item 4"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        // ListView 항목 클릭 리스너 설정
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 클릭된 항목의 데이터를 가져옵니다.
                String selectedItem = (String) parent.getItemAtPosition(position);

                // DetailActivity를 시작하고 선택된 항목의 데이터를 전달합니다.
                Intent intent = new Intent(PartnershipActivity.this, HospitalInfoActivity.class);
                intent.putExtra("item", selectedItem);
                startActivity(intent);
            }
        });

        // 드래그 핸들 초기화
        dragHandle = findViewById(R.id.drag_handle);
        mapParams = (LinearLayout.LayoutParams) findViewById(R.id.map).getLayoutParams();
        listViewParams = (LinearLayout.LayoutParams) listView.getLayoutParams();

        dragHandle.setOnTouchListener(new View.OnTouchListener() {
            private float initialY;
            private float initialMapWeight;
            private float initialListWeight;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        initialY = event.getRawY();
                        initialMapWeight = mapParams.weight;
                        initialListWeight = listViewParams.weight;
                        return true;

                    case MotionEvent.ACTION_MOVE:
                        float deltaY = initialY - event.getRawY();
                        float totalWeight = initialMapWeight + initialListWeight;
                        float newMapWeight = initialMapWeight + deltaY / v.getHeight();
                        float newListWeight = totalWeight - newMapWeight;

                        // 최소/최대 Weight 제한 설정
                        if (newMapWeight > 0 && newListWeight > 0) {
                            mapParams.weight = newMapWeight;
                            listViewParams.weight = newListWeight;
                            findViewById(R.id.map).setLayoutParams(mapParams);
                            listView.setLayoutParams(listViewParams);
                        }
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        // 기본 위치 설정 (예: 서울)
        LatLng seoul = new LatLng(37.5665, 126.9780);
        mMap.addMarker(new MarkerOptions().position(seoul).title("Marker in Seoul"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(seoul, 10));
    }
}
