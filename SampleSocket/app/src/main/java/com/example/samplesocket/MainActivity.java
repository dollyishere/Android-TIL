package com.example.samplesocket;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    // 서버 구동 후, 사용자가 입력한 값 추출을 위한
    EditText editText;

    // 클라이언트와 서버에 주고 받은 데이터 출력을 위한
    TextView textView;
    TextView textView2;

    // 전송받은 데이터를 액티비티의 위젯에 출력하기 위한
    Handler handler = new Handler(); // 핸들러는 데이터를 받는 쪽에 선언

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.editText);
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);

        // client 전송 버튼 클릭 시
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 사용자 입력 값 추출(스레드를 통해 전달되기에, 클릭할 때마다 재선언)
                final String data = editText.getText().toString();

                // 스레드
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 서버에 데이터 전송하는 메서드 호출(send())
                        send(data);
                    }
                }).start();

            }
        });

        // server 서버 시작 버튼 클릭 시
        Button button2 = findViewById(R.id.button2);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 서버 구동하는 메서드 호출(startServer())
                        startServer();
                    }
                }).start();

            }
        });
    }

    /* Client : 서버에게 입력된 값을 전송하는 메서드 선언 */
    public void send(String data) {
        try {
            // 서버와 통신할 수 있는 포트 번호 지정
            int portNumber = 5001;
            // 클라이언트 소켓 객체 생성
            // 클래스명이 좀 다르긴 해~
            Socket sock = new Socket("localhost", portNumber);
            printClientLog("소켓 연결함.");

            // 서버에게 보낼 데이터 (stream 방식)
            ObjectOutputStream outstream = new ObjectOutputStream(sock.getOutputStream());
            // 데이터 쓰기
            outstream.writeObject(data);
            outstream.flush();
            printClientLog("데이터 전송함.");

            // 서버가 보내온 데이터 읽기(stream 방식)
            ObjectInputStream instream = new ObjectInputStream(sock.getInputStream());
            printClientLog("서버로부터 받음 : " + instream.readObject());

            // 소켓 닫기
            sock.close();
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    /* Server : 서버를 구동시키는 메서드 선언 */
    public void startServer() {
        try {
            // 클라이언트와 통신할 수 있는 포트
            int portNumber = 5001;

            // 서버 소켓
            ServerSocket server = new ServerSocket(portNumber);
            printServerLog("서버 시작함 : " + portNumber);

            // 서버 계속 구동 중...
            // 무한 루프를 통해 서버 계속 실행
            while(true) {
                // 클라이언트 소켓에 대한 감지
                // 클라이언트 연결 요청을 기다리다가, 클라이언트가 연결 요청하면 통신을 위해 새로운 소켓 객체 반환
                Socket sock = server.accept();
                // 클라이언트 주소
                // 정확히는, 아래 코드와 함께 클라이언트의 ip 주소와 포트 번호를 가져오는 것
                InetAddress clientHost = sock.getLocalAddress();
                // 클라이언트 포트
                int clientPort = sock.getPort();
                printServerLog("클라이언트 연결됨 : " + clientHost + " : " + clientPort);

                // 클라이언트가 보낸 데이터 읽어 객체로 변환(stream 방식)
                // 동일한 자바 객체 주고 받을 때는 얘가 편함
                // 하지만 서버가 자바 계열이 아닐 수도 있음 => 그때는 DataInputStream으로 변경해서 사용
                ObjectInputStream instream = new ObjectInputStream(sock.getInputStream());
                // readObject()메서드로 stream 데이터 읽기
                Object obj = instream.readObject();
                printServerLog("데이터 받음 : " + obj);

                // 클라이언트에게 전송할 데이터(stream 방식)
                // 다른 계열 서버랑 통신할 때는 DataOutputStream으로 변경해서 사용
                ObjectOutputStream outstream = new ObjectOutputStream(sock.getOutputStream());
                // writeObject() 메서드로 클라이언트에게 전송할 데이터 쓰기
                outstream.writeObject(obj + " from Server.");
                // 출력 스트림에 버퍼링 된 데이터 실제로 전송
                outstream.flush();
                printServerLog("데이터 보냄.");

                // 소켓 닫기
                sock.close();
            }
        } catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    /* 클라이언트의 위젯(textView)에 출력하는 메서드 선언 */
    public void printClientLog(final String data) {
        Log.d("MainActivity", data);
        // 클라이언트 위젯 핸들러
        handler.post(new Runnable() {
            @Override
            public void run() {
                // 핸들러 내부에서 로직 처리
                textView.append(data + "\n");
            }
        });

    }

    /* 서버의 위젯(textView2)에 출력하는 메서드 선언 */
    public void printServerLog(final String data) {
        Log.d("MainActivity", data);
        // 서버 위젯 핸들러
        handler.post(new Runnable() {
            @Override
            public void run() {
                textView2.append(data + "\n");
            }
        });
    }

}