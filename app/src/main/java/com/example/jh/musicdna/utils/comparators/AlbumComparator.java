package com.example.jh.musicdna.utils.comparators;

import com.example.jh.musicdna.models.Album;

import java.util.Comparator;

/**
 * Created by jinhui on 2018/2/17.
 * Email:1004260403@qq.com
 */

public class AlbumComparator  implements Comparator<Album> {

    @Override
    public int compare(Album lhs, Album rhs) {
        return lhs.getName().toString().compareTo(rhs.getName().toString());
    }
}

