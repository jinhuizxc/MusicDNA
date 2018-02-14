package com.example.jh.musicdna.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jh.musicdna.R;
import com.example.jh.musicdna.broadcastreceiver.HeadSetReceiver;
import com.example.jh.musicdna.models.SavedDNA;
import com.example.jh.musicdna.models.Track;
import com.example.jh.musicdna.ui.fragment.AboutFragment.AboutFragment;
import com.example.jh.musicdna.ui.fragment.AllPlaylistsFragment.AllPlaylistsFragment;
import com.example.jh.musicdna.ui.fragment.EditLocalSongFragment;
import com.example.jh.musicdna.ui.fragment.EqualizerFragment.EqualizerFragment;
import com.example.jh.musicdna.ui.fragment.FavouritesFragment.FavouritesFragment;
import com.example.jh.musicdna.ui.fragment.FolderContentFragment.FolderContentFragment;
import com.example.jh.musicdna.ui.fragment.FolderFragment.FolderFragment;
import com.example.jh.musicdna.ui.fragment.LocalMusicViewPagerFragment.LocalMusicViewPagerFragment;
import com.example.jh.musicdna.ui.fragment.NewPlaylistFragment.NewPlaylistFragment;
import com.example.jh.musicdna.ui.fragment.QueueFragment.QueueFragment;
import com.example.jh.musicdna.ui.fragment.RecentsFragment.RecentsFragment;
import com.example.jh.musicdna.ui.fragment.SettingsFragment.SettingsFragment;
import com.example.jh.musicdna.ui.fragment.StreamMusicFragment.StreamMusicFragment;
import com.example.jh.musicdna.ui.fragment.ViewAlbumFragment.ViewAlbumFragment;
import com.example.jh.musicdna.ui.fragment.ViewArtistFragment.ViewArtistFragment;
import com.example.jh.musicdna.ui.fragment.ViewPlaylistFragment.ViewPlaylistFragment;
import com.example.jh.musicdna.ui.fragment.ViewSavedDNAFragment.ViewSavedDNAFragment;
import com.example.jh.musicdna.utils.CommonUtils;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.lantouzi.wheelview.WheelView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

import static android.view.View.GONE;

/**
 * Created by jinhui on 2018/2/14.
 * Email:1004260403@qq.com
 */

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SearchView.OnQueryTextListener {

    public static int themeColor = Color.parseColor("#B24242");

    public static int screen_width;
    public static int screen_height;
    public static float ratio;
    public static float ratio2;

    public HeadSetReceiver headSetReceiver;

    public DrawerLayout drawer;
    Toolbar toolbar;

    NavigationView navigationView;
    // 搜索menu
    MenuItem searchItem;
    // 搜索view
    SearchView searchView;

    // 睡眠计时器的时刻列表
    List<String> minuteList;
    boolean isSleepTimerEnabled = false;
    boolean isSleepTimerTimeout = false;
    long timerSetTime = 0;
    int timerTimeOutDuration = 0;
    Handler sleepHandler;

    public Context ctx;
    // 显示引导页
    ShowcaseView showCase;
    Button mEndButton;
    public static TextPaint tp;
    public static RelativeLayout.LayoutParams lps;
    public static int statusBarHeightinDp;
    public static int navBarHeightSizeinDp;
    public static boolean hasSoftNavbar = false;

    RelativeLayout localBanner;
    RelativeLayout localRecyclerContainer;
    RelativeLayout recentsRecyclerContainer;
    RelativeLayout streamRecyclerContainer;
    RelativeLayout playlistRecyclerContainer;

    public static boolean isPlayerVisible = false;
    public static boolean isLocalVisible = false;
    public static boolean isStreamVisible = false;
    public static boolean isQueueVisible = false;
    public static boolean isPlaylistVisible = false;
    public static boolean isEqualizerVisible = false;
    public static boolean isFavouriteVisible = false;
    public static boolean isAllPlaylistVisible = false;
    public static boolean isAllFolderVisible = false;
    public static boolean isFolderContentVisible = false;
    public static boolean isAllSavedDnaVisisble = false;
    public static boolean isSavedDNAVisible = false;
    public static boolean isAlbumVisible = false;
    public static boolean isArtistVisible = false;
    public static boolean isRecentVisible = false;
    public static boolean isFullScreenEnabled = false;
    public static boolean isSettingsVisible = false;
    public static boolean isNewPlaylistVisible = false;
    public static boolean isAboutVisible = false;
    public static boolean isEditVisible = false;

    // 保存DNA
    public static SavedDNA tempSavedDNA;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        assert wm != null;
        Display display = wm.getDefaultDisplay();
        screen_width = display.getWidth();
        screen_height = display.getHeight();

        ratio = (float) screen_height / (float) 1920;
        ratio2 = (float) screen_width / (float) 1080;
        ratio = Math.min(ratio, ratio2);

        setContentView(R.layout.activity_home);
        initView();
        // 动态注册广播
//        headSetReceiver = new HeadSetReceiver();
//        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
//        registerReceiver(headSetReceiver, filter);
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView != null) {
            navigationView.setNavigationItemSelectedListener(this);
        }
        // 设置打开navigation显示选中的item项。
        navigationView.setCheckedItem(R.id.nav_home);

        ctx = this;
        minuteList = new ArrayList<>();
        for (int i = 1; i < 25; i++) {
            minuteList.add(String.valueOf(i * 5));
        }

        // 引导页的配置
        mEndButton = new Button(this);
        mEndButton.setBackgroundColor(themeColor);
        mEndButton.setTextColor(Color.WHITE);

        tp = new TextPaint();
        tp.setColor(themeColor);
        tp.setTextSize(65 * ratio);
        tp.setFakeBoldText(true);

        hasSoftNavbar = CommonUtils.hasNavBar(this);
        statusBarHeightinDp = CommonUtils.getStatusBarHeight(this);
        navBarHeightSizeinDp = hasSoftNavbar ? CommonUtils.getNavBarHeight(this) : 0;

        lps = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lps.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        lps.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        int margin = ((Number) (getResources().getDisplayMetrics().density * 12)).intValue();
//        lps.setMargins(margin, margin, margin, navBarHeightSizeinDp + ((Number) (getResources().getDisplayMetrics().density * 5)).intValue());
        lps.setMargins(margin, margin, margin, navBarHeightSizeinDp + ((Number) (getResources().getDisplayMetrics().density * 40)).intValue());

        showCase = new ShowcaseView.Builder(this)
                .blockAllTouches()
                .singleShot(0)
                .setStyle(R.style.CustomShowcaseTheme)
                .useDecorViewAsParent()
                .replaceEndButton(mEndButton)
                .setContentTitlePaint(tp)
                .setTarget(new ViewTarget(R.id.recentsRecyclerLabel, this))
                .setContentTitle("Recents and Playlists")
                .setContentText("Here all you recent songs and playlists will be listed." +
                        "Long press the cards or playlists for more options \n" +
                        "\n" +
                        "(Press Next to continue / Press back to Hide)")
                .build();
        showCase.setButtonText("Next");
        showCase.setButtonPosition(lps);
        showCase.overrideButtonClick(new View.OnClickListener() {
            int count1 = 0;
            @Override
            public void onClick(View v) {
                count1++;
                switch (count1) {
                    case 1:
                        showCase.setTarget(new ViewTarget(R.id.local_banner_alt_showcase, (Activity) ctx));
                        showCase.setContentTitle("本地音乐");
                        showCase.setContentText("See all songs available locally, classified on basis of Artist and Album");
                        showCase.setButtonPosition(lps);
                        showCase.setButtonText("Next");
                        break;
                    case 2:
                        showCase.setTarget(new ViewTarget(searchView.getId(), (Activity) ctx));
                        showCase.setContentTitle("搜索");
                        showCase.setContentText("Search for songs from local library and SoundCloud™");
                        showCase.setButtonPosition(lps);
                        showCase.setButtonText("Done");
                        break;
                    case 3:
                        showCase.hide();
                        break;
                }
            }
        });

        localBanner = (RelativeLayout) findViewById(R.id.localBanner);
        localBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment("local");
            }
        });

        localRecyclerContainer = (RelativeLayout) findViewById(R.id.localRecyclerContainer);
        recentsRecyclerContainer = (RelativeLayout) findViewById(R.id.recentsRecyclerContainer);
        streamRecyclerContainer = (RelativeLayout) findViewById(R.id.streamRecyclerContainer);
        playlistRecyclerContainer = (RelativeLayout) findViewById(R.id.playlistRecyclerContainer);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            hideAllFrags();
            hideFragment("allPlaylists");
        } else if (id == R.id.nav_local) {
            showFragment("local");
        } else if (id == R.id.nav_playlists) {
            showFragment("allPlaylists");
        } else if (id == R.id.nav_recent) {
            showFragment("recent");
        } else if (id == R.id.nav_fav) {
            showFragment("favourite");
        } else if (id == R.id.nav_folder) {
            showFragment("allFolders");
        } else if (id == R.id.nav_view) {
            showFragment("allSavedDNAs");
        } else if (id == R.id.nav_settings) {
            showFragment("settings");
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * 显示各部分fragment
     * @param type
     */
    private void showFragment(String type) {

        if (!type.equals("viewAlbum") && !type.equals("folderContent") && !type.equals("viewArtist") && !type.equals("playlist") && !type.equals("newPlaylist") && !type.equals("About") && !type.equals("Edit"))
            hideAllFrags();

        if (!searchView.isIconified()) {
            searchView.setQuery("", true);
            searchView.setIconified(true);
            streamRecyclerContainer.setVisibility(GONE);
            new Thread(new CancelCall()).start();
        }

        // 本地音乐
        if (type.equals("local") && !isLocalVisible) {
            navigationView.setCheckedItem(R.id.nav_local);
            isLocalVisible = true;
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            LocalMusicViewPagerFragment newFragment = (LocalMusicViewPagerFragment) fm.findFragmentByTag("local");
            if (newFragment == null) {
                newFragment = new LocalMusicViewPagerFragment();
            }
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.slide_left,
                            R.anim.slide_right,
                            R.anim.slide_left,
                            R.anim.slide_right)
                    .add(R.id.fragContainer, newFragment, "local")
                    .show(newFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        } else if (type.equals("queue") && !isQueueVisible) {
            hideAllFrags();
            isQueueVisible = true;
            FragmentManager fm = getSupportFragmentManager();
            QueueFragment newFragment = (QueueFragment) fm.findFragmentByTag("queue");
            if (newFragment == null) {
                newFragment = new QueueFragment();
            }
            fm.beginTransaction()
                    .add(R.id.equalizer_queue_frag_container, newFragment, "queue")
                    .show(newFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        } else if (type.equals("stream") && !isStreamVisible) {
            isStreamVisible = true;
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            StreamMusicFragment newFragment = (StreamMusicFragment) fm.findFragmentByTag("stream");
            if (newFragment == null) {
                newFragment = new StreamMusicFragment();
            }
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.slide_left,
                            R.anim.slide_right,
                            R.anim.slide_left,
                            R.anim.slide_right)
                    .add(R.id.fragContainer, newFragment, "stream")
                    .show(newFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        } else if (type.equals("playlist") && !isPlaylistVisible) {
            isPlaylistVisible = true;
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            ViewPlaylistFragment newFragment = (ViewPlaylistFragment) fm.findFragmentByTag("playlist");
            if (newFragment == null) {
                newFragment = new ViewPlaylistFragment();
            }
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.slide_left,
                            R.anim.slide_right,
                            R.anim.slide_left,
                            R.anim.slide_right)
                    .add(R.id.content_frag, newFragment, "playlist")
                    .show(newFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        } else if (type.equals("equalizer") && !isEqualizerVisible) {
            isEqualizerVisible = true;
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            EqualizerFragment newFragment = (EqualizerFragment) fm.findFragmentByTag("equalizer");
            if (newFragment == null) {
                newFragment = new EqualizerFragment();
            }
            fm.beginTransaction()
                    .add(R.id.equalizer_queue_frag_container, newFragment, "equalizer")
                    .show(newFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        } else if (type.equals("favourite") && !isFavouriteVisible) {
            navigationView.setCheckedItem(R.id.nav_fav);
            isFavouriteVisible = true;
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            FavouritesFragment newFragment = (FavouritesFragment) fm.findFragmentByTag("favourite");
            if (newFragment == null) {
                newFragment = new FavouritesFragment();
            }
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.slide_left,
                            R.anim.slide_right,
                            R.anim.slide_left,
                            R.anim.slide_right)
                    .add(R.id.fragContainer, newFragment, "favourite")
                    .show(newFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        } else if (type.equals("newPlaylist") && !isNewPlaylistVisible) {
            navigationView.setCheckedItem(R.id.nav_playlists);
            isNewPlaylistVisible = true;
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            NewPlaylistFragment newFragment = (NewPlaylistFragment) fm.findFragmentByTag("newPlaylist");
            if (newFragment == null) {
                newFragment = new NewPlaylistFragment();
            }
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.slide_left,
                            R.anim.slide_right,
                            R.anim.slide_left,
                            R.anim.slide_right)
                    .add(R.id.content_frag, newFragment, "newPlaylist")
                    .show(newFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        } else if (type.equals("allPlaylists") && !isAllPlaylistVisible) {
            navigationView.setCheckedItem(R.id.nav_playlists);
            isAllPlaylistVisible = true;
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            AllPlaylistsFragment newFragment = (AllPlaylistsFragment) fm.findFragmentByTag("allPlaylists");
            if (newFragment == null) {
                newFragment = new AllPlaylistsFragment();
            }
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.slide_left,
                            R.anim.slide_right,
                            R.anim.slide_left,
                            R.anim.slide_right)
                    .add(R.id.fragContainer, newFragment, "allPlaylists")
                    .show(newFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        } else if (type.equals("folderContent") && !isFolderContentVisible) {
            isFolderContentVisible = true;
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            FolderContentFragment newFragment = (FolderContentFragment) fm.findFragmentByTag("folderContent");
            if (newFragment == null) {
                newFragment = new FolderContentFragment();
            }
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.slide_left,
                            R.anim.slide_right,
                            R.anim.slide_left,
                            R.anim.slide_right)
                    .add(R.id.content_frag, newFragment, "folderContent")
                    .show(newFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        } else if (type.equals("allFolders") && !isAllFolderVisible) {
            navigationView.setCheckedItem(R.id.nav_folder);
            isAllFolderVisible = true;
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            FolderFragment newFragment = (FolderFragment) fm.findFragmentByTag("allFolders");
            if (newFragment == null) {
                newFragment = new FolderFragment();
            }
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.slide_left,
                            R.anim.slide_right,
                            R.anim.slide_left,
                            R.anim.slide_right)
                    .add(R.id.fragContainer, newFragment, "allFolders")
                    .show(newFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        } else if (type.equals("allSavedDNAs") && !isAllSavedDnaVisisble) {
            navigationView.setCheckedItem(R.id.nav_view);
            isAllSavedDnaVisisble = true;
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            ViewSavedDNAFragment newFragment = (ViewSavedDNAFragment) fm.findFragmentByTag("allSavedDNAs");
            if (newFragment == null) {
                newFragment = new ViewSavedDNAFragment();
            }
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.slide_left,
                            R.anim.slide_right,
                            R.anim.slide_left,
                            R.anim.slide_right)
                    .add(R.id.fragContainer, newFragment, "allSavedDNAs")
                    .show(newFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        } else if (type.equals("viewAlbum") && !isAlbumVisible) {
            isAlbumVisible = true;
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            ViewAlbumFragment newFragment = (ViewAlbumFragment) fm.findFragmentByTag("viewAlbum");
            if (newFragment == null) {
                newFragment = new ViewAlbumFragment();
            }
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.slide_left,
                            R.anim.slide_right,
                            R.anim.slide_left,
                            R.anim.slide_right)
                    .add(R.id.fragContainer, newFragment, "viewAlbum")
                    .show(newFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        } else if (type.equals("viewArtist") && !isArtistVisible) {
            isArtistVisible = true;
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            ViewArtistFragment newFragment = (ViewArtistFragment) fm.findFragmentByTag("viewArtist");
            if (newFragment == null) {
                newFragment = new ViewArtistFragment();
            }
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.slide_left,
                            R.anim.slide_right,
                            R.anim.slide_left,
                            R.anim.slide_right)
                    .add(R.id.fragContainer, newFragment, "viewArtist")
                    .show(newFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        } else if (type.equals("recent") && !isRecentVisible) {
            navigationView.setCheckedItem(R.id.nav_recent);
            isRecentVisible = true;
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            RecentsFragment newFragment = (RecentsFragment) fm.findFragmentByTag("recent");
            if (newFragment == null) {
                newFragment = new RecentsFragment();
            }
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.slide_left,
                            R.anim.slide_right,
                            R.anim.slide_left,
                            R.anim.slide_right)
                    .add(R.id.fragContainer, newFragment, "recent")
                    .show(newFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        } else if (type.equals("settings") && !isSettingsVisible) {
            isSettingsVisible = true;
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            SettingsFragment newFragment = (SettingsFragment) fm.findFragmentByTag("settings");
            if (newFragment == null) {
                newFragment = new SettingsFragment();
            }
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.slide_left,
                            R.anim.slide_right,
                            R.anim.slide_left,
                            R.anim.slide_right)
                    .add(R.id.fragContainer, newFragment, "settings")
                    .show(newFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        } else if (type.equals("About") && !isAboutVisible) {
            setTitle("About");
            isAboutVisible = true;
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            AboutFragment newFragment = (AboutFragment) fm.findFragmentByTag("About");
            if (newFragment == null) {
                newFragment = new AboutFragment();
            }
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.slide_left,
                            R.anim.slide_right,
                            R.anim.slide_left,
                            R.anim.slide_right)
                    .add(R.id.content_frag, newFragment, "About")
                    .show(newFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        } else if (type.equals("Edit") && !isEditVisible) {
            isEditVisible = true;
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            EditLocalSongFragment newFragment = (EditLocalSongFragment) fm.findFragmentByTag("Edit");
            if (newFragment == null) {
                newFragment = new EditLocalSongFragment();
            }
            fm.beginTransaction()
                    .setCustomAnimations(R.anim.slide_left,
                            R.anim.slide_right,
                            R.anim.slide_left,
                            R.anim.slide_right)
                    .add(R.id.content_frag, newFragment, "Edit")
                    .show(newFragment)
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        }
    }

    private void hideAllFrags() {
        hideFragment("local");
        hideFragment("queue");
        hideFragment("stream");
        hideFragment("playlist");
        hideFragment("newPlaylist");
        hideFragment("allPlaylists");
        hideFragment("equalizer");
        hideFragment("favourite");
        hideFragment("folderContent");
        hideFragment("allFolders");
        hideFragment("allSavedDNAs");
        hideFragment("viewAlbum");
        hideFragment("viewArtist");
        hideFragment("recent");
        hideFragment("settings");
        hideFragment("About");

        navigationView.setCheckedItem(R.id.nav_home);

        setTitle("Music DNA");
    }

    private void hideFragment(String type) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        searchItem = menu.findItem(R.id.menu_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showFragment("settings");
            return true;
        }
        if (id == R.id.action_sleep) {
            showSleepDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 显示睡眠计时器的dialog
     */
    private void showSleepDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sleep_timer_dialog);

        final WheelView wheelPicker = (WheelView) dialog.findViewById(R.id.wheelPicker);
        wheelPicker.setItems(minuteList);

        TextView title = (TextView) dialog.findViewById(R.id.sleep_dialog_title_text);
        if (SplashActivity.tf4 != null)
            title.setTypeface(SplashActivity.tf4);

        Button setBtn = (Button) dialog.findViewById(R.id.set_button);
        Button cancelBtn = (Button) dialog.findViewById(R.id.cancel_button);
        final Button removerBtn = (Button) dialog.findViewById(R.id.remove_timer_button);

        final LinearLayout buttonWrapper = (LinearLayout) dialog.findViewById(R.id.button_wrapper);

        final TextView timerSetText = (TextView) dialog.findViewById(R.id.timer_set_text);

        setBtn.setBackgroundColor(themeColor);
        removerBtn.setBackgroundColor(themeColor);
        cancelBtn.setBackgroundColor(Color.WHITE);

        if (isSleepTimerEnabled) {
            wheelPicker.setVisibility(View.GONE);
            buttonWrapper.setVisibility(View.GONE);
            removerBtn.setVisibility(View.VISIBLE);
            timerSetText.setVisibility(View.VISIBLE);

            long currentTime = System.currentTimeMillis();
            long difference = currentTime - timerSetTime;

            int minutesLeft = (int) (timerTimeOutDuration - ((difference / 1000) / 60));
            if (minutesLeft > 1) {
                timerSetText.setText("Timer set for " + minutesLeft + " minutes from now.");
            } else if (minutesLeft == 1) {
                timerSetText.setText("Timer set for " + 1 + " minute from now.");
            } else {
                timerSetText.setText("Music will stop after completion of current song");
            }

        } else {
            wheelPicker.setVisibility(View.VISIBLE);
            buttonWrapper.setVisibility(View.VISIBLE);
            removerBtn.setVisibility(View.GONE);
            timerSetText.setVisibility(View.GONE);
        }

        removerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSleepTimerEnabled = false;
                isSleepTimerTimeout = false;
                timerTimeOutDuration = 0;
                timerSetTime = 0;
                sleepHandler.removeCallbacksAndMessages(null);
                Toast.makeText(ctx, "Timer removed", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        setBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSleepTimerEnabled = true;
                int minutes = Integer.parseInt(wheelPicker.getItems().get(wheelPicker.getSelectedPosition()));
                timerTimeOutDuration = minutes;
                timerSetTime = System.currentTimeMillis();
                sleepHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isSleepTimerTimeout = true;
                        // 暂时注销，待测试
//                        if (playerFragment.mMediaPlayer == null || !playerFragment.mMediaPlayer.isPlaying()) {
//                            main.runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Toast.makeText(ctx, "Sleep timer timed out, closing app", Toast.LENGTH_SHORT).show();
//                                    if (playerFragment != null && playerFragment.timer != null)
//                                        playerFragment.timer.cancel();
//                                    notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                                    try {
//                                        notificationManager.cancel(1);
//                                    } catch (Exception e) {
//                                        e.printStackTrace();
//                                    } finally {
//                                        finish();
//                                    }
//                                }
//                            });
//                        }
                    }
                }, minutes * 60 * 1000);
                Toast.makeText(ctx, "Timer set for " + minutes + " minutes", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSleepTimerEnabled = false;
                isSleepTimerTimeout = false;
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    Call<List<Track>> call;

    private class CancelCall implements Runnable {
        @Override
        public void run() {
            if (call != null)
                call.cancel();
        }
    }
}
