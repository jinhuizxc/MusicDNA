package com.example.jh.musicdna.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

/**
 * Created by jinhui on 2018/2/22.
 * Email:1004260403@qq.com
 */

public class MediaCacheUtils {

    public static void updateMediaCache(String title, String artist, String album, long id, Context ctx) {

        ContentResolver musicResolver = ctx.getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

        ContentValues newValues = new ContentValues();
        newValues.put(android.provider.MediaStore.Audio.Media.TITLE, title);
        newValues.put(android.provider.MediaStore.Audio.Media.ARTIST, artist);
        newValues.put(android.provider.MediaStore.Audio.Media.ALBUM, album);

        int res = musicResolver.update(musicUri, newValues, android.provider.MediaStore.Audio.Media._ID + "=?", new String[]{String.valueOf(id)});

        if (res > 0) {
//            Toast.makeText(this, "Updated MediaStore cache", Toast.LENGTH_SHORT).show();
        }

    }
}
