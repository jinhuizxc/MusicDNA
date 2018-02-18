package com.example.jh.musicdna.ui.fragment.LocalMusicFragments.localmusic;

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
import android.widget.RelativeLayout;

import com.example.jh.musicdna.MusicDNAApplication;
import com.example.jh.musicdna.R;
import com.example.jh.musicdna.clickitemtouchlistener.ClickItemTouchListener;
import com.example.jh.musicdna.custombottomsheets.CustomLocalBottomSheetDialog;
import com.example.jh.musicdna.models.LocalTrack;
import com.example.jh.musicdna.models.UnifiedTrack;
import com.example.jh.musicdna.ui.activity.HomeActivity;
import com.example.jh.musicdna.utils.CommonUtils;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.squareup.leakcanary.RefWatcher;

import java.util.Random;

/**
 * Created by jinhui on 2018/2/16.
 * Email:1004260403@qq.com
 *
 * 歌曲列表
 */

public class LocalMusicFragment extends Fragment {

    LocalTrackRecyclerAdapter adapter;
    OnLocalTrackSelectedListener mCallback;
    Context ctx;

    ShowcaseView showCase;

    RecyclerView lv;
    LinearLayoutManager mLayoutManager2;

    FloatingActionButton shuffleFab;

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
        return inflater.inflate(R.layout.fragment_local_music, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomMarginLayout = view.findViewById(R.id.bottom_margin_layout);
        if (HomeActivity.isReloaded)
            bottomMarginLayout.getLayoutParams().height = 0;
        else
            bottomMarginLayout.getLayoutParams().height = CommonUtils.dpTopx(65, getContext());

        shuffleFab = (FloatingActionButton) view.findViewById(R.id.play_all_fab_local);

        // 本地音乐列表为0时: FloatingActionButton不可见
//        if (HomeActivity.localTrackList.size() == 0) {
//            shuffleFab.setVisibility(View.INVISIBLE);
//        }
        if (HomeActivity.localTrackList.size() == 0) {
            shuffleFab.setVisibility(View.INVISIBLE);
        }

        shuffleFab.setBackgroundTintList(ColorStateList.valueOf(HomeActivity.themeColor));
        shuffleFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeActivity.queue.getQueue().clear();
                for (int i = 0; i < HomeActivity.localTrackList.size(); i++) {
                    UnifiedTrack ut = new UnifiedTrack(true, HomeActivity.localTrackList.get(i), null);
                    HomeActivity.queue.getQueue().add(ut);
                }
                if (HomeActivity.queue.getQueue().size() > 0) {
                    Random r = new Random();
                    int tmp = r.nextInt(HomeActivity.queue.getQueue().size());
                    HomeActivity.queueCurrentIndex = tmp;
                    LocalTrack track = HomeActivity.localTrackList.get(tmp);
                    HomeActivity.localSelectedTrack = track;
                    HomeActivity.streamSelected = false;
                    HomeActivity.localSelected = true;
                    HomeActivity.queueCall = false;
                    HomeActivity.isReloaded = false;
                    mCallback.onLocalTrackSelected(-1);
                }
            }
        });

        lv = (RecyclerView) view.findViewById(R.id.localMusicList);
        adapter = new LocalTrackRecyclerAdapter(HomeActivity.finalLocalSearchResultList, getContext());
        mLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        lv.setLayoutManager(mLayoutManager2);
        lv.setItemAnimator(new DefaultItemAnimator());
        lv.setAdapter(adapter);

        lv.addOnItemTouchListener(new ClickItemTouchListener(lv) {
            @Override
            public boolean onClick(RecyclerView parent, View view, int position, long id) {

                if (position >= 0) {
                    HomeActivity.queue.getQueue().clear();
                    for (int i = 0; i < HomeActivity.localTrackList.size(); i++) {
                        UnifiedTrack ut = new UnifiedTrack(true, HomeActivity.localTrackList.get(i), null);
                        HomeActivity.queue.getQueue().add(ut);
                    }
                    HomeActivity.queueCurrentIndex = getPosition(HomeActivity.finalLocalSearchResultList.get(position));
                    LocalTrack track = HomeActivity.finalLocalSearchResultList.get(position);
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
                    localBottomSheetDialog.setLocalTrack(activity.finalLocalSearchResultList.get(position));
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

        showCase = new ShowcaseView.Builder(getActivity())
                .blockAllTouches()
                .singleShot(1)
                .setStyle(R.style.CustomShowcaseTheme)
                .useDecorViewAsParent()
                .replaceEndButton(mEndButton)
                .setContentTitlePaint(HomeActivity.tp)
                .setTarget(new ViewTarget(R.id.songs_tab_alt_showcase, getActivity()))
                .setContentTitle("All Songs")
                .setContentText("All local Songs listed here.Click to Play.Long click for more options")
                .build();
        showCase.setButtonText("下一步");
        showCase.setButtonPosition(HomeActivity.lps);
        showCase.overrideButtonClick(new View.OnClickListener() {
            int count1 = 0;

            @Override
            public void onClick(View v) {
                count1++;
                switch (count1) {
                    case 1:
                        RelativeLayout.LayoutParams lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                        lps.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
//                        lps.setMargins(margin, margin, margin, 5 + HomeActivity.navBarHeightSizeinDp);
                        lps.setMargins(margin, margin, margin, ((Number) (getResources().getDisplayMetrics().density * 40)).intValue() + HomeActivity.navBarHeightSizeinDp);
                        showCase.setTarget(new ViewTarget(shuffleFab.getId(), getActivity()));
                        showCase.setContentTitle("Shuffle");
                        showCase.setContentText("Play all songs, shuffled randomly");
                        showCase.setButtonText("Done");
                        showCase.setButtonPosition(lps);
                        break;
                    case 2:
                        showCase.hide();
                        break;
                }
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mLayoutManager2.scrollToPositionWithOffset(0, 0);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                shuffleFab.animate().scaleX(1.0f).scaleY(1.0f).setDuration(300).setInterpolator(new OvershootInterpolator());
            }
        }, 500);
    }

    /**
     * 得到位置
     * @param lt
     * @return
     */
    public int getPosition(LocalTrack lt) {
        for (int i = 0; i < HomeActivity.localTrackList.size(); i++) {
            if (HomeActivity.localTrackList.get(i).getTitle().equals(lt.getTitle())) {
                return i;
            }
        }
        return -1;
    }

    public void hideShuffleFab() {
        if (shuffleFab != null)
            shuffleFab.setVisibility(View.INVISIBLE);
    }

    public void showShuffleFab() {
        if (shuffleFab != null)
            shuffleFab.setVisibility(View.VISIBLE);
    }

    public void updateAdapter() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    public boolean isShowcaseVisible() {
        return (showCase != null && showCase.isShowing());
    }

    public void hideShowcase() {
        showCase.hide();
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

}
