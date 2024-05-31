package com.example.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SmsReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsReceiver";

    public SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // sms를 받으면 해당 메서드가 자동 호출(BroadcastReceiver가 브로드캐스트 메시지를 수신할 때 호출)
    // 파라미터로 전달되는 Intent 객체 안에 sms 데이터가 들어 있음
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i(TAG, "onReceive() 메서드 호출됨.");

        // intent 객체 안에 들어있는 bundle 객체를 getExtras 메서드로 참조
        Bundle bundle = intent.getExtras();
        // 이 번들을 이용하여 sms 메세지 객체를 만듬 => smsMessage라는 자료형으로 된 배열 객체 반환환
       SmsMessage[] messages = parseSmsMessage(bundle);
       // 메세지 배열이 null이 아니고, 비어있지 않을 시 sms 메세지 처리
        if (messages != null && messages.length > 0) {
            // 발신자의 전화번호 추출
            String sender = messages[0].getOriginatingAddress();
            Log.i(TAG, "SMS sender : " + sender);
            // sms 내용을 추출
            String contents = messages[0].getMessageBody();
            Log.i(TAG, "SMS contents : " + contents);
            // sms 수신 시간을 추출
            Date receivedDate = new Date(messages[0].getTimestampMillis());
            Log.i(TAG, "SMS received date : " + receivedDate.toString());
            // activity로 데이터를 전달함
            sendToActivity(context, sender, contents, receivedDate);
        }
    }

    private SmsMessage[] parseSmsMessage(Bundle bundle) {
        // 문자메시지가 보내는 프로토콜 수신자와 같음
        // 카카오에서 보낸건지, 일반 문자에서 보냈는지 구분하는 식별자
        // 해당 값은 구분할 수 없음
        // 번들로부터 해당 값을 꺼내고, 배열로 이 값을 반환 받아야 함
        Object[] objs = (Object[]) bundle.get("pdus");

        // Sms문자 메시지라는 객체가 필요함
        // pdus라는 객체가 가지고 있는 값이 여러 개일 수 있으므로, 일단 배열로 받는 것임
        // 이 배열을 담을 SmsMessage 배열을 생성
        SmsMessage[] messages = new SmsMessage[objs.length];
        // 메세지 크기 smsCount 변수에 저장
        int smsCount = objs.length;
        // 메세지 수 만큼 반복
        for (int i = 0; i < smsCount; i++) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String format = bundle.getString("format");
                // 데이터가 Pdu 형태로 들어오므로, 이로부터 새롭게 형성해서 sms 형식으로 만들어달라는 의미임임
               messages[i] = SmsMessage.createFromPdu((byte[]) objs[i], format);
            } else {
                messages[i] = SmsMessage.createFromPdu((byte[]) objs[i]);
            }
        }

        return messages;
    }

    private void sendToActivity(Context context, String sender, String contents, Date receivedDate) {
        Intent myIntent = new Intent(context, SmsActivity.class);
        // intent 재활용
        // 플래그 설명:
        // - FLAG_ACTIVITY_NEW_TASK: 새로운 작업(Task)에서 Activity를 시작합니다. 이 플래그는 새로운 작업을 생성하거나 이미 존재하는 작업이 있다면 그 작업을 사용합니다.
        // - FLAG_ACTIVITY_SINGLE_TOP: 시작하려는 Activity가 이미 맨 위에 있다면 재사용합니다. 이 플래그는 동일한 인스턴스를 재사용하여 불필요한 인스턴스 생성과 onCreate 호출을 방지합니다.
        // - FLAG_ACTIVITY_CLEAR_TOP: 새로운 Activity가 시작되기 전에 같은 작업에 속한 기존의 Activity를 모두 제거합니다. 만약 시작하려는 Activity가 이미 작업에 있다면, 해당 Activity 위의 모든 Activity를 제거하고 그 Activity의 onNewIntent를 호출합니다.
        myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        myIntent.putExtra("sender", sender);
        myIntent.putExtra("contents", contents);
        myIntent.putExtra("receivedDate", format.format(receivedDate));
        // 리시버 클래스가 가진 컨텍스트를 통해 액티비티 시작
        context.startActivity(myIntent);
    }

}