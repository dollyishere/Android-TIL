package com.example.doitmission8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    public static final int REQUEST_CODE_MENU = 101;
    public static final String KEY_PRESSED_BTN_DATA = "data";

    EditText editId;
    EditText editPassword;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editId = findViewById(R.id.editTextId);
        editPassword = findViewById(R.id.editTextPassword);
        loginBtn = findViewById(R.id.button);

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);
                if ((!editId.getText().toString().isEmpty()) && (!editPassword.getText().toString().isEmpty())) {
                    startActivityForResult(intent, REQUEST_CODE_MENU);
                } else {
                    Toast.makeText(getApplicationContext(), "모든 값을 입력해주세요.", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}