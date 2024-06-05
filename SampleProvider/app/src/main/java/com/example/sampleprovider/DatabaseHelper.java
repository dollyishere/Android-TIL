package com.example.sampleprovider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "person.db"; // 데이터베이스 명
    private static final int DATABASE_VERSION = 1; // 데이터베이스 버전

    // 상수를 만드는 이유
    // 기본적으로 메서드 만드는 게 네 가지임(CRUD)
    // 만약 테이블명이나 필드명 바꾸면 네 가지 코드를 다 바꿔야 함
    // 따라서 그걸 편리하게 해주기 위해 상수 처리한 것(상수화된 변수 하나만 바꾸면 되니까)
    public static final String TABLE_NAME = "person"; // 테이블 버전
    public static final String PERSON_ID = "_id"; // 필드 명
    public static final String PERSON_NAME = "name"; // 필드 명
    public static final String PERSON_AGE = "age"; // 필드 명
    public static final String PERSON_MOBILE = "mobile"; // 필드명

    // 테이블이 가지고 있는 전체 컬럼 명을 저장하는 배열(static)
    public static final String[] ALL_COLUMNS = {PERSON_ID, PERSON_NAME,PERSON_AGE,PERSON_MOBILE};

    // 테이블 생성 쿼리문(static)
    // 문자열 연산이 많이 들어가기에, 띄어쓰기에 주의할 것...
    private static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    PERSON_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    PERSON_NAME + " TEXT, " +
                    PERSON_AGE + " INTEGER, " +
                    PERSON_MOBILE + " TEXT" +
                    ")";

    // 생성자
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // 테이블 생성
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    // 테이블 업그레이드
    // 테이블 존재할 시, 삭제하고 테이블 새로 생성해줌(DROP TABLE IF EXISTS)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }

}
