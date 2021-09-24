package com.gxa.speaker;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Button staticStart;
    private Button staticStop;
    private Button dynamicStart;
    private Button dynamicStop;
    private Context mContext;
    private PlayerHandler mHandler;
    private static final int MSG_PLAY_STATIC = 1;
    private static final int MSG_PLAY_DYNAMIC = 2;
    private String[] names = new String[]{"btphone.mp3", "music.mp3", "mycar.mp3", "navi.mp3", "pic.mp3", "radio.mp3", "settings.mp3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_main);

        mContext = this;
        mHandler = new PlayerHandler();

        staticStart = findViewById(R.id.static_start);
        staticStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        mHandler.sendEmptyMessage(MSG_PLAY_STATIC);
                    }
                }.start();
            }
        });
        staticStop = findViewById(R.id.static_stop);
        staticStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.removeMessages(MSG_PLAY_STATIC);
            }
        });

        dynamicStart = findViewById(R.id.dynamic_start);
        dynamicStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.sendEmptyMessage(MSG_PLAY_DYNAMIC);
            }
        });
        dynamicStop = findViewById(R.id.dynamic_stop);
        dynamicStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.removeMessages(MSG_PLAY_DYNAMIC);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class PlayerHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case MSG_PLAY_STATIC:
                    NodataSoundAgian("babyhello.mp3");
                    Log.d(TAG, "playSoundAgain:" + Thread.currentThread());
                    mHandler.sendEmptyMessageDelayed(MSG_PLAY_STATIC, 5 * 1000);
                    break;
                case MSG_PLAY_DYNAMIC:
                    double random = Math.random();
                    int temp = (int) (random * names.length);
                    Log.d(TAG, "handleMessage: name len -> " + names.length + ", random num " +
                            "-> " + temp);
                    NodataSoundAgian(names[temp]);
                    mHandler.sendEmptyMessageDelayed(MSG_PLAY_DYNAMIC, 12 * 1000);
                    break;
            }
        }
    }

    public void NodataSoundAgian(String musicName) {
        AssetFileDescriptor mRingFdAgain = null;
        try {
            mRingFdAgain = mContext.getResources().getAssets().openFd(musicName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MediaPlayer mpAgain = new MediaPlayer();
        try {
            mpAgain.setDataSource(mRingFdAgain.getFileDescriptor(), mRingFdAgain.getStartOffset(), mRingFdAgain.getLength());
            mpAgain.setVolume(1.40f, 1.40f);
            Log.d(TAG, "playSoundAgain-prepare");
            mpAgain.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mpAgain.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            public boolean onError(MediaPlayer mpAgain, int what, int extra) {
                Log.d(TAG, "playSoundAgain-onError");
                mpAgain.stop();
                mpAgain.release();
                return true;
            }
        });
        mpAgain.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mpAgain) {
                mpAgain.stop();
                mpAgain.release();
                Log.d(TAG, "playSoundAgain-onCompletion");
            }
        });
        mpAgain.start();
    }
}