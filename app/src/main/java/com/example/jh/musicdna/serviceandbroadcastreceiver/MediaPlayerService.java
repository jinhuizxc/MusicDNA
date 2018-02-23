package com.example.jh.musicdna.serviceandbroadcastreceiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaMetadata;
import android.media.MediaPlayer;
import android.media.Rating;
import android.media.session.MediaController;
import android.media.session.MediaSession;
import android.media.session.MediaSessionManager;
import android.media.session.PlaybackState;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.jh.musicdna.R;
import com.example.jh.musicdna.interfaces.ServiceCallbacks;
import com.example.jh.musicdna.serviceandbroadcastreceiver.notificationmanager.Constants;
import com.example.jh.musicdna.ui.activity.HomeActivity;
import com.example.jh.musicdna.ui.fragment.PlayerFragment.PlayerFragment;

/**
 * Created by jinhui on 2018/2/21.
 * Email:1004260403@qq.com
 * <p>
 * 不知：MediaSessionManager/MediaSession/MediaController
 */

public class MediaPlayerService extends Service implements PlayerFragment.onPlayPauseListener {

    PlayerFragment pFragment;
    private MediaSessionManager m_objMediaSessionManager;
    // MediaPlayer对象
    private MediaPlayer m_objMediaPlayer;
    private MediaSession m_objMediaSession;
    private MediaController m_objMediaController;

    private boolean isSwipable = false;
    // 通知管理类
    private NotificationManager notificationManager;

    // 绑定服务
    Intent startIntent;
    private ServiceCallbacks serviceCallbacks;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        startIntent = intent;
        return new LocalBinder();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        try {
            pFragment = ((HomeActivity) PlayerFragment.ctx).getPlayerFragment();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (pFragment != null)
            pFragment.mCallback7 = this;

        if (m_objMediaSessionManager == null) {
            initMediaSessions();
        }
        handleIntent(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 处理意图
     * @param intent
     */
    private void handleIntent(Intent intent) {
        if (intent == null || intent.getAction() == null)
            return;

        String action = intent.getAction();

        if (m_objMediaController != null && action.equalsIgnoreCase(Constants.ACTION_PLAY)) {
            m_objMediaController.getTransportControls().play();
        } else if (m_objMediaController != null && action.equalsIgnoreCase(Constants.ACTION_PAUSE)) {
            m_objMediaController.getTransportControls().pause();
        } else if (m_objMediaController != null && action.equalsIgnoreCase(Constants.ACTION_FAST_FORWARD)) {
            m_objMediaController.getTransportControls().fastForward();
        } else if (m_objMediaController != null && action.equalsIgnoreCase(Constants.ACTION_REWIND)) {
            m_objMediaController.getTransportControls().rewind();
        } else if (m_objMediaController != null && action.equalsIgnoreCase(Constants.ACTION_PREVIOUS)) {
            m_objMediaController.getTransportControls().skipToPrevious();
        } else if (m_objMediaController != null && action.equalsIgnoreCase(Constants.ACTION_NEXT)) {
            m_objMediaController.getTransportControls().skipToNext();
        } else if (m_objMediaController != null && action.equalsIgnoreCase(Constants.ACTION_STOP)) {
            m_objMediaController.getTransportControls().stop();
        }
    }

    /**
     * 初始化MediaSessions
     */
    private void initMediaSessions() {
        if (pFragment != null) {
            pFragment.mCallback7 = this;

            m_objMediaPlayer = pFragment.mMediaPlayer;
            m_objMediaSessionManager = (MediaSessionManager) getSystemService(Context.MEDIA_SESSION_SERVICE);
            m_objMediaSession = new MediaSession(getApplicationContext(), "sample session");

            m_objMediaSession.setFlags(MediaSession.FLAG_HANDLES_MEDIA_BUTTONS | MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);

            MediaMetadata.Builder metadataBuilder = new MediaMetadata.Builder();
            if (pFragment.localIsPlaying) {
                if (pFragment.localTrack != null) {
                    metadataBuilder.putString(MediaMetadata.METADATA_KEY_TITLE, pFragment.localTrack.getTitle());
                    metadataBuilder.putString(MediaMetadata.METADATA_KEY_ARTIST, pFragment.localTrack.getArtist());
                }
            } else {
                if (pFragment.track != null) {
                    metadataBuilder.putString(MediaMetadata.METADATA_KEY_TITLE, pFragment.track.getTitle());
                    metadataBuilder.putString(MediaMetadata.METADATA_KEY_ARTIST, "");
                }
            }

            if (pFragment.selected_track_image != null && pFragment.selected_track_image.getDrawable() != null) {
                if (((BitmapDrawable) pFragment.selected_track_image.getDrawable()).getBitmap() != null) {
                    metadataBuilder.putBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART, ((BitmapDrawable) pFragment.selected_track_image.getDrawable()).getBitmap());
                }
            }
            m_objMediaSession.setMetadata(metadataBuilder.build());
            PlaybackState.Builder stateBuilder = new PlaybackState.Builder();
            stateBuilder.setActions(PlaybackState.ACTION_PLAY | PlaybackState.ACTION_PLAY_PAUSE | PlaybackState.ACTION_PAUSE | PlaybackState.ACTION_SKIP_TO_NEXT | PlaybackState.ACTION_SKIP_TO_PREVIOUS);

            if (pFragment.mMediaPlayer != null) {
                stateBuilder.setState(!pFragment.mMediaPlayer.isPlaying() ? PlaybackState.STATE_PAUSED : PlaybackState.STATE_PLAYING, PlaybackState.PLAYBACK_POSITION_UNKNOWN, 1.0f);
            }
            m_objMediaSession.setPlaybackState(stateBuilder.build());

            m_objMediaSession.setActive(true);
            m_objMediaController = m_objMediaSession.getController();
            m_objMediaSession.setCallback(new MediaSession.Callback() {
                @Override
                public void onPlay() {
                    super.onPlay();
                    try {
                        Log.d(Constants.LOG_TAG, "onPlay");
                        PlayerFragment pFrag = pFragment;
                        if (pFrag != null) {
                            if (!pFrag.isStart) {
                                pFrag.togglePlayPause();
                            }
                            pFrag.isStart = false;
                            buildNotification(generateAction(R.drawable.ic_pause_notif, "Pause", Constants.ACTION_PAUSE));
                        }
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onPause() {
                    super.onPause();
                    try {
                        PlayerFragment pFrag = pFragment;
                        if (pFrag != null) {
                            pFrag.togglePlayPause();
                            buildNotification(generateAction(R.drawable.ic_play_notif, "Play", Constants.ACTION_PLAY));
                        }
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onSkipToNext() {
                    super.onSkipToNext();
                    // 暂时注释掉，onCallbackCalled
                    try {
                        if (pFragment != null) {
                            pFragment.onCallbackCalled(2);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    buildNotification(generateAction(R.drawable.ic_pause_notif, "Pause", Constants.ACTION_PAUSE));
                                }
                            }, 100);
                        }
                    } catch (Exception e) {

                    }
                }

                @Override
                public void onSkipToPrevious() {
                    super.onSkipToPrevious();
                    // 暂时注释掉，onCallbackCalled
                    try {
                        if (pFragment != null) {
                            pFragment.onCallbackCalled(3);
                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    buildNotification(generateAction(R.drawable.ic_pause_notif, "Pause", Constants.ACTION_PAUSE));
                                }
                            }, 100);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFastForward() {
                    super.onFastForward();
                    Log.d(Constants.LOG_TAG, "onFastForward");
                    if (pFragment != null)
                        pFragment.mCallback.onComplete();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            buildNotification(generateAction(R.drawable.ic_pause_notif, "Pause", Constants.ACTION_PAUSE));
                        }
                    }, 100);
                }

                @Override
                public void onRewind() {
                    super.onRewind();
                    Log.d(Constants.LOG_TAG, "onRewind");
                    if (pFragment != null)
                        pFragment.mCallback.onPreviousTrack();
                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            buildNotification(generateAction(R.drawable.ic_pause_notif, "Pause", Constants.ACTION_PAUSE));
                        }
                    }, 100);
                }

                @Override
                public void onStop() {
                    super.onStop();
//                    if (pFragment.mMediaPlayer != null) {
//                        if (pFragment.mMediaPlayer.isPlaying()) {
//                            buildNotification(generateAction(R.drawable.ic_pause_notif, "Pause", Constants.ACTION_PAUSE));
//                        } else {
//                            buildNotification(generateAction(R.drawable.ic_play_notif, "Play", Constants.ACTION_PLAY));
//                        }
//                    }
                }

                @Override
                public void onSeekTo(long pos) {
                    super.onSeekTo(pos);
                }

                @Override
                public void onSetRating(@NonNull Rating rating) {
                    super.onSetRating(rating);
                }
            });

        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        m_objMediaSession.release();
        return super.onUnbind(intent);
    }

    /**
     * onTaskRemoved 方法以前从未遇到过，不了解。
     * @param rootIntent
     */
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        try {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 生成Notification
     * @param action
     */
    private void buildNotification(Notification.Action action) {
        Notification.MediaStyle style = new Notification.MediaStyle();
        style.setShowActionsInCompactView(1);
        style.setMediaSession(m_objMediaSession.getSessionToken());

        Intent intent = new Intent(getApplicationContext(), MediaPlayerService.class);
        intent.setAction(Constants.ACTION_STOP);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);

        String artist;

        if (pFragment != null && pFragment.localIsPlaying) {
            artist = pFragment.localTrack.getArtist();
        } else {
            artist = "";
        }

        Intent notificationIntent = new Intent(this, HomeActivity.class);
        PendingIntent pendingNotificationIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Bitmap bmp = null;
        try {
            bmp = ((BitmapDrawable) pFragment.selected_track_image.getDrawable()).getBitmap();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bmp == null) {
            bmp = BitmapFactory.decodeResource(getResources(), R.drawable.ic_default);
        }

        Notification notification = new Notification.Builder(this)
                .setStyle(style)
                .setSmallIcon(R.drawable.ic_notification)
                .setVisibility(Notification.VISIBILITY_PUBLIC)
                .setDeleteIntent(pendingIntent)
                .addAction(generateAction(R.drawable.ic_skip_previous_notif, "Previous", Constants.ACTION_PREVIOUS))
                .addAction(action)
                .addAction(generateAction(R.drawable.ic_skip_next_notif, "Next", Constants.ACTION_NEXT))
                .setContentTitle(pFragment.selected_track_title.getText())
                .setContentText(artist)
                .setLargeIcon(bmp)
                .build();

        notification.contentIntent = pendingNotificationIntent;
        notification.priority = Notification.PRIORITY_MAX;

        if (isSwipable || (pFragment.mMediaPlayer != null && pFragment.mMediaPlayer.isPlaying())) {
            notification.flags |= Notification.FLAG_ONGOING_EVENT;
        }

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        try {
            notificationManager.notify(1, notification);
        } catch (Exception e) {
            e.printStackTrace();
        }

        updateMediaSession();
        
    }

    /**
     * 更新MediaSession，不太理解哦
     */
    private void updateMediaSession() {
        if (pFragment != null) {
            m_objMediaSession.setFlags(MediaSession.FLAG_HANDLES_MEDIA_BUTTONS | MediaSession.FLAG_HANDLES_TRANSPORT_CONTROLS);

            MediaMetadata.Builder metadataBuilder = new MediaMetadata.Builder();
            if (pFragment.localIsPlaying) {
                if (pFragment.localTrack != null) {
                    metadataBuilder.putString(MediaMetadata.METADATA_KEY_TITLE, pFragment.localTrack.getTitle());
                    metadataBuilder.putString(MediaMetadata.METADATA_KEY_ARTIST, pFragment.localTrack.getArtist());
                }
            } else {
                if (pFragment.track != null) {
                    metadataBuilder.putString(MediaMetadata.METADATA_KEY_TITLE, pFragment.track.getTitle());
                    metadataBuilder.putString(MediaMetadata.METADATA_KEY_ARTIST, "");
                }
            }
            if (((BitmapDrawable) pFragment.selected_track_image.getDrawable()).getBitmap() != null) {
                try {
                    metadataBuilder.putBitmap(MediaMetadata.METADATA_KEY_ALBUM_ART, ((BitmapDrawable) pFragment.selected_track_image.getDrawable()).getBitmap());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            m_objMediaSession.setMetadata(metadataBuilder.build());
            PlaybackState.Builder stateBuilder = new PlaybackState.Builder();
            stateBuilder.setActions(PlaybackState.ACTION_PLAY | PlaybackState.ACTION_PLAY_PAUSE | PlaybackState.ACTION_PAUSE | PlaybackState.ACTION_SKIP_TO_NEXT | PlaybackState.ACTION_SKIP_TO_PREVIOUS);
            try {
                if (pFragment.mMediaPlayer != null) {
                    stateBuilder.setState(!pFragment.mMediaPlayer.isPlaying() ? PlaybackState.STATE_PAUSED : PlaybackState.STATE_PLAYING, PlaybackState.PLAYBACK_POSITION_UNKNOWN, 1.0f);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            m_objMediaSession.setPlaybackState(stateBuilder.build());
            m_objMediaSession.setActive(true);
        }
    }

    /**
     * 生成action
     * @param icon
     * @param title
     * @param intentAction
     */
    private Notification.Action generateAction(int icon, String title, String intentAction) {
        Intent intent = new Intent(getApplicationContext(), MediaPlayerService.class);
        intent.setAction(intentAction);
        PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 1, intent, 0);
        return new Notification.Action.Builder(icon, title, pendingIntent).build();
    }

    /**
     * 播放停止的回调方法实现
     */
    @Override
    public void onPlayPause() {
        if (pFragment.mMediaPlayer != null && pFragment.mMediaPlayer.isPlaying()) {
            buildNotification(generateAction(R.drawable.ic_pause_notif, "Pause", Constants.ACTION_PAUSE));
        } else {
            buildNotification(generateAction(R.drawable.ic_play_notif, "Play", Constants.ACTION_PLAY));
        }
    }

    /**
     * 设置回调方法
     * @param callbacks
     */
    public void setCallbacks(ServiceCallbacks callbacks) {
        serviceCallbacks = callbacks;
        pFragment = serviceCallbacks.getPlayerFragment();
        if (pFragment != null)
            pFragment.mCallback7 = this;
        if (m_objMediaSessionManager == null) {
            initMediaSessions();
        }
        handleIntent(startIntent);
    }

    public class LocalBinder extends Binder {
        public MediaPlayerService getService() {
            // Return this instance of MyService so clients can call public methods
            return MediaPlayerService.this;
        }
    }
}
