package com.example.doitmission8;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {
    public static final String KEY_FOR_SUBMENU = "menu";
    public static final int REQUEST_CODE_SUBMENU = 102;

    Button customerBtn;
    Button sellingBtn;
    Button productBtn;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        customerBtn = findViewById(R.id.customerBtn);
        sellingBtn = findViewById(R.id.sellingBtn);
        productBtn = findViewById(R.id.productBtn);

        intent = new Intent(getApplicationContext(), SubMenuActivity.class);

        customerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(KEY_FOR_SUBMENU, "customer");
                startActivityForResult(intent, REQUEST_CODE_SUBMENU);
            }
        });

        sellingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(KEY_FOR_SUBMENU, "selling");
                startActivityForResult(intent, REQUEST_CODE_SUBMENU);
            }
        });

        productBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(KEY_FOR_SUBMENU, "product");
                startActivityForResult(intent, REQUEST_CODE_SUBMENU);
            }
        });
    }
}