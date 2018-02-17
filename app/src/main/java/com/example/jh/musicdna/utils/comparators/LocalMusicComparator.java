package com.example.jh.musicdna.utils.comparators;

import com.example.jh.musicdna.models.LocalTrack;

import java.util.Comparator;

/**
 * Created by jinhui on 2018/2/17.
 * Email:1004260403@qq.com
 */

public class LocalMusicComparator implements Comparator<LocalTrack> {
//    @Override
//    public int compare(LocalTrack o1, LocalTrack o2) {
//        return 0;
//    }

    @Override
    public int compare(LocalTrack lhs, LocalTrack rhs) {
        return lhs.getTitle().toString().compareTo(rhs.getTitle().toString());
    }
}
