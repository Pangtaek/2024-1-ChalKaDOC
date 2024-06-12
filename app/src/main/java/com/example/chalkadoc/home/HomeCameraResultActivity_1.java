package com.example.chalkadoc.home;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chalkadoc.R;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class HomeCameraResultActivity_1 extends AppCompatActivity {

    private ImageView imageView;
    private TextView resultTextView;
    private Interpreter tflite;

    private static final String TAG = "HomeCameraResultActivity_1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_camera_result_1);

        imageView = findViewById(R.id.iv_camera);
        resultTextView = findViewById(R.id.tv_eyes_result);

        String imageUriString = getIntent().getStringExtra("imageUri");
        if (imageUriString != null) {
            try {
                Uri imageUri = Uri.parse(imageUriString);
                Log.d(TAG, "Received imageUri: " + imageUri.toString());

                try (InputStream is = getContentResolver().openInputStream(imageUri)) {
                    if (is != null) {
                        Bitmap bitmap = BitmapFactory.decodeStream(is);
                        imageView.setImageBitmap(bitmap);
                        Log.d(TAG, "Image received and set successfully");

                        tflite = new Interpreter(loadModelFile());
                        Log.d(TAG, "TensorFlow Lite 모델 로드 성공");

                        String result = analyzeImage(bitmap);
                        resultTextView.setText(result);
                    } else {
                        throw new Exception("InputStream is null");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                resultTextView.setText("이미지 로드 실패");
                Log.e(TAG, "이미지 로드 실패", e);
            }
        } else {
            resultTextView.setText("이미지 데이터가 없습니다");
            Log.e(TAG, "이미지 URI가 없습니다");
        }

        TextView nextButton = findViewById(R.id.tv_detailResult);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사진을 DentalActivity로 전달하는 메서드 호출
                sendImageToDentalActivity();
            }
        });
    }

    // 사진을 DentalActivity로 전달하는 메서드
    private void sendImageToDentalActivity() {
        String imageUriString = getIntent().getStringExtra("imageUri");
        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);

            // DentalActivity로 전달할 Intent 생성
            Intent intent = new Intent(HomeCameraResultActivity_1.this, HomeCameraResultActivity_2.class);
            intent.putExtra("imageUri", imageUri.toString());

            // DentalActivity 실행
            startActivity(intent);
        } else {
            // 이미지 URI가 없는 경우 처리
            Toast.makeText(HomeCameraResultActivity_1.this, "이미지 데이터가 없습니다", Toast.LENGTH_SHORT).show();
        }
    }

    private MappedByteBuffer loadModelFile() throws Exception {
        AssetFileDescriptor fileDescriptor = getAssets().openFd("best-fp16.tflite");
        try (FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
             FileChannel fileChannel = inputStream.getChannel()) {
            long startOffset = fileDescriptor.getStartOffset();
            long declaredLength = fileDescriptor.getDeclaredLength();
            return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
        }
    }

    private String analyzeImage(Bitmap bitmap) {
        try {
            // 이미지 리사이즈
            int inputImageWidth = 320;
            int inputImageHeight = 320;
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, inputImageWidth, inputImageHeight, true);

            // 입력 ByteBuffer 생성
            ByteBuffer inputBuffer = ByteBuffer.allocateDirect(4 * inputImageWidth * inputImageHeight * 3);
            inputBuffer.order(ByteOrder.nativeOrder());

            int[] intValues = new int[inputImageWidth * inputImageHeight];
            resizedBitmap.getPixels(intValues, 0, inputImageWidth, 0, 0, inputImageWidth, inputImageHeight);

            for (int i = 0; i < intValues.length; ++i) {
                int val = intValues[i];
                inputBuffer.putFloat(((val >> 16) & 0xFF) / 255.0f);
                inputBuffer.putFloat(((val >> 8) & 0xFF) / 255.0f);
                inputBuffer.putFloat((val & 0xFF) / 255.0f);
            }

            // 모델의 출력 텐서 형상에 맞게 출력 배열 생성
            float[][][] output = new float[1][6300][14];

            // 모델 실행
            tflite.run(inputBuffer, output);
            Log.d(TAG, "TensorFlow Lite 모델 실행 성공");

            // Bitmap을 Canvas에 그리기 위한 준비
            Bitmap mutableBitmap = resizedBitmap.copy(Bitmap.Config.ARGB_8888, true);
            Canvas canvas = new Canvas(mutableBitmap);
            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2);

            Paint textPaint = new Paint();
            textPaint.setColor(Color.RED);
            textPaint.setTextSize(24);

            String[] labels = {"백내장", "포도막염", "익상편", "다래끼", "블랙헤드", "면포성여드름", "습진", "농포성여드름", "주사"};

            // 가장 높은 확률의 객체 찾기
            int maxIndex = -1;
            float maxProbability = -1.0f;

            for (int i = 0; i < 6300; i++) {
                float objectProbability = output[0][i][4]; // 객체 확률
                if (objectProbability > maxProbability) {
                    maxProbability = objectProbability;
                    maxIndex = i;
                }
            }

            if (maxIndex == -1) {
                return "유효한 예측 결과를 찾지 못했습니다.";
            }

            // 클래스 확률과 경계 상자 좌표 추출
            int classIndex = -1;
            float maxClassProbability = -1.0f;
            for (int j = 5; j < 14; j++) {
                float classProbability = output[0][maxIndex][j];
                if (classProbability > maxClassProbability) {
                    maxClassProbability = classProbability;
                    classIndex = j - 5; // 클래스 인덱스 조정
                }
            }

            if (classIndex == -1) {
                return "유효하지 않은 클래스 인덱스";
            }

            // 경계 상자 좌표 추출
            float centerX = output[0][maxIndex][0] * inputImageWidth;
            float centerY = output[0][maxIndex][1] * inputImageHeight;
            float width = output[0][maxIndex][2] * inputImageWidth;
            float height = output[0][maxIndex][3] * inputImageHeight;

            float left = centerX - (width / 2);
            float top = centerY - (height / 2);
            float right = centerX + (width / 2);
            float bottom = centerY + (height / 2);

            // 경계 상자와 클래스 이름 그리기
            canvas.drawRect(left, top, right, bottom, paint);
            canvas.drawText(labels[classIndex], left, top - 10, textPaint);

            imageView.setImageBitmap(mutableBitmap);
            return "예측: " + labels[classIndex];
        } catch (Exception e) {
            Log.e(TAG, "TensorFlow Lite 모델 실행 실패", e);
            return "분석 실패";
        }
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (tflite != null) {
            tflite.close();
        }
    }
}