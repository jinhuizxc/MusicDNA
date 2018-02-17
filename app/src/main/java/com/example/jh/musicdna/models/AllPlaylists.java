package com.example.jh.musicdna.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinhui on 2018/2/17.
 * Email:1004260403@qq.com
 */

public class AllPlaylists {

    private List<Playlist> allPlaylists;

    public AllPlaylists() {
        allPlaylists = new ArrayList<>();
    }

    public List<Playlist> getPlaylists() {
        return allPlaylists;
    }

    public void setPlaylists(List<Playlist> playlists) {
        this.allPlaylists = playlists;
    }

    public void addPlaylist(Playlist pl){
        allPlaylists.add(pl);
    }
}
