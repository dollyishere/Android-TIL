package com.example.sampleservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editText.getText().toString();

                Intent intent = new Intent(getApplicationContext(), MyService.class);

                intent.putExtra("command", "show");
                intent.putExtra("name", name);

                startService(intent);
            }
        });
    }

    // 재정의 할 때도 있고, 안할 때도 있음
    // 보통 onCreate까지는 무조건 실행됨
    // 화면이 이미 떠 있는 상태에서는 onCreate는 이미 종료된 상태이기에
    // 새로운 Intent를 처리하려면 onNewIntent 재정의가 필요함
    // 그렇지 않으면 기존 액티비티 상태를 유지하면서 새로운 데이터를 반영하는 것이 불가능함
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        processIntent(intent);
    }

    private void processIntent(Intent intent) {
        if (intent != null) {
            String command = intent.getStringExtra("command");
            String name = intent.getStringExtra("name");

            Toast.makeText(this, "SERVICE COMMAND  : " + command + ", " + "SERVICE NAME: " + name, Toast.LENGTH_LONG).show();
        }
    }
}