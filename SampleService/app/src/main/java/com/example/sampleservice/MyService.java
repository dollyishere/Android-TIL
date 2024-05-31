package com.example.sampleservice;

import android.app.Service;

import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class MyService extends Service {

    private static String TAG = "MyService";
    // Logcat에서 찾아보기 위한 문자열 선언
    public MyService() {
    }

    // 재정의 메서드
    // 서비스 클래스(객체)에 대한 생명주기 메서드들
    @Override
    public void onCreate() { // 서비스 시작 직전 호출
        super.onCreate();
        Log.d(TAG, "onCreate() 호출됨");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand 호출됨");

        // MainActivity로부터 전달된 인텐트 객체의 값 추축
        if (intent == null) {
            return Service.START_STICKY;
        } else {
            // 메서드 선언 후 처리
            processCommand(intent);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        // 시스템을 통해 다른 앱(H/W)들의 데이터를 하나로 묶어서 처리할 경우에 사용되는 메서드
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // 전달받은 인텐트 객체가 null이 아닐 시 호출되는 메서드
    private void processCommand(Intent intent) {
        String command = intent.getStringExtra("command");
        String name = intent.getStringExtra("name");

        Log.d(TAG, "COMMAND: " + command + "Name: " + name);

        for (int n=0;n<5;n++) {
            try {
                // 1초에 한번씩 실행하기 위해 현재 프로세스를 1초간 잠재움
                Thread.sleep(1000);
            } catch (Exception e) {
                Log.d(TAG, "스레드 오류");
            }
            Log.d(TAG, "Waiting..." + n + "second");
        }

        // MainActivity로 전송할 Intent 객체 생성
        Intent showIntent = new Intent(getApplicationContext(), MainActivity.class);
        // 액티비티 재생성을 막기 위한 플래그 설정, 인텐트 객체.addFlags()
        //플래그 값은 중복이 가능
        showIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // MainActivity에 전달할 값
        showIntent.putExtra("command", "show");
        showIntent.putExtra("name", name + "FROM SERVICE");

        // 액티비티 호출
        startActivity(showIntent);
    }


}