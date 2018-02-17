package com.example.jh.musicdna.interfaces;

import com.example.jh.musicdna.Config;
import com.example.jh.musicdna.models.Track;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by jinhui on 2018/2/17.
 * Email:1004260403@qq.com
 */

public interface StreamService {

    @GET("/tracks?client_id=" + Config.CLIENT_ID)
    Call<List<Track>> getTracks(@Query("q") String query, @Query("limit") int limit);
}
