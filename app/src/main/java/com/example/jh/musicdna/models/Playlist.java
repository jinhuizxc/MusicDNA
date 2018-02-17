package com.example.jh.musicdna.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinhui on 2018/2/17.
 * Email:1004260403@qq.com
 */

public class Playlist {

    private List<UnifiedTrack> songList;
    private String playlistName;

    public Playlist(String name) {
        playlistName = name;
        songList = new ArrayList<UnifiedTrack>();
    }

    public Playlist(List<UnifiedTrack> songList, String playlistName) {
        this.songList = songList;
        this.playlistName = playlistName;
    }

    public List<UnifiedTrack> getSongList() {
        return songList;
    }

    public void setSongList(List<UnifiedTrack> songList) {
        this.songList = songList;
    }

    public String getPlaylistName() {
        return playlistName;
    }

    public void setPlaylistName(String playlistName) {
        this.playlistName = playlistName;
    }

    public void addSong(UnifiedTrack track) {
        songList.add(track);
    }
}
