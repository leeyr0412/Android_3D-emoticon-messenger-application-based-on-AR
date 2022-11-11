package com.unity3d.player;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.os.Process;

import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Set;
import androidx.core.app.NotificationManagerCompat;
//import java.util.Set;

public class UnityPlayerActivity extends Activity implements IUnityPlayerLifecycleEvents
{
    public final static String TAG = "Logbogi";
    private static boolean notionoff;
    private static boolean girlChar;
    ToggleButton girlCharBtn;
    protected UnityPlayer mUnityPlayer; // don't change the name of this variable; referenced from native code
    RadioGroup radioGroup;




    // Override this in your custom UnityPlayerActivity to tweak the command line arguments passed to the Unity Android Player
    // The command line arguments are passed as a string, separated by spaces
    // UnityPlayerActivity calls this from 'onCreate'
    // Supported: -force-gles20, -force-gles30, -force-gles31, -force-gles31aep, -force-gles32, -force-gles, -force-vulkan
    // See https://docs.unity3d.com/Manual/CommandLineArguments.html
    // @param cmdLine the current command line arguments, may be null
    // @return the modified command line string or null
    protected String updateUnityCommandLineArguments(String cmdLine)
    {
        return cmdLine;
    }

    // Setup activity layout
    @Override protected void onCreate(Bundle savedInstanceState)
    {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        //setContentView(R.layout.activity_touch_controll);

        String cmdLine = updateUnityCommandLineArguments(getIntent().getStringExtra("unity"));
        getIntent().putExtra("unity", cmdLine);

        mUnityPlayer = new UnityPlayer(this, this);
        setContentView(mUnityPlayer);
        mUnityPlayer.requestFocus();

        Toast.makeText(this, "토스트 테스트 - 어플열림", Toast.LENGTH_LONG).show();

        //노티 권한 체크, 권한받기
        if (!permissionGrantred()) {
            Intent intent = new Intent(
                    "android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
            startActivity(intent);
        }

        //Intent intent = new Intent(this, TouchControllActivity.class);
        //startActivity(intent);



        int activity_sub_id = getResources().getIdentifier("activity_touch_controll","layout",getPackageName());
        int send_et_id = getResources().getIdentifier("NotiOnOff","id",getPackageName());


        TouchControllActivity sub = new TouchControllActivity();
        sub.Subfun(this,activity_sub_id,send_et_id);


        //        출력 캐릭터 설정
        girlChar = false;
        girlCharBtn = findViewById(R.id.charBtn);

        girlCharBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {//여캐일때
                    girlChar = true;
                    girlCharBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.chargirl));
                }
                else {
                    girlChar = false;
                    girlCharBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.charboy));
                }
            }
        });


//        알림 온오프
        notionoff=true;
        ToggleButton NotiONOFF = (ToggleButton) findViewById(R.id.NotiOnOff);
        NotiONOFF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    NotiONOFF.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_baseline_notifications_active_32));
                    notionoff=true;
                }else {
                    NotiONOFF.setBackgroundDrawable(getResources().getDrawable(R.drawable.ic_baseline_notifications_off_32));
                    notionoff=false;
                }
            }
        });

//        색선택
        radioGroup = findViewById(R.id.color_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.btnTextColorRed) {
                    UnityPlayer.UnitySendMessage("getText", "ChangeColor", "Red");
                    Log.d(TAG, "바꾼 글씨 색: 빨강");
                } else if (checkedId == R.id.btnTextColorBlue) {
                    UnityPlayer.UnitySendMessage("getText", "ChangeColor", "Blue");
                    Log.d(TAG, "바꾼 글씨 색: 파랑");
                } else if (checkedId == R.id.btnTextColorGreen) {
                    UnityPlayer.UnitySendMessage("getText", "ChangeColor", "Green");
                    Log.d(TAG, "바꾼 글씨 색: 초록");
                } else if (checkedId == R.id.btnTextColorWhite) {
                    UnityPlayer.UnitySendMessage("getText", "ChangeColor", "White");
                    Log.d(TAG, "바꾼 글씨 색: 흰");
                }
            }
        });


    }

    //이모티콘 띄우기
    public static void showEmoticons(String emo) {
        if (girlChar){//여캐
            if (emo.equals("smile")) {
                UnityPlayer.UnitySendMessage("showEmoticon", "showEmoticons", "female_smile");
                Log.d(TAG, "여캐 웃음 키워드 발견");
                return;
            } else if (emo.equals("handshake")) {
                UnityPlayer.UnitySendMessage("showEmoticon", "showEmoticons", "female_handshake");
                Log.d(TAG, "여캐 악수 키워드 발견");
                return;
            } else if (emo.equals("cry")) {
                UnityPlayer.UnitySendMessage("showEmoticon", "showEmoticons", "female_cry");
                Log.d(TAG, "여캐 울음 키워드 발견");
                return;
            } else if (emo.equals("dance")) {
                UnityPlayer.UnitySendMessage("showEmoticon", "showEmoticons", "female_dance");
                Log.d(TAG, "여캐 춤 키워드 발견");
                return;
            } else if (emo.equals("cheer")) {
                UnityPlayer.UnitySendMessage("showEmoticon", "showEmoticons", "female_cheer");
                Log.d(TAG, "여캐 환호 키워드 발견");
                return;
            } else if (emo.equals("sorry")) {
                UnityPlayer.UnitySendMessage("showEmoticon", "showEmoticons", "female_sorry");
                Log.d(TAG, "여캐 에잇");
                return;
            } else if (emo.equals("gamsa")) {
                UnityPlayer.UnitySendMessage("showEmoticon", "showEmoticons", "female_gamsa");
                Log.d(TAG, "여캐 꾸벅");
                return;
            }else if (emo.equals("hello")) {
                UnityPlayer.UnitySendMessage("showEmoticon", "showEmoticons", "female_hello");
                Log.d(TAG, "여캐 인사");
                return;
            }
            else if (emo.equals("handkiss")) {
                UnityPlayer.UnitySendMessage("showEmoticon", "showEmoticons", "female_handkiss");
                Log.d(TAG, "여캐 손키스");
                return;
            }
            else if (emo.equals("shooting")) {
                UnityPlayer.UnitySendMessage("showEmoticon", "showEmoticons", "female_shooting");
                Log.d(TAG, "여캐 총");
                return;
            }
            else if (emo.equals("simkung")) {
                UnityPlayer.UnitySendMessage("showEmoticon", "showEmoticons", "female_simkung");
                Log.d(TAG, "여캐 으악");
                return;
            }
        }
        else {//남캐
            if (emo.equals("smile")) {
                UnityPlayer.UnitySendMessage("showEmoticon", "showEmoticons", "smile");
                Log.d(TAG, "웃음 키워드 발견");
                return;
            } else if (emo.equals("handshake")) {
                UnityPlayer.UnitySendMessage("showEmoticon", "showEmoticons", "handshake");
                Log.d(TAG, "악수 키워드 발견");
                return;
            } else if (emo.equals("cry")) {
                UnityPlayer.UnitySendMessage("showEmoticon", "showEmoticons", "cry");
                Log.d(TAG, "울음 키워드 발견");
                return;
            } else if (emo.equals("dance")) {
                UnityPlayer.UnitySendMessage("showEmoticon", "showEmoticons", "dance");
                Log.d(TAG, "춤 키워드 발견");
                return;
            } else if (emo.equals("cheer")) {
                UnityPlayer.UnitySendMessage("showEmoticon", "showEmoticons", "cheer");
                Log.d(TAG, "환호 키워드 발견");
                return;
            } else if (emo.equals("sorry")) {
                UnityPlayer.UnitySendMessage("showEmoticon", "showEmoticons", "sorry");
                Log.d(TAG, "에잇 키워드 발견");
                return;
            } else if (emo.equals("gamsa")) {
                UnityPlayer.UnitySendMessage("showEmoticon", "showEmoticons", "gamsa");
                Log.d(TAG, "꾸벅 키워드 발견");
                return;
            }else if (emo.equals("hello")) {
                UnityPlayer.UnitySendMessage("showEmoticon", "showEmoticons", "man_hello");
                Log.d(TAG, "남캐 인사");
                return;
            }
            else if (emo.equals("handkiss")) {
                UnityPlayer.UnitySendMessage("showEmoticon", "showEmoticons", "man_handkiss");
                Log.d(TAG, "남캐 손키스");
                return;
            }
            else if (emo.equals("shooting")) {
                UnityPlayer.UnitySendMessage("showEmoticon", "showEmoticons", "man_shooting");
                Log.d(TAG, "남캐 총");
                return;
            }
            else if (emo.equals("simkung")) {
                UnityPlayer.UnitySendMessage("showEmoticon", "showEmoticons", "man_simkung");
                Log.d(TAG, "남캐 으악");
                return;
            }
        }
    }

    //노티
    private boolean permissionGrantred() {
        Set<String> sets = NotificationManagerCompat.getEnabledListenerPackages(this);
        if(sets != null && sets.contains(getPackageName())) {
            return true;
        } else {
            return false;
        }
    }

    public static void setMsg(String msg){
        if(notionoff) {
            UnityPlayer.UnitySendMessage("getText", "ChangeText", msg.toString());
            Log.d(TAG, "받고 전달한 메세지: " + msg.toString());
        }
        else {
            UnityPlayer.UnitySendMessage("getText", "ChangeText", "");
            Log.d(TAG, "알림 받지 않기 선택됨");
        }
    }




    // When Unity player unloaded move task to background
    @Override public void onUnityPlayerUnloaded() {
        moveTaskToBack(true);
    }

    // Callback before Unity player process is killed
    @Override public void onUnityPlayerQuitted() {
    }

    @Override protected void onNewIntent(Intent intent)
    {
        // To support deep linking, we need to make sure that the client can get access to
        // the last sent intent. The clients access this through a JNI api that allows them
        // to get the intent set on launch. To update that after launch we have to manually
        // replace the intent with the one caught here.
        setIntent(intent);
        mUnityPlayer.newIntent(intent);
    }

    // Quit Unity
    @Override protected void onDestroy ()
    {
        mUnityPlayer.destroy();
        super.onDestroy();
    }

    // If the activity is in multi window mode or resizing the activity is allowed we will use
    // onStart/onStop (the visibility callbacks) to determine when to pause/resume.
    // Otherwise it will be done in onPause/onResume as Unity has done historically to preserve
    // existing behavior.
    @Override protected void onStop()
    {
        super.onStop();

        if (!MultiWindowSupport.getAllowResizableWindow(this))
            return;

        mUnityPlayer.pause();
    }

    @Override protected void onStart()
    {
        super.onStart();

        if (!MultiWindowSupport.getAllowResizableWindow(this))
            return;

        mUnityPlayer.resume();
    }

    // Pause Unity
    @Override protected void onPause()
    {
        super.onPause();

        if (MultiWindowSupport.getAllowResizableWindow(this))
            return;

        mUnityPlayer.pause();
    }

    // Resume Unity
    @Override protected void onResume()
    {
        super.onResume();

        if (MultiWindowSupport.getAllowResizableWindow(this))
            return;

        mUnityPlayer.resume();

    }

    // Low Memory Unity
    @Override public void onLowMemory()
    {
        super.onLowMemory();
        mUnityPlayer.lowMemory();
    }

    // Trim Memory Unity
    @Override public void onTrimMemory(int level)
    {
        super.onTrimMemory(level);
        if (level == TRIM_MEMORY_RUNNING_CRITICAL)
        {
            mUnityPlayer.lowMemory();
        }
    }

    // This ensures the layout will be correct.
    @Override public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);
        mUnityPlayer.configurationChanged(newConfig);
    }

    // Notify Unity of the focus change.
    @Override public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        mUnityPlayer.windowFocusChanged(hasFocus);
    }

    // For some reason the multiple keyevent type is not supported by the ndk.
    // Force event injection by overriding dispatchKeyEvent().
    @Override public boolean dispatchKeyEvent(KeyEvent event)
    {
        if (event.getAction() == KeyEvent.ACTION_MULTIPLE)
            return mUnityPlayer.injectEvent(event);
        return super.dispatchKeyEvent(event);
    }

    // Pass any events not handled by (unfocused) views straight to UnityPlayer
    @Override public boolean onKeyUp(int keyCode, KeyEvent event)     { return mUnityPlayer.injectEvent(event); }
    @Override public boolean onKeyDown(int keyCode, KeyEvent event)   { return mUnityPlayer.injectEvent(event); }
    @Override public boolean onTouchEvent(MotionEvent event)          { return mUnityPlayer.injectEvent(event); }
    /*API12*/ public boolean onGenericMotionEvent(MotionEvent event)  { return mUnityPlayer.injectEvent(event); }
}
