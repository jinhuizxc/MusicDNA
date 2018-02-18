package com.example.jh.musicdna.ui.fragment.LocalMusicFragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jh.musicdna.MusicDNAApplication;
import com.example.jh.musicdna.R;
import com.example.jh.musicdna.ui.activity.HomeActivity;
import com.example.jh.musicdna.ui.activity.SplashActivity;
import com.example.jh.musicdna.ui.fragment.LocalMusicFragments.album.AlbumFragment;
import com.example.jh.musicdna.ui.fragment.LocalMusicFragments.artist.ArtistFragment;
import com.example.jh.musicdna.ui.fragment.LocalMusicFragments.localmusic.LocalMusicFragment;
import com.squareup.leakcanary.RefWatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jinhui on 2018/2/15.
 * Email:1004260403@qq.com
 */

public class LocalMusicViewPagerFragment extends Fragment {

    private static final String TAG = LocalMusicViewPagerFragment.class.getSimpleName();
    ViewPager viewPager;
    MyPageAdapter adapter;
    TabLayout tabLayout;

    ImageView backBtn;
    public ImageView searchIcon;
    public TextView fragTitle;
    public EditText searchBox;

    public boolean isSearchboxVisible = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_full_local_music, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ((HomeActivity) getActivity()).onQueryTextChange("");
        isSearchboxVisible = false;

        // 返回键
        backBtn = (ImageView) view.findViewById(R.id.local_fragment_back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        fragTitle = (TextView) view.findViewById(R.id.local_fragment_title);
        if (SplashActivity.tf4 != null)
            fragTitle.setTypeface(SplashActivity.tf4);

        searchBox = (EditText) view.findViewById(R.id.local_fragment_search_box);
        searchBox.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                ((HomeActivity) getActivity()).onQueryTextChange(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        searchIcon = (ImageView) view.findViewById(R.id.local_fragment_search_icon);
        searchIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSearchboxVisible) {
                    searchBox.setText("");
                    searchBox.setVisibility(View.INVISIBLE);
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    searchIcon.setImageResource(R.drawable.ic_search);
                    fragTitle.setVisibility(View.VISIBLE);
                } else {
                    searchBox.setVisibility(View.VISIBLE);
                    searchBox.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
                    searchIcon.setImageResource(R.drawable.ic_cross_white);
                    fragTitle.setVisibility(View.INVISIBLE);
                }
                isSearchboxVisible = !isSearchboxVisible;
            }
        });

        viewPager = (ViewPager) view.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setBackgroundColor(Color.parseColor("#111111"));
        tabLayout.setSelectedTabIndicatorColor(HomeActivity.themeColor);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        adapter = new MyPageAdapter(getChildFragmentManager());
        adapter.addFragment(new LocalMusicFragment(), getString(R.string.Songs));
        adapter.addFragment(new AlbumFragment(), getString(R.string.Albums));
        adapter.addFragment(new ArtistFragment(), getString(R.string.Artists));
        adapter.addFragment(new RecentlyAddedSongsFragment(), getString(R.string.Recent));
    }

    // 适配器继承FragmentPagerAdapter
    private class MyPageAdapter extends FragmentPagerAdapter {

        private List<Fragment> fragments = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return this.fragments.get(position);
        }

        @Override
        public int getCount() {
            return this.fragments.size();
        }

        // 复写gettitle方法
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        public void addFragment(Fragment fragment, String title) {
            fragments.add(fragment);
            mFragmentTitleList.add(title);
            Log.e(TAG, "mFragmentTitleList = " + mFragmentTitleList);
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

    public Fragment getFragmentByPosition(int position) {
        return (adapter != null) ? adapter.getItem(position) : null;
    }
}
