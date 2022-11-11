package com.unity3d.player;

import android.app.Notification;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.Toast;

public class NotificationListener extends NotificationListenerService {
    public final static String TAG = "Logbogi";
    public NotificationListener() {
    }

//    @Override
//    public IBinder onBind(Intent intent) {
//        // TODO: Return the communication channel to the service.
//        throw new UnsupportedOperationException("Not yet implemented");
//    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);

        Log.d(TAG, "onNotificationRemoved ~ " +
                " packageName: " + sbn.getPackageName() +
                " id: " + sbn.getId());
    }

//    @Override    public int onStartCommand(Intent intent, int flags, int startId) {
//        return super.onStartCommand(intent, flags, startId);    }

    @Override
    public void onNotificationPosted (StatusBarNotification sbn){
        super.onNotificationPosted(sbn);

        Notification notification = sbn.getNotification();
        String packageName = sbn.getPackageName();

        Log.d(TAG, "onNotificationPosted ~ " +
                " packageName: " + sbn.getPackageName() +
                " id: " + sbn.getId());

        if(packageName.equals("jp.naver.line.android")) {
            Bundle extras = sbn.getNotification().extras;
            String title = extras.getString(Notification.EXTRA_TITLE);      //노티 제목(보낸사람 이름)
            String text = extras.getString(Notification.EXTRA_TEXT);      //노티 내용(메세지 내용)
            KeywordCheck(text);
            String outText = title + " : \t" + text;
            Toast testToast = Toast.makeText(this.getApplicationContext(), "알림도착", Toast.LENGTH_SHORT);
            UnityPlayerActivity.setMsg(outText);
            testToast.show();
        }
    }

    private void KeywordCheck(String text) {
        if(text.contains("ㅋㅋㅋ")){
            UnityPlayerActivity.showEmoticons("smile");
        }
        else if(text.contains("악수")){
            UnityPlayerActivity.showEmoticons("handshake");
        }
        else if(text.contains("ㅠㅠ")||text.contains("ㅜㅜ")){
            UnityPlayerActivity.showEmoticons("cry");
        }
        else if(text.contains("둠칫")||text.contains("룰루")){
            UnityPlayerActivity.showEmoticons("dance");
        }else if(text.contains("와아")||text.contains("오예")){
            UnityPlayerActivity.showEmoticons("cheer");
        }
        else if(text.contains("뚜시")||text.contains("에잇")){
            UnityPlayerActivity.showEmoticons("sorry");
        }else if(text.contains("감사")||text.contains("꾸벅")){
            UnityPlayerActivity.showEmoticons("gamsa");
        }
        else if(text.contains("안뇽")||text.contains("하이")||text.contains("빠이")){
            UnityPlayerActivity.showEmoticons("hello");
        }else if(text.contains("빵야")||text.contains("탕탕")||text.contains("저격")){
            UnityPlayerActivity.showEmoticons("shooting");
        }
        else if(text.contains("으악")||text.contains("심쿵")||text.contains("기절")){
            UnityPlayerActivity.showEmoticons("simkung");
        }
        else if(text.contains("사랑해")||text.contains("너뿐")){
            UnityPlayerActivity.showEmoticons("handkiss");
        }






    }


//    private void sendMsg(String Msg){
//        Intent intent = new Intent("smsg");
//        intent.putExtra("msg",Msg);
//        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
//    }
}