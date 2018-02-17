package com.example.jh.musicdna.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinhui on 2018/2/17.
 * Email:1004260403@qq.com
 */

public class Favourite {

    private List<UnifiedTrack> favourite;

    public Favourite() {
        favourite = new ArrayList<>();
    }

    public List<UnifiedTrack> getFavourite() {
        return favourite;
    }

    public void setFavourite(List<UnifiedTrack> favourite) {
        this.favourite = favourite;
    }

}
