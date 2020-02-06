package org.androidtown.smsresendapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SMSActivity extends AppCompatActivity {

    TextView textView; //메시지를 표시하기 위한 텍스트뷰

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
        String sender = intent.getStringExtra("sender");
        String contents = intent.getStringExtra("contents");
        String date = intent.getStringExtra("date");

        //텍스트뷰에 메시지 정보 설정
        Toast.makeText(getApplicationContext(), sender+ "으로부터 문자 도착", Toast.LENGTH_LONG).show();
        textView.setText("보낸사람: "+sender+"\n내용: "+contents+"\n날짜: "+date);
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
}
