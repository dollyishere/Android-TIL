package com.example.samplethread;


import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    // 핸들러 객체 생성
    // 액티비티 내에 핸들러가 만들어진 상태
    MainHandler handler;

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

        handler = new MainHandler();
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

                // 메시지 보낼 수 있는 message 객체 handler에서 얻어옴
                Message message = handler.obtainMessage();
                Bundle bundle = new Bundle();
                bundle.putInt("value", value);
                message.setData(bundle);
                // 핸들러에 값 전달
                handler.sendMessage(message);
            }
        }
    }

    class MainHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // 번들 형태로 값을 뽑아옴
            Bundle bundle = msg.getData();
            int value = bundle.getInt("value");
            // 액티비티 내에서는 위젯을 건드릴 수 있기 때문에, 위젯을 건드리는 것이 가능함
            textView.setText("value 값 : " + value);
        }
    }

}
