package com.example.jh.musicdna.ui.fragment.PlayerFragment;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jh.musicdna.R;

/**
 * Created by jinhui on 2018/2/17.
 * Email:1004260403@qq.com
 *
 * 播放界面的布局
 */

public class PlayerFragment extends Fragment {

    public static int durationInMilliSec;
    public static MediaPlayer mMediaPlayer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_player, container, false);
        return view;
    }
}
