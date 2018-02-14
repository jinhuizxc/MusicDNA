package com.example.jh.musicdna.ui.fragment.EqualizerFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jh.musicdna.R;

/**
 * Created by jinhui on 2018/2/15.
 * Email:1004260403@qq.com
 */

public class EqualizerFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_equalizer, container, false);
    }
}
