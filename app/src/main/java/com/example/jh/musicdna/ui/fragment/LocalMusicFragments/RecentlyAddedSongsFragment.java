package com.example.jh.musicdna.ui.fragment.LocalMusicFragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;

import com.example.jh.musicdna.MusicDNAApplication;
import com.example.jh.musicdna.R;
import com.example.jh.musicdna.clickitemtouchlistener.ClickItemTouchListener;
import com.example.jh.musicdna.custombottomsheets.CustomLocalBottomSheetDialog;
import com.example.jh.musicdna.models.LocalTrack;
import com.example.jh.musicdna.models.UnifiedTrack;
import com.example.jh.musicdna.ui.activity.HomeActivity;
import com.example.jh.musicdna.ui.fragment.LocalMusicFragments.localmusic.LocalTrackRecyclerAdapter;
import com.example.jh.musicdna.utils.CommonUtils;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by jinhui on 2018/2/16.
 * Email:1004260403@qq.com
 */

public class RecentlyAddedSongsFragment extends Fragment {

    LocalTrackRecyclerAdapter adapter;
    OnLocalTrackSelectedListener mCallback;
    Context ctx;

    ShowcaseView showCase;

    RecyclerView lv;
    LinearLayoutManager mLayoutManager2;

    FloatingActionButton playAllFAB;

    HomeActivity activity;

    View bottomMarginLayout;

    public interface OnLocalTrackSelectedListener {
        void onLocalTrackSelected(int position);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnLocalTrackSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
        ctx = context;
        activity = (HomeActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_recently_added_songs, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomMarginLayout = view.findViewById(R.id.bottom_margin_layout);
        if (HomeActivity.isReloaded) {
            bottomMarginLayout.getLayoutParams().height = 0;
        } else {
            bottomMarginLayout.getLayoutParams().height = CommonUtils.dpTopx(65, getContext());
        }

        playAllFAB = (FloatingActionButton) view.findViewById(R.id.play_all_fab_local);

        if (HomeActivity.recentlyAddedTrackList.size() == 0) {
            playAllFAB.setVisibility(View.INVISIBLE);
        }

        playAllFAB.setBackgroundTintList(ColorStateList.valueOf(HomeActivity.themeColor));
        playAllFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.queue.getQueue().clear();
                for (int i = 0; i < HomeActivity.recentlyAddedTrackList.size(); i++) {
                    UnifiedTrack ut = new UnifiedTrack(true, HomeActivity.recentlyAddedTrackList.get(i), null);
                    HomeActivity.queue.getQueue().add(ut);
                }
                HomeActivity.queueCurrentIndex = 0;
                LocalTrack track = HomeActivity.recentlyAddedTrackList.get(0);
                HomeActivity.localSelectedTrack = track;
                HomeActivity.streamSelected = false;
                HomeActivity.localSelected = true;
                HomeActivity.queueCall = false;
                HomeActivity.isReloaded = false;
                mCallback.onLocalTrackSelected(-1);
            }
        });

        lv = (RecyclerView) view.findViewById(R.id.recentlyAddedLocalMusicList);
        adapter = new LocalTrackRecyclerAdapter(HomeActivity.finalRecentlyAddedTrackSearchResultList, getContext());
        mLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        lv.setLayoutManager(mLayoutManager2);
        lv.setItemAnimator(new DefaultItemAnimator());
        lv.setAdapter(adapter);

        lv.addOnItemTouchListener(new ClickItemTouchListener(lv) {
            @Override
            public boolean onClick(RecyclerView parent, View view, int position, long id) {

                if (position >= 0) {
                    HomeActivity.queue.getQueue().clear();
                    for (int i = 0; i < HomeActivity.recentlyAddedTrackList.size(); i++) {
                        UnifiedTrack ut = new UnifiedTrack(true, HomeActivity.recentlyAddedTrackList.get(i), null);
                        HomeActivity.queue.getQueue().add(ut);
                    }
                    HomeActivity.queueCurrentIndex = getPosition(HomeActivity.finalRecentlyAddedTrackSearchResultList.get(position));
                    LocalTrack track = HomeActivity.finalRecentlyAddedTrackSearchResultList.get(position);
                    HomeActivity.localSelectedTrack = track;
                    HomeActivity.streamSelected = false;
                    HomeActivity.localSelected = true;
                    HomeActivity.queueCall = false;
                    HomeActivity.isReloaded = false;
                    mCallback.onLocalTrackSelected(-1);
                }

                return true;
            }

            @Override
            public boolean onLongClick(RecyclerView parent, View view, final int position, long id) {
                if (position >= 0) {
                    CustomLocalBottomSheetDialog localBottomSheetDialog = new CustomLocalBottomSheetDialog();
                    localBottomSheetDialog.setPosition(position);
                    localBottomSheetDialog.setLocalTrack(activity.finalRecentlyAddedTrackSearchResultList.get(position));
                    localBottomSheetDialog.show(activity.getSupportFragmentManager(), "local_song_bottom_sheet");
                }
                return true;
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        Button mEndButton = new Button(getContext());
        mEndButton.setBackgroundColor(HomeActivity.themeColor);
        mEndButton.setTextColor(Color.WHITE);

    }

    @Override
    public void onResume() {
        super.onResume();
        mLayoutManager2.scrollToPositionWithOffset(0, 0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                playAllFAB.animate().scaleX(1.0f).scaleY(1.0f).setDuration(300).setInterpolator(new OvershootInterpolator());
            }
        }, 500);
    }


    public int getPosition(LocalTrack lt) {
        for (int i = 0; i < HomeActivity.recentlyAddedTrackList.size(); i++) {
            if (HomeActivity.recentlyAddedTrackList.get(i).getTitle().equals(lt.getTitle())) {
                return i;
            }
        }
        return -1;
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

    public void hidePlayAllFab() {
        if (playAllFAB != null)
            playAllFAB.setVisibility(View.INVISIBLE);
    }

    public void showPlayAllFab() {
        if (playAllFAB != null)
            playAllFAB.setVisibility(View.VISIBLE);
    }

    public void updateAdapter() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

}
