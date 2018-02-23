package com.example.jh.musicdna.serviceandbroadcastreceiver.notificationmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.Log;

/**
 * Created by jinhui on 2018/2/21.
 * Email:1004260403@qq.com
 *
 * AudioPlayerBroadcastReceiver广播类，哪里进行注册啦？
 * AudioPlayerBroadcastReceiver 去掉也可以运行，目前不需要配置广播哦
 */

public class AudioPlayerBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = AudioPlayerBroadcastReceiver.class.getSimpleName();
    onCallbackListener callback;

    public interface onCallbackListener {
        public void onCallbackCalled(int i);
        public void togglePLayPauseCallback();
        public boolean getPauseClicked();
        public void setPauseClicked(boolean bool);
        public MediaPlayer getMediaPlayer();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "AudioPlayerBroadcastReceiver onReceive");
        callback = (onCallbackListener) context;

        String action = intent.getAction();
        if (action.equalsIgnoreCase("com.example.jh.musicdna.ACTION_PLAY_PAUSE")) {
            try {
                if (!callback.getPauseClicked()) {
                    callback.setPauseClicked(true);
                }
                callback.togglePLayPauseCallback();
                callback.onCallbackCalled(6);
            } catch (Exception e) {

            }

        } else if (action.equalsIgnoreCase("com.example.jh.musicdna.ACTION_NEXT")) {

            try {
                callback.onCallbackCalled(2);
            } catch (Exception e) {

            }
        } else if (action.equalsIgnoreCase("com.example.jh.musicdna.ACTION_PREV")) {
            try {
                callback.onCallbackCalled(3);
            } catch (Exception e) {

            }
        }

    }
}
