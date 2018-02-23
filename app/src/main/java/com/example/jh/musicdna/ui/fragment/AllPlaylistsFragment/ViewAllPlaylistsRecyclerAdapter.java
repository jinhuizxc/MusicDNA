package com.example.jh.musicdna.ui.fragment.AllPlaylistsFragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jh.musicdna.R;
import com.example.jh.musicdna.imageloader.ImageLoader;
import com.example.jh.musicdna.models.Playlist;
import com.example.jh.musicdna.models.UnifiedTrack;
import com.example.jh.musicdna.ui.activity.SplashActivity;

import java.util.List;

/**
 * Created by jinhui on 2018/2/22.
 * Email:1004260403@qq.com
 */

public class ViewAllPlaylistsRecyclerAdapter extends RecyclerView.Adapter<ViewAllPlaylistsRecyclerAdapter.MyViewHolder> {

    List<Playlist> playlists;
    Context ctx;
    ImageLoader imgLoader;

    public ViewAllPlaylistsRecyclerAdapter(List<Playlist> playlists, Context ctx) {
        this.playlists = playlists;
        this.ctx = ctx;
        imgLoader = new ImageLoader(ctx);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playlist_custom_row, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Playlist p = playlists.get(position);
        List<UnifiedTrack> list = p.getSongList();
        if (SplashActivity.tf3 != null)
            holder.playListName.setTypeface(SplashActivity.tf3);
        holder.playListName.setText(p.getPlaylistName());
        if (list.size() >= 3) {
            String[] names = new String[3];
            for (int i = 0; i < 3; i++) {
                if (list.get(i).getType()) {
                    names[i] = list.get(i).getLocalTrack().getTitle();
                    imgLoader.DisplayImage(list.get(i).getLocalTrack().getPath(), holder.img[i]);
                } else {
                    names[i] = list.get(i).getStreamTrack().getTitle();
                    imgLoader.DisplayImage(list.get(i).getStreamTrack().getArtworkURL(), holder.img[i]);
                }
                holder.name[i].setText(names[i]);
            }
            holder.continuedSymbol.setVisibility(View.VISIBLE);
        } else {
            int sz = list.size();
            String[] names = new String[3];
            for (int i = 0; i < sz; i++) {
                if (list.get(i).getType()) {
                    names[i] = list.get(i).getLocalTrack().getTitle();
                    imgLoader.DisplayImage(list.get(i).getLocalTrack().getPath(), holder.img[i]);
                } else {
                    names[i] = list.get(i).getStreamTrack().getTitle();
                    imgLoader.DisplayImage(list.get(i).getStreamTrack().getArtworkURL(), holder.img[i]);
                }
                holder.name[i].setText(names[i]);
            }
        }
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView[] img = new ImageView[3];
        TextView playListName;
        TextView continuedSymbol;
        TextView[] name = new TextView[3];

        public MyViewHolder(View itemView) {
            super(itemView);
            img[0] = (ImageView) itemView.findViewById(R.id.image1);
            img[1] = (ImageView) itemView.findViewById(R.id.image2);
            img[2] = (ImageView) itemView.findViewById(R.id.image3);
            playListName = (TextView) itemView.findViewById(R.id.playlist_name);
            name[0] = (TextView) itemView.findViewById(R.id.name1);
            name[1] = (TextView) itemView.findViewById(R.id.name2);
            name[2] = (TextView) itemView.findViewById(R.id.name3);
            continuedSymbol = (TextView) itemView.findViewById(R.id.name5);
        }
    }
}
