package com.example.jh.musicdna.utils.comparators;

import com.example.jh.musicdna.models.Artist;

import java.util.Comparator;

/**
 * Created by jinhui on 2018/2/17.
 * Email:1004260403@qq.com
 */

public class ArtistComparator implements Comparator<Artist> {

    @Override
    public int compare(Artist lhs, Artist rhs) {
        return lhs.getName().toString().compareTo(rhs.getName().toString());
    }
}

