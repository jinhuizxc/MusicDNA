package com.example.jh.musicdna.ui.fragment.QueueFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
import com.example.jh.musicdna.ui.fragment.PlayerFragment.PlayerFragment;
import com.example.jh.musicdna.view.customviews.CustomPlayingIndicator;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by jinhui on 2018/2/19.
 * Email:1004260403@qq.com
 */

public class QueueRecyclerAdapter extends RecyclerView.Adapter<QueueRecyclerAdapter.MyViewHolder>
        implements ItemTouchHelperAdapter {

    private List<UnifiedTrack> queue;
    Context ctx;
    ImageLoader imgLoader;

    private OnDragStartListener mDragStartListener;

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        UnifiedTrack prev = queue.remove(fromPosition);
        queue.add(toPosition, prev);
        notifyItemMoved(fromPosition, toPosition);
        if (fromPosition == HomeActivity.queueCurrentIndex) {
            HomeActivity.queueCurrentIndex = toPosition;
        } else if (fromPosition > HomeActivity.queueCurrentIndex && toPosition == HomeActivity.queueCurrentIndex) {
            HomeActivity.queueCurrentIndex++;
        } else if (fromPosition < HomeActivity.queueCurrentIndex && toPosition == HomeActivity.queueCurrentIndex) {
            HomeActivity.queueCurrentIndex--;
        }
        ((HomeActivity) ctx).updateVisualizerRecycler();
        // 保存到队列的异步任务
        new HomeActivity.SaveQueue().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onItemDismiss(int position) {

    }

    public interface OnDragStartListener {
        void onDragStarted(RecyclerView.ViewHolder viewHolder);
    }

    public QueueRecyclerAdapter(List<UnifiedTrack> queue, Context ctx, OnDragStartListener listener) {
        this.queue = queue;
        this.ctx = ctx;
        mDragStartListener = listener;
        imgLoader = new ImageLoader(ctx);
    }

    @Override
    public QueueRecyclerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_3, parent, false);

        return new MyViewHolder(itemView);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(final QueueRecyclerAdapter.MyViewHolder holder, int position) {
        if (HomeActivity.queueCurrentIndex == position && !HomeActivity.isReloaded) {
            holder.title.setTextColor(HomeActivity.themeColor);
            holder.title.setTypeface(null, Typeface.BOLD);
            holder.indicator.setVisibility(View.VISIBLE);
            if (PlayerFragment.mMediaPlayer != null) {
                if (PlayerFragment.mMediaPlayer.isPlaying()) {
                    holder.indicator.play();
                } else {
                    holder.indicator.pause();
                }
            }
        } else {
            holder.title.setTextColor(Color.WHITE);
            holder.title.setTypeface(null, Typeface.NORMAL);
            holder.indicator.setVisibility(View.INVISIBLE);
        }

        holder.holderImg.setColorFilter(HomeActivity.themeColor);
        holder.indicator.setDrawColor(HomeActivity.themeColor);

        UnifiedTrack ut = queue.get(position);
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

        holder.holderImg.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onDragStarted(holder);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return queue.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder
            implements ItemTouchHelperViewHolder {

        ImageView art;
        TextView title, artist;
        CustomPlayingIndicator indicator;
        ImageView holderImg;

        public MyViewHolder(View view) {
            super(view);
            art = (ImageView) view.findViewById(R.id.img);
            title = (TextView) view.findViewById(R.id.title);
            artist = (TextView) view.findViewById(R.id.url);
            indicator = (CustomPlayingIndicator) view.findViewById(R.id.currently_playing_indicator);
            holderImg = (ImageView) view.findViewById(R.id.holderImage);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.parseColor("#333333"));
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(Color.BLACK);
        }
    }
}
