package com.example.receiver;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class SmsActivity extends AppCompatActivity {
    // 나중에 들어올 값들 세 개 미리 꺼내둠
    EditText editText;
    EditText editText2;
    EditText editText3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        editText3 = findViewById(R.id.editText3);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 현재 activity 종료
                finish();
            }
        });

        // intent 얻어내는 개념 =>
        // onCreate 내부기에 얻어낼 수 있음
        Intent passedIntent = getIntent();
        processIntent(passedIntent);
    }

    // 새로 전달된 Intent를 처리하기 위한 메서드
    @Override
    protected void onNewIntent(Intent intent) {
        processIntent(intent);

        super.onNewIntent(intent);
    }

    // 화면이 생성될 때 데이터가 들어오거나... onNewIntent로 다시 Intent 재활용하거나
    // 모든 케이스에서 이 메서드로 다시 돌아옴
    private void processIntent(Intent intent) {
        if (intent != null) {
            String sender = intent.getStringExtra("sender");
            String contents = intent.getStringExtra("contents");
            String receivedDate = intent.getStringExtra("receivedDate");

            editText.setText(sender);
            editText2.setText(contents);
            editText3.setText(receivedDate);
        }
    }

}
