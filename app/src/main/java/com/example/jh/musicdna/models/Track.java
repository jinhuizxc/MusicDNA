package com.example.jh.musicdna.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by jinhui on 2018/2/15.
 * Email:1004260403@qq.com
 */

public class Track {

    @SerializedName("title")
    private String mTitle;

    @SerializedName("id")
    private int mID;

    @SerializedName("duration")
    private int mDuration;

    @SerializedName("stream_url")
    private String mStreamURL;

    @SerializedName("artwork_url")
    private String mArtworkURL;

    public String getTitle() {
        return mTitle;
    }

    public int getID() {
        return mID;
    }

    public int getDuration() {
        return mDuration;
    }

    public String getStreamURL() {
        return mStreamURL;
    }

    public String getArtworkURL() {
        return mArtworkURL;
    }
}
