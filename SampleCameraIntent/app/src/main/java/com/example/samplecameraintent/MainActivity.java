package com.example.samplecameraintent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;


import java.io.File;
import java.io.FileNotFoundException;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;

    File file;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

    }

    public void takePicture() {
        try {
            // 해당 파일명 가진 파일이 이미 존재한다면, 지우기
            file = createFile();
            if (file.exists()) {
                file.delete();
            }

            file.createNewFile();
        } catch(Exception e) {
            e.printStackTrace();
        }

        // 이 프로젝트를 구동시키기 위해서는 sdk 버전이 최소 24 이상이어야 함
        if(Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID, file);
        } else {
            uri = Uri.fromFile(file);
        }

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

        // 101번으로 요청
        startActivityForResult(intent, 101);
    }

    /* 카메라 앱으로 찍은 사진 이름을 어떻게 저장할 것인지? 어디에 저장할 것인지(파일 경로)를 지정하는 메서드 */
    private File createFile() {
        String filename = "capture.jpg";
        // 파일 경로(getFilesDir()) 구한 뒤 파일 이름 덧붙여서 파일 객체 생성한 후, return
        File outFile = new File(getFilesDir(), filename);
        Log.d("Main", "File path : " + outFile.getAbsolutePath());

        return outFile;
    }

    /* 미디어 스토어로부터 들어온 요청 값 처리 */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK) {
            try {
                // 불러온 이미지를 비트맵 형태로 디코딩 시키고 imageView에 세팅
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uri));
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


}
