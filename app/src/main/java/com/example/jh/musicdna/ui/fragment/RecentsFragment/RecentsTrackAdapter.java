package com.example.jh.musicdna.ui.fragment.RecentsFragment;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jh.musicdna.R;
import com.example.jh.musicdna.imageloader.ImageLoader;
import com.example.jh.musicdna.itemtouchhelpers.ItemTouchHelperAdapter;
import com.example.jh.musicdna.itemtouchhelpers.ItemTouchHelperViewHolder;
import com.example.jh.musicdna.models.LocalTrack;
import com.example.jh.musicdna.models.Track;
import com.example.jh.musicdna.models.UnifiedTrack;
import com.example.jh.musicdna.ui.activity.HomeActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by jinhui on 2018/2/19.
 * Email:1004260403@qq.com
 */

public class RecentsTrackAdapter extends RecyclerView.Adapter<RecentsTrackAdapter.MyViewHolder>
        implements ItemTouchHelperAdapter {

    private List<UnifiedTrack> songList;
    private Context ctx;
    ImageLoader imgLoader;

    HomeActivity homeActivity;

    public interface OnDragStartListener {
        void onDragStarted(RecyclerView.ViewHolder viewHolder);
    }

    public interface onRemoveListener {
        void updateRecentsFragment();
    }

    private OnDragStartListener mDragStartListener;
    onRemoveListener mCallback;

    public RecentsTrackAdapter(List<UnifiedTrack> songList, RecentsFragment recentsContext, Context ctx) {
        this.songList = songList;
        mDragStartListener = recentsContext;
        mCallback = recentsContext;
        this.ctx = ctx;
        homeActivity = (HomeActivity) ctx;
        imgLoader = new ImageLoader(ctx);
    }

    @Override
    public RecentsTrackAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_2, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecentsTrackAdapter.MyViewHolder holder, int position) {
        UnifiedTrack ut = songList.get(position);
        if (ut.getType()) {
            LocalTrack lt = ut.getLocalTrack();
            imgLoader.DisplayImage(lt.getPath(), holder.art);
            holder.title.setText(lt.getTitle());
            holder.artist.setText(lt.getArtist());
        } else {
            Track t = ut.getStreamTrack();
            Picasso.with(ctx)
                    .load(t.getArtworkURL())
                    .resize(100, 100)
                    .error(R.drawable.ic_default)
                    .placeholder(R.drawable.ic_default)
                    .into(holder.art);
            holder.title.setText(t.getTitle());
            holder.artist.setText("");
        }
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        UnifiedTrack prev = songList.remove(fromPosition);
        songList.add(toPosition, prev);
        notifyItemMoved(fromPosition, toPosition);

        new HomeActivity.SaveRecents().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onItemDismiss(int position) {
        songList.remove(position);
        notifyItemRemoved(position);
        if (position < 10) {
            HomeActivity.continuePlayingList.clear();
            for (int i = 0; i < Math.min(10, HomeActivity.recentlyPlayed.getRecentlyPlayed().size()); i++) {
                HomeActivity.continuePlayingList.add(HomeActivity.recentlyPlayed.getRecentlyPlayed().get(i));
            }
            homeActivity.rAdapter.notifyDataSetChanged();
            homeActivity.refreshHeaderImages();
        }

        mCallback.updateRecentsFragment();

        new HomeActivity.SaveRecents().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
            implements ItemTouchHelperViewHolder {

        ImageView art;
        TextView title, artist;

        public MyViewHolder(View view) {
            super(view);
            art = (ImageView) view.findViewById(R.id.img_2);
            title = (TextView) view.findViewById(R.id.title_2);
            artist = (TextView) view.findViewById(R.id.url_2);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.DKGRAY);
            if (Build.VERSION.SDK_INT >= 21) {
                itemView.setTranslationZ(12);
            }
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(Color.parseColor("#111111"));
            if (Build.VERSION.SDK_INT >= 21) {
                itemView.setTranslationZ(0);
            }
        }
    }

}
