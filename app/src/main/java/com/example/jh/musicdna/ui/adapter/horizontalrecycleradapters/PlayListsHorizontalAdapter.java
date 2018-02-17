package com.example.jh.musicdna.ui.adapter.horizontalrecycleradapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jh.musicdna.R;
import com.example.jh.musicdna.imageloader.ImageLoader;
import com.example.jh.musicdna.models.Playlist;
import com.example.jh.musicdna.models.UnifiedTrack;

import java.util.List;

/**
 * Created by jinhui on 2018/2/17.
 * Email:1004260403@qq.com
 */

public class PlayListsHorizontalAdapter  extends RecyclerView.Adapter<PlayListsHorizontalAdapter.MyViewHolder> {

    Context ctx;
    List<Playlist> playlists;
    ImageLoader imgLoader;

    public PlayListsHorizontalAdapter(List<Playlist> playlists, Context ctx) {
        this.playlists = playlists;
        this.ctx = ctx;
        imgLoader = new ImageLoader(ctx);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playlist_item_card_holder, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Playlist pl = playlists.get(position);
        List<UnifiedTrack> list = pl.getSongList();

        for (int i = 0; i < 4; i++) {
            holder.img[i].setImageBitmap(null);
        }

        if (list.size() >= 4) {
            for (int i = 0; i < 4; i++) {
                if (list.get(i).getType()) {
                    imgLoader.DisplayImage(list.get(i).getLocalTrack().getPath(), holder.img[i]);
                } else {
                    imgLoader.DisplayImage(list.get(i).getStreamTrack().getArtworkURL(), holder.img[i]);
                }
            }
        } else {
            int sz = list.size();
            for (int i = 0; i < sz; i++) {
                if (list.get(i).getType()) {
                    imgLoader.DisplayImage(list.get(i).getLocalTrack().getPath(), holder.img[i]);
                } else {
                    imgLoader.DisplayImage(list.get(i).getStreamTrack().getArtworkURL(), holder.img[i]);
                }
            }
        }

        holder.playlistName.setText(pl.getPlaylistName());
        if (pl.getSongList().size() > 1)
            holder.playlistSize.setText(String.valueOf(pl.getSongList().size()) + " Songs");
        else
            holder.playlistSize.setText(String.valueOf(pl.getSongList().size()) + " Song");

    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView img[] = new ImageView[4];
        TextView playlistName, playlistSize;
        RelativeLayout bottomHolder;

        public MyViewHolder(View view) {
            super(view);
            img[0] = (ImageView) itemView.findViewById(R.id.image1);
            img[1] = (ImageView) itemView.findViewById(R.id.image2);
            img[2] = (ImageView) itemView.findViewById(R.id.image3);
            img[3] = (ImageView) itemView.findViewById(R.id.image4);
            playlistName = (TextView) view.findViewById(R.id.playlist_card_title);
            playlistSize = (TextView) view.findViewById(R.id.playlist_num_songs);
            bottomHolder = (RelativeLayout) view.findViewById(R.id.playlist_bottomHolder);
        }
    }
}
