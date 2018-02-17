package com.example.jh.musicdna.ui.fragment.StreamFragment;

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

import com.example.jh.musicdna.R;
import com.example.jh.musicdna.clickitemtouchlistener.ClickItemTouchListener;
import com.example.jh.musicdna.custombottomsheets.CustomGeneralBottomSheetDialog;
import com.example.jh.musicdna.models.Track;
import com.example.jh.musicdna.models.UnifiedTrack;
import com.example.jh.musicdna.ui.activity.HomeActivity;
import com.example.jh.musicdna.utils.CommonUtils;

/**
 * Created by jinhui on 2018/2/15.
 * Email:1004260403@qq.com
 */

public class StreamMusicFragment extends Fragment {

    public static StreamTrackListAdapter adapter;
    OnTrackSelectedListener mCallback;
    Context ctx;

    RecyclerView lv;

    View bottomMarginLayout;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mCallback = (OnTrackSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
        ctx = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_stream_music, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bottomMarginLayout = view.findViewById(R.id.bottom_margin_layout);
        if (HomeActivity.isReloaded)
            bottomMarginLayout.getLayoutParams().height = 0;
        else
            bottomMarginLayout.getLayoutParams().height = CommonUtils.dpTopx(65, getContext());

        lv = (RecyclerView) view.findViewById(R.id.trackList);

        adapter = new StreamTrackListAdapter(getContext(), HomeActivity.streamingTrackList);
        LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        lv.setLayoutManager(mLayoutManager2);
        lv.setItemAnimator(new DefaultItemAnimator());
        lv.setAdapter(adapter);

        // lv的itemtouch监听，暂时注释掉
        lv.addOnItemTouchListener(new ClickItemTouchListener(lv) {
            @Override
            public boolean onClick(RecyclerView parent, View view, int position, long id) {
                Track track = HomeActivity.streamingTrackList.get(position);
                if (HomeActivity.queue.getQueue().size() == 0) {
                    HomeActivity.queueCurrentIndex = 0;
                    HomeActivity.queue.getQueue().add(new UnifiedTrack(false, null, track));
                } else if (HomeActivity.queueCurrentIndex == HomeActivity.queue.getQueue().size() - 1) {
                    HomeActivity.queueCurrentIndex++;
                    HomeActivity.queue.getQueue().add(new UnifiedTrack(false, null, track));
                } else if (HomeActivity.isReloaded) {
                    HomeActivity.isReloaded = false;
                    HomeActivity.queueCurrentIndex = HomeActivity.queue.getQueue().size();
                    HomeActivity.queue.getQueue().add(new UnifiedTrack(false, null, track));
                } else {
                    HomeActivity.queue.getQueue().add(++HomeActivity.queueCurrentIndex, new UnifiedTrack(false, null, track));
                }
                HomeActivity.selectedTrack = track;
                HomeActivity.streamSelected = true;
                HomeActivity.localSelected = false;
                HomeActivity.queueCall = false;
                HomeActivity.isReloaded = false;
                mCallback.onTrackSelected(position);
                return true;
            }

            @Override
            public boolean onLongClick(RecyclerView parent, View view, final int position, long id) {
                CustomGeneralBottomSheetDialog generalBottomSheetDialog = new CustomGeneralBottomSheetDialog();
                generalBottomSheetDialog.setPosition(position);
                generalBottomSheetDialog.setTrack(new UnifiedTrack(false, null, HomeActivity.streamingTrackList.get(position)));
                generalBottomSheetDialog.setFragment("Stream");
                generalBottomSheetDialog.show(getActivity().getSupportFragmentManager(), "general_bottom_sheet_dialog");
                return true;
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

    }

    public void dataChanged() {
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    public interface OnTrackSelectedListener {
        void onTrackSelected(int position);
    }

}
