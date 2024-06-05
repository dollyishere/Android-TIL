package com.example.samplealbum;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        // 실제 장터에 올라간 앱에는 Toast 코드는 없을 듯.....
        AndPermission.with(this)
                .runtime()
                .permission(Permission.READ_EXTERNAL_STORAGE,Permission.WRITE_EXTERNAL_STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        showToast("허용된 권한 갯수 : " + permissions.size());
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        showToast("거부된 권한 갯수 : " + permissions.size());
                    }
                })
                .start();

    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }


    public void openGallery() {
        Intent intent = new Intent();
        // MINE 타입이 image로 시작하는 데이터를 가져오라는 의미가 됨
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);

        // 결과 받는 쪽
        // 101번은 우리가 만들어둔 쪽
        // 앨범에서 사진을 선택할 수 있는 화면을 띄우게 됨
        startActivityForResult(intent, 101);
    }

    // 요청 코드, 결과 코드, intent 데이터가 자동 전달됨
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 전달된 코드 값이 내가 보낸 코드 값과 같은 지 확인
        if(requestCode == 101) {
            // 결과 처리 값이 내부 상수(RESULT_OK)와 같은지 확인
            if(resultCode == RESULT_OK) {
                // 값을 INTENT로부터 꺼내게 되면 Uri 타입이 잡히게 됨
                // action이 content이기 때문
                Uri fileUri = data.getData();

                // 내 resolver 꺼내오기
                ContentResolver resolver = getContentResolver();

                try {
                    // 가져온 값을 그림으로 다시 변환해주기(bitmap 형태로)
                    InputStream instream = resolver.openInputStream(fileUri);
                    Bitmap imgBitmap = BitmapFactory.decodeStream(instream);
                    // 이 비트맵을 imageView에 세팅
                    imageView.setImageBitmap(imgBitmap);
                    // instream 종료
                    instream.close();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
