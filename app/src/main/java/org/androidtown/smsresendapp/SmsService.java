package org.androidtown.smsresendapp;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

public class SmsService extends Service implements ActivityCompat.OnRequestPermissionsResultCallback{

    String sender;
    String contents;
    String date;
    String myPhnNo;
    private static String[] senderList = {"15884000","15882588"};
    private static String[] receiverList = {"01030063952"};

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        Log.d("test", "서비스의 onBind");

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 서비스에서 가장 먼저 호출됨(최초에 한번만)
        Log.d("test", "서비스의 onCreate");

        TelephonyManager telManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (checkSelfPermission(Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }

        myPhnNo = telManager.getLine1Number();
        if(myPhnNo.startsWith("+82")){
            myPhnNo = myPhnNo.replace("+82", "0");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 서비스가 호출될 때마다 실행
        Log.d("test", "서비스의 onStartCommand");

        try {

            // 인텐트로부터 발신자, 내용, 날짜 정보 받아와서 저장
            sender = intent.getStringExtra("sender");
            contents = intent.getStringExtra("contents");
            date = intent.getStringExtra("date");

            Log.d("sender", sender);
            Log.d("myPhnNo", myPhnNo);
            Log.d("contents", contents);
            Log.d("date", date);

            //전송
            if(!sender.equals(myPhnNo)) {

                SmsManager smsManager = SmsManager.getDefault();

                for (String tmpSnd: senderList) {
                    if (sender.equals(tmpSnd)) {
                        for (String tmpRcv : receiverList) {
                            smsManager.sendTextMessage(tmpRcv, null, contents, null, null);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // 서비스가 종료될 때 실행
        Log.d("test", "서비스의 onDestroy");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

    }
}
