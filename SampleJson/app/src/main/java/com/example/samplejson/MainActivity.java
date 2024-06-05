package com.example.samplejson;

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
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    // 요청 url 추출을 위함
    EditText editText;
    // 결과 출력을 위함
    TextView textView;

    // volley의 요청 객체 큐
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
                // 요청 큐에 요청 객체 저장 메서드 호출
                makeRequest();
            }
        });

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(getApplicationContext());
        }

    }

    /* 요청 객체 생성 후, 요청 큐에 저장 메서드 선언 */
    public void makeRequest() {
        String url = editText.getText().toString().trim(); // 문자열의 좌우 공백 제거

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        println("응답 -> " + response);

                        processResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        println("에러 -> " + error.getMessage());
                    }
                }
        ) { // post 방식 요청 시
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();

                return params;
            }
        };

        request.setShouldCache(false);
        requestQueue.add(request);
        println("요청 보냄.");
    }

    /* textView에 결과를 출력하는 메서드 선언 */
    public void println(String data) {
        textView.append(data + "\n");
    }

    /* json 데이터를 gson 라이브러리로 읽어서 처리하는 메서드 선언 */
    // 응답이 성공했을 떄 호출되는 메서드
    public void processResponse(String response) {
        // Gson 객체 생성
        Gson gson = new Gson();

        // gson 객체로부터 MovieList 객체 생성
        MovieList movieList = gson.fromJson(response, MovieList.class); // json을 자바 객체로 변환

        // MovieList 객체에서 MovieListResult 객체를 가져옴
        MovieListResult result = movieList.boxOfficeResult;

        // MovieListResult 객체에서 Movie 객체 리스트를 가져옴
        ArrayList<Movie> movies = result.dailyBoxOfficeList;
        // 결과 출력 메서드 호출
        println("영화정보의 수 : " + movieList.boxOfficeResult.dailyBoxOfficeList.size());

        // Movie 객체 리스트를 순회하며 각 Movie 객체를 처리
        for (Movie movie : movies) {
            // 각 Movie 객체의 필드를 출력 (또는 다른 작업 수행)
            println("Rank: " + movie.rank);
            println("Movie Name: " + movie.movieNm);
            // 다른 필드들에 대해서도 동일하게 출력 또는 작업 수행
        }
    }

}
