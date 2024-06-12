package com.example.chalkadoc.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.example.chalkadoc.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class HomeCameraTakePictureActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int GALLERY_REQUEST_CODE = 2;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    private static final String TAG = "CameraActivity";

    private ImageView imageView;
    private ImageButton cameraButton;
    private ImageButton galleryButton;
    private Button analyzeButton;

    private Bitmap selectedBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_camera_take_picture);

        imageView = findViewById(R.id.iv_camera);
        cameraButton = findViewById(R.id.ib_take_picture);
        galleryButton = findViewById(R.id.ib_album);
        analyzeButton = findViewById(R.id.btn_cameraCheckStart);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(HomeCameraTakePictureActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    openCamera();
                } else {
                    ActivityCompat.requestPermissions(HomeCameraTakePictureActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
                }
            }
        });

        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        analyzeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openResultActivity();
            }
        });

        checkSelfPermission();
    }

    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, GALLERY_REQUEST_CODE);
    }

    private void openResultActivity() {
        if (selectedBitmap != null) {
            try {
                File imageFile = saveBitmapToFile(selectedBitmap);
                Uri imageUri = FileProvider.getUriForFile(this, "com.example.ai_face.fileprovider", imageFile);
                Intent intent = new Intent(HomeCameraTakePictureActivity.this, HomeCameraResultActivity_1.class);
                intent.putExtra("imageUri", imageUri.toString());
                Log.d(TAG, "Starting ResultActivity with imageUri: " + imageUri.toString());
                startActivity(intent);
            } catch (Exception e) {
                Log.e(TAG, "Error starting ResultActivity: ", e);
            }
        } else {
            Toast.makeText(this, "이미지가 선택되지 않았습니다", Toast.LENGTH_SHORT).show();
        }
    }

    private File saveBitmapToFile(Bitmap bitmap) throws Exception {
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imageFile = new File(storageDir, "selected_image.png");
        FileOutputStream fos = new FileOutputStream(imageFile);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
        fos.close();
        Log.d(TAG, "Image saved at: " + imageFile.getAbsolutePath());
        return imageFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case CAMERA_REQUEST_CODE:
                    selectedBitmap = (Bitmap) data.getExtras().get("data");
                    imageView.setImageBitmap(selectedBitmap);
                    Log.d(TAG, "Camera image selected");
                    break;
                case GALLERY_REQUEST_CODE:
                    Uri selectedImageUri = data.getData();
                    try {
                        InputStream is = getContentResolver().openInputStream(selectedImageUri);
                        selectedBitmap = BitmapFactory.decodeStream(is);
                        is.close();
                        imageView.setImageBitmap(selectedBitmap);
                        Log.d(TAG, "Gallery image selected");
                    } catch (Exception e) {
                        Log.e(TAG, "Error loading gallery image: ", e);
                    }
                    break;
            }
        } else {
            Log.d(TAG, "Activity result not OK, requestCode: " + requestCode + ", resultCode: " + resultCode);
        }
    }

    public void checkSelfPermission() {
        String temp = "";
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.READ_EXTERNAL_STORAGE + " ";
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            temp += Manifest.permission.WRITE_EXTERNAL_STORAGE + " ";
        }
        if (!TextUtils.isEmpty(temp)) {
            ActivityCompat.requestPermissions(this, temp.trim().split(" "), 1);
        } else {
            Toast.makeText(this, "권한이 모두 허용되었습니다", Toast.LENGTH_SHORT).show();
        }
    }
}
