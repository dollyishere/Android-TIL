package com.example.sampleprovider;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 상태 및 조회 결과를 출력
        textView = findViewById(R.id.textView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 스프링이었다면 비즈니스 로직인 서비스단의 메서드
                insertPerson();
            }
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queryPerson();
            }
        });

        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updatePerson();
            }
        });

        Button button4 = findViewById(R.id.button4);
        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletePerson();
            }
        });
    }

    /* insert 버튼 클릭 시, 컨텐트를 추가하기 위한 메서드 선언 */
    public void insertPerson() {
        println("insertPerson 호출됨");

        String uriString = "content://com.example.sampleprovider/person";
        Uri uri = new Uri.Builder().build().parse(uriString); // => 추가로 값 더해야 하므로 Uri.Builder().build().parse() 사용

        // 커서와 resolver 이용해서 uri 접근
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        String[] columns = cursor.getColumnNames();
        println("columns count -> " + columns.length);
        for (int i = 0; i < columns.length; i++) {
            println("#" + i + " : " + columns[i]);
        }

        // 추가할 데이터 저장 객체
        ContentValues values = new ContentValues();
        values.put("name", "john");
        values.put("age", 20);
        values.put("mobile", "010-1000-1000");


        // 실제 레코드를 추가하는 메서드
        uri = getContentResolver().insert(uri, values);
        println("insert 결과 -> " + uri.toString());
    }

    /* query(조회) 버튼 클릭 시, 요청 컨텐트를 조회하기 위한 메서드 선언 */
    public void queryPerson() {
        try {
            String uriString = "content://com.example.sampleprovider/person";
            Uri uri = new Uri.Builder().build().parse(uriString);

            String[] columns = new String[] {"name", "age", "mobile"};
            Cursor cursor = getContentResolver().query(uri, columns, null, null, "name ASC");
            println("query 결과 : " + cursor.getCount());

            int index = 0;
            while(cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex(columns[0]));
                int age = cursor.getInt(cursor.getColumnIndex(columns[1]));
                String mobile = cursor.getString(cursor.getColumnIndex(columns[2]));

                println("#" + index + " -> " + name + ", " + age + ", " + mobile);
                index += 1;
            }

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /* update 버튼 클릭 시, 컨텐트 수정을 위한 메서드 선언 */
    public void updatePerson() {
        String uriString = "content://com.example.sampleprovider/person";
        Uri uri = new Uri.Builder().build().parse(uriString);
        // update person set moblie='010-0000-0000' where moblie = '010-2323-2525'
        String selection = "mobile = ?"; // where mobile = '????'
        String[] selectionArgs = new String[] {"010-1000-1000"}; // 위의 ????에 들어갈 값

        // set moblie ='010-0000-000' : ContentValues 객체
        ContentValues updateValue = new ContentValues();
        updateValue.put("mobile", "010-2000-2000");

        int count = getContentResolver().update(uri, updateValue, selection, selectionArgs);
        println("update 결과 : " + count);
    }

    /* delete 버튼 클릭 시, 컨텐트를 삭제하기 위한 메서드 선언 */
    public void deletePerson() {
        String uriString = "content://com.example.sampleprovider/person";
        Uri uri = new Uri.Builder().build().parse(uriString);

        String selection = "name = ?"; // where name = '???????'
        String[] selectionArgs = new String[] {"john"}; // ???에 들어갈 값

        int count = getContentResolver().delete(uri, selection, selectionArgs);
        println("delete 결과 : " + count);
    }

    /* 상태 출력 메서드 선언 */
    public void println(String data) {
        textView.append(data + "\n");
    }
}
