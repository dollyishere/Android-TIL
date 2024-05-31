package com.example.receiver;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // AndPermission 라이브러리를 사용하여 런타임 권한 요청을 처리하는 코드
        // with과 this를 통해 권한 요청 결과를 반환할 컨텍스트를 알게 됨
        // 권한 요청 다이얼로그가 현재 액티비티나 프래그먼트에서 표시되고, 결과 처리를 해당 컨텍스트에서 하는 게 가능
        AndPermission.with(this) // this는 activity나 Fragment의 인스턴스
                .runtime() // 런타임(앱이 실행 중일 때 권한을 요청하는 것) 권한 요청 시작
                // AndroidManifest.xml에서는 이 권한이 반드시 부여되어 있어야 함
                .permission(Permission.RECEIVE_SMS)
                // 접속 권한은 이부분에서 생성(grant)
                .onGranted(new Action<List<String>>() {
                    // 사용자가 권한 허용했을 시 호출되는 콜백
                    // 넘겨지는 값은 androidmanifest에서 사용된 값임
                    // 허용된 권한을 바탕으로 필요한 작업 수행이 가능
                    @Override
                    public void onAction(List<String> permissions) {
                        showToast("허용된 권한 갯수 : " + permissions.size());
                    }
                })
                // 사용자가 권한을 거부했을 때 호출되는 콜백
                // 여기서 permissions는 허가된 권한들이 아니라, 거부당한 권한들의 목록임
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        showToast("거부된 권한 갯수 : " + permissions.size());
                    }
                })
                // 접속을 허가받은 권한을 실제로 사용하기 위한 start 메서드
                .start();

    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

}
