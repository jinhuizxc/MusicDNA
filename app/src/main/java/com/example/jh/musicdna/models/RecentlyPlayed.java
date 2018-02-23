package com.example.jh.musicdna.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinhui on 2018/2/17.
 * Email:1004260403@qq.com
 */

public class RecentlyPlayed {

    private List<UnifiedTrack> recentlyPlayed;

    public RecentlyPlayed() {
        recentlyPlayed = new ArrayList<>();
    }

    public List<UnifiedTrack> getRecentlyPlayed() {
        return recentlyPlayed;
    }

    public void setRecentlyPlayed(List<UnifiedTrack> recentlyPlayed) {
        this.recentlyPlayed = recentlyPlayed;
    }

    public void addSong(UnifiedTrack track){
        recentlyPlayed.add(track);
    }

    @Override
    public String toString() {
        return "RecentlyPlayed{" +
                "recentlyPlayed=" + recentlyPlayed +
                '}';
    }
}
