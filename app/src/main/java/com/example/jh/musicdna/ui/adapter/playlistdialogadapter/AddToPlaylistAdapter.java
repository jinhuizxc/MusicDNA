package com.example.jh.musicdna.ui.adapter.playlistdialogadapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.jh.musicdna.R;
import com.example.jh.musicdna.models.Playlist;

import java.util.List;

/**
 * Created by jinhui on 2018/2/17.
 * Email:1004260403@qq.com
 */

public class AddToPlaylistAdapter extends BaseAdapter {

    List<Playlist> allPlaylists;
    Context ctx;

    public AddToPlaylistAdapter(List<Playlist> allPlaylists, Context ctx) {
        this.allPlaylists = allPlaylists;
        this.ctx = ctx;
    }

    @Override
    public int getCount() {
        return allPlaylists.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.playlist_list_row, parent, false);

        Playlist pl = allPlaylists.get(position);

        TextView playlistName = (TextView) v.findViewById(R.id.playlist_name_holder);
        playlistName.setText(pl.getPlaylistName());

        return v;
    }

}
