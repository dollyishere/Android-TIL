package com.example.sampleattp;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    // 사용자가 요청할 URL 입력
    EditText editText;

    // 요청 결과데이터를 출력
    TextView textView;

    // 요청 결과 데이터를 textView에 출력할 핸들러 (안드로이드 정책 강화로 인해 스레드가 필수이기 때문)
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);

        // 요청 버튼 클릭
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사용자가 입력한 요청 url 추출
                final String urlStr = editText.getText().toString();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 요청할 메서드 호출(request())
                        request(urlStr);
                    }
                }).start();
            }
        });
    }

    /* http 요청 메서드 선언: request() */
    public void request(String urlStr) {
        // 대용량 문자열을 빨리 처리하기 위한 StringBuilder 객체
        StringBuilder output = new StringBuilder();
        try {
            // 요청 URL 객체
            URL url = new URL(urlStr);
            // URL 객체로부터 openConnecti() 메서드를 통해 접속, 데이터 요청
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            if (conn != null) { // 요청 주소 확인
                // 응답 대기 시간 설정
                conn.setConnectTimeout(10000);
                // 요청 방식 설정
                conn.setRequestMethod("GET");
                // 요청 결과를 허용
                conn.setDoInput(true);

                // 응답 코드 받기
                int resCode = conn.getResponseCode();
                Log.d("RESPONSE CODE: ", resCode + "");

                // 요청 결과 데이터를 한 줄씩 읽기 위한 객체: BufferedReader
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                // 한 줄 읽은 데이터를 저장
                String line = null;
                // 모든 데이터를 읽을 때까지 반복 처리
                while (true) {
                    line = reader.readLine();
                    if (line == null) { // 더 이상 읽을 데이터 없으면 반복 중지
                        break;
                    }

                    output.append(line + "\n"); // 읽은 데이터 output에 추가
                }
                // BufferedReader 객체 종료
                reader.close();
                // HttpUrlConnection 객체 종료
                conn.disconnect();
            }
        } catch (Exception ex) {
            println("예외 발생함 : " + ex.toString());
        }
        // textView에 결과 데이터를 출력하는 메서드 호출
        println("응답 -> " + output.toString());
    }

    /* 요청 결과를 위젯(textView)에 출력하는 메서드 선언 : println() */
    public void println(final String data) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                textView.append(data + "\n");
            }
        });

    }

}
