package com.example.jh.musicdna.models;

import java.util.List;

/**
 * Created by jinhui on 2018/2/17.
 * Email:1004260403@qq.com
 */

public class Artist {

    private String Name;
    private List<LocalTrack> artistSongs;

    public Artist(String name, List<LocalTrack> artistSongs) {
        Name = name;
        this.artistSongs = artistSongs;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public List<LocalTrack> getArtistSongs() {
        return artistSongs;
    }

    public void setArtistSongs(List<LocalTrack> artistSongs) {
        this.artistSongs = artistSongs;
    }

}
