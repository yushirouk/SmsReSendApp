package org.androidtown.smsresendapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

import java.util.Date;


public class SMSReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle bundle = intent.getExtras(); //인텐트에서 부가데이터 가져오기

        //bundle에 들어있는 SMS 메시지를 parsing
        SmsMessage[] messages = parseSmsMessage(bundle);

        if(messages != null && messages.length >0){
            String sender = messages[0].getOriginatingAddress(); // 메시지 송신자
            String contents = messages[0].getMessageBody(); // 메시지 내용
            Date receivedDate = new Date(messages[0].getTimestampMillis()); // 수신 날짜
            String date = receivedDate.toString();

            //아래의 내용은 인텐트를 통해 받은 내용을 화면으로 출력하는 것이다.
            //내가 만들고자 하는 앱에서는 사용할 필요가 없다. (백그라운드에서 돌기만 하면 됨.)



            //MainActivity로 보낼 인텐트 만들고 메시지 정보 부가데이터로 넣기
            Intent mIntent = new Intent(context.getApplicationContext(), SMSActivity.class);
            mIntent.putExtra("sender", sender);
            mIntent.putExtra("contents", contents);
            mIntent.putExtra("date", date);

            //브로드캐스트 수신자는 화면이 없으므로 새로운 액티비티 화면 새로 띄우려면 인텐트에 FLAG_ACTIVITY_NEW_TASK 추가해야함
            mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // -> onCreate() 메소드에서 처리

            //이미 액티비티가 메모리에 존재하면 새로 만들지 말고 사용 -> onNewIntent() 메소드에서 처리
            mIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

            /*
            1. 만들어진 액티비티가 없으면 onCreate() 메소드에서 인텐트를 처리
            2. 이미 만들어진 액티비티가 있으면 onNewIntent() 메소드에서 인텐트를 처리
             */

            //액티비티 띄우기
            context.getApplicationContext().startActivity(mIntent);
        }
    }

    /* 1. Bundle 객체에서 PDU 포맷의 메시지 객체 가져오기
           2. 메시지 객체 갯수만큼 SmsMessage 배열 만들기
           3. createFromPdu() 메소드 사용해서 PDU 포맷의 메시지 SmsMessage 객체로 변환하여 SmsMessage 배열에 저장 */
    private SmsMessage[] parseSmsMessage(Bundle bundle){
        Object[] objs = (Object[]) bundle.get("pdus"); //PDU 포맷의 메시지 복원
        SmsMessage[] messages = new SmsMessage[objs.length];

        int smsCount = objs.length;
        for(int i=0; i<smsCount; i++){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){ // API 23 이상
                String format = bundle.getString("format");
                messages[i] = SmsMessage.createFromPdu((byte[])objs[i], format);
            }else{
                messages[i] = SmsMessage.createFromPdu((byte[])objs[i]);
            }
        }

        return messages;
    }
}