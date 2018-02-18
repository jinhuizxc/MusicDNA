package com.example.jh.musicdna.lyrics;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by jinhui on 2018/2/19.
 * Email:1004260403@qq.com
 *
 * 歌词类
 */

public class Lyrics implements Serializable, Parcelable{

    protected Lyrics(Parcel in) {
    }

    public static final Creator<Lyrics> CREATOR = new Creator<Lyrics>() {
        @Override
        public Lyrics createFromParcel(Parcel in) {
            return new Lyrics(in);
        }

        @Override
        public Lyrics[] newArray(int size) {
            return new Lyrics[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
