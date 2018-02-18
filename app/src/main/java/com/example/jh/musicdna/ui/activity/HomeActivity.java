package com.example.jh.musicdna.ui.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextPaint;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jh.musicdna.Config;
import com.example.jh.musicdna.R;
import com.example.jh.musicdna.broadcastreceiver.HeadSetReceiver;
import com.example.jh.musicdna.clickitemtouchlistener.ClickItemTouchListener;
import com.example.jh.musicdna.custombottomsheets.CustomLocalBottomSheetDialog;
import com.example.jh.musicdna.imageloader.ImageLoader;
import com.example.jh.musicdna.interfaces.StreamService;
import com.example.jh.musicdna.models.Album;
import com.example.jh.musicdna.models.AllMusicFolders;
import com.example.jh.musicdna.models.AllPlaylists;
import com.example.jh.musicdna.models.AllSavedDNA;
import com.example.jh.musicdna.models.Artist;
import com.example.jh.musicdna.models.EqualizerModel;
import com.example.jh.musicdna.models.Favourite;
import com.example.jh.musicdna.models.LocalTrack;
import com.example.jh.musicdna.models.MusicFolder;
import com.example.jh.musicdna.models.Playlist;
import com.example.jh.musicdna.models.Queue;
import com.example.jh.musicdna.models.RecentlyPlayed;
import com.example.jh.musicdna.models.SavedDNA;
import com.example.jh.musicdna.models.Settings;
import com.example.jh.musicdna.models.Track;
import com.example.jh.musicdna.models.UnifiedTrack;
import com.example.jh.musicdna.ui.adapter.horizontalrecycleradapters.LocalTracksHorizontalAdapter;
import com.example.jh.musicdna.ui.adapter.horizontalrecycleradapters.PlayListsHorizontalAdapter;
import com.example.jh.musicdna.ui.adapter.horizontalrecycleradapters.RecentsListHorizontalAdapter;
import com.example.jh.musicdna.ui.adapter.horizontalrecycleradapters.StreamTracksHorizontalAdapter;
import com.example.jh.musicdna.ui.adapter.playlistdialogadapter.AddToPlaylistAdapter;
import com.example.jh.musicdna.ui.fragment.AboutFragment.AboutFragment;
import com.example.jh.musicdna.ui.fragment.AllPlaylistsFragment.AllPlaylistsFragment;
import com.example.jh.musicdna.ui.fragment.EditLocalSongFragment;
import com.example.jh.musicdna.ui.fragment.EqualizerFragment.EqualizerFragment;
import com.example.jh.musicdna.ui.fragment.FavouritesFragment.FavouritesFragment;
import com.example.jh.musicdna.ui.fragment.FolderContentFragment.FolderContentFragment;
import com.example.jh.musicdna.ui.fragment.FolderFragment.FolderFragment;
import com.example.jh.musicdna.ui.fragment.LocalMusicFragments.RecentlyAddedSongsFragment;
import com.example.jh.musicdna.ui.fragment.LocalMusicFragments.artist.ArtistFragment;
import com.example.jh.musicdna.ui.fragment.LocalMusicFragments.album.AlbumFragment;
import com.example.jh.musicdna.ui.fragment.LocalMusicFragments.localmusic.LocalMusicFragment;
import com.example.jh.musicdna.ui.fragment.LocalMusicFragments.LocalMusicViewPagerFragment;
import com.example.jh.musicdna.ui.fragment.NewPlaylistFragment.NewPlaylistFragment;
import com.example.jh.musicdna.ui.fragment.PlayerFragment.PlayerFragment;
import com.example.jh.musicdna.ui.fragment.QueueFragment.QueueFragment;
import com.example.jh.musicdna.ui.fragment.RecentsFragment.RecentsFragment;
import com.example.jh.musicdna.ui.fragment.SettingsFragment.SettingsFragment;
import com.example.jh.musicdna.ui.fragment.StreamFragment.StreamMusicFragment;
import com.example.jh.musicdna.ui.fragment.ViewAlbumFragment.ViewAlbumFragment;
import com.example.jh.musicdna.ui.fragment.ViewArtistFragment.ViewArtistFragment;
import com.example.jh.musicdna.ui.fragment.ViewPlaylistFragment.ViewPlaylistFragment;
import com.example.jh.musicdna.ui.fragment.ViewSavedDNAFragment.ViewSavedDNAFragment;
import com.example.jh.musicdna.utils.CommonUtils;
import com.example.jh.musicdna.utils.comparators.AlbumComparator;
import com.example.jh.musicdna.utils.comparators.ArtistComparator;
import com.example.jh.musicdna.utils.comparators.LocalMusicComparator;
import com.example.jh.musicdna.view.CustomLinearGradient;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.gson.Gson;
import com.lantouzi.wheelview.WheelView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.view.View.GONE;

/**
 * Created by jinhui on 2018/2/14.
 * Email:1004260403@qq.com
 */

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SearchView.OnQueryTextListener,
        LocalMusicFragment.OnLocalTrackSelectedListener,
        AlbumFragment.onAlbumClickListener,
        ArtistFragment.onArtistClickListener,
        RecentlyAddedSongsFragment.OnLocalTrackSelectedListener {

    private static final String TAG = "HomeActivity";
    // 链表集合
    public static List<LocalTrack> localTrackList = new ArrayList<>();
    public static List<LocalTrack> finalLocalSearchResultList = new ArrayList<>();
    public static List<LocalTrack> finalSelectedTracks = new ArrayList<>();
    public static List<LocalTrack> recentlyAddedTrackList = new ArrayList<>();
    public static List<LocalTrack> finalRecentlyAddedTrackSearchResultList = new ArrayList<>();
    public static List<Track> streamingTrackList = new ArrayList<>();
    public static List<Album> albums = new ArrayList<>();
    public static List<Album> finalAlbums = new ArrayList<>();
    public static List<Artist> artists = new ArrayList<>();
    public static List<Artist> finalArtists = new ArrayList<>();
    public static List<UnifiedTrack> continuePlayingList = new ArrayList<>();

    // boolean 合集
    public static boolean isReloaded = true;
    public static boolean hasSoftNavbar = false;
    public static boolean streamSelected = false;
    public static boolean localSelected = false;
    public static boolean queueCall = false;

    // 基本控件
    private Dialog progress;
    // TextView集合
    public TextView copyrightText;
    TextView localNothingText;
    TextView playlistNothingText;
    TextView recentsNothingText;
    TextView streamNothingText;
    public FrameLayout bottomToolbar;

    ImageView[] imgView = new ImageView[10];
    public CircleImageView spImgHome;
    public TextView spTitleHome;
    View playerContainer;


    public static Track selectedTrack;

    public static int queueCurrentIndex = 0;
    public static LocalTrack localSelectedTrack;


    // 适配器合集
    public LocalTracksHorizontalAdapter adapter;
    public StreamTracksHorizontalAdapter sAdapter;
    public PlayListsHorizontalAdapter pAdapter;
    public RecentsListHorizontalAdapter rAdapter;


    public static int themeColor = Color.parseColor("#B24242");

    public static int screen_width;
    public static int screen_height;
    public static float ratio;
    public static float ratio2;

    public HeadSetReceiver headSetReceiver;

    public DrawerLayout drawer;
    Toolbar toolbar;

    public CollapsingToolbarLayout collapsingToolbar;
    public CustomLinearGradient customLinearGradient;

    // 版本code
    public String versionName;
    public int versionCode;
    public int prevVersionCode = -1;

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

    public ImageLoader imgLoader;
    public Context ctx;
    // 显示引导页
    ShowcaseView showCase;
    Button mEndButton;
    public static TextPaint tp;
    public static RelativeLayout.LayoutParams lps;
    public static int statusBarHeightinDp;
    public static int navBarHeightSizeinDp;


    RelativeLayout localBanner;
    RelativeLayout localRecyclerContainer;
    RelativeLayout recentsRecyclerContainer;
    RelativeLayout streamRecyclerContainer;
    RelativeLayout playlistRecyclerContainer;

    // RecyclerView集合
    RecyclerView soundcloudRecyclerView;
    RecyclerView localsongsRecyclerView;
    RecyclerView playlistsRecycler;
    RecyclerView recentsRecycler;


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
    static boolean isSavePLaylistsRunning = false;
    public static boolean isEqualizerEnabled = false;
    public static boolean isEqualizerReloaded = false;
    public static boolean isFavourite = false;


    public static int[] seekbarpos = new int[5];
    public static int presetPos;
    public static short reverbPreset = -1;
    public static short bassStrength = -1;

    // Fragment 集合
    public PlayerFragment playerFragment;

    // 保存DNA
    public static SavedDNA tempSavedDNA;
    public static AllSavedDNA savedDNAs;

    // 碎片管理对象
    FragmentManager fragmentManager;
    public NetworkInfo mWifi;
    public ConnectivityManager connManager;

    // 基本的设置类
    public static Settings settings;

    // 数据保存
    public SharedPreferences mPrefs;
    public static SharedPreferences.Editor prefsEditor;
    public static Gson gson;

    public static Queue queue;
    public static Queue originalQueue;

    // 本地音乐库
    public static AllPlaylists allPlaylists;
    public static Playlist tempPlaylist;
    public static Artist tempArtist;
    public static Album tempAlbum;
    public static MusicFolder tempMusicFolder;
    public static RecentlyPlayed recentlyPlayed;
    public static Favourite favouriteTracks;
    public static EqualizerModel equalizerModel;
    public static AllMusicFolders allMusicFolders;

    public Activity main;
    // 最小的流长度
    public static float minAudioStrength = 0.40f;

    Call<List<Track>> call;

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

        PackageInfo pInfo;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = pInfo.versionName;
            versionCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        copyrightText = (TextView) findViewById(R.id.copyright_text);
        copyrightText.setText("Music DNA v" + versionName);

        if (SplashActivity.tf4 != null) {
            try {
                copyrightText.setTypeface(SplashActivity.tf4);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

    private void initView() {

        fragmentManager = getSupportFragmentManager();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        if (SplashActivity.tf4 != null) {
            collapsingToolbar.setCollapsedTitleTypeface(SplashActivity.tf4);
            collapsingToolbar.setExpandedTitleTypeface(SplashActivity.tf4);
        }

        customLinearGradient = (CustomLinearGradient) findViewById(R.id.custom_linear_gradient);
        customLinearGradient.setAlpha(170);
        customLinearGradient.invalidate();


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

        imgLoader = new ImageLoader(this);
        ctx = this;
        // 初始化头部image
        initializeHeaderImages();


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

        bottomToolbar = (FrameLayout) findViewById(R.id.bottomMargin);
        spImgHome = (CircleImageView) findViewById(R.id.selected_track_image_sp_home);
        spTitleHome = (TextView) findViewById(R.id.selected_track_title_sp_home);


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


        connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connManager != null;
        mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);


        mPrefs = getPreferences(MODE_PRIVATE);
        prefsEditor = mPrefs.edit();
        gson = new Gson();

        main = this;

        localNothingText = (TextView) findViewById(R.id.localNothingText);
        streamNothingText = (TextView) findViewById(R.id.streamNothingText);
        playlistNothingText = (TextView) findViewById(R.id.playlistNothingText);
        recentsNothingText = (TextView) findViewById(R.id.recentsNothingText);

        // dialog的初始化, 暂时注释掉
        progress = new Dialog(ctx);
        progress.setCancelable(false);
        progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progress.setContentView(R.layout.custom_progress_dialog);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progress.show();
        // 启动异步任务,加载保存的数据，暂时注释掉
        new loadSavedData().execute();
    }

    private void initializeHeaderImages() {
        imgView[0] = (ImageView) findViewById(R.id.home_header_img_1);
        imgView[1] = (ImageView) findViewById(R.id.home_header_img_2);
        imgView[2] = (ImageView) findViewById(R.id.home_header_img_3);
        imgView[3] = (ImageView) findViewById(R.id.home_header_img_4);
        imgView[4] = (ImageView) findViewById(R.id.home_header_img_5);
        imgView[5] = (ImageView) findViewById(R.id.home_header_img_6);
        imgView[6] = (ImageView) findViewById(R.id.home_header_img_7);
        imgView[7] = (ImageView) findViewById(R.id.home_header_img_8);
        imgView[8] = (ImageView) findViewById(R.id.home_header_img_9);
        imgView[9] = (ImageView) findViewById(R.id.home_header_img_10);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            hideAllFrags();
            Log.e(TAG, "hideAllFrags 方法执行=========================");
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

    /*
     * Standard Activity methods
     *
     * 疑问点：
     * fragment的返回键事件，isLocalVisible = false;
     * onNavigationItemSelected里将
     * hideFragment()注释掉不可以再次打开本地音乐列表，补全就可以，不合理啊
     * onBackPressed并还没有写没有置isLocalVisible = false;这是一个bug？
     * 很大原因与事务处理有关，不纠结了！addToBackStack fragment的栈有关系?
     *
     * (Fragment后台栈管理
     * 事务回退
     * 对Fragment的增删都是通过事务的形式来完成的，事务可以被提交，也可以被回退。
     * 在提交事务前，如果调用FragmentTransaction的 addToBackStack方法，就可以将该事务添加到后台栈，之后可以通过返回键/popBackStack方法，回退该事务。)
     */

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * 显示各部分fragment
     *
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
            FragmentManager fm = getSupportFragmentManager();
            LocalMusicViewPagerFragment newFragment = (LocalMusicViewPagerFragment) fm.findFragmentByTag("local");
            Log.e(TAG, "newFragment = " + newFragment);
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
            FragmentManager fm = getSupportFragmentManager();
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
            FragmentManager fm = getSupportFragmentManager();
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
            FragmentManager fm = getSupportFragmentManager();
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

    /**
     * 隐藏fragment
     *
     * @param type
     */
    private void hideFragment(String type) {
        if (type.equals("local")) {
            isLocalVisible = false;
            Log.e(TAG, "hideFragment方法执行=========================");
            setTitle("Music DNA");
            navigationView.setCheckedItem(R.id.nav_home);
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            android.support.v4.app.Fragment frag = fm.findFragmentByTag("local");
            if (frag != null) {
                fm.beginTransaction()
                        .remove(frag)
                        .commitAllowingStateLoss();
            }
        } else if (type.equals("queue")) {
            isQueueVisible = false;
            navigationView.setCheckedItem(R.id.nav_home);
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            android.support.v4.app.Fragment frag = fm.findFragmentByTag("queue");
            if (frag != null) {
                fm.beginTransaction()
                        .remove(frag)
                        .commitAllowingStateLoss();
            }
        } else if (type.equals("stream")) {
            isStreamVisible = false;
            setTitle("Music DNA");
            navigationView.setCheckedItem(R.id.nav_home);
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            android.support.v4.app.Fragment frag = fm.findFragmentByTag("stream");
            if (frag != null) {
                fm.beginTransaction()
                        .remove(frag)
                        .commitAllowingStateLoss();
            }
        } else if (type.equals("playlist")) {
            isPlaylistVisible = false;
            FragmentManager fm = getSupportFragmentManager();
            Fragment frag = fm.findFragmentByTag("playlist");
            if (frag != null) {
                fm.beginTransaction()
                        .remove(frag)
                        .commitAllowingStateLoss();
            }
        } else if (type.equals("equalizer")) {
            // 暂时注释掉
//            isEqualizerVisible = false;
//            // SaveEqualizer的异步任务
//            new SaveEqualizer().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
//            android.support.v4.app.Fragment frag = fm.findFragmentByTag("equalizer");
//            if (frag != null) {
//                fm.beginTransaction()
//                        .remove(frag)
//                        .commitAllowingStateLoss();
//            }
        } else if (type.equals("favourite")) {
            isFavouriteVisible = false;
            setTitle("Music DNA");
            navigationView.setCheckedItem(R.id.nav_home);
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            android.support.v4.app.Fragment frag = fm.findFragmentByTag("favourite");
            if (frag != null) {
                fm.beginTransaction()
                        .remove(frag)
                        .commitAllowingStateLoss();
            }
        } else if (type.equals("newPlaylist")) {
            isNewPlaylistVisible = false;
            navigationView.setCheckedItem(R.id.nav_home);
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            android.support.v4.app.Fragment frag = fm.findFragmentByTag("newPlaylist");
            if (frag != null) {
                fm.beginTransaction()
                        .remove(frag)
                        .commitAllowingStateLoss();
            }
        } else if (type.equals("allPlaylists")) {
            isAllPlaylistVisible = false;
            setTitle("Music DNA");
            navigationView.setCheckedItem(R.id.nav_home);
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            android.support.v4.app.Fragment frag = fm.findFragmentByTag("allPlaylists");
            if (frag != null) {
                fm.beginTransaction()
                        .remove(frag)
                        .commitAllowingStateLoss();
            }
        } else if (type.equals("folderContent")) {
            isFolderContentVisible = false;
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            android.support.v4.app.Fragment frag = fm.findFragmentByTag("folderContent");
            if (frag != null) {
                fm.beginTransaction()
                        .remove(frag)
                        .commitAllowingStateLoss();
            }
        } else if (type.equals("allFolders")) {
            isAllFolderVisible = false;
            setTitle("Music DNA");
            navigationView.setCheckedItem(R.id.nav_home);
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            android.support.v4.app.Fragment frag = fm.findFragmentByTag("allFolders");
            if (frag != null) {
                fm.beginTransaction()
                        .remove(frag)
                        .commitAllowingStateLoss();
            }
        } else if (type.equals("allSavedDNAs")) {
            isAllSavedDnaVisisble = false;
            setTitle("Music DNA");
            navigationView.setCheckedItem(R.id.nav_home);
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            android.support.v4.app.Fragment frag = fm.findFragmentByTag("allSavedDNAs");
            if (frag != null) {
                fm.beginTransaction()
                        .remove(frag)
                        .commitAllowingStateLoss();
            }
        } else if (type.equals("viewAlbum")) {
            isAlbumVisible = false;
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            android.support.v4.app.Fragment frag = fm.findFragmentByTag("viewAlbum");
            if (frag != null) {
                fm.beginTransaction()
                        .remove(frag)
                        .commitAllowingStateLoss();
            }
        } else if (type.equals("viewArtist")) {
            isArtistVisible = false;
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            android.support.v4.app.Fragment frag = fm.findFragmentByTag("viewArtist");
            if (frag != null) {
                fm.beginTransaction()
                        .remove(frag)
                        .commitAllowingStateLoss();
            }
        } else if (type.equals("recent")) {
            isRecentVisible = false;
            setTitle("Music DNA");
            navigationView.setCheckedItem(R.id.nav_home);
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            android.support.v4.app.Fragment frag = fm.findFragmentByTag("recent");
            if (frag != null) {
                fm.beginTransaction()
                        .remove(frag)
                        .commitAllowingStateLoss();
            }
        } else if (type.equals("settings")) {
            isSettingsVisible = false;
            setTitle("Music DNA");
            navigationView.setCheckedItem(R.id.nav_home);
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            android.support.v4.app.Fragment frag = fm.findFragmentByTag("settings");
            if (frag != null) {
                fm.beginTransaction()
                        .remove(frag)
                        .commitAllowingStateLoss();
            }
        } else if (type.equals("About")) {
            isAboutVisible = false;
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            android.support.v4.app.Fragment frag = fm.findFragmentByTag("About");
            if (frag != null) {
                fm.beginTransaction()
                        .remove(frag)
                        .commitAllowingStateLoss();
            }
        } else if (type.equals("Edit")) {
            isEditVisible = false;
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            android.support.v4.app.Fragment frag = fm.findFragmentByTag("Edit");
            if (frag != null) {
                fm.beginTransaction()
                        .remove(frag)
                        .commitAllowingStateLoss();
            }
        }
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
        updateLocalList(newText.trim());
        updateStreamingList(newText.trim());
//        updateAlbumList(newText.trim());
//        updateArtistList(newText.trim());
//        updateRecentlyAddedLocalList(newText.trim());
        return true;
    }

    /**
     * 更新流列表
     *
     * @param query
     */
    private void updateStreamingList(String query) {
        if (!isLocalVisible) {

            mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if ((settings.isStreamOnlyOnWifiEnabled() && mWifi.isConnected()) || (!settings.isStreamOnlyOnWifiEnabled())) {

                // 启动新的线程
                new Thread(new CancelCall()).start();

                /*Update the Streaming List*/

                if (!query.equals("")) {
                    streamRecyclerContainer.setVisibility(View.VISIBLE);
                    // 开始加载指示器
                    startLoadingIndicator();
                    Retrofit client = new Retrofit.Builder()
                            .baseUrl(Config.API_URL)
                            .addConverterFactory(GsonConverterFactory.create())
                            .build();
                    StreamService ss = client.create(StreamService.class);
                    call = ss.getTracks(query, 75);
                    call.enqueue(new Callback<List<Track>>() {

                        @Override
                        public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                            if (response.isSuccessful()) {
                                streamingTrackList = response.body();
                                sAdapter = new StreamTracksHorizontalAdapter(streamingTrackList, ctx);
                                LinearLayoutManager mLayoutManager = new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false);
                                soundcloudRecyclerView.setLayoutManager(mLayoutManager);
                                soundcloudRecyclerView.setItemAnimator(new DefaultItemAnimator());
                                soundcloudRecyclerView.setAdapter(sAdapter);

                                if (streamingTrackList.size() == 0) {
                                    streamRecyclerContainer.setVisibility(GONE);
                                } else {
                                    streamRecyclerContainer.setVisibility(View.VISIBLE);
                                }

                                stopLoadingIndicator();
                                (soundcloudRecyclerView.getAdapter()).notifyDataSetChanged();

                                StreamMusicFragment sFrag = (StreamMusicFragment) fragmentManager.findFragmentByTag("stream");
                                if (sFrag != null) {
                                    sFrag.dataChanged();
                                }
                            } else {
                                stopLoadingIndicator();
                            }
                            Log.d("RETRO", response.body() + "");
                        }

                        @Override
                        public void onFailure(Call<List<Track>> call, Throwable t) {
                            Log.d("RETRO1", t.getMessage());
                        }
                    });

                } else {
                    stopLoadingIndicator();
                    streamRecyclerContainer.setVisibility(GONE);
                }
            } else {
                stopLoadingIndicator();
                streamRecyclerContainer.setVisibility(GONE);
            }
        }
    }

    /**
     * // 停止加载指示器
     */
    private void stopLoadingIndicator() {
        findViewById(R.id.loadingIndicator).setVisibility(View.INVISIBLE);
        soundcloudRecyclerView.setVisibility(View.VISIBLE);
        if (streamingTrackList.size() == 0) {
            streamNothingText.setVisibility(View.VISIBLE);
        }
    }

    /**
     * // 开始加载指示器
     */
    private void startLoadingIndicator() {
        findViewById(R.id.loadingIndicator).setVisibility(View.VISIBLE);
        soundcloudRecyclerView.setVisibility(View.INVISIBLE);
        streamNothingText.setVisibility(View.INVISIBLE);
    }

    /**
     * 更新本地列表
     *
     * @param query
     */
    private void updateLocalList(String query) {
        LocalMusicViewPagerFragment flmFrag = (LocalMusicViewPagerFragment) fragmentManager.findFragmentByTag("local");
        LocalMusicFragment lFrag = null;
        if (flmFrag != null)
            lFrag = (LocalMusicFragment) flmFrag.getFragmentByPosition(0);

        if (lFrag != null)
            lFrag.hideShuffleFab();

        /*Update the Local List*/

        if (!isLocalVisible)
            localRecyclerContainer.setVisibility(View.VISIBLE);

        finalLocalSearchResultList.clear();
        for (int i = 0; i < localTrackList.size(); i++) {
            LocalTrack lt = localTrackList.get(i);
            String tmp1 = lt.getTitle().toLowerCase();
            String tmp2 = query.toLowerCase();
            if (tmp1.contains(tmp2)) {
                finalLocalSearchResultList.add(lt);
            }
        }

        if (!isLocalVisible && localsongsRecyclerView != null) {
            if (finalLocalSearchResultList.size() == 0) {
                localsongsRecyclerView.setVisibility(GONE);
                localNothingText.setVisibility(View.VISIBLE);
            } else {
                localsongsRecyclerView.setVisibility(View.VISIBLE);
                localNothingText.setVisibility(View.INVISIBLE);
            }
            (localsongsRecyclerView.getAdapter()).notifyDataSetChanged();
        }

        if (lFrag != null)
            lFrag.updateAdapter();

        if (query.equals("")) {
            localRecyclerContainer.setVisibility(GONE);
        }
        if (query.equals("") && isLocalVisible) {
            if (lFrag != null)
                lFrag.showShuffleFab();
        }
    }

    /**
     * 得到保存的dna数据
     */
    public void getSavedData() {
        try {
            Gson gson = new Gson();
            Log.d("TIME", "start");
            String json2 = mPrefs.getString("allPlaylists", "");
            allPlaylists = gson.fromJson(json2, AllPlaylists.class);
            Log.d("TIME", "allPlaylists");
            String json3 = mPrefs.getString("queue", "");
            queue = gson.fromJson(json3, Queue.class);
            Log.d("TIME", "queue");
            String json4 = mPrefs.getString("recentlyPlayed", "");
            recentlyPlayed = gson.fromJson(json4, RecentlyPlayed.class);
            Log.d("TIME", "recents");
            String json5 = mPrefs.getString("favouriteTracks", "");
            favouriteTracks = gson.fromJson(json5, Favourite.class);
            Log.d("TIME", "fav");
            String json6 = mPrefs.getString("queueCurrentIndex", "");
            queueCurrentIndex = gson.fromJson(json6, Integer.class);
            Log.d("TIME", "queueCurrentindex");
            String json8 = mPrefs.getString("settings", "");
            settings = gson.fromJson(json8, Settings.class);
            Log.d("TIME", "settings");
            String json9 = mPrefs.getString("equalizer", "");
            equalizerModel = gson.fromJson(json9, EqualizerModel.class);
            Log.d("TIME", "equalizer");
            String json = mPrefs.getString("savedDNAs", "");
            savedDNAs = gson.fromJson(json, AllSavedDNA.class);
            Log.d("TIME", "savedDNAs");
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String json7 = mPrefs.getString("versionCode", "");
            prevVersionCode = gson.fromJson(json7, Integer.class);
            Log.d("TIME", "VersionCode : " + prevVersionCode + " : " + versionCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * bottomsheet的监听
     *
     * @param position
     * @param action
     * @param fragment
     * @param type
     */
    public void bottomSheetListener(int position, String action, String fragment, boolean type) {

//        UnifiedTrack ut = null;
//        if (fragment == null) {
//            ut = new UnifiedTrack(true, finalLocalSearchResultList.get(position), null);
//        } else if (fragment.equals("Artist")) {
//            ut = new UnifiedTrack(true, tempArtist.getArtistSongs().get(position), null);
//        } else if (fragment.equals("Album")) {
//            ut = new UnifiedTrack(true, tempAlbum.getAlbumSongs().get(position), null);
//        } else if (fragment.equals("Folder")) {
//            ut = new UnifiedTrack(true, tempMusicFolder.getLocalTracks().get(position), null);
//        } else if (fragment.equals("Recents")) {
//            ut = recentlyPlayed.getRecentlyPlayed().get(position);
//        } else if (fragment.equals("RecentHorizontalList")) {
//            ut = continuePlayingList.get(position);
//        } else if (fragment.equals("Stream")) {
//            ut = new UnifiedTrack(false, null, streamingTrackList.get(position));
//        }
//
//        if (action.equals("Add to Playlist")) {
//            showAddToPlaylistDialog(ut);
//            pAdapter.notifyDataSetChanged();
//        }
//        if (action.equals("Add to Queue")) {
//            queue.getQueue().add(ut);
//            updateVisualizerRecycler();
//        }
//        if (action.equals("Play")) {
//            if (queue.getQueue().size() == 0) {
//                queueCurrentIndex = 0;
//                queue.getQueue().add(ut);
//            } else if (queueCurrentIndex == queue.getQueue().size() - 1) {
//                queueCurrentIndex++;
//                queue.getQueue().add(ut);
//            } else if (isReloaded) {
//                isReloaded = false;
//                queueCurrentIndex = queue.getQueue().size();
//                queue.getQueue().add(ut);
//            } else {
//                queue.getQueue().add(++queueCurrentIndex, ut);
//            }
//            streamSelected = !type;
//            localSelected = type;
//            queueCall = false;
//            isReloaded = false;
//            if (type) {
//                localSelectedTrack = ut.getLocalTrack();
//                onLocalTrackSelected(position);
//            } else {
//                selectedTrack = ut.getStreamTrack();
//                onTrackSelected(position);
//            }
//            updateVisualizerRecycler();
//        }
//        if (action.equals("Play Next")) {
//            if (queue.getQueue().size() == 0) {
//                queueCurrentIndex = 0;
//                queue.getQueue().add(ut);
//                streamSelected = !type;
//                localSelected = type;
//                queueCall = false;
//                isReloaded = false;
//                if (type) {
//                    localSelectedTrack = ut.getLocalTrack();
//                    onLocalTrackSelected(position);
//                } else {
//                    selectedTrack = ut.getStreamTrack();
//                    onTrackSelected(position);
//                }
//            } else if (queueCurrentIndex == queue.getQueue().size() - 1) {
//                queue.getQueue().add(ut);
//            } else if (isReloaded) {
//                isReloaded = false;
//                queueCurrentIndex = queue.getQueue().size();
//                queue.getQueue().add(ut);
//                streamSelected = !type;
//                localSelected = type;
//                queueCall = false;
//                isReloaded = false;
//                if (type) {
//                    localSelectedTrack = ut.getLocalTrack();
//                    onLocalTrackSelected(position);
//                } else {
//                    selectedTrack = ut.getStreamTrack();
//                    onTrackSelected(position);
//                }
//            } else {
//                queue.getQueue().add(queueCurrentIndex + 1, ut);
//            }
//            updateVisualizerRecycler();
//        }
//        if (action.equals("Add to Favourites")) {
//            addToFavourites(ut);
//        }
//        if (action.equals("Share")) {
//            FileUtils.shareLocalSong(ut.getLocalTrack().getPath(), this);
//        }
//        if (action.equals("Edit")) {
//            editSong = ut.getLocalTrack();
//            showFragment("Edit");
//        }
    }

    /**
     * 更新VisualizerRecycler
     */
//    public void updateVisualizerRecycler() {
//        if (playerFragment != null && playerFragment.snappyRecyclerView != null) {
//            playerFragment.snappyRecyclerView.getAdapter().notifyDataSetChanged();
//            playerFragment.snappyRecyclerView.scrollToPosition(queueCurrentIndex);
//            playerFragment.snappyRecyclerView.setTransparency();
//        }
//    }

    /**
     * 显示添加到音乐列表的对话框
     *
     * @param track
     */
    private void showAddToPlaylistDialog(final UnifiedTrack track) {
        final Dialog dialog = new Dialog(ctx);
        dialog.setContentView(R.layout.add_to_playlist_dialog);
        dialog.setTitle("Add to Playlist");

        ListView lv = (ListView) dialog.findViewById(R.id.playlist_list);
        AddToPlaylistAdapter adapter;
        if (allPlaylists.getPlaylists() != null && allPlaylists.getPlaylists().size() != 0) {
            adapter = new AddToPlaylistAdapter(allPlaylists.getPlaylists(), ctx);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Playlist temp = allPlaylists.getPlaylists().get(position);
                    boolean isRepeat = false;
                    for (UnifiedTrack ut : temp.getSongList()) {
                        if (track.getType() && ut.getType() && track.getLocalTrack().getTitle().equals(ut.getLocalTrack().getTitle())) {
                            isRepeat = true;
                            break;
                        } else if (!track.getType() && !ut.getType() && track.getStreamTrack().getTitle().equals(ut.getStreamTrack().getTitle())) {
                            isRepeat = true;
                            break;
                        }
                    }
                    if (!isRepeat) {
                        temp.addSong(track);
                        playlistsRecycler.setVisibility(View.VISIBLE);
                        playlistNothingText.setVisibility(View.INVISIBLE);
                        pAdapter.notifyDataSetChanged();
                        Toast.makeText(ctx, "Added to Playlist : " + temp.getPlaylistName(), Toast.LENGTH_SHORT).show();
                        // 保存音乐列表的异步任务？
                        new SavePlaylists().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        dialog.dismiss();
                    } else {
                        Toast.makeText(ctx, "Song already present in Playlist", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            lv.setVisibility(GONE);
        }

        // set the custom dialog components - text, image and button
        final EditText text = (EditText) dialog.findViewById(R.id.new_playlist_name);
        ImageView image = (ImageView) dialog.findViewById(R.id.confirm_button);
        // if button is clicked, close the custom dialog
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isNameRepeat = false;
                if (text.getText().toString().trim().equals("")) {
                    text.setError("Enter Playlist Name!");
                } else {
                    for (int i = 0; i < allPlaylists.getPlaylists().size(); i++) {
                        if (text.getText().toString().equals(allPlaylists.getPlaylists().get(i).getPlaylistName())) {
                            isNameRepeat = true;
                            text.setError("Playlist with same name exists!");
                            break;
                        }
                    }
                    if (!isNameRepeat) {
                        List<UnifiedTrack> l = new ArrayList<UnifiedTrack>();
                        l.add(track);
                        Playlist pl = new Playlist(l, text.getText().toString().trim());
                        allPlaylists.addPlaylist(pl);
                        playlistsRecycler.setVisibility(View.VISIBLE);
                        playlistNothingText.setVisibility(View.INVISIBLE);
                        pAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                        new SavePlaylists().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    }
                }
            }
        });

        dialog.show();
    }


    @Override
    public void onLocalTrackSelected(int position) {
        Log.e(TAG, "onLocalTrackSelected"); //  E/HomeActivity: onLocalTrackSelected
        isReloaded = false;
        HideBottomFakeToolbar();

        // 2/19 关于打开播放界面的方法要认真分析处理在整理下！
//        if (!queueCall) {
//            // 隐藏键盘
//            CommonUtils.hideKeyboard(this);
//
//            searchView.setQuery("", true);
//            searchView.setIconified(true);
//            new Thread(new CancelCall()).start();
//
////            hideTabs();
//            isPlayerVisible = true;
//
//            PlayerFragment frag = playerFragment;
//            FragmentManager fm = getSupportFragmentManager();
//            PlayerFragment newFragment = new PlayerFragment();
//            if (frag == null) {
//                playerFragment = newFragment;
//                int flag = 0;
//                for (int i = 0; i < favouriteTracks.getFavourite().size(); i++) {
//                    UnifiedTrack ut = favouriteTracks.getFavourite().get(i);
//                    if (ut.getType() && ut.getLocalTrack().getTitle().equals(localSelectedTrack.getTitle())) {
//                        flag = 1;
//                        isFavourite = true;
//                        break;
//                    }
//                }
//                if (flag == 0) {
//                    isFavourite = false;
//                }
//
//                playerFragment.localIsPlaying = true;
//                playerFragment.localTrack = localSelectedTrack;
//                fm.beginTransaction()
//                        .setCustomAnimations(R.anim.slide_up,
//                                R.anim.slide_down,
//                                R.anim.slide_up,
//                                R.anim.slide_down)
//                        .add(R.id.player_frag_container, newFragment, "player")
//                        .show(newFragment)
//                        .commitAllowingStateLoss();
//            } else {
//                if (playerFragment.localTrack != null && playerFragment.localIsPlaying && localSelectedTrack.getTitle() == playerFragment.localTrack.getTitle()) {
//
//                } else {
//                    int flag = 0;
//                    for (int i = 0; i < favouriteTracks.getFavourite().size(); i++) {
//                        UnifiedTrack ut = favouriteTracks.getFavourite().get(i);
//                        if (ut.getType() && ut.getLocalTrack().getTitle().equals(localSelectedTrack.getTitle())) {
//                            flag = 1;
//                            isFavourite = true;
//                            break;
//                        }
//                    }
//                    if (flag == 0) {
//                        isFavourite = false;
//                    }
//                    playerFragment.localIsPlaying = true;
//                    playerFragment.localTrack = localSelectedTrack;
//                    frag.refresh();
//                }
//            }
//
//            if (!isQueueVisible)
//                showPlayer();
//
//        } else {
//            PlayerFragment frag = playerFragment;
//            playerFragment.localIsPlaying = true;
//            playerFragment.localTrack = localSelectedTrack;
//
//            int flag = 0;
//            for (int i = 0; i < favouriteTracks.getFavourite().size(); i++) {
//                UnifiedTrack ut = favouriteTracks.getFavourite().get(i);
//                if (ut.getType() && ut.getLocalTrack().getTitle().equals(localSelectedTrack.getTitle())) {
//                    flag = 1;
//                    isFavourite = true;
//                    break;
//                }
//            }
//            if (flag == 0) {
//                isFavourite = false;
//            }
//            if (frag != null)
//                frag.refresh();
//        }
//
//        if (playerFragment != null && playerFragment.snappyRecyclerView != null) {
//            playerFragment.snappyRecyclerView.getAdapter().notifyDataSetChanged();
//            playerFragment.snappyRecyclerView.setTransparency();
//        }
//
//        QueueFragment qFrag = (QueueFragment) fragMan.findFragmentByTag("queue");
//        if (qFrag != null) {
//            qFrag.updateQueueAdapter();
//        }
//
//        UnifiedTrack track = new UnifiedTrack(true, playerFragment.localTrack, null);
//        for (int i = 0; i < recentlyPlayed.getRecentlyPlayed().size(); i++) {
//            if (recentlyPlayed.getRecentlyPlayed().get(i).getType() && recentlyPlayed.getRecentlyPlayed().get(i).getLocalTrack().getTitle().equals(track.getLocalTrack().getTitle())) {
//                recentlyPlayed.getRecentlyPlayed().remove(i);
////                rAdapter.notifyItemRemoved(i);
//                break;
//            }
//        }
//        recentlyPlayed.getRecentlyPlayed().add(0, track);
//        if (recentlyPlayed.getRecentlyPlayed().size() == 51) {
//            recentlyPlayed.getRecentlyPlayed().remove(50);
//        }
//        recentsRecycler.setVisibility(View.VISIBLE);
//        recentsNothingText.setVisibility(View.INVISIBLE);
//        continuePlayingList.clear();
//        for (int i = 0; i < Math.min(10, recentlyPlayed.getRecentlyPlayed().size()); i++) {
//            continuePlayingList.add(recentlyPlayed.getRecentlyPlayed().get(i));
//        }
//        rAdapter.notifyDataSetChanged();
//        refreshHeaderImages();
//
//        RecentsFragment rFrag = (RecentsFragment) fragMan.findFragmentByTag("recent");
//        if (rFrag != null && rFrag.rtAdpater != null) {
//            rFrag.rtAdpater.notifyDataSetChanged();
//        }

    }

    @Override
    public void onAlbumClick() {
        showFragment("viewAlbum");
    }

    @Override
    public void onArtistClick() {
        searchView.setQuery("", true);
        searchView.setIconified(true);
        showFragment("viewArtist");
    }

    /**
     * queue的item项点击
     *
     * @param position
     */
    public void onQueueItemClicked2(int position) {
        if (position <= (queue.getQueue().size() - 1)) {
            queueCurrentIndex = position;
            UnifiedTrack ut = queue.getQueue().get(position);
            if (ut.getType()) {
                LocalTrack track = ut.getLocalTrack();
                localSelectedTrack = track;
                streamSelected = false;
                localSelected = true;
                queueCall = true;
                isReloaded = false;
                onLocalTrackSelected(position);
            } else {
                Track track = ut.getStreamTrack();
                selectedTrack = track;
                streamSelected = true;
                localSelected = false;
                queueCall = true;
                isReloaded = false;
                // 2/19 暂时注释掉
//                onTrackSelected(position);
            }
        }
    }

    private class CancelCall implements Runnable {
        @Override
        public void run() {
            if (call != null)
                call.cancel();
        }
    }

    private class SavePlaylists extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            if (!isSavePLaylistsRunning) {
                isSavePLaylistsRunning = true;
                try {
                    String json2 = gson.toJson(allPlaylists);
                    prefsEditor.putString("allPlaylists", json2);
                } catch (Exception e) {

                }
                isSavePLaylistsRunning = false;
            }
            return null;
        }
    }

    // 2/17，没写完，待继续写，暂时注释掉
    @SuppressLint("StaticFieldLeak")
    private class loadSavedData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            getSavedData();
            return "done";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            progress.dismiss();
            main.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    if (settings == null) {
                        settings = new Settings();
                    }

                    themeColor = settings.getThemeColor();
                    minAudioStrength = settings.getMinAudioStrength();


                    navigationView.setItemIconTintList(ColorStateList.valueOf(themeColor));
                    collapsingToolbar.setContentScrimColor(themeColor);
                    customLinearGradient.setStartColor(themeColor);
                    customLinearGradient.invalidate();

                    try {
                        if (Build.VERSION.SDK_INT >= 21) {
                            Window window = ((Activity) ctx).getWindow();
                            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                            window.setStatusBarColor(CommonUtils.getDarkColor(themeColor));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (prevVersionCode == -1 || prevVersionCode <= 30) {
                        savedDNAs = null;
                    }

                    if (allPlaylists == null) {
                        allPlaylists = new AllPlaylists();
                    }

                    if (tempPlaylist == null) {
                        tempPlaylist = new Playlist(null, null);
                    }

                    if (queue == null) {
                        queue = new Queue();
                    }

                    if (favouriteTracks == null) {
                        favouriteTracks = new Favourite();
                    }

                    if (recentlyPlayed == null) {
                        recentlyPlayed = new RecentlyPlayed();
                    }
                    if (allMusicFolders == null) {
                        allMusicFolders = new AllMusicFolders();
                    }
                    if (savedDNAs == null) {
                        savedDNAs = new AllSavedDNA();
                    }

                    if (equalizerModel == null) {
                        equalizerModel = new EqualizerModel();
                        isEqualizerEnabled = true;
                        isEqualizerReloaded = false;
                    } else {
                        isEqualizerEnabled = equalizerModel.isEqualizerEnabled();
                        isEqualizerReloaded = true;
                        seekbarpos = equalizerModel.getSeekbarpos();
                        presetPos = equalizerModel.getPresetPos();
                        reverbPreset = equalizerModel.getReverbPreset();
                        bassStrength = equalizerModel.getBassStrength();
                    }

                    // 保存版本号的异步任务
                    new SaveVersionCode().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                    getLocalSongs();
                    refreshHeaderImages();

                    if (queue != null && queue.getQueue().size() != 0) {
                        UnifiedTrack utHome = queue.getQueue().get(queueCurrentIndex);
                        if (utHome.getType()) {
                            imgLoader.DisplayImage(utHome.getLocalTrack().getPath(), spImgHome);
                            spTitleHome.setText(utHome.getLocalTrack().getTitle());
                        } else {
                            imgLoader.DisplayImage(utHome.getStreamTrack().getArtworkURL(), spImgHome);
                            spTitleHome.setText(utHome.getStreamTrack().getTitle());
                        }
                    } else {
                        bottomToolbar.setVisibility(GONE);
                    }

                    for (int i = 0; i < Math.min(10, recentlyPlayed.getRecentlyPlayed().size()); i++) {
                        continuePlayingList.add(recentlyPlayed.getRecentlyPlayed().get(i));
                    }

                    rAdapter = new RecentsListHorizontalAdapter(continuePlayingList, ctx);
                    recentsRecycler = (RecyclerView) findViewById(R.id.recentsMusicList_home);
                    recentsRecycler.setNestedScrollingEnabled(false);
                    LinearLayoutManager mLayoutManager3 = new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false);
                    recentsRecycler.setLayoutManager(mLayoutManager3);
                    recentsRecycler.setItemAnimator(new DefaultItemAnimator());
                    AlphaInAnimationAdapter alphaAdapter3 = new AlphaInAnimationAdapter(rAdapter);
                    alphaAdapter3.setFirstOnly(false);
                    recentsRecycler.setAdapter(alphaAdapter3);

                    // recentsMusicList_home最近的音乐列表的监听
//                    recentsRecycler.addOnItemTouchListener(new ClickItemTouchListener(recentsRecycler) {
//                        @Override
//                        public boolean onClick(RecyclerView parent, View view, final int position, long id) {
//                            UnifiedTrack ut = continuePlayingList.get(position);
//                            boolean isRepeat = false;
//                            int pos = 0;
//                            for (int i = 0; i < queue.getQueue().size(); i++) {
//                                UnifiedTrack ut1 = queue.getQueue().get(i);
//                                if (ut1.getType() && ut.getType() && ut1.getLocalTrack().getTitle().equals(ut.getLocalTrack().getTitle())) {
//                                    isRepeat = true;
//                                    pos = i;
//                                    break;
//                                }
//                                if (!ut1.getType() && !ut.getType() && ut1.getStreamTrack().getTitle().equals(ut.getStreamTrack().getTitle())) {
//                                    isRepeat = true;
//                                    pos = i;
//                                    break;
//                                }
//                            }
//                            if (!isRepeat) {
//                                if (ut.getType()) {
//                                    LocalTrack track = ut.getLocalTrack();
//                                    if (queue.getQueue().size() == 0) {
//                                        queueCurrentIndex = 0;
//                                        queue.getQueue().add(new UnifiedTrack(true, track, null));
//                                    } else if (queueCurrentIndex == queue.getQueue().size() - 1) {
//                                        queueCurrentIndex++;
//                                        queue.getQueue().add(new UnifiedTrack(true, track, null));
//                                    } else if (isReloaded) {
//                                        isReloaded = false;
//                                        queueCurrentIndex = queue.getQueue().size();
//                                        queue.getQueue().add(new UnifiedTrack(true, track, null));
//                                    } else {
//                                        queue.getQueue().add(++queueCurrentIndex, new UnifiedTrack(true, track, null));
//                                    }
//                                    localSelectedTrack = track;
//                                    streamSelected = false;
//                                    localSelected = true;
//                                    queueCall = false;
//                                    isReloaded = false;
//                                    // 暂时注释掉
////                                    onLocalTrackSelected(position);
//                                } else {
//                                    Track track = ut.getStreamTrack();
//                                    if (queue.getQueue().size() == 0) {
//                                        queueCurrentIndex = 0;
//                                        queue.getQueue().add(new UnifiedTrack(false, null, track));
//                                    } else if (queueCurrentIndex == queue.getQueue().size() - 1) {
//                                        queueCurrentIndex++;
//                                        queue.getQueue().add(new UnifiedTrack(false, null, track));
//                                    } else if (isReloaded) {
//                                        isReloaded = false;
//                                        queueCurrentIndex = queue.getQueue().size();
//                                        queue.getQueue().add(new UnifiedTrack(false, null, track));
//                                    } else {
//                                        queue.getQueue().add(++queueCurrentIndex, new UnifiedTrack(false, null, track));
//                                    }
//                                    selectedTrack = track;
//                                    streamSelected = true;
//                                    localSelected = false;
//                                    queueCall = false;
//                                    isReloaded = false;
//                                    onTrackSelected(position);
//                                }
//                            } else {
//                                onQueueItemClicked(pos);
//                            }
//
//                            return true;
//                        }
//
//                        @Override
//                        public boolean onLongClick(RecyclerView parent, View view, final int position, long id) {
//                            final UnifiedTrack ut = continuePlayingList.get(position);
//                            CustomGeneralBottomSheetDialog generalBottomSheetDialog = new CustomGeneralBottomSheetDialog();
//                            generalBottomSheetDialog.setPosition(position);
//                            generalBottomSheetDialog.setTrack(ut);
//                            generalBottomSheetDialog.setFragment("RecentHorizontalList");
//                            generalBottomSheetDialog.show(getSupportFragmentManager(), "general_bottom_sheet_dialog");
//                            return true;
//                        }
//
//                        @Override
//                        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//                        }
//                    });

                    pAdapter = new PlayListsHorizontalAdapter(allPlaylists.getPlaylists(), ctx);
                    playlistsRecycler = (RecyclerView) findViewById(R.id.playlist_home);
                    playlistsRecycler.setNestedScrollingEnabled(false);
                    LinearLayoutManager mLayoutManager2 = new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false);
                    playlistsRecycler.setLayoutManager(mLayoutManager2);
                    playlistsRecycler.setItemAnimator(new DefaultItemAnimator());
                    AlphaInAnimationAdapter alphaAdapter2 = new AlphaInAnimationAdapter(pAdapter);
                    alphaAdapter2.setFirstOnly(false);
                    playlistsRecycler.setAdapter(alphaAdapter2);

                    // playlist_home 的点击监听
//                    playlistsRecycler.addOnItemTouchListener(new ClickItemTouchListener(playlistsRecycler) {
//                        @Override
//                        public boolean onClick(RecyclerView parent, View view, final int position, long id) {
//                            tempPlaylist = allPlaylists.getPlaylists().get(position);
//                            tempPlaylistNumber = position;
//                            showFragment("playlist");
//                            return true;
//                        }
//
//                        @Override
//                        public boolean onLongClick(RecyclerView parent, View view, final int position, long id) {
//                            PopupMenu popup = new PopupMenu(ctx, view);
//                            popup.getMenuInflater().inflate(R.menu.playlist_popup, popup.getMenu());
//
//                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//                                @Override
//                                public boolean onMenuItemClick(MenuItem item) {
//                                    if (item.getTitle().equals("Play")) {
//
//                                        tempPlaylist = allPlaylists.getPlaylists().get(position);
//                                        tempPlaylistNumber = position;
//
//                                        int size = tempPlaylist.getSongList().size();
//                                        queue.getQueue().clear();
//                                        for (int i = 0; i < size; i++) {
//                                            queue.addToQueue(tempPlaylist.getSongList().get(i));
//                                        }
//
//                                        queueCurrentIndex = 0;
//                                        onPlaylistPlayAll();
//                                    } else if (item.getTitle().equals("Add to Queue")) {
//                                        Playlist pl = allPlaylists.getPlaylists().get(position);
//                                        for (UnifiedTrack ut : pl.getSongList()) {
//                                            queue.addToQueue(ut);
//                                        }
//                                    } else if (item.getTitle().equals("Delete")) {
//                                        allPlaylists.getPlaylists().remove(position);
//                                        AllPlaylistsFragment plFrag = (AllPlaylistsFragment) fragMan.findFragmentByTag("allPlaylists");
//                                        if (plFrag != null) {
//                                            plFrag.itemRemoved(position);
//                                        }
//                                        pAdapter.notifyItemRemoved(position);
//                                        new SavePlaylists().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//                                    } else if (item.getTitle().equals("Rename")) {
//                                        renamePlaylistNumber = position;
//                                        renamePlaylistDialog(allPlaylists.getPlaylists().get(position).getPlaylistName());
//                                    }
//                                    return true;
//                                }
//                            });
//                            popup.show();
//                            return true;
//                        }
//
//                        @Override
//                        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//                        }
//                    });

                    adapter = new LocalTracksHorizontalAdapter(finalLocalSearchResultList, ctx);
                    localsongsRecyclerView = (RecyclerView) findViewById(R.id.localMusicList_home);
                    LinearLayoutManager mLayoutManager = new LinearLayoutManager(ctx, LinearLayoutManager.HORIZONTAL, false);
                    localsongsRecyclerView.setLayoutManager(mLayoutManager);
                    localsongsRecyclerView.setItemAnimator(new DefaultItemAnimator());
                    AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(adapter);
                    alphaAdapter.setFirstOnly(false);
                    localsongsRecyclerView.setAdapter(alphaAdapter);

                    localsongsRecyclerView.addOnItemTouchListener(new ClickItemTouchListener(localsongsRecyclerView) {
                        @Override
                        public boolean onClick(RecyclerView parent, View view, int position, long id) {
                            LocalTrack track = finalLocalSearchResultList.get(position);
                            if (queue.getQueue().size() == 0) {
                                queueCurrentIndex = 0;
                                queue.getQueue().add(new UnifiedTrack(true, track, null));
                            } else if (queueCurrentIndex == queue.getQueue().size() - 1) {
                                queueCurrentIndex++;
                                queue.getQueue().add(new UnifiedTrack(true, track, null));
                            } else if (isReloaded) {
                                isReloaded = false;
                                queueCurrentIndex = queue.getQueue().size();
                                queue.getQueue().add(new UnifiedTrack(true, track, null));
                            } else {
                                queue.getQueue().add(++queueCurrentIndex, new UnifiedTrack(true, track, null));
                            }
                            localSelectedTrack = track;
                            streamSelected = false;
                            localSelected = true;
                            queueCall = false;
                            isReloaded = false;
                            onLocalTrackSelected(position);
                            return true;
                        }

                        @Override
                        public boolean onLongClick(RecyclerView parent, View view, final int position, long id) {
                            CustomLocalBottomSheetDialog localBottomSheetDialog = new CustomLocalBottomSheetDialog();
                            localBottomSheetDialog.setPosition(position);
                            localBottomSheetDialog.setLocalTrack(finalLocalSearchResultList.get(position));
                            localBottomSheetDialog.show(getSupportFragmentManager(), "local_song_bottom_sheet");
                            return true;
                        }

                        @Override
                        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                        }
                    });

                    soundcloudRecyclerView = (RecyclerView) findViewById(R.id.trackList_home);

                    // trackList_home 的监听
//                    soundcloudRecyclerView.addOnItemTouchListener(new ClickItemTouchListener(soundcloudRecyclerView) {
//                        @Override
//                        public boolean onClick(RecyclerView parent, View view, int position, long id) {
//                            Track track = streamingTrackList.get(position);
//                            if (queue.getQueue().size() == 0) {
//                                queueCurrentIndex = 0;
//                                queue.getQueue().add(new UnifiedTrack(false, null, track));
//                            } else if (queueCurrentIndex == queue.getQueue().size() - 1) {
//                                queueCurrentIndex++;
//                                queue.getQueue().add(new UnifiedTrack(false, null, track));
//                            } else if (isReloaded) {
//                                isReloaded = false;
//                                queueCurrentIndex = queue.getQueue().size();
//                                queue.getQueue().add(new UnifiedTrack(false, null, track));
//                            } else {
//                                queue.getQueue().add(++queueCurrentIndex, new UnifiedTrack(false, null, track));
//                            }
//                            selectedTrack = track;
//                            streamSelected = true;
//                            localSelected = false;
//                            queueCall = false;
//                            isReloaded = false;
//                            onTrackSelected(position);
//                            return true;
//                        }
//
//                        @Override
//                        public boolean onLongClick(RecyclerView parent, View view, final int position, long id) {
//                            CustomGeneralBottomSheetDialog generalBottomSheetDialog = new CustomGeneralBottomSheetDialog();
//                            generalBottomSheetDialog.setPosition(position);
//                            generalBottomSheetDialog.setTrack(new UnifiedTrack(false, null, streamingTrackList.get(position)));
//                            generalBottomSheetDialog.setFragment("Stream");
//                            generalBottomSheetDialog.show(getSupportFragmentManager(), "general_bottom_sheet_dialog");
//                            return true;
//                        }
//
//                        @Override
//                        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
//
//                        }
//                    });

                    playerContainer = findViewById(R.id.player_frag_container);

                    if (finalLocalSearchResultList.size() == 0) {
                        localsongsRecyclerView.setVisibility(GONE);
                        localNothingText.setVisibility(View.VISIBLE);
                    } else {
                        localsongsRecyclerView.setVisibility(View.VISIBLE);
                        localNothingText.setVisibility(View.INVISIBLE);
                    }

                    if (recentlyPlayed.getRecentlyPlayed().size() == 0) {
                        recentsRecycler.setVisibility(GONE);
                        recentsNothingText.setVisibility(View.VISIBLE);
                    } else {
                        recentsRecycler.setVisibility(View.VISIBLE);
                        recentsNothingText.setVisibility(View.INVISIBLE);
                    }

                    if (streamingTrackList.size() == 0) {
                        streamRecyclerContainer.setVisibility(GONE);
                        streamNothingText.setVisibility(View.VISIBLE);
                    } else {
                        streamRecyclerContainer.setVisibility(View.VISIBLE);
                        streamNothingText.setVisibility(View.INVISIBLE);
                    }

                    if (allPlaylists.getPlaylists().size() == 0) {
                        playlistsRecycler.setVisibility(GONE);
                        playlistNothingText.setVisibility(View.VISIBLE);
                    } else {
                        playlistsRecycler.setVisibility(View.VISIBLE);
                        playlistNothingText.setVisibility(View.INVISIBLE);
                    }

                }
            });
        }
    }

    /*
     * Methods for playing selected songs
     * onTrackSelected -> Used to stream tracks from soundcloud
     * onLocalTrackSelected -> Used to play local songs
     *
     * 暂时注释掉
     */
//    private void onTrackSelected(int position) {
//        isReloaded = false;
//        HideBottomFakeToolbar();
//
//        if (!queueCall) {
//            CommonUtils.hideKeyboard(this);
//
//            searchView.setQuery("", false);
//            searchView.setIconified(true);
//            new Thread(new CancelCall()).start();
//
//            isPlayerVisible = true;
//
//            PlayerFragment frag = playerFragment;
//            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
//            PlayerFragment newFragment = new PlayerFragment();
//            if (frag == null) {
//                playerFragment = newFragment;
//                int flag = 0;
//                for (int i = 0; i < favouriteTracks.getFavourite().size(); i++) {
//                    UnifiedTrack ut = favouriteTracks.getFavourite().get(i);
//                    if (!ut.getType() && ut.getStreamTrack().getTitle().equals(selectedTrack.getTitle())) {
//                        flag = 1;
//                        isFavourite = true;
//                        break;
//                    }
//                }
//                if (flag == 0) {
//                    isFavourite = false;
//                }
//                playerFragment.localIsPlaying = false;
//                playerFragment.track = selectedTrack;
//                fm.beginTransaction()
//                        .setCustomAnimations(R.anim.slide_up,
//                                R.anim.slide_down,
//                                R.anim.slide_up,
//                                R.anim.slide_down)
//                        .add(R.id.player_frag_container, newFragment, "player")
//                        .show(newFragment)
//                        .addToBackStack(null)
//                        .commitAllowingStateLoss();
//            } else {
//                if (playerFragment.track != null && !playerFragment.localIsPlaying && selectedTrack.getTitle() == playerFragment.track.getTitle()) {
//
//                } else {
//                    int flag = 0;
//                    for (int i = 0; i < favouriteTracks.getFavourite().size(); i++) {
//                        UnifiedTrack ut = favouriteTracks.getFavourite().get(i);
//                        if (!ut.getType() && ut.getStreamTrack().getTitle().equals(selectedTrack.getTitle())) {
//                            flag = 1;
//                            isFavourite = true;
//                            break;
//                        }
//                    }
//                    if (flag == 0) {
//                        isFavourite = false;
//                    }
//                    playerFragment.localIsPlaying = false;
//                    playerFragment.track = selectedTrack;
//                    frag.refresh();
//                }
//            }
//            if (!isQueueVisible)
//                showPlayer();
//        } else {
//            PlayerFragment frag = playerFragment;
//            playerFragment.localIsPlaying = false;
//            playerFragment.track = selectedTrack;
//            int flag = 0;
//            for (int i = 0; i < favouriteTracks.getFavourite().size(); i++) {
//                UnifiedTrack ut = favouriteTracks.getFavourite().get(i);
//                if (!ut.getType() && ut.getStreamTrack().getTitle().equals(selectedTrack.getTitle())) {
//                    flag = 1;
//                    isFavourite = true;
//                    break;
//                }
//            }
//            if (flag == 0) {
//                isFavourite = false;
//            }
//            frag.refresh();
//        }
//
//        if (playerFragment != null && playerFragment.snappyRecyclerView != null) {
//            playerFragment.snappyRecyclerView.getAdapter().notifyDataSetChanged();
//            playerFragment.snappyRecyclerView.setTransparency();
//        }
//
//        QueueFragment qFrag = (QueueFragment) fragMan.findFragmentByTag("queue");
//        if (qFrag != null) {
//            qFrag.updateQueueAdapter();
//        }
//
//        UnifiedTrack track = new UnifiedTrack(false, null, playerFragment.track);
//        for (int i = 0; i < recentlyPlayed.getRecentlyPlayed().size(); i++) {
//            if (!recentlyPlayed.getRecentlyPlayed().get(i).getType() && recentlyPlayed.getRecentlyPlayed().get(i).getStreamTrack().getTitle().equals(track.getStreamTrack().getTitle())) {
//                recentlyPlayed.getRecentlyPlayed().remove(i);
////                rAdapter.notifyItemRemoved(i);
//                break;
//            }
//        }
//        recentlyPlayed.getRecentlyPlayed().add(0, track);
//        if (recentlyPlayed.getRecentlyPlayed().size() > 50) {
//            recentlyPlayed.getRecentlyPlayed().remove(50);
//        }
//        recentsRecycler.setVisibility(View.VISIBLE);
//        recentsNothingText.setVisibility(View.INVISIBLE);
//        continuePlayingList.clear();
//        for (int i = 0; i < Math.min(10, recentlyPlayed.getRecentlyPlayed().size()); i++) {
//            continuePlayingList.add(recentlyPlayed.getRecentlyPlayed().get(i));
//        }
//        rAdapter.notifyDataSetChanged();
//        refreshHeaderImages();
//
//        RecentsFragment rFrag = (RecentsFragment) fragMan.findFragmentByTag("recent");
//        if (rFrag != null && rFrag.rtAdpater != null) {
//            rFrag.rtAdpater.notifyDataSetChanged();
//        }
//
//    }

    /**
     * 本地track的选择， 暂时注释掉
     * @param position
     */
//    public void onLocalTrackSelected(int position) {
//
//        isReloaded = false;
//        HideBottomFakeToolbar();
//
//        if (!queueCall) {
//
//            CommonUtils.hideKeyboard(this);
//
//            searchView.setQuery("", true);
//            searchView.setIconified(true);
//            new Thread(new CancelCall()).start();
//
////            hideTabs();
//            isPlayerVisible = true;
//
//            PlayerFragment frag = playerFragment;
//            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
//            PlayerFragment newFragment = new PlayerFragment();
//            if (frag == null) {
//                playerFragment = newFragment;
//                int flag = 0;
//                for (int i = 0; i < favouriteTracks.getFavourite().size(); i++) {
//                    UnifiedTrack ut = favouriteTracks.getFavourite().get(i);
//                    if (ut.getType() && ut.getLocalTrack().getTitle().equals(localSelectedTrack.getTitle())) {
//                        flag = 1;
//                        isFavourite = true;
//                        break;
//                    }
//                }
//                if (flag == 0) {
//                    isFavourite = false;
//                }
//
//                playerFragment.localIsPlaying = true;
//                playerFragment.localTrack = localSelectedTrack;
//                fm.beginTransaction()
//                        .setCustomAnimations(R.anim.slide_up,
//                                R.anim.slide_down,
//                                R.anim.slide_up,
//                                R.anim.slide_down)
//                        .add(R.id.player_frag_container, newFragment, "player")
//                        .show(newFragment)
//                        .commitAllowingStateLoss();
//            } else {
//                if (playerFragment.localTrack != null && playerFragment.localIsPlaying && localSelectedTrack.getTitle() == playerFragment.localTrack.getTitle()) {
//
//                } else {
//                    int flag = 0;
//                    for (int i = 0; i < favouriteTracks.getFavourite().size(); i++) {
//                        UnifiedTrack ut = favouriteTracks.getFavourite().get(i);
//                        if (ut.getType() && ut.getLocalTrack().getTitle().equals(localSelectedTrack.getTitle())) {
//                            flag = 1;
//                            isFavourite = true;
//                            break;
//                        }
//                    }
//                    if (flag == 0) {
//                        isFavourite = false;
//                    }
//                    playerFragment.localIsPlaying = true;
//                    playerFragment.localTrack = localSelectedTrack;
//                    frag.refresh();
//                }
//            }
//
//            if (!isQueueVisible)
//                showPlayer();
//
//        } else {
//            PlayerFragment frag = playerFragment;
//            playerFragment.localIsPlaying = true;
//            playerFragment.localTrack = localSelectedTrack;
//
//            int flag = 0;
//            for (int i = 0; i < favouriteTracks.getFavourite().size(); i++) {
//                UnifiedTrack ut = favouriteTracks.getFavourite().get(i);
//                if (ut.getType() && ut.getLocalTrack().getTitle().equals(localSelectedTrack.getTitle())) {
//                    flag = 1;
//                    isFavourite = true;
//                    break;
//                }
//            }
//            if (flag == 0) {
//                isFavourite = false;
//            }
//            if (frag != null)
//                frag.refresh();
//        }
//
//        if (playerFragment != null && playerFragment.snappyRecyclerView != null) {
//            playerFragment.snappyRecyclerView.getAdapter().notifyDataSetChanged();
//            playerFragment.snappyRecyclerView.setTransparency();
//        }
//
//        QueueFragment qFrag = (QueueFragment) fragMan.findFragmentByTag("queue");
//        if (qFrag != null) {
//            qFrag.updateQueueAdapter();
//        }
//        UnifiedTrack track = new UnifiedTrack(true, playerFragment.localTrack, null);
//        for (int i = 0; i < recentlyPlayed.getRecentlyPlayed().size(); i++) {
//            if (recentlyPlayed.getRecentlyPlayed().get(i).getType() && recentlyPlayed.getRecentlyPlayed().get(i).getLocalTrack().getTitle().equals(track.getLocalTrack().getTitle())) {
//                recentlyPlayed.getRecentlyPlayed().remove(i);
////                rAdapter.notifyItemRemoved(i);
//                break;
//            }
//        }
//        recentlyPlayed.getRecentlyPlayed().add(0, track);
//        if (recentlyPlayed.getRecentlyPlayed().size() == 51) {
//            recentlyPlayed.getRecentlyPlayed().remove(50);
//        }
//        recentsRecycler.setVisibility(View.VISIBLE);
//        recentsNothingText.setVisibility(View.INVISIBLE);
//        continuePlayingList.clear();
//        for (int i = 0; i < Math.min(10, recentlyPlayed.getRecentlyPlayed().size()); i++) {
//            continuePlayingList.add(recentlyPlayed.getRecentlyPlayed().get(i));
//        }
//        rAdapter.notifyDataSetChanged();
//        refreshHeaderImages();
//
//        RecentsFragment rFrag = (RecentsFragment) fragMan.findFragmentByTag("recent");
//        if (rFrag != null && rFrag.rtAdpater != null) {
//            rFrag.rtAdpater.notifyDataSetChanged();
//        }
//
//    }

    /**
     * 隐藏底部toolbar
     */
    public void HideBottomFakeToolbar() {
        bottomToolbar.setVisibility(View.INVISIBLE);
    }

    /**
     * 刷新头部图片
     */
    public void refreshHeaderImages() {
        int numSongs = localTrackList.size();
        int numRecents = recentlyPlayed.getRecentlyPlayed().size();
        if (numRecents == 0) {
            if (numSongs == 0) {
                for (int i = 0; i < 10; i++) {
                    imgLoader.DisplayImage(null, imgView[i]);
                }
            } else if (numSongs < 10) {
                for (int i = 0; i < numSongs; i++) {
                    imgLoader.DisplayImage(localTrackList.get(i).getPath(), imgView[i]);
                }
                for (int i = numSongs; i < 10; i++) {
                    imgLoader.DisplayImage(null, imgView[i]);
                }
            } else {
                for (int i = 0; i < 10; i++) {
                    imgLoader.DisplayImage(localTrackList.get(i).getPath(), imgView[i]);
                }
            }
        } else if (numRecents < 10) {
            UnifiedTrack ut;
            for (int i = 0; i < numRecents; i++) {
                ut = recentlyPlayed.getRecentlyPlayed().get(i);
                if (ut.getType())
                    imgLoader.DisplayImage(ut.getLocalTrack().getPath(), imgView[i]);
                else
                    imgLoader.DisplayImage(ut.getStreamTrack().getArtworkURL(), imgView[i]);
            }
            for (int i = numRecents; i < Math.min(numRecents + numSongs, 10); i++) {
                imgLoader.DisplayImage(localTrackList.get(i - numRecents).getPath(), imgView[i]);
            }
            if (numRecents + numSongs < 10) {
                for (int i = numRecents + numSongs; i < 10; i++) {
                    imgLoader.DisplayImage(null, imgView[i]);
                }
            }
        } else {
            UnifiedTrack ut;
            for (int i = 0; i < 10; i++) {
                ut = recentlyPlayed.getRecentlyPlayed().get(i);
                if (ut.getType())
                    imgLoader.DisplayImage(ut.getLocalTrack().getPath(), imgView[i]);
                else
                    imgLoader.DisplayImage(ut.getStreamTrack().getArtworkURL(), imgView[i]);
            }
        }
    }

    /**
     * 获取本地音乐，今天晚上完成本地音乐的检索！
     */
    private void getLocalSongs() {

        localTrackList.clear();
        recentlyAddedTrackList.clear();
        finalLocalSearchResultList.clear();
        finalRecentlyAddedTrackSearchResultList.clear();
        albums.clear();
        finalAlbums.clear();
        artists.clear();
        finalArtists.clear();

        ContentResolver musicResolver = this.getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor musicCursor = musicResolver.query(musicUri, null, null, null, MediaStore.MediaColumns.DATE_ADDED + " DESC");
        Log.e(TAG, "musicCursor = " + musicCursor);
        // E/HomeActivity: musicCursor = android.content.ContentResolver$CursorWrapperInner@a02ae70
        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int titleColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.ARTIST);
            int albumColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.ALBUM);
            int pathColumn = musicCursor.getColumnIndex
                    (android.provider.MediaStore.Audio.Media.DATA);
            int durationColumn = musicCursor.getColumnIndex
                    (MediaStore.Audio.Media.DURATION);

            // add songs to list
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String thisArtist = musicCursor.getString(artistColumn);
                String thisAlbum = musicCursor.getString(albumColumn);
                String path = musicCursor.getString(pathColumn);
                long duration = musicCursor.getLong(durationColumn);
                if (duration > 10000) {
                    LocalTrack lt = new LocalTrack(thisId, thisTitle, thisArtist, thisAlbum, path, duration);
                    localTrackList.add(lt);
                    Log.e(TAG, "localTrackList = " + localTrackList);
                    // 事实证明是有数据的：
                    // localTrackList = [LocalTrack{id=181794, title='sound', artist='<unknown>', album='actor0', path='/storage/emulated/0/chat/Resource/74/actor0/sound.mp3', duration=17189}, LocalTrack{id=181795, title='From Paris With Love', artist='Koch (BMI)', album='1062', path='/storage/emulated/0/chat/Resource/78/actor0/audio.mp3', duration=18290}]
                    finalLocalSearchResultList.add(lt);
                    if (recentlyAddedTrackList.size() <= 50) {
                        recentlyAddedTrackList.add(lt);
                        finalRecentlyAddedTrackSearchResultList.add(lt);
                    }

                    int pos;
                    if (thisAlbum != null) {
                        pos = checkAlbum(thisAlbum);
                        if (pos != -1) {
                            albums.get(pos).getAlbumSongs().add(lt);
                        } else {
                            List<LocalTrack> llt = new ArrayList<>();
                            llt.add(lt);
                            Album ab = new Album(thisAlbum, llt);
                            albums.add(ab); // 添加专辑名字
                        }
                        if (pos != -1) {
                            finalAlbums.get(pos).getAlbumSongs().add(lt);
                        } else {
                            List<LocalTrack> llt = new ArrayList<>();
                            llt.add(lt);
                            Album ab = new Album(thisAlbum, llt);
                            finalAlbums.add(ab);
                        }
                    }

                    if (thisArtist != null) {
                        pos = checkArtist(thisArtist);
                        if (pos != -1) {
                            artists.get(pos).getArtistSongs().add(lt);
                        } else {
                            List<LocalTrack> llt = new ArrayList<>();
                            llt.add(lt);
                            Artist ab = new Artist(thisArtist, llt);
                            artists.add(ab);
                        }
                        if (pos != -1) {
                            finalArtists.get(pos).getArtistSongs().add(lt);
                        } else {
                            List<LocalTrack> llt = new ArrayList<>();
                            llt.add(lt);
                            Artist ab = new Artist(thisArtist, llt);
                            finalArtists.add(ab);
                        }
                    }

                    File f = new File(path);
                    String dirName = f.getParentFile().getName();
                    if (getFolder(dirName) == null) {
                        MusicFolder mf = new MusicFolder(dirName);
                        mf.getLocalTracks().add(lt);
                        allMusicFolders.getMusicFolders().add(mf);
                    } else {
                        getFolder(dirName).getLocalTracks().add(lt);
                    }
                }

            }
            while (musicCursor.moveToNext());
        }

        if (musicCursor != null)
            musicCursor.close();

        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        try {
            if (localTrackList.size() > 0) {
                Collections.sort(localTrackList, new LocalMusicComparator());
                Collections.sort(finalLocalSearchResultList, new LocalMusicComparator());
            }
            if (albums.size() > 0) {
                Collections.sort(albums, new AlbumComparator());
                Collections.sort(finalAlbums, new AlbumComparator());
            }
            if (artists.size() > 0) {
                Collections.sort(artists, new ArtistComparator());
                Collections.sort(finalArtists, new ArtistComparator());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<UnifiedTrack> tmp = new ArrayList<>();
        boolean queueCurrentIndexCollision = false;
        int indexCorrection = 0;
        for (int i = 0; i < queue.getQueue().size(); i++) {
            UnifiedTrack ut = queue.getQueue().get(i);
            if (ut.getType()) {
                if (!checkTrack(ut.getLocalTrack())) {
                    if (i == queueCurrentIndex) {
                        queueCurrentIndexCollision = true;
                    } else if (i < queueCurrentIndex) {
                        indexCorrection++;
                    }
                    tmp.add(ut);
                }
            }
        }
        for (int i = 0; i < tmp.size(); i++) {
            queue.getQueue().remove(tmp.get(i));
        }
        if (queueCurrentIndexCollision) {
            if (queue.getQueue().size() > 0) {
                queueCurrentIndex = 0;
            } else {
                queue = new Queue();
            }
        } else {
            queueCurrentIndex -= indexCorrection;
        }

        tmp.clear();

        for (int i = 0; i < recentlyPlayed.getRecentlyPlayed().size(); i++) {
            UnifiedTrack ut = recentlyPlayed.getRecentlyPlayed().get(i);
            if (ut.getType()) {
                if (!checkTrack(ut.getLocalTrack())) {
                    tmp.add(ut);
                }
            }
        }
        for (int i = 0; i < tmp.size(); i++) {
            recentlyPlayed.getRecentlyPlayed().remove(tmp.get(i));
        }

        List<UnifiedTrack> temp = new ArrayList<>();
        List<Playlist> tmpPL = new ArrayList<>();

        for (int i = 0; i < allPlaylists.getPlaylists().size(); i++) {
            Playlist pl = allPlaylists.getPlaylists().get(i);
            for (int j = 0; j < pl.getSongList().size(); j++) {
                UnifiedTrack ut = pl.getSongList().get(j);
                if (ut.getType()) {
                    if (!checkTrack(ut.getLocalTrack())) {
                        temp.add(ut);
                    }
                }
            }
            for (int j = 0; j < temp.size(); j++) {
                pl.getSongList().remove(temp.get(j));
            }
            temp.clear();
            if (pl.getSongList().size() == 0) {
                tmpPL.add(pl);
            }
        }
        for (int i = 0; i < tmpPL.size(); i++) {
            allPlaylists.getPlaylists().remove(tmpPL.get(i));
        }
        tmpPL.clear();
    }

    /**
     * 检查_跟踪track
     *
     * @param lt
     * @return
     */
    public boolean checkTrack(LocalTrack lt) {
        for (int i = 0; i < localTrackList.size(); i++) {
            LocalTrack localTrack = localTrackList.get(i);
            if (localTrack.getTitle().equals(lt.getTitle())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文件夹
     *
     * @param folderName
     * @return
     */
    public MusicFolder getFolder(String folderName) {
        MusicFolder mf = null;
        for (int i = 0; i < allMusicFolders.getMusicFolders().size(); i++) {
            MusicFolder mf1 = allMusicFolders.getMusicFolders().get(i);
            if (mf1.getFolderName().equals(folderName)) {
                mf = mf1;
                break;
            }
        }
        return mf;
    }

    /**
     * 检查艺术家
     *
     * @param artist
     * @return
     */
    public int checkArtist(String artist) {
        for (int i = 0; i < artists.size(); i++) {
            Artist at = artists.get(i);
            if (at.getName().equals(artist)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * 检查album
     *
     * @param album
     * @return
     */
    public int checkAlbum(String album) {
        for (int i = 0; i < albums.size(); i++) {
            Album ab = albums.get(i);
            if (ab.getName().equals(album)) {
                return i;
            }
        }
        return -1;
    }


    public class SaveVersionCode extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                String json7 = gson.toJson(versionCode);
                prefsEditor.putString("versionCode", json7);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
