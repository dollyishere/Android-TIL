package com.example.sampledatabase;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    // 데이터베이스 이름, 테이블 이름 추출
    EditText editText;  // 데이터베이스 이름(~.db)
    EditText editText2; // 테이블 이름
    // 상태 출력
    TextView textView;

    // MainActivity는 Context를 상속받아 선언됨...
    SQLiteDatabase database;
    // 생성된 테이블명 저장 변수
    String tableName;

    // db helper
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        editText2 = findViewById(R.id.editText2);
        textView = findViewById(R.id.textView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 입력된 db명 추출
                String databaseName = editText.getText().toString();
                // db 생성 메서드 호출
                createDatabase(databaseName);
            }
        });

        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 입력된 테이블명 호출
                tableName = editText2.getText().toString();
                // 테이블 생성 메서드 호출
                createTable(tableName);
                // 레코드 삽입
                insertRecord();
            }
        });

        Button button3 = findViewById(R.id.button3);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                executeQuery();
            }
        });
    }


    /* 입력된 값(name)을 통한 데이터베이스 생성 관련 개발자 정의 메서드 */
    private void createDatabase(String name) {
        println("createDatabase 호출됨.");
        // 해당 이름을 가진 데이터베이스가 없으면 만들고, 있으면 불러오기
        // 두번째 인자값이 MODE_PRIVATE이므로 이 데이터베이스는 이 앱만 사용 가능하도록 설정
        // 데이터베이스 생성 시에는 값 돌려받을 필요 없으므로 세번째 인자값은 null 값
        // database = openOrCreateDatabase(name, MODE_PRIVATE, null);

        // 헬퍼를 만들어도, 데이터베이스 파일이 바로 만들어지는 것은 아님
        dbHelper = new DatabaseHelper(this);
        // 이하 코드 실행한 시점에서 실제로 쓰기 위한 데이터베이스가 만들어짐
        // => 정확히는, 데이터베이스가 파일로 만들어지는 시점이라 할 수 있음
        // Readable도 있는데 데이터베이스를 읽기 용도로 사용할 때 주로 쓰임
        database = dbHelper.getWritableDatabase();

        println("데이터베이스 생성함 : " + name);
    }

    /* 입력된 값(name)을 통한 테이블 생성 관련 개발자 정의 메서드 */
    private void createTable(String name) {
        println("createTable 호출됨.");

        // 만약 기존 데이터베이스가 존재하지 않는다면, 해당 메서드를 빠져나감
        if (database == null) {
            println("데이터베이스를 먼저 생성하세요.");
            return;
        }

        // 테이블 설정 관련 SQL문 execSQL로 지정
        // 기존에 테이블이 존재하면 생성하지 말고, 없으면 생성(create table if not exists) => 외우기!
        // database.execSQL() 통해 해당 쿼리 실행
        database.execSQL("create table if not exists " + name + "("
                + " _id integer PRIMARY KEY autoincrement, "
                + " name text, "
                + " age integer, "
                + " mobile text)");

        println("테이블 생성함 : " + name);
    }

    /* 레코드를 추가하는 메서드 선언  */
    private void insertRecord() {
        println("insertRecord 호출됨.");

        // 만약 기존 데이터베이스이나 테이블이 존재하지 않는다면, 해당 메서드를 빠져나감
        if (database == null) {
            println("데이터베이스를 먼저 생성하세요.");
            return;
        }

        if (tableName == null) {
            println("테이블을 먼저 생성하세요.");
            return;
        }

        // 테이블에 신규 값 추가
        database.execSQL("insert into " + tableName
                + "(name, age, mobile) "
                + " values "
                + "('John', 20, '010-1000-1000')");

        println("레코드 추가함.");
    }

    /* 데이터 textView에 뽑아서 보여주기 */
    public void println(String data) {
        textView.append(data + "\n");
    }

    /* 조회 메서드 선언 : Cursor 객체로 반환 받음 / rawQuery()를 이용 */
    public void executeQuery() {
        println("executeQuery 호출됨.");
        // 값을 돌려 받아야 하기에 execSQL 대신 rawQuery 사용
        Cursor cursor = database.rawQuery("select _id, name, age, mobile from emp", null);
        // 조회된 레코드 갯수 추출(cursor.getCount())
        int recordCount = cursor.getCount();
        println("레코드 개수 : " + recordCount);

        // 조회된 전체 레코드들을 보유하고 있는 Cursor 객체로부터 하나씩 추출
        // while문으로 바꿔도 ok(조건 cursor.moveToNext();)
        for (int i = 0; i < recordCount; i++) {
            // 다음 커서로 이동
            cursor.moveToNext();

            // 데이터 get 메서드를 이용해 뽑아오기
            int id = cursor.getInt(0); // 각 컬럼에 대한 인덱스 번호로 조회
            String name = cursor.getString(1);
            int age = cursor.getInt(2);
            String mobile = cursor.getString(3);

            println("레코드 #" + i + " : " + id + ", " + name + ", " + age + ", " + mobile);
        }
        // Cursor 객체 종료
        cursor.close();
    }

}
