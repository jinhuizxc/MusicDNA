package com.example.jh.musicdna.ui.fragment.FavouritesFragment;

import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jh.musicdna.MusicDNAApplication;
import com.example.jh.musicdna.R;
import com.example.jh.musicdna.clickitemtouchlistener.ClickItemTouchListener;
import com.example.jh.musicdna.imageloader.ImageLoader;
import com.example.jh.musicdna.itemtouchhelpers.SimpleItemTouchHelperCallback;
import com.example.jh.musicdna.models.LocalTrack;
import com.example.jh.musicdna.models.Track;
import com.example.jh.musicdna.models.UnifiedTrack;
import com.example.jh.musicdna.ui.activity.HomeActivity;
import com.example.jh.musicdna.ui.activity.SplashActivity;
import com.example.jh.musicdna.utils.CommonUtils;
import com.squareup.leakcanary.RefWatcher;
import com.squareup.picasso.Picasso;

/**
 * Created by jinhui on 2018/2/15.
 * Email:1004260403@qq.com
 *
 * 喜欢界面
 */

public class FavouritesFragment extends Fragment implements
        FavouriteTrackAdapter.OnDragStartListener,
        FavouriteTrackAdapter.onEmptyListener,
        FavouriteTrackAdapter.onMoveRemoveistener {

    RecyclerView favouriteRecycler;
    FavouriteTrackAdapter fAdapter;
    LinearLayoutManager mLayoutManager2;

    ItemTouchHelper mItemTouchHelper;

    FavouriteFragmentCallback mCallback;

    LinearLayout noFavouriteContent;

    FloatingActionButton playAll;

    View bottomMarginLayout;

    ImageView backdrop;
    TextView fragTitle;
    ImageView backBtn, addToQueueIcon, fragIcon;

    ImageLoader imgLoader;

    public interface FavouriteFragmentCallback {
        void onFavouriteItemClicked(int position);

        void onFavouritePlayAll();

        void addFavToQueue();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (FavouriteFragmentCallback) context;
            imgLoader = new ImageLoader(context);
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        backBtn = (ImageView) view.findViewById(R.id.fav_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        fragIcon = (ImageView) view.findViewById(R.id.fav_frag_icon);
        fragIcon.setImageTintList(ColorStateList.valueOf(HomeActivity.themeColor));

        fragTitle = (TextView) view.findViewById(R.id.fav_fragment_title);
        if (SplashActivity.tf4 != null)
            fragTitle.setTypeface(SplashActivity.tf4);

        addToQueueIcon = (ImageView) view.findViewById(R.id.add_fav_to_queue_icon);
        addToQueueIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.addFavToQueue();
            }
        });

        backdrop = (ImageView) view.findViewById(R.id.fav_backdrop);
        if (HomeActivity.favouriteTracks.getFavourite().size() > 0) {
            UnifiedTrack ut = HomeActivity.favouriteTracks.getFavourite().get(0);
            if (ut.getType()) {
                LocalTrack lt = ut.getLocalTrack();
                imgLoader.DisplayImage(lt.getPath(), backdrop);
            } else {
                Track t = ut.getStreamTrack();
                Picasso.with(getContext())
                        .load(t.getArtworkURL())
                        .resize(100, 100)
                        .error(R.drawable.ic_default)
                        .placeholder(R.drawable.ic_default)
                        .into(backdrop);
            }
        }

        bottomMarginLayout = view.findViewById(R.id.bottom_margin_layout);
        if (HomeActivity.isReloaded)
            bottomMarginLayout.getLayoutParams().height = 0;
        else
            bottomMarginLayout.getLayoutParams().height = CommonUtils.dpTopx(65, getContext());

        favouriteRecycler = (RecyclerView) view.findViewById(R.id.favouriteRecycler);
        noFavouriteContent = (LinearLayout) view.findViewById(R.id.noFavouriteContent);
        playAll = (FloatingActionButton) view.findViewById(R.id.fav_play_all_fab);
        if (SplashActivity.tf4 != null)
            ((TextView) view.findViewById(R.id.favNoContentText)).setTypeface(SplashActivity.tf4);

        if (HomeActivity.favouriteTracks.getFavourite().size() == 0) {
            favouriteRecycler.setVisibility(View.INVISIBLE);
            playAll.setVisibility(View.INVISIBLE);
            noFavouriteContent.setVisibility(View.VISIBLE);
        } else {
            favouriteRecycler.setVisibility(View.VISIBLE);
            playAll.setVisibility(View.VISIBLE);
            noFavouriteContent.setVisibility(View.INVISIBLE);
        }

        fAdapter = new FavouriteTrackAdapter(HomeActivity.favouriteTracks.getFavourite(), this, getContext());
        mLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        favouriteRecycler.setLayoutManager(mLayoutManager2);
        favouriteRecycler.setItemAnimator(new DefaultItemAnimator());
        favouriteRecycler.setAdapter(fAdapter);

        favouriteRecycler.addOnItemTouchListener(new ClickItemTouchListener(favouriteRecycler) {
            @Override
            public boolean onClick(RecyclerView parent, View view, int position, long id) {
                mCallback.onFavouriteItemClicked(position);
                return true;
            }

            @Override
            public boolean onLongClick(RecyclerView parent, View view, int position, long id) {
                return true;
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        if (HomeActivity.favouriteTracks.getFavourite().size() == 0) {
            playAll.setVisibility(View.INVISIBLE);
        } else {
            playAll.setVisibility(View.VISIBLE);
        }
        playAll.setBackgroundTintList(ColorStateList.valueOf(HomeActivity.themeColor));
        playAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (HomeActivity.favouriteTracks.getFavourite().size() > 0) {
                    HomeActivity.queue.getQueue().clear();
                    for (int i = 0; i < HomeActivity.favouriteTracks.getFavourite().size(); i++) {
                        HomeActivity.queue.getQueue().add(HomeActivity.favouriteTracks.getFavourite().get(i));
                    }
                    HomeActivity.queueCurrentIndex = 0;
                    mCallback.onFavouritePlayAll();
                }
            }
        });

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(fAdapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(favouriteRecycler);
    }

    @Override
    public void onResume() {
        super.onResume();
        mLayoutManager2.scrollToPositionWithOffset(0, 0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                playAll.animate().scaleX(1.0f).scaleY(1.0f).setDuration(300).setInterpolator(new OvershootInterpolator());
            }
        }, 500);
    }

    @Override
    public void onDragStarted(RecyclerView.ViewHolder viewHolder) {
        mItemTouchHelper.startDrag(viewHolder);
    }

    @Override
    public void onEmpty() {
        noFavouriteContent.setVisibility(View.VISIBLE);
        playAll.setVisibility(View.GONE);
    }

    @Override
    public void updateFavFragment() {
        if (HomeActivity.favouriteTracks.getFavourite().size() > 0) {
            UnifiedTrack ut = HomeActivity.favouriteTracks.getFavourite().get(0);
            if (ut.getType()) {
                LocalTrack lt = ut.getLocalTrack();
                imgLoader.DisplayImage(lt.getPath(), backdrop);
            } else {
                Track t = ut.getStreamTrack();
                Picasso.with(getContext())
                        .load(t.getArtworkURL())
                        .resize(100, 100)
                        .error(R.drawable.ic_default)
                        .placeholder(R.drawable.ic_default)
                        .into(backdrop);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RefWatcher refWatcher = MusicDNAApplication.getRefWatcher(getContext());
        refWatcher.watch(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RefWatcher refWatcher = MusicDNAApplication.getRefWatcher(getContext());
        refWatcher.watch(this);
    }

    // 更新数据
    public void updateData() {
        if (HomeActivity.favouriteTracks.getFavourite().size() == 0) {
            favouriteRecycler.setVisibility(View.INVISIBLE);
            playAll.setVisibility(View.INVISIBLE);
            noFavouriteContent.setVisibility(View.VISIBLE);
        } else {
            favouriteRecycler.setVisibility(View.VISIBLE);
            playAll.setVisibility(View.VISIBLE);
            noFavouriteContent.setVisibility(View.INVISIBLE);
        }
        if (fAdapter != null) {
            fAdapter.notifyDataSetChanged();
        }
    }

}
