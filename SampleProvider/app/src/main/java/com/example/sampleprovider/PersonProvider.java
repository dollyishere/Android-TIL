package com.example.sampleprovider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.Nullable;

public class PersonProvider extends ContentProvider {
    // 제공자 고유 값(앱 패키지명)
    private static final String AUTHORITY = "com.example.sampleprovider";

    // 제공 데이터 형식 (테이블.. 파일.. SharedPreference)
    private static final String BASE_PATH = "person";

    // content:// Uri
    // 이 형식으로 만들 때 사용하는 게 바로 Uri.parse() 메서드임
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH );

    // 모든 데이터에 접근 가능함
    private static final int PERSONS = 1;
    // id가 2번인 애만 접근 가능
    private static final int PERSON_ID = 2;

    // 다른 앱이 요청한 Uri와 내 앱에서 허용한 Uri를 매칭 객체 생성(UriMatcher)
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        // 모든 데이터에 접근 가능한 경로
        uriMatcher.addURI(AUTHORITY, BASE_PATH, PERSONS);
        // id가 2번인 애인 데이터에만 접근 가능한 경로
        uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", PERSON_ID);
    }

    // 다른 앱이 요청했을 때, 요청한 데이터를 내보내기 위해 SQLiteDatabase 객체가 필요
    private SQLiteDatabase database;

    @Override
    public boolean onCreate() {
        // 우리 데이터베이스를 helper가 관리하고 있으므로, helper에 위임
        DatabaseHelper helper = new DatabaseHelper(getContext());
        // 데이터 쓰기 형식으로 허용
        database = helper.getWritableDatabase();

        return true;
    }

    /* 이하 메서드들이 실제 호출되는 것은 MainActivity에서 */

    // 컨텐트 조회 메서드
    // Uri, where절(없으면 null), 정렬
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        // 조회 결과 저장
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case PERSONS:
                cursor =  database.query(DatabaseHelper.TABLE_NAME, DatabaseHelper.ALL_COLUMNS,
                        s,null,null,null, DatabaseHelper.PERSON_NAME +" ASC");
                break;
            default:
                throw new IllegalArgumentException("알 수 없는 URI " + uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    // uri 값을 전달하면 결과 값으로 MINE 타입을 반환
    // Uri 값이 유효한 경우에만 실행되고 그렇지 않을 시 예외처리
    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case PERSONS:
                return "vnd.android.cursor.dir/persons";
            default:
                throw new IllegalArgumentException("알 수 없는 URI " + uri);
        }
    }

    // 컨텐트 추가 메서드
    // 두번째 값이 ContentValues 값임
    // 리턴값이 uri으로 반환 타입이 약간 틀림
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        // 테이블에 새로운 레코드 추가 : 이때 insert()는 추가된 레코드의 개수를 반환
        long id = database.insert(DatabaseHelper.TABLE_NAME, null, contentValues);

        // 레코드가 추가되었으면
        if (id > 0) {
            // 추가된 레코드의 위치(주소) 추출
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, id);
            //내 앱의 Context에게 변경 사항이 있음을 알림
            getContext().getContentResolver().notifyChange(_uri, null);
            // 하여 반환
            return _uri;
        }

        // id 값이 0 또는 -1일시 레코드 추가에 실패한 것이기에 예외 발생시킴
        throw new SQLException("추가 실패 -> URI :" + uri);
    }

    // 컨텐트 삭제 메서드
    // 두번째 파라미터가 where
    @Override
    public int delete(Uri uri, String s, String[] strings) {
        int count = 0;
        // switch case 분기화
        switch (uriMatcher.match(uri)) {
            case PERSONS:
                count =  database.delete(DatabaseHelper.TABLE_NAME, s, strings);
                break;
            default:
                throw new IllegalArgumentException("알 수 없는 URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    // 컨텐트 수정 메서드
    // 수정될 값들(ContentValues)
    // 업데이트에서 주의해야 할 점 => 두 번째 파라미터 값이 null값이면 절대 안됨
    // 세번째는 where문
    // 네번째는 세번째 파라미터에 값이 있ㅇ르 시 그 안에 들어갈 조건 값을 대체함
    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case PERSONS:
                count =  database.update(DatabaseHelper.TABLE_NAME, contentValues, s, strings);
                break;
            default:
                throw new IllegalArgumentException("알 수 없는 URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }
}
