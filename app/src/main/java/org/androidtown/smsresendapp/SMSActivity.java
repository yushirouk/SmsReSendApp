package org.androidtown.smsresendapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.snackbar.Snackbar;

public class SMSActivity extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{

    TextView textView; //메시지를 표시하기 위한 텍스트뷰
    String sender = null;
    String contents = null;
    // 앱을 실행하기 위해 필요한 퍼미션을 정의합니다.

    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.SEND_SMS, // 카메라
            Manifest.permission.RECEIVE_SMS};  // 외부 저장소

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);

        //RECEIVE_SMS는 위험권한이므로 사용자에게 권한 요청을 하기위한 코드
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        if(permissionCheck == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "SMS 수신권한 있음", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "SMS 수신권한 없음", Toast.LENGTH_SHORT).show();
            if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.RECEIVE_SMS)){
                Toast.makeText(this, "SMS 권한 설정이 필요함", Toast.LENGTH_SHORT).show();
            } else {
                // 권한이 할당되지 않았으면 해당 권한을 요청
                ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.RECEIVE_SMS},1);
            }
        }

        //텍스트뷰 객체 참조하기
        textView = findViewById(R.id.textview);

        Intent intent = getIntent();

        // 인텐트로부터 발신자, 내용, 날짜 정보 받아와서 저장
        sender = intent.getStringExtra("sender");
        contents = intent.getStringExtra("contents");
        String date = intent.getStringExtra("date");

        //텍스트뷰에 메시지 정보 설정
        Toast.makeText(getApplicationContext(), sender+ "으로부터 문자 도착", Toast.LENGTH_LONG).show();
        textView.setText("보낸사람: "+sender+"\n내용: "+contents+"\n날짜: "+date);

        ActivityCompat.requestPermissions( SMSActivity.this, REQUIRED_PERMISSIONS, 100);
    }

    public void sendSMS (){
        try {

            //전송
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(sender, null, contents, null, null);
            Toast.makeText(getApplicationContext(), "전송 완료!", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "SMS faild, please try again later!", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    // 새로운 인텐트가 도착하면 호출되는 콜백 메소드
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        // 인텐트로부터 발신자, 내용, 날짜 정보 받아와서 저장
        String sender = intent.getStringExtra("sender");
        String contents = intent.getStringExtra("contents");
        String date = intent.getStringExtra("date");

        //텍스트뷰에 메시지 정보 설정
        Toast.makeText(getApplicationContext(), sender+ "으로부터 문자 도착", Toast.LENGTH_LONG).show();
        textView.setText("보낸사람: "+sender+"\n내용: "+contents+"\n날짜: "+date);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grandResults) {

        if ( requestCode == 100 && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }



            if ( check_result ) {

                // 모든 퍼미션을 허용했다면 SMS전송시작

                sendSMS();
            }
            else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {


                    // 사용자가 거부만 선택한 경우에는 앱을 다시 실행하여 허용을 선택하면 앱을 사용할 수 있습니다.

                    Snackbar.make(textView, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();

                }else {


                    // “다시 묻지 않음”을 사용자가 체크하고 거부를 선택한 경우에는 설정(앱 정보)에서 퍼미션을 허용해야 앱을 사용할 수 있습니다.
                    Snackbar.make(textView, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ",
                            Snackbar.LENGTH_INDEFINITE).setAction("확인", new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {

                            finish();
                        }
                    }).show();
                }
            }
        }
    }
}
