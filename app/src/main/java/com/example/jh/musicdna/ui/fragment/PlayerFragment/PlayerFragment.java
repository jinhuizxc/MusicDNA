package com.example.jh.musicdna.ui.fragment.PlayerFragment;

import android.content.Context;
import android.media.MediaPlayer;
import android.media.audiofx.BassBoost;
import android.media.audiofx.Equalizer;
import android.media.audiofx.PresetReverb;
import android.media.audiofx.Visualizer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.jh.musicdna.R;
import com.example.jh.musicdna.imageloader.ImageLoader;
import com.example.jh.musicdna.lyrics.Lyrics;
import com.example.jh.musicdna.models.LocalTrack;
import com.example.jh.musicdna.models.Track;
import com.example.jh.musicdna.models.UnifiedTrack;
import com.example.jh.musicdna.ui.activity.HomeActivity;
import com.example.jh.musicdna.utils.DownloadThread;
import com.example.jh.musicdna.view.customviews.CustomProgressBar;
import com.example.jh.musicdna.view.snappyrecyclerview.CustomAdapter;
import com.example.jh.musicdna.view.snappyrecyclerview.SnappyRecyclerView;
import com.example.jh.musicdna.view.visualizers.VisualizerView;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Timer;

/**
 * Created by jinhui on 2018/2/17.
 * Email:1004260403@qq.com
 *
 * 播放界面的布局
 */

public class PlayerFragment extends Fragment {

    public SnappyRecyclerView snappyRecyclerView;
    CustomAdapter customAdapter;

    public static VisualizerView mVisualizerView;
    public static MediaPlayer mMediaPlayer;
    public static Visualizer mVisualizer;
    public static Equalizer mEqualizer;
    public static BassBoost bassBoost;
    public static PresetReverb presetReverb;

    public static boolean isReplayIconVisible = false;

    public static boolean isPrepared = false;

    AVLoadingIndicatorView bufferingIndicator;

    public static CustomProgressBar cpb;

    Pair<String, String> temp;

    TextView currTime, totalTime;

    public ImageView repeatController;
    public ImageView shuffleController;

    public ImageView equalizerIcon;
    public ImageView mainTrackController;
    public ImageView nextTrackController;
    public ImageView previousTrackController;
    public ImageView favouriteIcon;
    public ImageView queueIcon;

    public ImageView saveDNAToggle;

    boolean isFav = false;

    public RelativeLayout bottomContainer;
    public RelativeLayout seekBarContainer;
    public RelativeLayout toggleContainer;

    public ImageView selected_track_image;
    public TextView selected_track_title;
    public TextView selected_track_artist;
    public ImageView player_controller;

    public RelativeLayout smallPlayer;

    ImageView favControllerSp, nextControllerSp;

    ImageLoader imgLoader;

    public SeekBar progressBar;

    public static int durationInMilliSec;
    static boolean completed = false;
    public boolean pauseClicked = false;
    boolean isTracking = false;

    public static boolean localIsPlaying = false;

    public Timer timer;

    public static Track track;
    public static LocalTrack localTrack;

    static boolean isRefreshed = false;

    public PlayerFragmentCallbackListener mCallback;
    public onPlayPauseListener mCallback7;


    HomeActivity homeActivity;
    public static Context ctx;

    public RelativeLayout spToolbar;
    ImageView overflowMenuAB;
    ImageView spImgAB;
    TextView spTitleAB;
    TextView spArtistAB;

    TextView lyricsStatus;
    AVLoadingIndicatorView lyricsLoadingIndicator;
    // 下载线程
    public DownloadThread downloadThread;
    public RelativeLayout lyricsContainer;
    public ImageView lyricsIcon;
    public TextView lyricsContent;
    public boolean isLyricsVisisble = false;
    public Lyrics currentLyrics = null;

    public boolean isStart = true;

    ShowcaseView showCase;

    long startTrack;
    long endTrack;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        return view;
    }

    // 播放界面的回调方法
    public interface PlayerFragmentCallbackListener{
        void onComplete();

        void onPreviousTrack();

        void onEqualizerClicked();

        void onQueueClicked();

        void onPrepared();

        void onFullScreen();

        void onSettingsClicked();

        void onAddedtoFavfromPlayer();

        void onShuffleEnabled();

        void onShuffleDisabled();

        void onSmallPlayerTouched();

        void addCurrentSongtoPlaylist(UnifiedTrack ut);
    }

    // 播放暂停的回调方法
    public interface onPlayPauseListener{
        void onPlayPause();
    }

}
