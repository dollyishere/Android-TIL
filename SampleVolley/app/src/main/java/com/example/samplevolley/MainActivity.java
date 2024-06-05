package com.example.samplevolley;


import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    // 요청을 위한 URL 추출
    EditText editText;
    // 요청 결과 출력
    TextView textView;
    // 요청객체를 담기 위한 요청 큐(한 번 생성 후, 계속 재활용: static)
    // 일반적으로 한 액티비티 내에서만 사용하는 것이 아니라 앱 전체에서 사용함
    // 일반적으로 application 클래스 안에 넣어두거나 별도의 클래스를 하나 만들어서 사용함
    static RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 요청 버튼 클릭 시, 요청 객체를 생성하여, 요청 큐에 담는 메서드 호출
                makeRequest();
            }
        });

        // 요청 큐 객체 생성
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

    }

    /* 요청 객체를 생성하여, 요청 큐에 담는 메서드 선언: makeRequest() */
    public void makeRequest() {
        // 사용자 입력 요청 URL 추출
        String url = editText.getText().toString();

        Response.Listener<String> success = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // textView에 출력하는 메서드 호출(println())
                println("응답 -> " + response);

            }
        };

        // 문자열을 주고 받는 요청 객체 생성
        StringRequest request = new StringRequest(
                Request.Method.GET, // 전송방식 : GET
                url, // 요청 URL
                success,
                new Response.ErrorListener() { // 응답 실패 시, 이벤트 (이벤트 리스너 객체를 선언 후, 사용도 가능)
                    // textView에 출력하는 메서드 호출(println())
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        println("에러 -> " + error.getMessage());
                    }
                }
        ) {
            // POST 방식 때 요청에 대한 파라미터 설정
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                // 현재 GET 방식 사용하기에 내부 코드 생략
                return super.getParams();
            }
        };
        // 요청 객체의 cache 사용 여부 설정
        // 다양한 파라미터로 여러 번 요청할 때는 true, 아니면 false
        request.setShouldCache(false);
        // 요청 객체를 요청 큐에 저장
        requestQueue.add(request);
        // textView에 출력하는 메서드 호출(println())
        println("요청 보냄.");
    }

    /* textView에 출력하는 메서드 선언 */
    public void println(String data) {
        textView.append(data + "\n");
    }

}
