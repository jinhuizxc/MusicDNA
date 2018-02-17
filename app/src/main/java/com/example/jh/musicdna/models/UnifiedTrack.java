package com.example.jh.musicdna.models;

/**
 * Created by jinhui on 2018/2/17.
 * Email:1004260403@qq.com
 */

public class UnifiedTrack {

    boolean type;                       // true->localTrack         false->streamTrack
    LocalTrack localTrack;
    Track streamTrack;

    public UnifiedTrack(boolean type, LocalTrack localTrack, Track streamTrack) {
        this.type = type;
        this.localTrack = localTrack;
        this.streamTrack = streamTrack;
    }

    public boolean getType() {
        return type;
    }

    public void setType(boolean type) {
        this.type = type;
    }

    public LocalTrack getLocalTrack() {
        return localTrack;
    }

    public void setLocalTrack(LocalTrack localTrack) {
        this.localTrack = localTrack;
    }

    public Track getStreamTrack() {
        return streamTrack;
    }

    public void setStreamTrack(Track streamTrack) {
        this.streamTrack = streamTrack;
    }

}
