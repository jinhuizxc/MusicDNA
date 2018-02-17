package com.example.jh.musicdna.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinhui on 2018/2/17.
 * Email:1004260403@qq.com
 */

public class AllMusicFolders {

    List<MusicFolder> musicFolders;

    public AllMusicFolders() {
        musicFolders = new ArrayList<>();
    }

    public List<MusicFolder> getMusicFolders() {
        return musicFolders;
    }

    public void setMusicFolders(List<MusicFolder> musicFolders) {
        this.musicFolders = musicFolders;
    }

}
