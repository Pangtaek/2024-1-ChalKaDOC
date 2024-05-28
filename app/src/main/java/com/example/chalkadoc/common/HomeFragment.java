package com.example.chalkadoc.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.chalkadoc.R;


public class HomeFragment extends Fragment {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container,false);
        ImageView cameraImage = v.findViewById(R.id.iv_camera);

        cameraImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity().getApplicationContext(), HomeCameraActivity.class);
                startActivity(intent);
            }
        });

        ImageView allianceImage = v.findViewById(R.id.iv_partnership);

        allianceImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity().getApplicationContext(), PartnershipActivity.class);
                startActivity(intent);
            }
        });

        return v;
    }
}