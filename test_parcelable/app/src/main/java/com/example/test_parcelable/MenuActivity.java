package com.example.test_parcelable;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {
    TextView textView;

    public static final String KEY_SIMPLE_DATA = "data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        textView = findViewById(R.id.textView);
        Button button = findViewById(
                R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 만약 main 액티비티에 값 전달 시
                Intent intent = new Intent();
                intent.putExtra("name", "mike"); // 생략 가능
                // 작업 성공 여부 및 intent 객체
                // 단순 화면 전환 시에는 생략 가능?
                setResult(RESULT_OK, intent);
                // 현 액티비티 종료
                finish();
            }
        });

        Intent intent = getIntent(); // "data" : SimpleData 객체
        processIntent(intent);
    }

    private void processIntent(Intent intent) {
        if (intent != null) {
            Bundle bundle = intent.getExtras(); // 반드시 bundle 타입의 변수로 지정
            SimpleData data = bundle.getParcelable(KEY_SIMPLE_DATA); // 키명: "data"
            if (intent != null) {
                textView.setText("전달 받은 데이터\nNumber : " + data.number + "\nMessage : " + data.message);
            }
        }
    }
}