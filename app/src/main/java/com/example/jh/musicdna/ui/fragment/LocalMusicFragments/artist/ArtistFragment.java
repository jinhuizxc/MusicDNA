package com.example.jh.musicdna.ui.fragment.LocalMusicFragments.artist;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.jh.musicdna.MusicDNAApplication;
import com.example.jh.musicdna.R;
import com.example.jh.musicdna.clickitemtouchlistener.ClickItemTouchListener;
import com.example.jh.musicdna.ui.activity.HomeActivity;
import com.example.jh.musicdna.ui.fragment.LocalMusicFragments.artist.ArtistRecyclerAdapter;
import com.squareup.leakcanary.RefWatcher;

/**
 * Created by jinhui on 2018/2/16.
 * Email:1004260403@qq.com
 *
 * 艺术家页
 */

public class ArtistFragment extends Fragment {

    public ArtistRecyclerAdapter arAdapter;
    public RecyclerView rv;

    public onArtistClickListener mCallback;
    LinearLayoutManager llManager;

    View bottomMarginLayout;

    public interface onArtistClickListener {
        public void onArtistClick();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (onArtistClickListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artist, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomMarginLayout = view.findViewById(R.id.bottom_margin_layout);
        if (HomeActivity.isReloaded)
            bottomMarginLayout.setVisibility(View.GONE);
        else
            bottomMarginLayout.setVisibility(View.VISIBLE);

        rv = (RecyclerView) view.findViewById(R.id.artists_recycler);
        arAdapter = new ArtistRecyclerAdapter(HomeActivity.finalArtists);
        llManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(llManager);
        rv.setItemAnimator(new DefaultItemAnimator());
        rv.setAdapter(arAdapter);

        rv.addOnItemTouchListener(new ClickItemTouchListener(rv) {
            @Override
            public boolean onClick(RecyclerView parent, View view, int position, long id) {
                HomeActivity.tempArtist = HomeActivity.finalArtists.get(position);
                mCallback.onArtistClick();
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

    }

    @Override
    public void onResume() {
        super.onResume();
        llManager.scrollToPositionWithOffset(0, 0);
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

    public void updateAdapter() {
        if (arAdapter != null)
            arAdapter.notifyDataSetChanged();
    }
}
