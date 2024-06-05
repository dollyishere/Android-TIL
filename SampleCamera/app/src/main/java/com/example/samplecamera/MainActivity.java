package com.example.samplecamera;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.runtime.Permission;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    // 따로 전역변수화
    CameraSurfaceView cameraView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FrameLayout previewFrame = findViewById(R.id.previewFrame);
        // 객체 생성
        cameraView = new CameraSurfaceView(this);
        // framelayout에 surficeView add
        previewFrame.addView(cameraView);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                takePicture();
            }
        });

        // 권한이 아주 위험할 때(예: 하드웨어 접근) 이하 코드는 반드시 필요함
        AndPermission.with(this)
                .runtime()
                .permission(
                        Permission.CAMERA,
                        Permission.READ_EXTERNAL_STORAGE,
                        Permission.WRITE_EXTERNAL_STORAGE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        showToast("허용된 권한 갯수 : " + permissions.size());
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        showToast("거부된 권한 갯수 : " + permissions.size());
                    }
                })
                .start();

    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /* CameraSurfaceView가 선행되어 있어야 함 */
    /* 사진 찍기 버튼 누르면 호출되는 메서드 */
    public void takePicture() {
        // 미리보기 화면을 캡쳐
        cameraView.capture(new Camera.PictureCallback() { // 콜백으로 선언되어 있기에 콜백이라는 사실을 전달해줘야 함
            public void onPictureTaken(byte[] data, Camera camera) { // 이미지 바이트와 현재 사용되고 있는 카메라 객체가 전달됨
                try {
                    // 이 캡쳐된 이미지 데이터를 비트맵으로 만들고,
                    // 어느 배열에, 맨 첫 번째 방의 데이터로부터, 맨 마지막 데이터까지
                    // 일부분을 자르는 것도 가능함
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    // 미디어 스캐너의 스캐너 결과 값을 저장하는 미디어 스토어의 이미지의 미디어 insertImage()를 통해 미디어 앨범에 해당 이미지 파일 저장
                    String outUriStr = MediaStore.Images.Media.insertImage(
                            getContentResolver(), // 리졸버
                            bitmap, // 비트맵
                            "Captured Image", // 헤더에 들어갈 이름
                            "Captured Image using Camera."); // 간단한 설명글

                    if (outUriStr == null) {
                        Log.d("SampleCapture", "Image insert failed.");
                        return; // takePicture() 종료
                    } else {
                        Uri outUri = Uri.parse(outUriStr);
                        sendBroadcast(new Intent(
                                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, outUri));
                    }
                    // 다시 프리뷰 화면으로 넘어가주어야 함
                    camera.startPreview();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /* SurfaceView를 상속받아야 하고, SurfaceHolder가 가지고 있는 Callback을 구현 받아야 함 */
    private class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
        private SurfaceHolder mHolder;
        private Camera camera = null;

        // 서피스 뷰의 상태 변경 시 자동으로 호출되는 콜백 메서드들

        // 생성자에서 서피스 객체 참조 후 설정
        public CameraSurfaceView(Context context) {
            super(context);

            mHolder = getHolder();
            mHolder.addCallback(this); // 여기서 this는 CameraSurfaceView
            // 따라서 홀더가 surfaceView의 상태를 관리함
        }

        // 카메라 객체 가져오는 메서드
        public void getCameraInstance(){
            try {
                // 카메라 열어버림
                // 사용할 수 있는 카메라 객체를 얻는 것(카메라 조리개 열기)
                camera = Camera.open();
            } catch (Exception e){
                showToast("카메라가 다른 앱에서 사용중입니다.");
            }
        }

        // 서피스 뷰가 만들어질 때 카메라 객체를 참조한 후 미리보기 화면으로 홀더 객체 설정
        public void surfaceCreated(SurfaceHolder holder) {
            getCameraInstance();
            setCameraOrientation(); // 카메라 방향 => 카메라 방향에 따른 surface 변형 필요

            try {
                camera.setPreviewDisplay(mHolder);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 서피스 뷰의 화면 크기가 바뀌는 등의 변경 시점에 미리보기 시작
        // preview 화면 사이즈 변경도 가능함
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            try {
                camera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 서피스 뷰가 없어질 때 미리보기 중지
        // 카메라는 반드시 destroy 해줘야 함
        public void surfaceDestroyed(SurfaceHolder holder) {
            try {
                // preview 멈춤
                camera.stopPreview();
                camera.release();
                camera = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 카메라 객체의 takePicture 메서드를 호출하여 사진 촬영
        public boolean capture(Camera.PictureCallback handler) {
            if (camera != null) {
                camera.takePicture(null, null, handler);
                return true;
            } else {
                return false;
            }
        }

        /* 카메라 방향 설정 */
        public void setCameraOrientation() {
            if (camera == null) {
                return;
            }

            Camera.CameraInfo info = new Camera.CameraInfo(); // 어떤 카메라인지 정보 얻어낼 수 있는 객체 얻기
            Camera.getCameraInfo(0, info); // getCameraInfo를 통해 카메라 정보 얻어오기
            // 0과 1은 카메라 방향을 뜻함

            // 특정한 창을 관리해주는 게 WindowManager
            // 해당 정보는 서비스에서 얻어올 수 있음(어떤 서비스를 사용할 것인가?)
            // 원래 return은 서비스인데, WindowManager로 형 변환 시켜줘야 함
            WindowManager manager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            //
            int rotation = manager.getDefaultDisplay().getRotation();

            int degrees = 0;
            // 각도에 따라 방향 확인
            switch (rotation) {
                case Surface.ROTATION_0: degrees = 0; break;
                case Surface.ROTATION_90: degrees = 90; break;
                case Surface.ROTATION_180: degrees = 180; break;
                case Surface.ROTATION_270: degrees = 270; break;
            }

            int result;
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                result = (info.orientation + degrees) % 360;
                result = (360 - result) % 360;
            } else {
                result = (info.orientation - degrees + 360) % 360;
            }

            camera.setDisplayOrientation(result);
        }

    }

}
