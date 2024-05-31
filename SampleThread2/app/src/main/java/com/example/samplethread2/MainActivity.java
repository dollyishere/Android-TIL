package com.example.samplethread2;

import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    TextView textView;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.textView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BackgroundThread thread = new BackgroundThread();
                thread.start();
            }
        });

    }


    class BackgroundThread extends Thread {
        int value = 0;

        public void run() {
            for (int i = 0; i < 100; i++) {
                try {
                    Thread.sleep(1000);
                } catch(Exception e) {}

                value += 1;
                Log.d("Thread", "value : " + value);
                // 새롭게 생성되는 runnable 객체 내부의 run()
                // run() 내부에서 위젯에 접근해달라고 요청(행위)
                // post 계열 메서드로 보내게 되면 handler로 해당 메서드가 넘어가서 실행이 됨(일을 넘기는 것)
                // 이전의 방식은 스레드 내에서 일을 해서 끝난 결과를 handler에 전달한거고, 이건 일 자체를 핸들러에게 위임하는 것
                handler.post(new Runnable() {
                    public void run() {
                        textView.setText("value 값 : " + value);
                    }
                });
            }
        }
    }

}
