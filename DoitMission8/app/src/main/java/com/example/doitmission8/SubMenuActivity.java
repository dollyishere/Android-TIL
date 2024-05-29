package com.example.doitmission8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SubMenuActivity extends AppCompatActivity {
    TextView textView;
    Intent intent;

    public static final String KEY_MENU_DATA = "menu";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_menu);

        textView = findViewById(R.id.textView);
        Button goToMenu = findViewById(R.id.button2);
        goToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        Button goToLogin = findViewById(R.id.button3);
        goToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Intent intent = getIntent();
        if (intent != null) {
            String menu = intent.getStringExtra(KEY_MENU_DATA);
            String menuText = "";
            switch (menu) {
                case "customer":
                    menuText = "고객 관리";
                    break;
                case "selling":
                    menuText = "매출 관리";
                    break;
                default:
                    menuText = "상품 관리";
                    break;
            }
            textView.setText(menuText);
        }


    }
}