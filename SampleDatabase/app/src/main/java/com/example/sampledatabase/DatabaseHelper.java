package com.example.sampledatabase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
    // 데이터베이스 이름 상수
    public static String NAME = "employee.db";
    // 데이터베이스 버전 상수
    public static int VERSION = 1;

    // 생성자 반드시 필요
    // 생성자는 반드시 context를 인자로 받아야 함
    // 부모 클래스인 SQLiteOpenHelper의 생성자를 통해서 넘겨 받은 context, 이름, 커서, 버전명까지 전달받아야 함(super)
    public DatabaseHelper(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 데이터베이스가 생성되면 자동 호출
        println("onCreate 호출됨");

        // 테이블(emp) 생성
        String sql = "create table if not exists emp("
                + " _id integer PRIMARY KEY autoincrement, "
                + " name text, "
                + " age integer, "
                + " mobile text)";

        // 쿼리 실행
        db.execSQL(sql);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        println("onOpen 호출됨");
    } // 상태 값만 출력

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 상태값 출력하기
        println("onUpgrade 호출됨 : " + oldVersion + " -> " + newVersion);
        // 기존 데이터베이스 버전과 신규 데이터베이스 버전 비교
        // 기존 테이블과 신규 테이블 레코드 비교하는 것보다, 기존 거 날리고 신버전 만드는 게 낫기에 기존 테이블 날리는 경우가 더 많음
        // 기본 버전이 1이라 1로 써도 되고, oldVersion 매개변수 써도 됨
        if (newVersion > 1) {
            db.execSQL("DROP TABLE IF EXISTS emp");
        }
    }

    public void println(String data) {
        Log.d("DatabaseHelper", data);
    }
}
