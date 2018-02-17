package com.example.jh.musicdna.custombottomsheets;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.jh.musicdna.R;
import com.example.jh.musicdna.imageloader.ImageLoader;
import com.example.jh.musicdna.models.LocalTrack;
import com.example.jh.musicdna.ui.activity.HomeActivity;

/**
 * Created by jinhui on 2018/2/17.
 * Email:1004260403@qq.com
 */

public class CustomLocalBottomSheetDialog  extends BottomSheetDialogFragment implements View.OnClickListener {

    LinearLayout playText, playNextText, addToQueueText, addToPlaylistText, addToFavouriteText, shareText, editText;

    ImageView localSongImage;
    TextView localSongTitle, localSongArtist;

    HomeActivity activity;

    int position = 0;
    String fragment;
    LocalTrack localTrack;

    ImageLoader imgLoader;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = (HomeActivity) activity;
        imgLoader = new ImageLoader(activity);
    }

    public void setPosition(int pos) {
        position = pos;
    }

    public void setLocalTrack(LocalTrack localTrack) {
        this.localTrack = localTrack;
    }

    public void setFragment(String frag) {
        fragment = frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.local_song_bottom_sheet, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        localSongImage = (ImageView) view.findViewById(R.id.local_song_bottom_sheet_image);
        localSongTitle = (TextView) view.findViewById(R.id.local_song_bottom_sheet_title);
        localSongArtist = (TextView) view.findViewById(R.id.local_song_bottom_sheet_artist);

        imgLoader.DisplayImage(localTrack.getPath(), localSongImage);
        localSongTitle.setText(localTrack.getTitle());
        localSongArtist.setText(localTrack.getArtist());

        playText = (LinearLayout) view.findViewById(R.id.local_song_bottom_sheet_play_wrapper);
        playText.setOnClickListener(this);
        playNextText = (LinearLayout) view.findViewById(R.id.local_song_bottom_sheet_play_next_wrapper);
        playNextText.setOnClickListener(this);
        addToQueueText = (LinearLayout) view.findViewById(R.id.local_song_bottom_sheet_add_to_queue_wrapper);
        addToQueueText.setOnClickListener(this);
        addToPlaylistText = (LinearLayout) view.findViewById(R.id.local_song_bottom_sheet_add_to_playlist_wrapper);
        addToPlaylistText.setOnClickListener(this);
        addToFavouriteText = (LinearLayout) view.findViewById(R.id.local_song_bottom_sheet_add_to_fav_wrapper);
        addToFavouriteText.setOnClickListener(this);
        shareText = (LinearLayout) view.findViewById(R.id.local_song_bottom_sheet_share_wrapper);
        shareText.setOnClickListener(this);
        editText = (LinearLayout) view.findViewById(R.id.local_song_bottom_sheet_edit_wrapper);
        editText.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.local_song_bottom_sheet_play_wrapper:
                activity.bottomSheetListener(position, getString(R.string.Play), fragment, true);
                break;
            case R.id.local_song_bottom_sheet_play_next_wrapper:
                activity.bottomSheetListener(position, getString(R.string.Play_Next), fragment, true);
                break;
            case R.id.local_song_bottom_sheet_add_to_queue_wrapper:
                activity.bottomSheetListener(position, getString(R.string.Add_to_Queue), fragment, true);
                break;
            case R.id.local_song_bottom_sheet_add_to_playlist_wrapper:
                activity.bottomSheetListener(position, getString(R.string.Add_to_Playlist), fragment, true);
                break;
            case R.id.local_song_bottom_sheet_add_to_fav_wrapper:
                activity.bottomSheetListener(position, getString(R.string.Add_to_Favourites), fragment, true);
                break;
            case R.id.local_song_bottom_sheet_share_wrapper:
                activity.bottomSheetListener(position, getString(R.string.Share), fragment, true);
                break;
            case R.id.local_song_bottom_sheet_edit_wrapper:
                activity.bottomSheetListener(position, getString(R.string.EDIT), fragment, true);
                break;
        }
        dismiss();
    }
}

