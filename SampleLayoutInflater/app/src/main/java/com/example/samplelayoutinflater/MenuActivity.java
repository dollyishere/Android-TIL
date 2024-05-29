package com.example.samplelayoutinflater;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

public class MenuActivity extends AppCompatActivity {
    LinearLayout container;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        container = findViewById(R.id.container);
        button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                // 어떤 걸 넣을 건지, 어디에 넣을 건지, 새로 만들어진 걸 붙일건지, 아니면 불러만 두고 말 건지?
                inflater.inflate(R.layout.sub1, container, true);
                CheckBox checkBox = container.findViewById(R.id.checkBox);
                checkBox.setText("로딩되었어용");
            }
        });
    }
}