package com.example.jh.musicdna.models;

import java.util.List;

/**
 * Created by jinhui on 2018/2/17.
 * Email:1004260403@qq.com
 */

public class Album {

    private String Name;
    private List<LocalTrack> albumSongs;

    public Album(String name, List<LocalTrack> albumSongs) {
        Name = name;
        this.albumSongs = albumSongs;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public List<LocalTrack> getAlbumSongs() {
        return albumSongs;
    }

    public void setAlbumSongs(List<LocalTrack> albumSongs) {
        this.albumSongs = albumSongs;
    }

}
