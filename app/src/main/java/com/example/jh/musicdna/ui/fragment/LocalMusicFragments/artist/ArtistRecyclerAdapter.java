package com.example.jh.musicdna.ui.fragment.LocalMusicFragments.artist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jh.musicdna.R;
import com.example.jh.musicdna.models.Artist;

import java.util.List;

/**
 * Created by jinhui on 2018/2/18.
 * Email:1004260403@qq.com
 */

public class ArtistRecyclerAdapter  extends RecyclerView.Adapter<ArtistRecyclerAdapter.MyViewHolder> {

    List<Artist> artistList;

    public ArtistRecyclerAdapter(List<Artist> artistList) {
        this.artistList = artistList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_4, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ArtistRecyclerAdapter.MyViewHolder holder, int position) {
        Artist ab = artistList.get(position);
        holder.title.setText(ab.getName());
        if (ab.getArtistSongs().size() > 1)
            holder.numSongs.setText(ab.getArtistSongs().size() + " Songs");
        else
            holder.numSongs.setText(ab.getArtistSongs().size() + " Song");
    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title, numSongs;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.artist_name);
            numSongs = (TextView) view.findViewById(R.id.num_songs);
        }
    }

}
