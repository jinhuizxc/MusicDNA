package com.example.jh.musicdna.ui.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextPaint;
import android.util.Log;
import android.view.Display;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jh.musicdna.Config;
import com.example.jh.musicdna.MusicDNAApplication;
import com.example.jh.musicdna.R;
import com.example.jh.musicdna.serviceandbroadcastreceiver.broadcastreceiver.HeadSetReceiver;
import com.example.jh.musicdna.clickitemtouchlistener.ClickItemTouchListener;
import com.example.jh.musicdna.custombottomsheets.CustomGeneralBottomSheetDialog;
import com.example.jh.musicdna.custombottomsheets.CustomLocalBottomSheetDialog;
import com.example.jh.musicdna.imageloader.ImageLoader;
import com.example.jh.musicdna.interfaces.ServiceCallbacks;
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
import com.example.jh.musicdna.serviceandbroadcastreceiver.notificationmanager.Constants;
import com.example.jh.musicdna.serviceandbroadcastreceiver.MediaPlayerService;
import com.example.jh.musicdna.ui.adapter.horizontalrecycleradapters.LocalTracksHorizontalAdapter;
import com.example.jh.musicdna.ui.adapter.horizontalrecycleradapters.PlayListsHorizontalAdapter;
import com.example.jh.musicdna.ui.adapter.horizontalrecycleradapters.RecentsListHorizontalAdapter;
import com.example.jh.musicdna.ui.adapter.horizontalrecycleradapters.StreamTracksHorizontalAdapter;
import com.example.jh.musicdna.ui.adapter.playlistdialogadapter.AddToPlaylistAdapter;
import com.example.jh.musicdna.ui.fragment.AboutFragment.AboutFragment;
import com.example.jh.musicdna.ui.fragment.AllPlaylistsFragment.AllPlaylistsFragment;
import com.example.jh.musicdna.ui.fragment.EditLocalSongFragment.EditLocalSongFragment;
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
import com.example.jh.musicdna.ui.fragment.ViewPlaylistFragment.PlaylistTrackAdapter;
import com.example.jh.musicdna.ui.fragment.ViewPlaylistFragment.ViewPlaylistFragment;
import com.example.jh.musicdna.ui.fragment.ViewSavedDNAFragment.ViewSavedDNAFragment;
import com.example.jh.musicdna.utils.CommonUtils;
import com.example.jh.musicdna.utils.FileUtils;
import com.example.jh.musicdna.utils.MediaCacheUtils;
import com.example.jh.musicdna.utils.comparators.AlbumComparator;
import com.example.jh.musicdna.utils.comparators.ArtistComparator;
import com.example.jh.musicdna.utils.comparators.LocalMusicComparator;
import com.example.jh.musicdna.view.CustomLinearGradient;
import com.example.jh.musicdna.view.visualizers.VisualizerView;
import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;
import com.google.gson.Gson;
import com.lantouzi.wheelview.WheelView;
import com.squareup.leakcanary.RefWatcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

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
 * <p>
 * 无论如果要把这个类完成！2/22
 * <p>
 * 最后一项任务：
 * <p>
 * 列表为空的判断，必须：
 * 实现PlaylistTrackAdapter.onPlaylistEmptyListener,
 * <p>
 * 项目中没有bindService服务，仅仅是启动服务
 * <p>
 * 音频播放的执行，项目暂时不需要广播可以运行，与广播无关哦
 *
 * 音乐播放/通知栏
 *
 * 最后的最后进行测试bug；
 */

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        SearchView.OnQueryTextListener,
        LocalMusicFragment.OnLocalTrackSelectedListener,
        AlbumFragment.onAlbumClickListener,
        ArtistFragment.onArtistClickListener,
        RecentlyAddedSongsFragment.OnLocalTrackSelectedListener,
        ViewAlbumFragment.albumCallbackListener,
        ViewArtistFragment.artistCallbackListener,
        PlayerFragment.PlayerFragmentCallbackListener,
        PlayerFragment.onPlayPauseListener,
        PlaylistTrackAdapter.onPlaylistEmptyListener,
        FavouritesFragment.FavouriteFragmentCallback,
        RecentsFragment.RecentsCallbackListener,
        FolderFragment.onFolderClickedListener,
        FolderContentFragment.FolderCallbackListener,
        EqualizerFragment.onCheckChangedListener,
        SettingsFragment.SettingsFragmentCallbackListener,
        QueueFragment.queueCallbackListener,
        ServiceCallbacks,
        HeadSetReceiver.onHeadsetEventListener,
        AllPlaylistsFragment.AllPlaylistCallbackListener,
        EditLocalSongFragment.EditFragmentCallbackListener,
        NewPlaylistFragment.NewPlaylistFragmentCallbackListener,
        ViewPlaylistFragment.PlaylistCallbackListener,
        StreamMusicFragment.OnTrackSelectedListener {

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
    public static List<LocalTrack> tempFolderContent;

    // boolean 合集
    public static boolean isReloaded = true;
    public static boolean hasSoftNavbar = false;
    public static boolean streamSelected = false;
    public static boolean localSelected = false;
    public static boolean queueCall = false;

    // Canvas类
    public static Canvas cacheCanvas;

    // 编辑本地音乐
    public static LocalTrack editSong;

    // 基本控件
    private Dialog progress;
    // TextView集合
    public TextView copyrightText;
    TextView localNothingText;
    TextView playlistNothingText;
    TextView recentsNothingText;
    TextView streamNothingText;
    public FrameLayout bottomToolbar;
    public Toolbar spHome;
    public ImageView playerControllerHome;
    ImageView[] imgView = new ImageView[10];
    public CircleImageView spImgHome;
    public TextView spTitleHome;
    View playerContainer;
    ImageView navImageView;

    TextView recentsViewAll, playlistsViewAll;
    TextView localViewAll, streamViewAll;


    public static Track selectedTrack;

    public static int queueCurrentIndex = 0;
    public static LocalTrack localSelectedTrack;


    // 适配器合集
    public LocalTracksHorizontalAdapter adapter;
    public StreamTracksHorizontalAdapter sAdapter;
    public PlayListsHorizontalAdapter pAdapter;
    public RecentsListHorizontalAdapter rAdapter;


    public static int themeColor = Color.parseColor("#B24242"); // 红色

    // int变量
    public static int screen_width;
    public static int screen_height;
    public static float ratio;
    public static float ratio2;
    public static int renamePlaylistNumber;
    public static int tempPlaylistNumber;
    public int originalQueueIndex = 0;

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

    public static NotificationManager notificationManager;
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
    ImageView favBanner;
    ImageView recentBanner;
    ImageView folderBanner;
    ImageView savedDNABanner;
    ImageView localBannerPlayAll;

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
    public static boolean hasQueueEnded = false;


    // 播放界面： 控制音乐的boolean值
    public static boolean isFavourite = false;
    public static boolean shuffleEnabled = false;
    public static boolean repeatEnabled = false;
    public static boolean repeatOnceEnabled = false;
    public static boolean isSaveDNAEnabled = false;
    public static boolean nextControllerClicked = false;
    public static float seekBarColor;

    static boolean isSaveQueueRunning = false;
    public boolean isPlayerTransitioning = false;
    static boolean isSaveRecentsRunning = false;
    static boolean isSaveFavouritesRunning = false;
    static boolean isSaveDNAsRunning = false;
    static boolean isSaveSettingsRunning = false;
    static boolean isSaveEqualizerRunning = false;

    boolean isNotificationVisible = false;


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
    public byte[] mBytes;

    // 广播与服务
    public HeadSetReceiver headSetReceiver;
    // 服务的绑定连接
    ServiceConnection serviceConnection;
    private boolean bound = false;
    private MediaPlayerService myService;

    // PhoneStateListener
    // PhoneStateListener是给三方app监听通信状态变化的方法
    public PhoneStateListener phoneStateListener;
    boolean wasMediaPlayerPlaying = false;

    public Bitmap selectedImage;

    //////////////////////////////////////////


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
        initCase();
        initView();
        initServiceAndReceiver();

        PackageInfo pInfo;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = pInfo.versionName;
            versionCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        copyrightText = (TextView) findViewById(R.id.copyright_text);
        copyrightText.setText("Music DNA v " + versionName);

        if (SplashActivity.tf4 != null) {
            try {
                copyrightText.setTypeface(SplashActivity.tf4);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void initServiceAndReceiver() {
        // 动态注册广播,不用注册
        headSetReceiver = new HeadSetReceiver();
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(headSetReceiver, filter);
        Log.e(TAG, "动态注册广播");

        // 绑定服务连接
        serviceConnection = new ServiceConnection() {

            @Override
            public void onServiceConnected(ComponentName className, IBinder service) {
                // cast the IBinder and get MyService instance
                MediaPlayerService.LocalBinder binder = (MediaPlayerService.LocalBinder) service;
                myService = binder.getService();
                bound = true;
                myService.setCallbacks(HomeActivity.this); // register
            }

            @Override
            public void onServiceDisconnected(ComponentName arg0) {
                bound = false;
            }
        };
    }

    /**
     * showCase
     */
    private void initCase() {
        // 引导页的配置
        mEndButton = new Button(this);
        mEndButton.setBackgroundColor(themeColor);
        mEndButton.setTextColor(Color.WHITE);

        tp = new TextPaint();
        tp.setColor(themeColor);
        tp.setTextSize(65 * ratio);
        tp.setFakeBoldText(true);

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
    }

    private void initView() {

        // 初始化碎片管理对象
        fragmentManager = getSupportFragmentManager();
        // 初始化toolbar
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
        // navImageView的点击事件
        View header = navigationView.getHeaderView(0);
        navImageView = (ImageView) header.findViewById(R.id.nav_image_view);
        if (navImageView != null) {
            navImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PlayerFragment pFrag = getPlayerFragment();
                    if (pFrag != null) {
                        if (pFrag.mMediaPlayer != null && pFrag.mMediaPlayer.isPlaying()) {
                            onBackPressed();
                            isPlayerVisible = true;
//                            hideTabs();
                            showPlayer();
                        }
                    }
                }
            });
        }

        imgLoader = new ImageLoader(this);
        ctx = this;

        // 初始化头部image
        initializeHeaderImages();

        minuteList = new ArrayList<>();
        for (int i = 1; i < 25; i++) {
            minuteList.add(String.valueOf(i * 5));
        }
        // 睡眠计时器的handler
        sleepHandler = new Handler();

        // 最近/播放列表
        recentsViewAll = (TextView) findViewById(R.id.recents_view_all);
        recentsViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment("recent");
            }
        });
        playlistsViewAll = (TextView) findViewById(R.id.playlists_view_all);
        playlistsViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment("allPlaylists");
            }
        });

        // 显示 Local/SoundCloud的布局
        localViewAll = (TextView) findViewById(R.id.localViewAll);
        localViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment("local");
            }
        });
        streamViewAll = (TextView) findViewById(R.id.streamViewAll);
        streamViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment("stream");
            }
        });

        hasSoftNavbar = CommonUtils.hasNavBar(this);
        statusBarHeightinDp = CommonUtils.getStatusBarHeight(this);
        navBarHeightSizeinDp = hasSoftNavbar ? CommonUtils.getNavBarHeight(this) : 0;


        // 5个banner + 1个播放view
        localBanner = (RelativeLayout) findViewById(R.id.localBanner);
        localBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment("local");
            }
        });
        favBanner = (ImageView) findViewById(R.id.favBanner);
        favBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment("favourite");
            }
        });
        recentBanner = (ImageView) findViewById(R.id.recentBanner);
        recentBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment("recent");
            }
        });
        folderBanner = (ImageView) findViewById(R.id.folderBanner);
        folderBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment("allFolders");
            }
        });
        savedDNABanner = (ImageView) findViewById(R.id.savedDNABanner);
        savedDNABanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment("allSavedDNAs");
            }
        });

        // 播放本地里列表所有歌曲
        localBannerPlayAll = (ImageView) findViewById(R.id.local_banner_play_all);
        localBannerPlayAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                queue.getQueue().clear();
                for (int i = 0; i < localTrackList.size(); i++) {
                    UnifiedTrack ut = new UnifiedTrack(true, localTrackList.get(i), null);
                    queue.getQueue().add(ut);
                }
                if (queue.getQueue().size() > 0) {
                    Random r = new Random();
                    int tmp = r.nextInt(queue.getQueue().size());
                    queueCurrentIndex = tmp;
                    LocalTrack track = localTrackList.get(tmp);
                    localSelectedTrack = track;
                    streamSelected = false;
                    localSelected = true;
                    queueCall = false;
                    isReloaded = false;
                    onLocalTrackSelected(-1);
                }
            }
        });

        // 底部播放显示布局，显示底部最近的歌曲
        bottomToolbar = (FrameLayout) findViewById(R.id.bottomMargin);
        spHome = (Toolbar) findViewById(R.id.smallPlayer_home);
        spHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (queue != null && queue.getQueue().size() > 0) {
                    onQueueItemClicked(queueCurrentIndex);
                    bottomToolbar.setVisibility(View.INVISIBLE);
                }
            }
        });
        spImgHome = (CircleImageView) findViewById(R.id.selected_track_image_sp_home);
        spTitleHome = (TextView) findViewById(R.id.selected_track_title_sp_home);
        playerControllerHome = (ImageView) findViewById(R.id.player_control_sp_home);
        playerControllerHome.setImageResource(R.drawable.ic_play_arrow_white_48dp);
        playerControllerHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (queue != null && queue.getQueue().size() > 0) {
                    onQueueItemClicked(queueCurrentIndex);
                    bottomToolbar.setVisibility(View.INVISIBLE);
                }
            }
        });

        localRecyclerContainer = (RelativeLayout) findViewById(R.id.localRecyclerContainer);
        recentsRecyclerContainer = (RelativeLayout) findViewById(R.id.recentsRecyclerContainer);
        streamRecyclerContainer = (RelativeLayout) findViewById(R.id.streamRecyclerContainer);
        playlistRecyclerContainer = (RelativeLayout) findViewById(R.id.playlistRecyclerContainer);

        // 设置字体
        if (SplashActivity.tf4 != null) {
            try {
                ((TextView) findViewById(R.id.playListRecyclerLabel)).setTypeface(SplashActivity.tf4);
                ((TextView) findViewById(R.id.recentsRecyclerLabel)).setTypeface(SplashActivity.tf4);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        localNothingText = (TextView) findViewById(R.id.localNothingText);
        streamNothingText = (TextView) findViewById(R.id.streamNothingText);
        playlistNothingText = (TextView) findViewById(R.id.playlistNothingText);
        recentsNothingText = (TextView) findViewById(R.id.recentsNothingText);


        connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert connManager != null;
        mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String incomingNumber) {

                PlayerFragment pFrag = playerFragment;

                if (playerFragment != null) {
                    if (state == TelephonyManager.CALL_STATE_RINGING) {
                        //Incoming call: Pause music
                        if (pFrag.mMediaPlayer != null && pFrag.mMediaPlayer.isPlaying()) {
                            wasMediaPlayerPlaying = true;
                            pFrag.togglePlayPause();
                        } else {
                            wasMediaPlayerPlaying = false;
                        }
                    } else if (state == TelephonyManager.CALL_STATE_IDLE) {
                        //Not in call: Play music
                        if (pFrag.mMediaPlayer != null && !pFrag.mMediaPlayer.isPlaying() && wasMediaPlayerPlaying) {
                            pFrag.togglePlayPause();
                        }
                    } else if (state == TelephonyManager.CALL_STATE_OFFHOOK) {
                        //A call is dialing, active or on hold
                        if (playerFragment.mMediaPlayer != null && pFrag.mMediaPlayer.isPlaying()) {
                            wasMediaPlayerPlaying = true;
                            pFrag.togglePlayPause();
                        } else {
                            wasMediaPlayerPlaying = false;
                        }
                    }
                }
                super.onCallStateChanged(state, incomingNumber);
            }
        };
        TelephonyManager mgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (mgr != null) {
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        }

        // 数据恢复
        mPrefs = getPreferences(MODE_PRIVATE);
        prefsEditor = mPrefs.edit();
        gson = new Gson();

        main = this;

        // dialog的初始化, 加载保存的数据
        progress = new Dialog(ctx);
        progress.setCancelable(false);
        progress.requestWindowFeature(Window.FEATURE_NO_TITLE);
        progress.setContentView(R.layout.custom_progress_dialog);
        progress.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        progress.show();
        // 启动SavedData的异步任务,加载保存的数据
        new loadSavedData().execute();
    }

    /**
     * 初始化头部image
     */
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
        PlayerFragment plFrag = playerFragment;
        LocalMusicViewPagerFragment flmFrag = (LocalMusicViewPagerFragment) fragmentManager.findFragmentByTag("local");
        LocalMusicFragment lFrag = null;
        if (flmFrag != null) {
            lFrag = (LocalMusicFragment) flmFrag.getFragmentByPosition(0);
        }
        QueueFragment qFrag = (QueueFragment) fragmentManager.findFragmentByTag("queue");
        EqualizerFragment eqFrag = (EqualizerFragment) fragmentManager.findFragmentByTag("equalizer");
        ViewSavedDNAFragment vsdFrag = (ViewSavedDNAFragment) fragmentManager.findFragmentByTag("allSavedDNAs");


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (showCase != null && showCase.isShowing()) {
            showCase.hide();
        } else if (plFrag != null && plFrag.isShowcaseVisible()) {
            plFrag.hideShowcase();
        } else if (lFrag != null && lFrag.isShowcaseVisible()) {
            lFrag.hideShowcase();
        } else if (qFrag != null && qFrag.isShowcaseVisible()) {
            qFrag.hideShowcase();
        } else if (eqFrag != null && eqFrag.isShowcaseVisible()) {
            eqFrag.hideShowcase();
        } else if (vsdFrag != null && vsdFrag.isShowcaseVisible()) {
            vsdFrag.hideShowcase();
        } else if (isFullScreenEnabled) {
            isFullScreenEnabled = false;
            plFrag.bottomContainer.setVisibility(View.VISIBLE);
            plFrag.seekBarContainer.setVisibility(View.VISIBLE);
            plFrag.toggleContainer.setVisibility(View.VISIBLE);
            plFrag.spToolbar.setVisibility(View.VISIBLE);
            onFullScreen();
        } else if (isEqualizerVisible) {
            showPlayer2();
        } else if (isQueueVisible) {
            showPlayer3();
        } else if (isPlayerVisible && !isPlayerTransitioning && playerFragment.smallPlayer != null) {
            hidePlayer();
//            showTabs();
            isPlayerVisible = false;
        } else if (isLocalVisible && flmFrag != null && flmFrag.searchBox != null && flmFrag.isSearchboxVisible) {
            flmFrag.searchBox.setText("");
            flmFrag.searchBox.setVisibility(View.INVISIBLE);
            flmFrag.isSearchboxVisible = false;
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            flmFrag.searchIcon.setImageResource(R.drawable.ic_search);
            flmFrag.fragTitle.setVisibility(View.VISIBLE);
        } else if (!searchView.isIconified()) {
            searchView.setQuery("", true);
            searchView.setIconified(true);
            new Thread(new CancelCall()).start();
            if (localRecyclerContainer.getVisibility() == View.VISIBLE || streamRecyclerContainer.getVisibility() == View.VISIBLE) {
                localRecyclerContainer.setVisibility(GONE);
                streamRecyclerContainer.setVisibility(GONE);
            }
        } else if (localRecyclerContainer.getVisibility() == View.VISIBLE || streamRecyclerContainer.getVisibility() == View.VISIBLE) {
            localRecyclerContainer.setVisibility(GONE);
            streamRecyclerContainer.setVisibility(GONE);
        } else {
            if (isEditVisible) {
                hideFragment("Edit");
            } else if (isAlbumVisible) {
                hideFragment("viewAlbum");
            } else if (isArtistVisible) {
                hideFragment("viewArtist");
            } else {
                if (isLocalVisible) {
                    hideFragment("local");
                    setTitle("Music DNA");
                } else if (isQueueVisible) {
                    hideFragment("queue");
                    setTitle("Music DNA");
                } else if (isStreamVisible) {
                    hideFragment("stream");
                    setTitle("Music DNA");
                } else if (isPlaylistVisible) {
                    hideFragment("playlist");
                    setTitle("Music DNA");
                } else if (isNewPlaylistVisible) {
                    hideFragment("newPlaylist");
                    setTitle("Music DNA");
                } else if (isEqualizerVisible) {
                    finalSelectedTracks.clear();
                    hideFragment("equalizer");
                    setTitle("Music DNA");
                } else if (isFavouriteVisible) {
                    hideFragment("favourite");
                    setTitle("Music DNA");
                } else if (isAllPlaylistVisible) {
                    hideFragment("allPlaylists");
                    setTitle("Music DNA");
                } else if (isFolderContentVisible) {
                    hideFragment("folderContent");
                    setTitle("Folders");
                } else if (isAllFolderVisible) {
                    hideFragment("allFolders");
                    setTitle("Music DNA");
                } else if (isAllSavedDnaVisisble) {
                    hideFragment("allSavedDNAs");
                    setTitle("Music DNA");
                } else if (isSavedDNAVisible) {
                    hideFragment("savedDNA");
                    setTitle("Music DNA");
                } else if (isRecentVisible) {
                    hideFragment("recent");
                    setTitle("Music DNA");
                } else if (isAboutVisible) {
                    hideFragment("About");
                    setTitle("Settings");
                } else if (isSettingsVisible) {
                    hideFragment("settings");
                    // SaveSettings的异步任务
                    new SaveSettings().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    setTitle("Music DNA");
                } else if (!isPlayerTransitioning) {
                    startActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME));
                }
            }
        }
    }

    /**
     * 显示player2
     */
    private void showPlayer2() {
        isPlayerVisible = true;
        isEqualizerVisible = false;
        isQueueVisible = false;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideFragment("equalizer");
            }
        }, 350);

        playerContainer.setVisibility(View.VISIBLE);
        if (playerFragment.mVisualizerView != null)
            playerFragment.mVisualizerView.setVisibility(View.INVISIBLE);

        isPlayerTransitioning = true;

        playerContainer.animate()
                .setDuration(300)
                .translationX(0)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        isPlayerTransitioning = false;
                        if (playerFragment != null && playerFragment.snappyRecyclerView != null) {
                            playerFragment.snappyRecyclerView.setTransparency();
                        }
                        if (!playerFragment.isLyricsVisisble) {
                            playerFragment.mVisualizerView.setVisibility(View.VISIBLE);
                        }
                    }
                });
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
            FragmentManager fm = getSupportFragmentManager();
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
            FragmentManager fm = getSupportFragmentManager();
            EqualizerFragment newFragment = (EqualizerFragment) fm.findFragmentByTag("equalizer");
            if (newFragment == null) {
                newFragment = new EqualizerFragment();
                Log.e(TAG, "EqualizerFragment");
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
            FragmentManager fm = getSupportFragmentManager();
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
            FragmentManager fm = getSupportFragmentManager();
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
            FragmentManager fm = getSupportFragmentManager();
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
            FragmentManager fm = getSupportFragmentManager();
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
            FragmentManager fm = getSupportFragmentManager();
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
            FragmentManager fm = getSupportFragmentManager();
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
            FragmentManager fm = getSupportFragmentManager();
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
            FragmentManager fm = getSupportFragmentManager();
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
     * 隐藏fragment，
     * 通过findFragmentByTag标签去隐藏某个界面
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
            isEqualizerVisible = false;
            // SaveEqualizer的异步任务
            new SaveEqualizer().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

            FragmentManager fm = getSupportFragmentManager();
            Fragment frag = fm.findFragmentByTag("equalizer");
            if (frag != null) {
                fm.beginTransaction()
                        .remove(frag)
                        .commitAllowingStateLoss();
            }
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
            FragmentManager fm = getSupportFragmentManager();
            Fragment frag = fm.findFragmentByTag("newPlaylist");
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
        // 对searchView设置querytext的监听
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
//                timerSetText.setText("Timer set for " + minutesLeft + " minutes from now.");
                timerSetText.setText("定时器从现在起还有：" + minutesLeft + "分钟");
            } else if (minutesLeft == 1) {
//                timerSetText.setText("Timer set for " + 1 + " minute from now.");
                timerSetText.setText("定时器从现在起还有：" + 1 + "分钟");
            } else {
//                timerSetText.setText("Music will stop after completion of current song");
                timerSetText.setText("音乐会在完成播放当前歌曲后停止");
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
//                Toast.makeText(ctx, "Timer removed", Toast.LENGTH_SHORT).show();
                Toast.makeText(ctx, "定时器移除成功", Toast.LENGTH_SHORT).show();
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
                        if (playerFragment.mMediaPlayer == null || !playerFragment.mMediaPlayer.isPlaying()) {
                            main.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
//                                    Toast.makeText(ctx, "Sleep timer timed out, closing app", Toast.LENGTH_SHORT).show();
                                    Toast.makeText(ctx, "睡眠事件超时，将关闭app", Toast.LENGTH_SHORT).show();
                                    if (playerFragment != null && playerFragment.timer != null)
                                        playerFragment.timer.cancel();
                                    notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    try {
                                        notificationManager.cancel(1);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } finally {
                                        finish();
                                    }
                                }
                            });
                        }
                    }
                }, minutes * 60 * 1000);
//                Toast.makeText(ctx, "Timer set for " + minutes + " minutes", Toast.LENGTH_SHORT).show();
                Toast.makeText(ctx, "睡眠时间设置" + minutes + " 分钟", Toast.LENGTH_SHORT).show();
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

    /*
    * Query change listeners for searchview and required methods for searching and updating recycler views.
    * onQueryTextSubmit(String query) -> updates lists based on query when entered is pressed on keyboard.
    * onQueryTextChange(String query) -> updates lists based on query while text is being entered into the search view.
    * updateLocalList() / updateAlbumList() / updateArtistList() / updateStreamingList() -> helper methods to update corresponding recyclers.
    */
    @Override
    public boolean onQueryTextSubmit(String query) {
        CommonUtils.hideKeyboard(this);
        updateLocalList(query.trim());
        updateStreamingList(query.trim());
        updateAlbumList(query.trim());
        updateArtistList(query.trim());
        updateRecentlyAddedLocalList(query.trim());
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        updateLocalList(newText.trim());
        updateStreamingList(newText.trim());
        updateAlbumList(newText.trim());
        updateArtistList(newText.trim());
        updateRecentlyAddedLocalList(newText.trim());
        return true;
    }

    private void updateRecentlyAddedLocalList(String query) {
        LocalMusicViewPagerFragment flmFrag = (LocalMusicViewPagerFragment) fragmentManager.findFragmentByTag("local");
        RecentlyAddedSongsFragment rasFrag = null;
        if (flmFrag != null)
            rasFrag = (RecentlyAddedSongsFragment) flmFrag.getFragmentByPosition(3);

        if (rasFrag != null)
            rasFrag.hidePlayAllFab();

        /*Update the Local List*/

        if (!isLocalVisible)
            localRecyclerContainer.setVisibility(View.VISIBLE);

        finalRecentlyAddedTrackSearchResultList.clear();
        for (int i = 0; i < recentlyAddedTrackList.size(); i++) {
            LocalTrack lt = recentlyAddedTrackList.get(i);
            String tmp1 = lt.getTitle().toLowerCase();
            String tmp2 = query.toLowerCase();
            if (tmp1.contains(tmp2)) {
                finalRecentlyAddedTrackSearchResultList.add(lt);
            }
        }

        if (!isLocalVisible && localsongsRecyclerView != null) {
            if (finalRecentlyAddedTrackSearchResultList.size() == 0) {
                localsongsRecyclerView.setVisibility(GONE);
                localNothingText.setVisibility(View.VISIBLE);
            } else {
                localsongsRecyclerView.setVisibility(View.VISIBLE);
                localNothingText.setVisibility(View.INVISIBLE);
            }
            (localsongsRecyclerView.getAdapter()).notifyDataSetChanged();
        }

        if (rasFrag != null)
            rasFrag.updateAdapter();

        if (query.equals("")) {
            localRecyclerContainer.setVisibility(GONE);
        }
        if (query.equals("") && isLocalVisible) {
            if (rasFrag != null)
                rasFrag.showPlayAllFab();
        }

    }

    /**
     * 更新艺术家列表
     *
     * @param query
     */
    private void updateArtistList(String query) {
        finalArtists.clear();
        for (int i = 0; i < artists.size(); i++) {
            Artist artist = artists.get(i);
            String tmp1 = artist.getName().toLowerCase();
            String tmp2 = query.toLowerCase();
            if (tmp1.contains(tmp2)) {
                finalArtists.add(artist);
            }
        }

        LocalMusicViewPagerFragment flmFrag = (LocalMusicViewPagerFragment) fragmentManager.findFragmentByTag("local");
        if (flmFrag != null) {
            ArtistFragment aFrag = (ArtistFragment) flmFrag.getFragmentByPosition(2);
            if (aFrag != null) {
                aFrag.updateAdapter();
            }
        }
    }

    /**
     * 更新唱片列表
     *
     * @param query
     */
    private void updateAlbumList(String query) {
        finalAlbums.clear();
        for (int i = 0; i < albums.size(); i++) {
            Album album = albums.get(i);
            String tmp1 = album.getName().toLowerCase();
            String tmp2 = query.toLowerCase();
            if (tmp1.contains(tmp2)) {
                finalAlbums.add(album);
            }
        }
        LocalMusicViewPagerFragment flmFrag = (LocalMusicViewPagerFragment) fragmentManager.findFragmentByTag("local");
        if (flmFrag != null) {
            AlbumFragment aFrag = (AlbumFragment) flmFrag.getFragmentByPosition(1);
            if (aFrag != null) {
                aFrag.updateAdapter();
            }
        }
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

                // 启动CancelCall的线程
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
                    // 获取call对象
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
     * 停止加载指示器
     */
    private void stopLoadingIndicator() {
        findViewById(R.id.loadingIndicator).setVisibility(View.INVISIBLE);
        soundcloudRecyclerView.setVisibility(View.VISIBLE);
        if (streamingTrackList.size() == 0) {
            streamNothingText.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 开始加载指示器
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
            Log.e(TAG, "得到保存的dna数据 = " + recentlyPlayed);
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

        UnifiedTrack ut = null;
        if (fragment == null) {
            ut = new UnifiedTrack(true, finalLocalSearchResultList.get(position), null);
        } else if (fragment.equals(getString(R.string.Artists))) {
            ut = new UnifiedTrack(true, tempArtist.getArtistSongs().get(position), null);
        } else if (fragment.equals(getString(R.string.Albums))) {
            ut = new UnifiedTrack(true, tempAlbum.getAlbumSongs().get(position), null);
        } else if (fragment.equals(getString(R.string.Folders))) {
            ut = new UnifiedTrack(true, tempMusicFolder.getLocalTracks().get(position), null);
        } else if (fragment.equals(getString(R.string.RECENT))) {
            ut = recentlyPlayed.getRecentlyPlayed().get(position);
        } else if (fragment.equals(getString(R.string.RecentHorizontalList))) {
            ut = continuePlayingList.get(position);
        } else if (fragment.equals(getString(R.string.Stream))) {
            ut = new UnifiedTrack(false, null, streamingTrackList.get(position));
        }

        if (action.equals(getString(R.string.Add_to_Playlist))) {
            showAddToPlaylistDialog(ut);
            pAdapter.notifyDataSetChanged();
        }
        if (action.equals(getString(R.string.Add_to_Queue))) {
            queue.getQueue().add(ut);
            updateVisualizerRecycler();
        }
        if (action.equals(getString(R.string.Play))) {
            if (queue.getQueue().size() == 0) {
                queueCurrentIndex = 0;
                queue.getQueue().add(ut);
            } else if (queueCurrentIndex == queue.getQueue().size() - 1) {
                queueCurrentIndex++;
                queue.getQueue().add(ut);
            } else if (isReloaded) {
                isReloaded = false;
                queueCurrentIndex = queue.getQueue().size();
                queue.getQueue().add(ut);
            } else {
                queue.getQueue().add(++queueCurrentIndex, ut);
            }
            streamSelected = !type;
            localSelected = type;
            queueCall = false;
            isReloaded = false;
            if (type) {
                localSelectedTrack = ut.getLocalTrack();
                onLocalTrackSelected(position);
            } else {
                selectedTrack = ut.getStreamTrack();
                onTrackSelected(position);
            }
            updateVisualizerRecycler();
        }
        if (action.equals(getString(R.string.Play_Next))) {
            if (queue.getQueue().size() == 0) {
                queueCurrentIndex = 0;
                queue.getQueue().add(ut);
                streamSelected = !type;
                localSelected = type;
                queueCall = false;
                isReloaded = false;
                if (type) {
                    localSelectedTrack = ut.getLocalTrack();
                    onLocalTrackSelected(position);
                } else {
                    selectedTrack = ut.getStreamTrack();
                    onTrackSelected(position);
                }
            } else if (queueCurrentIndex == queue.getQueue().size() - 1) {
                queue.getQueue().add(ut);
            } else if (isReloaded) {
                isReloaded = false;
                queueCurrentIndex = queue.getQueue().size();
                queue.getQueue().add(ut);
                streamSelected = !type;
                localSelected = type;
                queueCall = false;
                isReloaded = false;
                if (type) {
                    localSelectedTrack = ut.getLocalTrack();
                    onLocalTrackSelected(position);
                } else {
                    selectedTrack = ut.getStreamTrack();
                    onTrackSelected(position);
                }
            } else {
                queue.getQueue().add(queueCurrentIndex + 1, ut);
            }
            updateVisualizerRecycler();
        }
        if (action.equals(getString(R.string.Add_to_Favourites))) {
            addToFavourites(ut);
        }
        if (action.equals(getString(R.string.Share))) {
            FileUtils.shareLocalSong(ut.getLocalTrack().getPath(), this);
        }
        if (action.equals(getString(R.string.EDIT))) {
            editSong = ut.getLocalTrack();
            showFragment("Edit");
        }
    }

    /**
     * 添加到喜欢
     *
     * @param ut
     */
    private void addToFavourites(UnifiedTrack ut) {
        boolean isRepeat = false;
        for (int i = 0; i < favouriteTracks.getFavourite().size(); i++) {
            UnifiedTrack ut1 = favouriteTracks.getFavourite().get(i);
            if (ut.getType() && ut1.getType()) {
                if (ut.getLocalTrack().getTitle().equals(ut1.getLocalTrack().getTitle())) {
                    isRepeat = true;
                    break;
                }
            } else if (!ut.getType() && !ut1.getType()) {
                if (ut.getStreamTrack().getTitle().equals(ut1.getStreamTrack().getTitle())) {
                    isRepeat = true;
                    break;
                }
            }
        }

        if (!isRepeat)
            favouriteTracks.getFavourite().add(ut);
    }


    /**
     * 显示添加到音乐列表的对话框
     *
     * @param track
     */
    private void showAddToPlaylistDialog(final UnifiedTrack track) {
        final Dialog dialog = new Dialog(ctx);
        dialog.setContentView(R.layout.add_to_playlist_dialog);
        dialog.setTitle(getString(R.string.Add_to_Playlist));

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
//                        Toast.makeText(ctx, "Added to Playlist : " + temp.getPlaylistName(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(ctx, "添加到播放列表 : " + temp.getPlaylistName(), Toast.LENGTH_SHORT).show();
                        // 保存音乐列表的异步任务？
                        new SavePlaylists().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        dialog.dismiss();
                    } else {
//                        Toast.makeText(ctx, "Song already present in Playlist", Toast.LENGTH_SHORT).show();
                        Toast.makeText(ctx, "歌曲已经在播放列表啦", Toast.LENGTH_SHORT).show();
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
//                    text.setError("Enter Playlist Name!");
                    text.setError(getString(R.string.Enter_Playlist_Name));
                } else {
                    for (int i = 0; i < allPlaylists.getPlaylists().size(); i++) {
                        if (text.getText().toString().equals(allPlaylists.getPlaylists().get(i).getPlaylistName())) {
                            isNameRepeat = true;
//                            text.setError("Playlist with same name exists!");
                            text.setError(getString(R.string.Playlist_exists));
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
                        // 保存播放列表的异步任务
                        new SavePlaylists().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        Toast.makeText(HomeActivity.this, "新的播放列表保存成功", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        dialog.show();
    }

    /**
     * 2/19 播放界面今天完成，可以播放打开的音乐！
     *
     * @param position
     */
    @Override
    public void onLocalTrackSelected(int position) {
        Log.e(TAG, "onLocalTrackSelected"); //  E/HomeActivity: onLocalTrackSelected
        isReloaded = false;
        HideBottomFakeToolbar();

        // 2/19 关于打开播放界面的方法要认真分析处理在整理下！
        if (!queueCall) {
            // 隐藏键盘
            CommonUtils.hideKeyboard(this);

            searchView.setQuery("", true);
            searchView.setIconified(true);
            new Thread(new CancelCall()).start();

//            hideTabs();
            isPlayerVisible = true;

            PlayerFragment frag = playerFragment;
            FragmentManager fm = getSupportFragmentManager();
            PlayerFragment newFragment = new PlayerFragment();
            if (frag == null) {
                playerFragment = newFragment;
                int flag = 0;
                for (int i = 0; i < favouriteTracks.getFavourite().size(); i++) {
                    UnifiedTrack ut = favouriteTracks.getFavourite().get(i);
                    if (ut.getType() && ut.getLocalTrack().getTitle().equals(localSelectedTrack.getTitle())) {
                        flag = 1;
                        isFavourite = true;
                        break;
                    }
                }
                if (flag == 0) {
                    isFavourite = false;
                }

                playerFragment.localIsPlaying = true;
                playerFragment.localTrack = localSelectedTrack;
                fm.beginTransaction()
                        .setCustomAnimations(R.anim.slide_up,
                                R.anim.slide_down,
                                R.anim.slide_up,
                                R.anim.slide_down)
                        .add(R.id.player_frag_container, newFragment, "player")
                        .show(newFragment)
                        .commitAllowingStateLoss();
            } else {
                if (playerFragment.localTrack != null && playerFragment.localIsPlaying && localSelectedTrack.getTitle() == playerFragment.localTrack.getTitle()) {

                } else {
                    int flag = 0;
                    for (int i = 0; i < favouriteTracks.getFavourite().size(); i++) {
                        UnifiedTrack ut = favouriteTracks.getFavourite().get(i);
                        if (ut.getType() && ut.getLocalTrack().getTitle().equals(localSelectedTrack.getTitle())) {
                            flag = 1;
                            isFavourite = true;
                            break;
                        }
                    }
                    if (flag == 0) {
                        isFavourite = false;
                    }
                    playerFragment.localIsPlaying = true;
                    playerFragment.localTrack = localSelectedTrack;
                    frag.refresh();
                }
            }

            if (!isQueueVisible)
                showPlayer();

        } else {
            PlayerFragment frag = playerFragment;
            playerFragment.localIsPlaying = true;
            playerFragment.localTrack = localSelectedTrack;

            int flag = 0;
            for (int i = 0; i < favouriteTracks.getFavourite().size(); i++) {
                UnifiedTrack ut = favouriteTracks.getFavourite().get(i);
                if (ut.getType() && ut.getLocalTrack().getTitle().equals(localSelectedTrack.getTitle())) {
                    flag = 1;
                    isFavourite = true;
                    break;
                }
            }
            if (flag == 0) {
                isFavourite = false;
            }
            if (frag != null)
                frag.refresh();
        }

        if (playerFragment != null && playerFragment.snappyRecyclerView != null) {
            playerFragment.snappyRecyclerView.getAdapter().notifyDataSetChanged();
            playerFragment.snappyRecyclerView.setTransparency();
        }

        QueueFragment qFrag = (QueueFragment) fragmentManager.findFragmentByTag("queue");
        if (qFrag != null) {
            qFrag.updateQueueAdapter();
        }

        UnifiedTrack track = new UnifiedTrack(true, playerFragment.localTrack, null);
        for (int i = 0; i < recentlyPlayed.getRecentlyPlayed().size(); i++) {
            if (recentlyPlayed.getRecentlyPlayed().get(i).getType() && recentlyPlayed.getRecentlyPlayed().get(i).getLocalTrack().getTitle().equals(track.getLocalTrack().getTitle())) {
                recentlyPlayed.getRecentlyPlayed().remove(i);
//                rAdapter.notifyItemRemoved(i);
                break;
            }
        }
        recentlyPlayed.getRecentlyPlayed().add(0, track);
        if (recentlyPlayed.getRecentlyPlayed().size() == 51) {
            recentlyPlayed.getRecentlyPlayed().remove(50);
        }
        recentsRecycler.setVisibility(View.VISIBLE);
        recentsNothingText.setVisibility(View.INVISIBLE);
        continuePlayingList.clear();
        for (int i = 0; i < Math.min(10, recentlyPlayed.getRecentlyPlayed().size()); i++) {
            continuePlayingList.add(recentlyPlayed.getRecentlyPlayed().get(i));
        }
        rAdapter.notifyDataSetChanged();
        refreshHeaderImages();

        RecentsFragment rFrag = (RecentsFragment) fragmentManager.findFragmentByTag("recent");
        if (rFrag != null && rFrag.rtAdpater != null) {
            rFrag.rtAdpater.notifyDataSetChanged();
        }

    }

    /**
     * 显示player
     */
    public void showPlayer() {
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = ((Activity) (ctx)).getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.parseColor("#000000"));
        }

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        searchView.setQuery("", false);
        searchView.setIconified(true);
        new Thread(new CancelCall()).start();

        isPlayerVisible = true;
        isEqualizerVisible = false;
        isQueueVisible = false;

        playerContainer.setVisibility(View.VISIBLE);
        if (playerFragment != null && playerFragment.mVisualizerView != null)
            playerFragment.mVisualizerView.setVisibility(View.INVISIBLE);

        if (playerFragment != null && playerFragment.player_controller != null) {
            playerFragment.player_controller.setAlpha(1.0f);
            playerFragment.player_controller.animate()
                    .setDuration(300)
                    .alpha(0.0f);
        }

        if (playerFragment != null && playerFragment.cpb != null) {
            playerFragment.cpb.animate()
                    .alpha(0.0f);
        }
        if (playerFragment != null && playerFragment.smallPlayer != null) {
            playerFragment.smallPlayer.animate()
                    .alpha(0.0f);
        }

        if (playerFragment != null && playerFragment.spToolbar != null) {
            playerFragment.spToolbar.setVisibility(View.VISIBLE);
            playerFragment.spToolbar.animate().alpha(1.0f);
        }

        isPlayerTransitioning = true;

        playerContainer.animate()
                .setDuration(300)
                .translationY(0)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        isPlayerTransitioning = false;
                        if (playerFragment != null && playerFragment.snappyRecyclerView != null) {
                            playerFragment.snappyRecyclerView.setTransparency();
                        }
                    }
                });


        if (playerFragment != null && playerFragment.snappyRecyclerView != null) {
            playerFragment.snappyRecyclerView.setVisibility(View.VISIBLE);
            playerFragment.snappyRecyclerView.animate()
                    .alpha(1.0f)
                    .setDuration(300);
        }

        final Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (playerFragment != null && playerFragment.mVisualizerView != null) {
                    if (playerFragment.isLyricsVisisble) {
                        playerFragment.mVisualizerView.setVisibility(GONE);
                        playerFragment.lyricsContainer.setVisibility(View.VISIBLE);
                    } else {
                        playerFragment.mVisualizerView.setVisibility(View.VISIBLE);
                    }
                }
            }
        }, 400);

    }

    /*
    * AlbumFragment callbacks START
    * onAlbumClick() -> a particular album is selected from all albums list.
    */
    @Override
    public void onAlbumClick() {
        showFragment("viewAlbum");
    }

    /*
    * ArtistFragment callbacks START
    * onArtistClick() -> a particular artist is selected from all artists list.
    */
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
                onTrackSelected(position);
            }
        }
    }

     /*
     * ViewAlbumFragment callbacks START
     * onAlbumPlayAll() -> play all fab is selected in view album fragment.
     * addAlbumToQueue() -> add to queue button is selected in view album fragment.
     * onAlbumSongClick() -> a song is selected in view album fragment.
     */

    @Override
    public void onAlbumSongClick() {
        onLocalTrackSelected(-1);
    }

    @Override
    public void onAlbumPlayAll() {
        onQueueItemClicked(0);
        showPlayer();
    }

    @Override
    public void addAlbumToQueue() {
        List<LocalTrack> list = tempAlbum.getAlbumSongs();
        for (LocalTrack lt : list) {
            HomeActivity.queue.addToQueue(new UnifiedTrack(true, lt, null));
        }
        if (playerFragment != null && playerFragment.snappyRecyclerView != null) {
            playerFragment.snappyRecyclerView.getAdapter().notifyDataSetChanged();
            playerFragment.snappyRecyclerView.setTransparency();
        }
        Toast.makeText(ctx, "添加 " + list.size() + " 首歌到队列", Toast.LENGTH_SHORT).show();
    }

    /*
    * ViewArtistFragment callbacks START
    * onArtistPlayAll() -> play all fab is selected in view artist fragment.
    * addArtistToQueue() -> add to queue button is selected in view artist fragment.
    * onArtistSongClick() -> a song is selected in view artist fragment.
    */
    @Override
    public void onArtistSongClick() {
        onLocalTrackSelected(-1);
    }

    @Override
    public void onArtistPlayAll() {
        onQueueItemClicked(0);
        showPlayer();
    }

    @Override
    public void addArtistToQueue() {
        List<LocalTrack> list = tempArtist.getArtistSongs();
        for (LocalTrack lt : list) {
            HomeActivity.queue.addToQueue(new UnifiedTrack(true, lt, null));
        }
        if (playerFragment != null && playerFragment.snappyRecyclerView != null) {
            playerFragment.snappyRecyclerView.getAdapter().notifyDataSetChanged();
            playerFragment.snappyRecyclerView.setTransparency();
        }
        Toast.makeText(ctx, "添加 " + list.size() + " 首歌到队列", Toast.LENGTH_SHORT).show();
    }

    /**
     * 更新VisualizerRecycler
     */
    public void updateVisualizerRecycler() {
        if (playerFragment != null && playerFragment.snappyRecyclerView != null) {
            playerFragment.snappyRecyclerView.getAdapter().notifyDataSetChanged();
            playerFragment.snappyRecyclerView.scrollToPosition(queueCurrentIndex);
            playerFragment.snappyRecyclerView.setTransparency();
        }
    }

    /*
    * Methods to control the transitions of the player fragment
    * hidePlayer() / showPlayer()  -> the vertical animation to hide/show the visualizer
    * hidePlayer2() / showPlayer2()  -> the horizontal animation to hide/show the equalizer
    * hidePlayer3() / showPlayer3()  -> the horizontal animation to hide/show the queue
    *
    * 隐藏player
    */
    public void hidePlayer() {

        if (playerFragment != null && playerFragment.mVisualizerView != null) {
            playerFragment.mVisualizerView.setVisibility(View.GONE);
            playerFragment.lyricsContainer.setVisibility(View.GONE);
        }

        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        if (Build.VERSION.SDK_INT >= 21) {
            Window window = ((Activity) (ctx)).getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(CommonUtils.getDarkColor(themeColor));
        }

        isPlayerVisible = false;

        if (playerFragment != null && playerFragment.cpb != null) {
            playerFragment.cpb.setAlpha(0.0f);
            playerFragment.cpb.setVisibility(View.VISIBLE);
            playerFragment.cpb.animate()
                    .alpha(1.0f);
        }
        if (playerFragment != null && playerFragment.smallPlayer != null) {
            playerFragment.smallPlayer.setAlpha(0.0f);
            playerFragment.smallPlayer.setVisibility(View.VISIBLE);
            playerFragment.smallPlayer.animate()
                    .alpha(1.0f);
        }

        if (playerFragment != null && playerFragment.spToolbar != null) {
            playerFragment.spToolbar.animate()
                    .alpha(0.0f)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            playerFragment.spToolbar.setVisibility(GONE);
                        }
                    });
        }

        playerContainer.setVisibility(View.VISIBLE);
        // 设置位移动画
        if (playerFragment != null) {
            playerContainer.animate()
                    .translationY(playerContainer.getHeight() - playerFragment.smallPlayer.getHeight())
                    .setDuration(300);
        } else {
            playerContainer.animate()
                    .translationY(playerContainer.getHeight() - playerFragment.smallPlayer.getHeight())
                    .setDuration(300)
                    .setStartDelay(500);
        }

        if (playerFragment != null) {

            playerFragment.player_controller.setAlpha(0.0f);
            playerFragment.player_controller.setImageDrawable(playerFragment.mainTrackController.getDrawable());

            playerFragment.player_controller.animate()
                    .alpha(1.0f);

            playerFragment.snappyRecyclerView.animate()
                    .alpha(0.0f)
                    .withEndAction(new Runnable() {
                        @Override
                        public void run() {
                            playerFragment.snappyRecyclerView.setVisibility(GONE);
                        }
                    });
        }
    }

   /*
     * Methods and classes for updating visualizer.
     * MyAsyncTask -> AsyncTask to update visualiser in background thread.
     * updatePoints() -> Actual method to render the DNA.
     */

    public void updateVisualizer(byte[] bytes) {
        mBytes = bytes;
        try {
            new MyAsyncTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * PlayerFragment callbacks START
     * onComplete() -> called when a song ends or next button is clicked in player or notification.
     * onPreviousTrack() -> called when previous button is clicked in player or notification.
     * onEqualizerClicked() -> called when equalizer icon is clicked.
     * onQueueCLicked() -> called when queue icon is clicked.
     * onPrepared() -> called when media player prepareAsync() is completed.
     * onSettingsClicked() -> called when settings menu item is selected.
     * onFullScreen() -> called when fullscreen menu item is selected or player is long pressed.
     * onAddedtoFavfromPlayer() -> called when favourite icon is clicked.
     * onShuffleEnabled() / onShuffleEnabled() -> shuffle enebled or disabled.
     * onSmallPlayerTouched() -> called when the down icon at the top of the player ic clicked to hide the player.
     * <p>
     * 保存dna的异步任务完成后执行
     */
    @Override
    public void onComplete() {

        // Check for sleep timer and whether it has timed out
        if (isSleepTimerEnabled && isSleepTimerTimeout) {
            Toast.makeText(ctx, "Sleep timer timed out, closing app", Toast.LENGTH_SHORT).show();

            if (playerFragment != null && playerFragment.timer != null)
                playerFragment.timer.cancel();

            // Remove the notification
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.cancel(1);

            // Finish the activity
            finish();
            return;
        }

        // Save the DNA if saving is enabled
        if (isSaveDNAEnabled) {
            new SaveTheDNAs().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        }

        QueueFragment qFrag = (QueueFragment) fragmentManager.findFragmentByTag("queue");
        queueCall = true;

        PlayerFragment plFrag = playerFragment;

        if (repeatOnceEnabled && !nextControllerClicked) {

            /*
            * Executed if repeat once is enabled and user did not click the next button from player.
            */

            // Set Progress bar to 0
            plFrag.progressBar.setProgress(0);
            plFrag.progressBar.setSecondaryProgress(0);

            // Get Visualizer and Visualizer View to initial state
            plFrag.mVisualizer.setEnabled(true);
            VisualizerView.w = screen_width;
            VisualizerView.h = screen_height;
            VisualizerView.conf = Bitmap.Config.ARGB_8888;
            VisualizerView.bmp = Bitmap.createBitmap(VisualizerView.w, VisualizerView.h, VisualizerView.conf);
            cacheCanvas = new Canvas(VisualizerView.bmp);

            // Play the song again by seeking media player to 0
            plFrag.mMediaPlayer.seekTo(0);

            // Setup the icons
            plFrag.mainTrackController.setImageResource(R.drawable.ic_pause_white_48dp);
            plFrag.isReplayIconVisible = false;
            plFrag.player_controller.setImageResource(R.drawable.ic_pause_white_48dp);

            // Resume the MediaPlayer
            plFrag.isPrepared = true;
            plFrag.mMediaPlayer.start();
        } else {

            /*
            * Executed if repeat once is disabled.
            * Execution depends on the current position in queue and whether the next button was clicked or not.
            * If current position is at the end of the queue, then number of elements in the queue are checked.
            */

            if (queueCurrentIndex < queue.getQueue().size() - 1) {
                queueCurrentIndex++;
                nextControllerClicked = false;
                hasQueueEnded = false;
                if (qFrag != null) {
                    qFrag.updateQueueAdapter();
                }
                if (queue.getQueue().get(queueCurrentIndex).getType()) {
                    localSelectedTrack = queue.getQueue().get(queueCurrentIndex).getLocalTrack();
                    streamSelected = false;
                    localSelected = true;
                    onLocalTrackSelected(-1);
                } else {
                    selectedTrack = queue.getQueue().get(queueCurrentIndex).getStreamTrack();
                    streamSelected = true;
                    localSelected = false;
                    onTrackSelected(-1);
                }
            } else {
                if ((repeatEnabled || repeatOnceEnabled) && (queue.getQueue().size() > 1)) {
                    nextControllerClicked = false;
                    hasQueueEnded = false;
                    queueCurrentIndex = 0;
                    if (qFrag != null) {
                        qFrag.updateQueueAdapter();
                    }
                    onQueueItemClicked(0);
                } else if ((repeatEnabled || repeatOnceEnabled) && (queue.getQueue().size() == 1)) {
                    nextControllerClicked = false;
                    hasQueueEnded = false;
                    plFrag.progressBar.setProgress(0);
                    plFrag.progressBar.setSecondaryProgress(0);
                    plFrag.mVisualizer.setEnabled(true);
                    plFrag.mMediaPlayer.seekTo(0);
                    plFrag.mainTrackController.setImageResource(R.drawable.ic_pause_white_48dp);
                    plFrag.isReplayIconVisible = false;
                    plFrag.player_controller.setImageResource(R.drawable.ic_pause_white_48dp);
                    plFrag.isPrepared = true;
                    plFrag.mMediaPlayer.start();
                } else {
                    if ((nextControllerClicked || hasQueueEnded) && (queue.getQueue().size() > 1)) {
                        nextControllerClicked = false;
                        hasQueueEnded = false;
                        queueCurrentIndex = 0;
                        if (qFrag != null) {
                            qFrag.updateQueueAdapter();
                        }
                        onQueueItemClicked(0);
                    } else if ((nextControllerClicked || hasQueueEnded) && (queue.getQueue().size() == 1)) {
                        nextControllerClicked = false;
                        hasQueueEnded = false;
                        plFrag.progressBar.setProgress(0);
                        plFrag.progressBar.setSecondaryProgress(0);
                        if (plFrag.mVisualizer != null)
                            plFrag.mVisualizer.setEnabled(true);
                        plFrag.mMediaPlayer.seekTo(0);
                        plFrag.mainTrackController.setImageResource(R.drawable.ic_pause_white_48dp);
                        plFrag.isReplayIconVisible = false;
                        plFrag.player_controller.setImageResource(R.drawable.ic_pause_white_48dp);
                        plFrag.isPrepared = true;
                        plFrag.mMediaPlayer.start();
                    } else {
                        // keep queue at last track or you are doomed
                    }
                }
            }
        }
    }

    /**
     * 前一首
     */
    @Override
    public void onPreviousTrack() {
        QueueFragment qFrag = (QueueFragment) fragmentManager.findFragmentByTag("queue");

        /*
        * Execution depends on the current position in the queue
        */

        if (queueCurrentIndex > 0) {
            queueCall = true;
            queueCurrentIndex--;
            if (qFrag != null) {
                qFrag.updateQueueAdapter();
            }
            if (queue.getQueue().get(queueCurrentIndex).getType()) {
                localSelectedTrack = queue.getQueue().get(queueCurrentIndex).getLocalTrack();
                streamSelected = false;
                localSelected = true;
                onLocalTrackSelected(-1);
            } else {
                selectedTrack = queue.getQueue().get(queueCurrentIndex).getStreamTrack();
                streamSelected = true;
                localSelected = false;
                onTrackSelected(-1);
            }
        } else {
            // keep queue at 0
        }
    }

    // 打开均衡器界面
    @Override
    public void onEqualizerClicked() {
        Log.e(TAG, "打开均衡器界面");
        hideAllFrags();
        Log.e(TAG, "1");
        hidePlayer2();
        Log.e(TAG, "2");
        showFragment("equalizer");
        Log.e(TAG, "3");
    }

    private void hidePlayer2() {
        isEqualizerVisible = true;

        if (playerFragment.mVisualizerView != null)
            playerFragment.mVisualizerView.setVisibility(View.GONE);

        final Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                playerContainer.animate()
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .translationX(playerContainer.getWidth());
            }
        }, 50);

        playerContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onQueueClicked() {
        hideAllFrags();
        hidePlayer3();
        showFragment("queue");
    }

    /**
     * 隐藏player3
     */
    private void hidePlayer3() {
        isQueueVisible = true;

        if (playerFragment.mVisualizerView != null)
            playerFragment.mVisualizerView.setVisibility(View.INVISIBLE);

        final Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                playerContainer.animate()
                        .setInterpolator(new AccelerateDecelerateInterpolator())
                        .translationX(-1 * playerContainer.getWidth());
            }
        }, 50);

        playerContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPrepared() {
        showNotification();
    }

    /**
     * 显示通知栏
     */
    private void showNotification() {
        if (!isNotificationVisible) {
            Intent intent = new Intent(this, MediaPlayerService.class);
            intent.setAction(Constants.ACTION_PLAY);
            startService(intent);
            isNotificationVisible = true;
        }
    }

    @Override
    public void onFullScreen() {
        //Adds Haptic Feedback(Vibration) on entering and exiting full screen mode of video player
        View view = findViewById(R.id.root_view);
        view.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);


        if (isFullScreenEnabled) {
            // Long Press to Exit
            Toast.makeText(this, getString(R.string.Long_Press_to_Exit), Toast.LENGTH_SHORT).show();
            ActionBar actionBar = getSupportActionBar();
            actionBar.hide();
        } else {
            ActionBar actionBar = getSupportActionBar();
            actionBar.show();
        }
    }

    @Override
    public void onSettingsClicked() {
        if (playerFragment.smallPlayer != null) {
            hidePlayer();
            isPlayerVisible = false;
            showFragment("settings");
        }
    }

    /**
     * 添加喜欢歌曲
     */
    @Override
    public void onAddedtoFavfromPlayer() {
        FavouritesFragment favouritesFragment = (FavouritesFragment) fragmentManager.findFragmentByTag("favourite");
        if (favouritesFragment != null) {
            favouritesFragment.updateData();
        }
    }

    @Override
    public void onShuffleEnabled() {
        originalQueue = new Queue();
        for (UnifiedTrack ut : queue.getQueue()) {
            originalQueue.addToQueue(ut);
        }
        originalQueueIndex = queueCurrentIndex;
        UnifiedTrack ut = queue.getQueue().get(queueCurrentIndex);
        Collections.shuffle(queue.getQueue());
        for (int i = 0; i < queue.getQueue().size(); i++) {
            if (ut.equals(queue.getQueue().get(i))) {
                queue.getQueue().remove(i);
                break;
            }
        }
        queue.getQueue().add(0, ut);
        queueCurrentIndex = 0;

        new SaveQueue().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onShuffleDisabled() {
        UnifiedTrack ut1 = queue.getQueue().get(queueCurrentIndex);
        for (int i = 0; i < queue.getQueue().size(); i++) {
            UnifiedTrack ut = queue.getQueue().get(i);
            if (!originalQueue.getQueue().contains(ut)) {
                originalQueue.getQueue().add(ut);
            }
        }
        queue.getQueue().clear();
        for (UnifiedTrack ut : originalQueue.getQueue()) {
            queue.addToQueue(ut);
        }
        for (int i = 0; i < queue.getQueue().size(); i++) {
            if (ut1.equals(queue.getQueue().get(i))) {
                queueCurrentIndex = i;
                break;
            }
        }

        new SaveQueue().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    @Override
    public void onSmallPlayerTouched() {
        if (!isPlayerVisible) {
            isPlayerVisible = true;
            showPlayer();
        } else {
            isPlayerVisible = false;
            hidePlayer();
        }
    }

    @Override
    public void addCurrentSongtoPlaylist(UnifiedTrack ut) {
        showAddToPlaylistDialog(ut);
    }

    /*
    * ServiceCallbacks callbacks START
    * getPlayerFragment() -> returns an instance of the player fragment.
    */
    public PlayerFragment getPlayerFragment() {
        return playerFragment;
    }

    /*
    *  FavouriteFragment callbacks START
    *  onFavouriteItemClicked() -> when a song is selected from favourites.
    *  onFavouritePlayAll() -> when favourite play all fab is selected.
    *  addFavToQueue() -> when `add favourite to queue` is selected.
    */

    @Override
    public void onFavouriteItemClicked(int position) {
        UnifiedTrack ut = favouriteTracks.getFavourite().get(position);
        if (ut.getType()) {
            LocalTrack track = ut.getLocalTrack();
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
        } else {
            Track track = ut.getStreamTrack();
            if (queue.getQueue().size() == 0) {
                queueCurrentIndex = 0;
                queue.getQueue().add(new UnifiedTrack(false, null, track));
            } else if (queueCurrentIndex == queue.getQueue().size() - 1) {
                queueCurrentIndex++;
                queue.getQueue().add(new UnifiedTrack(false, null, track));
            } else if (isReloaded) {
                isReloaded = false;
                queueCurrentIndex = queue.getQueue().size();
                queue.getQueue().add(new UnifiedTrack(false, null, track));
            } else {
                queue.getQueue().add(++queueCurrentIndex, new UnifiedTrack(false, null, track));
            }
            selectedTrack = track;
            streamSelected = true;
            localSelected = false;
            queueCall = false;
            isReloaded = false;
            onTrackSelected(position);
        }
    }

    @Override
    public void onFavouritePlayAll() {
        if (queue.getQueue().size() > 0) {
            onQueueItemClicked(0);
            hideFragment("favourite");
        }
    }

    @Override
    public void addFavToQueue() {
        for (UnifiedTrack ut : favouriteTracks.getFavourite()) {
            queue.addToQueue(ut);
        }
        if (playerFragment != null && playerFragment.snappyRecyclerView != null) {
            playerFragment.snappyRecyclerView.getAdapter().notifyDataSetChanged();
            playerFragment.snappyRecyclerView.setTransparency();
        }
        Toast.makeText(ctx, "添加 " + favouriteTracks.getFavourite().size() + "首歌到队列", Toast.LENGTH_SHORT).show();
    }

    /*
     *  QueueFragment callbacks START
     *  onQueueItemClicked() -> when a song is selected from queue.
     *  onQueueSave() -> queue save as playlist fab is clicked.
     *  onQueueClear() -> when queue clear option is selected.
     *
     *  队列的item点击事件
     */
    public void onQueueItemClicked(final int position) {
        if (isPlayerVisible && isQueueVisible)
            showPlayer3();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                queueCurrentIndex = position;
                UnifiedTrack ut = queue.getQueue().get(position);
                if (ut.getType()) {
                    LocalTrack track = ut.getLocalTrack();
                    localSelectedTrack = track;
                    streamSelected = false;
                    localSelected = true;
                    queueCall = false;
                    isReloaded = false;
                    onLocalTrackSelected(position);
                } else {
                    Track track = ut.getStreamTrack();
                    selectedTrack = track;
                    streamSelected = true;
                    localSelected = false;
                    queueCall = false;
                    isReloaded = false;
                    onTrackSelected(position);
                }
            }
        }, 500);
    }

    @Override
    public void onQueueSave() {
        showSaveQueueDialog();
    }

    @Override
    public void onQueueClear() {
        clearQueue();
        new SaveQueue().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    /**
     * 显示保存队列的dialog
     */
    private void showSaveQueueDialog() {
        final Dialog dialog = new Dialog(ctx);
        dialog.setContentView(R.layout.add_to_playlist_dialog);
        dialog.setTitle(getString(R.string.Save_Queue));

        ListView lv = (ListView) dialog.findViewById(R.id.playlist_list);
        lv.setVisibility(GONE);

        // set the custom dialog components - text, image and button
        final EditText text = (EditText) dialog.findViewById(R.id.new_playlist_name);
        ImageView image = (ImageView) dialog.findViewById(R.id.confirm_button);
        // if button is clicked, close the custom dialog
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isNameRepeat = false;
                if (text.getText().toString().trim().equals("")) {
                    text.setError(getString(R.string.Enter_Playlist_Name));
                } else {
                    for (int i = 0; i < allPlaylists.getPlaylists().size(); i++) {
                        if (text.getText().toString().equals(allPlaylists.getPlaylists().get(i).getPlaylistName())) {
                            isNameRepeat = true;
                            text.setError(getString(R.string.Playlist_exists));
                            break;
                        }
                    }
                    if (!isNameRepeat) {
                        Playlist pl = new Playlist(text.getText().toString());
                        for (int i = 0; i < queue.getQueue().size(); i++) {
                            pl.getSongList().add(queue.getQueue().get(i));
                        }
                        allPlaylists.addPlaylist(pl);
                        playlistsRecycler.setVisibility(View.VISIBLE);
                        playlistNothingText.setVisibility(View.INVISIBLE);
                        pAdapter.notifyDataSetChanged();
                        // 保存
                        new SavePlaylists().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        Toast.makeText(HomeActivity.this, getString(R.string.Queue_saved), Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            }
        });
        dialog.show();
    }


    /**
     * 清空队列
     */
    private void clearQueue() {
        QueueFragment qFrag = (QueueFragment) fragmentManager.findFragmentByTag("queue");
        for (int i = 0; i < queue.getQueue().size(); i++) {
            if (i < queueCurrentIndex) {
                queue.getQueue().remove(i);
                queueCurrentIndex--;
                if (qFrag != null) {
                    qFrag.notifyAdapterItemRemoved(i);
                }
                i--;
            } else if (i > queueCurrentIndex) {
                queue.getQueue().remove(i);
                if (qFrag != null) {
                    qFrag.notifyAdapterItemRemoved(i);
                }
                i--;
            }
        }
        if (playerFragment != null && playerFragment.snappyRecyclerView != null) {
            playerFragment.snappyRecyclerView.getAdapter().notifyDataSetChanged();
            playerFragment.snappyRecyclerView.setTransparency();
        }
    }

    /**
     * 显示player3
     */
    private void showPlayer3() {
        isPlayerVisible = true;
        isEqualizerVisible = false;
        isQueueVisible = false;

        if (playerFragment.mVisualizerView != null)
            playerFragment.mVisualizerView.setVisibility(View.INVISIBLE);

        isPlayerTransitioning = true;

        playerContainer.animate()
                .setDuration(300)
                .translationX(0)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        isPlayerTransitioning = false;
                        if (playerFragment != null && playerFragment.snappyRecyclerView != null) {
                            playerFragment.snappyRecyclerView.setTransparency();
                        }
                        if (!playerFragment.isLyricsVisisble) {
                            playerFragment.mVisualizerView.setVisibility(View.VISIBLE);
                        }
                    }
                });

        final Handler handler2 = new Handler();
        handler2.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideFragment("queue");
            }
        }, 400);
    }


    /*
     * RecentsFragment callbacks START
     * onRecentItemClicked() -> a song is selected from the recents fragment.
     * onRecentFromQueue() -> when a song is selected from recents that is already in queue.
     * addRecentsToQueue() -> add recents to queue option is selcted
     */
    @Override
    public void onRecentItemClicked(boolean isLocal) {
        if (isLocal) {
            onLocalTrackSelected(-1);
        } else {
            onTrackSelected(-1);
        }
    }

    @Override
    public void onRecentFromQueue(int pos) {
        onQueueItemClicked(pos);
    }

    @Override
    public void addRecentsToQueue() {
        for (UnifiedTrack ut : recentlyPlayed.getRecentlyPlayed()) {
            queue.addToQueue(ut);
        }
        if (playerFragment != null && playerFragment.snappyRecyclerView != null) {
            playerFragment.snappyRecyclerView.getAdapter().notifyDataSetChanged();
            playerFragment.snappyRecyclerView.setTransparency();
        }
        Toast.makeText(ctx, "添加 " + recentlyPlayed.getRecentlyPlayed().size() + "首歌到队列", Toast.LENGTH_SHORT).show();
    }

    /*
     * FolderFragment callbacks START
     */
    @Override
    public void onFolderClicked(int pos) {
        tempMusicFolder = allMusicFolders.getMusicFolders().get(pos);
        tempFolderContent = tempMusicFolder.getLocalTracks();
        showFragment("folderContent");
    }


    /*
     *  AllPlaylistsFragment callbacks START
     */
    @Override
    public void onPlaylistSelected(int pos) {
        tempPlaylist = allPlaylists.getPlaylists().get(pos);
        tempPlaylistNumber = pos;
        showFragment("playlist");
    }

    /**
     * playlist播放all
     */
    public void onPlaylistMenuPLayAll() {
        onPlaylistPlayAll();
    }

    @Override
    public void onPlaylistRename() {
        renamePlaylistDialog(allPlaylists.getPlaylists().get(renamePlaylistNumber).getPlaylistName());
    }

    @Override
    public void newPlaylistListener() {
        showFragment("newPlaylist");
    }

    /**
     * 重命名播放列表的对话框
     *
     * @param oldName
     */
    public void renamePlaylistDialog(String oldName) {
        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.save_image_dialog);

        TextView titleText = (TextView) dialog.findViewById(R.id.dialog_title);
        titleText.setText(getString(R.string.Rename));
        if (SplashActivity.tf4 != null)
            titleText.setTypeface(SplashActivity.tf4);

        Button btn = (Button) dialog.findViewById(R.id.save_image_btn);
        final EditText newName = (EditText) dialog.findViewById(R.id.save_image_filename_text);

        CheckBox cb = (CheckBox) dialog.findViewById(R.id.text_checkbox);
        cb.setVisibility(GONE);

        newName.setText(oldName);
        btn.setBackgroundColor(themeColor);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isNameRepeat = false;
                if (newName.getText().toString().trim().equals("")) {
                    newName.setError(getString(R.string.Enter_Playlist_Name));
                } else {
                    for (int i = 0; i < allPlaylists.getPlaylists().size(); i++) {
                        if (newName.getText().toString().equals(allPlaylists.getPlaylists().get(i).getPlaylistName())) {
                            isNameRepeat = true;
                            newName.setError(getString(R.string.Playlist_exists));
                            break;
                        }
                    }
                    if (!isNameRepeat) {
                        allPlaylists.getPlaylists().get(renamePlaylistNumber).setPlaylistName(newName.getText().toString());
                        if (pAdapter != null) {
                            pAdapter.notifyItemChanged(renamePlaylistNumber);
                        }
                        AllPlaylistsFragment plFrag = (AllPlaylistsFragment) fragmentManager.findFragmentByTag("allPlaylists");
                        if (plFrag != null) {
                            plFrag.itemChanged(renamePlaylistNumber);
                        }
                        if (isPlaylistVisible) {
                            ViewPlaylistFragment vplFragment = (ViewPlaylistFragment) fragmentManager.findFragmentByTag("playlist");
                            vplFragment.updateViewPlaylistFragment();
                        }
                        new SavePlaylists().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        dialog.dismiss();
                    }
                }
            }
        });

        dialog.show();

    }


    /*
    * ViewPlaylistFragment callbacks START
    * onPlaylistPlayAll() -> when play all fab is clciked in a playlist.
    * onPlaylistItemClicked() -> when a song is selected from a playlist.
    * playlistRename() -> when playlist rename is selected.
    * playlistAddToQueue() -> when playlist add to queue is selected.
    * onPlaylistEmpty() -> called when playlist becomes empty due to swipin out of last element.
    */
    public void onPlaylistPlayAll() {
        onQueueItemClicked(0);
        hideFragment("playlist");
        setTitle("Music DNA");
    }

    @Override
    public void onPlaylistItemClicked(int position) {
        UnifiedTrack ut = tempPlaylist.getSongList().get(position);
        if (ut.getType()) {
            LocalTrack track = ut.getLocalTrack();
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
        } else {
            Track track = ut.getStreamTrack();
            if (queue.getQueue().size() == 0) {
                queueCurrentIndex = 0;
                queue.getQueue().add(new UnifiedTrack(false, null, track));
            } else if (queueCurrentIndex == queue.getQueue().size() - 1) {
                queueCurrentIndex++;
                queue.getQueue().add(new UnifiedTrack(false, null, track));
            } else if (isReloaded) {
                isReloaded = false;
                queueCurrentIndex = queue.getQueue().size();
                queue.getQueue().add(new UnifiedTrack(false, null, track));
            } else {
                queue.getQueue().add(++queueCurrentIndex, new UnifiedTrack(false, null, track));
            }
            selectedTrack = track;
            streamSelected = true;
            localSelected = false;
            queueCall = false;
            isReloaded = false;
            onTrackSelected(position);
        }
    }

    @Override
    public void playlistRename() {
        renamePlaylistNumber = tempPlaylistNumber;
        renamePlaylistDialog(tempPlaylist.getPlaylistName());
    }

    @Override
    public void playlistAddToQueue() {
        Playlist pl = HomeActivity.allPlaylists.getPlaylists().get(tempPlaylistNumber);
        for (UnifiedTrack ut : pl.getSongList()) {
            HomeActivity.queue.addToQueue(ut);
        }
        if (playerFragment != null && playerFragment.snappyRecyclerView != null) {
            playerFragment.snappyRecyclerView.getAdapter().notifyDataSetChanged();
            playerFragment.snappyRecyclerView.setTransparency();
        }
        Toast.makeText(ctx, "添加 " + pl.getSongList().size() + " 首歌到队列", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPlaylistEmpty() {
        AllPlaylistsFragment plFrag = (AllPlaylistsFragment) fragmentManager.findFragmentByTag("allPlaylists");
        if (plFrag != null && plFrag.vpAdapter != null) {
            plFrag.vpAdapter.notifyItemRemoved(tempPlaylistNumber);
        }
        if (pAdapter != null) {
            pAdapter.notifyItemRemoved(tempPlaylistNumber);
        }
    }

    /*
    * FolderContentFragment callbacks START
    */
    @Override
    public void onFolderContentPlayAll() {
        queue.getQueue().clear();
        for (int i = 0; i < tempFolderContent.size(); i++) {
            queue.getQueue().add(new UnifiedTrack(true, tempFolderContent.get(i), null));
        }
        queueCurrentIndex = 0;
        onPlaylistMenuPLayAll();
    }

    @Override
    public void onFolderContentItemClick(int position) {
        onLocalTrackSelected(position);
    }

    @Override
    public void folderAddToQueue() {
        List<LocalTrack> list = tempFolderContent;
        for (LocalTrack lt : list) {
            HomeActivity.queue.addToQueue(new UnifiedTrack(true, lt, null));
        }
        if (playerFragment != null && playerFragment.snappyRecyclerView != null) {
            playerFragment.snappyRecyclerView.getAdapter().notifyDataSetChanged();
            playerFragment.snappyRecyclerView.setTransparency();
        }
        Toast.makeText(ctx, "添加 " + list.size() + "首歌到队列", Toast.LENGTH_SHORT).show();
    }

    /*
    * EqualizerFragment callbacks START
    * onCheckChanged() -> when equalizer switch state is changed.
    */
    @Override
    public void onCheckChanged(boolean isChecked) {
        EqualizerFragment eqFrag = (EqualizerFragment) fragmentManager.findFragmentByTag("equalizer");
        if (isChecked) {
            try {
                isEqualizerEnabled = true;
                int pos = presetPos;
                if (pos != 0) {
                    playerFragment.mEqualizer.usePreset((short) (pos - 1));
                } else {
                    for (short i = 0; i < 5; i++) {
                        playerFragment.mEqualizer.setBandLevel(i, (short) seekbarpos[i]);
                    }
                }
                if (bassStrength != -1 && reverbPreset != -1) {
                    playerFragment.bassBoost.setEnabled(true);
                    playerFragment.bassBoost.setStrength(bassStrength);
                    playerFragment.presetReverb.setEnabled(true);
                    playerFragment.presetReverb.setPreset(reverbPreset);
                }
                playerFragment.mMediaPlayer.setAuxEffectSendLevel(1.0f);
                if (eqFrag != null)
                    eqFrag.setBlockerVisibility(View.GONE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            try {
                isEqualizerEnabled = false;
                playerFragment.mEqualizer.usePreset((short) 0);
                playerFragment.bassBoost.setStrength((short) (((float) 1000 / 19) * (1)));
                playerFragment.presetReverb.setPreset((short) 0);
                if (eqFrag != null)
                    eqFrag.setBlockerVisibility(View.VISIBLE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        equalizerModel.isEqualizerEnabled = isChecked;
    }

    /*
    * SettingsFragment callbacks START
    * onColorChanged() -> called when theme color is selected from the colorPicker
    * onAlbumArtBackgroundChangedVisibility() -> callback to handle toggling of album art behind DNA
    * onAboutClicked() -> callback to show the about fragment.
    */

    @Override
    public void onColorChanged() {
        navigationView.setItemIconTintList(ColorStateList.valueOf(themeColor));
    }

    @Override
    public void onAlbumArtBackgroundChangedVisibility(int visibility) {
        PlayerFragment plFrag = getPlayerFragment();
        if (plFrag != null) {
            plFrag.toggleAlbumArtBackground();
        }
    }

    @Override
    public void onAboutClicked() {
        showFragment("About");
    }

    /*
    * HeadsetReceiver callbacks START
    * onHeadsetRemoved() -> callback to check when the headset is removed from the device.
    * onHeadsetNextClicked() -> callback to handle the next button click on a headset.
    * onHeadsetPreviousClicked() -> callback to handle the previous button click on a headset.
    * onHeadsetPlayPauseClicked() -> callback to handle the play/pause button click on a headset.
    */

    @Override
    public void onHeadsetRemoved() {
        PlayerFragment pFrag = getPlayerFragment();
        if (pFrag != null) {
            if (pFrag.mMediaPlayer != null && pFrag.mMediaPlayer.isPlaying()) {
                if (pFrag.isReplayIconVisible) {

                } else {
                    if (!pFrag.pauseClicked) {
                        pFrag.pauseClicked = true;
                    }
                    pFrag.togglePlayPause();
                }
            }
        }
    }

    @Override
    public void onHeadsetNextClicked() {
        PlayerFragment pFrag = getPlayerFragment();
        if (pFrag != null && !isReloaded) {
            pFrag.nextTrackController.performClick();
        }
    }

    @Override
    public void onHeadsetPreviousClicked() {
        PlayerFragment pFrag = getPlayerFragment();
        if (pFrag != null && !isReloaded) {
            pFrag.previousTrackController.performClick();
        }
    }

    @Override
    public void onHeadsetPlayPauseClicked() {
        PlayerFragment pFrag = getPlayerFragment();
        if (pFrag != null && !isReloaded) {
            if (pFrag.mMediaPlayer != null && pFrag.mMediaPlayer.isPlaying()) {
                if (pFrag.isReplayIconVisible) {
                    hasQueueEnded = true;
                    onComplete();
                } else {
                    if (!pFrag.pauseClicked) {
                        pFrag.pauseClicked = true;
                    }
                    pFrag.togglePlayPause();
                }
            }
        }
    }

    /**
     * 更新所有的播放列表项
     */
    public void updateAllPlaylistFragment() {
        AllPlaylistsFragment playListFragment = (AllPlaylistsFragment) fragmentManager.findFragmentByTag("allPlaylists");
        if (playListFragment != null && playListFragment.allPlaylistRecycler != null) {
            playListFragment.allPlaylistRecycler.getAdapter().notifyDataSetChanged();
        }
    }

    /*
     * EditSongFragment callbacks START
     * onEditSongSave() -> called when save is clicked to store edited fields.
     * deleteMediaStoreCache() -> used to delete the media store cache so that it is rebuilt and new album art is cached.
     * getNewBitmap() -> called to statr the image picker intent for album art.
     */
    @Override
    public void onEditSongSave(boolean wasSaveSuccessful) {
        hideFragment("Edit");
        if (!wasSaveSuccessful) {
            Toast.makeText(this, "Error occured while editing", Toast.LENGTH_SHORT).show();
            return;
        }

        MediaCacheUtils.updateMediaCache(editSong.getTitle(), editSong.getArtist(), editSong.getAlbum(), editSong.getId(), this);

        refreshAlbumAndArtists();

        if (isAlbumVisible) {
            hideFragment("viewAlbum");

        } else if (isArtistVisible) {
            hideFragment("viewArtist");
        }
        LocalMusicViewPagerFragment flmFrag = (LocalMusicViewPagerFragment) fragmentManager.findFragmentByTag("local");
        LocalMusicFragment lFrag = null;
        if (flmFrag != null) {
            lFrag = (LocalMusicFragment) flmFrag.getFragmentByPosition(0);
        }
        if (lFrag != null) {
            lFrag.updateAdapter();
        }

        ArtistFragment artFrag = null;
        if (flmFrag != null) {
            artFrag = (ArtistFragment) flmFrag.getFragmentByPosition(2);
        }
        if (artFrag != null) {
            artFrag.updateAdapter();
        }

        AlbumFragment albFrag = null;
        if (flmFrag != null) {
            albFrag = (AlbumFragment) flmFrag.getFragmentByPosition(1);
        }
        if (albFrag != null) {
            albFrag.updateAdapter();
        }
    }

    /**
     * 更新唱片与艺术家
     */
    private void refreshAlbumAndArtists() {
        albums.clear();
        finalAlbums.clear();
        artists.clear();
        finalArtists.clear();

        for (int i = 0; i < localTrackList.size(); i++) {
            LocalTrack lt = localTrackList.get(i);

            String thisAlbum = lt.getAlbum();

            int pos = checkAlbum(thisAlbum);

            if (pos != -1) {
                albums.get(pos).getAlbumSongs().add(lt);
            } else {
                List<LocalTrack> llt = new ArrayList<>();
                llt.add(lt);
                Album ab = new Album(thisAlbum, llt);
                albums.add(ab);
            }

            if (pos != -1) {
                finalAlbums.get(pos).getAlbumSongs().add(lt);
            } else {
                List<LocalTrack> llt = new ArrayList<>();
                llt.add(lt);
                Album ab = new Album(thisAlbum, llt);
                finalAlbums.add(ab);
            }

            String thisArtist = lt.getArtist();

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
    }

    @Override
    public void getNewBitmap() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, 1);
    }

    @Override
    public void deleteMediaStoreCache() {
        File dir = new File(Environment.getExternalStorageDirectory() + "/Android/data/com.android.providers.media/albumthumbs");
        if (dir.isDirectory()) {
            Toast.makeText(this, "Clearing cache", Toast.LENGTH_SHORT).show();
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                new File(dir, children[i]).delete();
            }
        }
    }

    /*
    * NewPlaylistFragment callbacks START
    * onCancel() -> callback to handle cancellation of creating new playlist.
    * onDone() -> callback to handle completion of creating new playlist.
    */
    @Override
    public void onCancel() {
        finalSelectedTracks.clear();
    }

    @Override
    public void onDone() {
        if (finalSelectedTracks.size() == 0) {
            finalSelectedTracks.clear();
            onBackPressed();
        } else {
            newPlaylistNameDialog();
        }
    }

    /**
     * 新的播放列表名的对话框
     */
    private void newPlaylistNameDialog() {
        final Dialog dialog = new Dialog(ctx);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.save_image_dialog);

        TextView titleText = (TextView) dialog.findViewById(R.id.dialog_title);
        titleText.setText("Playlist Name");
        if (SplashActivity.tf4 != null)
            titleText.setTypeface(SplashActivity.tf4);

        Button btn = (Button) dialog.findViewById(R.id.save_image_btn);
        final EditText newName = (EditText) dialog.findViewById(R.id.save_image_filename_text);

        CheckBox cb = (CheckBox) dialog.findViewById(R.id.text_checkbox);
        cb.setVisibility(GONE);

        btn.setBackgroundColor(themeColor);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isNameRepeat = false;
                if (newName.getText().toString().trim().equals("")) {
                    newName.setError("Enter Playlist Name!");
                } else {
                    for (int i = 0; i < allPlaylists.getPlaylists().size(); i++) {
                        if (newName.getText().toString().equals(allPlaylists.getPlaylists().get(i).getPlaylistName())) {
                            isNameRepeat = true;
                            newName.setError("Playlist with same name exists!");
                            break;
                        }
                    }
                    if (!isNameRepeat) {
                        UnifiedTrack ut;
                        Playlist pl = new Playlist(newName.getText().toString());
                        for (int i = 0; i < finalSelectedTracks.size(); i++) {
                            ut = new UnifiedTrack(true, finalSelectedTracks.get(i), null);
                            pl.getSongList().add(ut);
                        }
                        allPlaylists.addPlaylist(pl);
                        finalSelectedTracks.clear();
                        if (pAdapter != null) {
                            pAdapter.notifyDataSetChanged();
                            if (allPlaylists.getPlaylists().size() > 0) {
                                playlistsRecycler.setVisibility(View.VISIBLE);
                                playlistNothingText.setVisibility(View.INVISIBLE);
                            }
                        }
                        AllPlaylistsFragment plFrag = (AllPlaylistsFragment) fragmentManager.findFragmentByTag("allPlaylists");
                        if (plFrag != null) {
                            plFrag.dataChanged();
                        }
                        new SavePlaylists().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                        dialog.dismiss();
                        onBackPressed();
                    }
                }
            }
        });

        dialog.show();
    }

    /**
     * 播放暂停的方法
     */
    @Override
    public void onPlayPause() {
        showNotification();
    }


    // 保存theDNA的异步任务
    public static class SaveTheDNAs extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if (!isSaveDNAsRunning) {
                isSaveDNAsRunning = true;
                try {
                    String json = gson.toJson(savedDNAs);
                    prefsEditor.putString("savedDNAs", json);
                } catch (Exception e) {

                }
                isSaveDNAsRunning = false;
            }
            return null;
        }
    }

    private class CancelCall implements Runnable {
        @Override
        public void run() {
            if (call != null)
                call.cancel();
        }
    }


    /**
     * 保存播放列表的异步任务
     */
    public static class SavePlaylists extends AsyncTask<Void, Void, Void> {
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
                    // 显示头部专辑图片
                    refreshHeaderImages();

                    if (queue != null && queue.getQueue().size() != 0) {
                        UnifiedTrack utHome = queue.getQueue().get(queueCurrentIndex);
                        // 设置底部图片与title
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
                        Log.e(TAG, "recentlyPlayed = " + recentlyPlayed);
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
                    recentsRecycler.addOnItemTouchListener(new ClickItemTouchListener(recentsRecycler) {
                        @Override
                        public boolean onClick(RecyclerView parent, View view, final int position, long id) {
                            UnifiedTrack ut = continuePlayingList.get(position);
                            boolean isRepeat = false;
                            int pos = 0;
                            for (int i = 0; i < queue.getQueue().size(); i++) {
                                UnifiedTrack ut1 = queue.getQueue().get(i);
                                if (ut1.getType() && ut.getType() && ut1.getLocalTrack().getTitle().equals(ut.getLocalTrack().getTitle())) {
                                    isRepeat = true;
                                    pos = i;
                                    break;
                                }
                                if (!ut1.getType() && !ut.getType() && ut1.getStreamTrack().getTitle().equals(ut.getStreamTrack().getTitle())) {
                                    isRepeat = true;
                                    pos = i;
                                    break;
                                }
                            }
                            if (!isRepeat) {
                                if (ut.getType()) {
                                    LocalTrack track = ut.getLocalTrack();
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
                                    // 暂时注释掉
//                                    onLocalTrackSelected(position);
                                } else {
                                    Track track = ut.getStreamTrack();
                                    if (queue.getQueue().size() == 0) {
                                        queueCurrentIndex = 0;
                                        queue.getQueue().add(new UnifiedTrack(false, null, track));
                                    } else if (queueCurrentIndex == queue.getQueue().size() - 1) {
                                        queueCurrentIndex++;
                                        queue.getQueue().add(new UnifiedTrack(false, null, track));
                                    } else if (isReloaded) {
                                        isReloaded = false;
                                        queueCurrentIndex = queue.getQueue().size();
                                        queue.getQueue().add(new UnifiedTrack(false, null, track));
                                    } else {
                                        queue.getQueue().add(++queueCurrentIndex, new UnifiedTrack(false, null, track));
                                    }
                                    selectedTrack = track;
                                    streamSelected = true;
                                    localSelected = false;
                                    queueCall = false;
                                    isReloaded = false;
                                    onTrackSelected(position);
                                }
                            } else {
                                onQueueItemClicked(pos);
                            }

                            return true;
                        }

                        @Override
                        public boolean onLongClick(RecyclerView parent, View view, final int position, long id) {
                            final UnifiedTrack ut = continuePlayingList.get(position);
                            CustomGeneralBottomSheetDialog generalBottomSheetDialog = new CustomGeneralBottomSheetDialog();
                            generalBottomSheetDialog.setPosition(position);
                            generalBottomSheetDialog.setTrack(ut);
                            generalBottomSheetDialog.setFragment("RecentHorizontalList");
                            generalBottomSheetDialog.show(getSupportFragmentManager(), "general_bottom_sheet_dialog");
                            return true;
                        }

                        @Override
                        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                        }
                    });

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
                    playlistsRecycler.addOnItemTouchListener(new ClickItemTouchListener(playlistsRecycler) {
                        @Override
                        public boolean onClick(RecyclerView parent, View view, final int position, long id) {
                            tempPlaylist = allPlaylists.getPlaylists().get(position);
                            tempPlaylistNumber = position;
                            showFragment("playlist");
                            return true;
                        }

                        @Override
                        public boolean onLongClick(RecyclerView parent, View view, final int position, long id) {
                            PopupMenu popup = new PopupMenu(ctx, view);
                            popup.getMenuInflater().inflate(R.menu.playlist_popup, popup.getMenu());

                            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                                @Override
                                public boolean onMenuItemClick(MenuItem item) {
                                    if (item.getTitle().equals(getString(R.string.Play))) {

                                        tempPlaylist = allPlaylists.getPlaylists().get(position);
                                        tempPlaylistNumber = position;

                                        int size = tempPlaylist.getSongList().size();
                                        queue.getQueue().clear();
                                        for (int i = 0; i < size; i++) {
                                            queue.addToQueue(tempPlaylist.getSongList().get(i));
                                        }

                                        queueCurrentIndex = 0;
                                        onPlaylistPlayAll();
                                    } else if (item.getTitle().equals(getString(R.string.Add_to_Queue))) {
                                        Playlist pl = allPlaylists.getPlaylists().get(position);
                                        for (UnifiedTrack ut : pl.getSongList()) {
                                            queue.addToQueue(ut);
                                        }
                                    } else if (item.getTitle().equals(getString(R.string.Delete))) {
                                        allPlaylists.getPlaylists().remove(position);
                                        AllPlaylistsFragment plFrag = (AllPlaylistsFragment) fragmentManager.findFragmentByTag("allPlaylists");
                                        if (plFrag != null) {
                                            plFrag.itemRemoved(position);
                                        }
                                        pAdapter.notifyItemRemoved(position);
                                        new SavePlaylists().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                                    } else if (item.getTitle().equals(getString(R.string.Rename))) {
                                        renamePlaylistNumber = position;
                                        renamePlaylistDialog(allPlaylists.getPlaylists().get(position).getPlaylistName());
                                    }
                                    return true;
                                }
                            });
                            popup.show();
                            return true;
                        }

                        @Override
                        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                        }
                    });

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
                    soundcloudRecyclerView.addOnItemTouchListener(new ClickItemTouchListener(soundcloudRecyclerView) {
                        @Override
                        public boolean onClick(RecyclerView parent, View view, int position, long id) {
                            Track track = streamingTrackList.get(position);
                            if (queue.getQueue().size() == 0) {
                                queueCurrentIndex = 0;
                                queue.getQueue().add(new UnifiedTrack(false, null, track));
                            } else if (queueCurrentIndex == queue.getQueue().size() - 1) {
                                queueCurrentIndex++;
                                queue.getQueue().add(new UnifiedTrack(false, null, track));
                            } else if (isReloaded) {
                                isReloaded = false;
                                queueCurrentIndex = queue.getQueue().size();
                                queue.getQueue().add(new UnifiedTrack(false, null, track));
                            } else {
                                queue.getQueue().add(++queueCurrentIndex, new UnifiedTrack(false, null, track));
                            }
                            selectedTrack = track;
                            streamSelected = true;
                            localSelected = false;
                            queueCall = false;
                            isReloaded = false;
                            onTrackSelected(position);
                            return true;
                        }

                        @Override
                        public boolean onLongClick(RecyclerView parent, View view, final int position, long id) {
                            CustomGeneralBottomSheetDialog generalBottomSheetDialog = new CustomGeneralBottomSheetDialog();
                            generalBottomSheetDialog.setPosition(position);
                            generalBottomSheetDialog.setTrack(new UnifiedTrack(false, null, streamingTrackList.get(position)));
                            generalBottomSheetDialog.setFragment("Stream");
                            generalBottomSheetDialog.show(getSupportFragmentManager(), "general_bottom_sheet_dialog");
                            return true;
                        }

                        @Override
                        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                        }
                    });

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
    public void onTrackSelected(int position) {
        isReloaded = false;
        HideBottomFakeToolbar();

        if (!queueCall) {
            CommonUtils.hideKeyboard(this);

            searchView.setQuery("", false);
            searchView.setIconified(true);
            new Thread(new CancelCall()).start();

            isPlayerVisible = true;

            PlayerFragment frag = playerFragment;
            android.support.v4.app.FragmentManager fm = getSupportFragmentManager();
            PlayerFragment newFragment = new PlayerFragment();
            if (frag == null) {
                playerFragment = newFragment;
                int flag = 0;
                for (int i = 0; i < favouriteTracks.getFavourite().size(); i++) {
                    UnifiedTrack ut = favouriteTracks.getFavourite().get(i);
                    if (!ut.getType() && ut.getStreamTrack().getTitle().equals(selectedTrack.getTitle())) {
                        flag = 1;
                        isFavourite = true;
                        break;
                    }
                }
                if (flag == 0) {
                    isFavourite = false;
                }
                playerFragment.localIsPlaying = false;
                playerFragment.track = selectedTrack;
                fm.beginTransaction()
                        .setCustomAnimations(R.anim.slide_up,
                                R.anim.slide_down,
                                R.anim.slide_up,
                                R.anim.slide_down)
                        .add(R.id.player_frag_container, newFragment, "player")
                        .show(newFragment)
                        .addToBackStack(null)
                        .commitAllowingStateLoss();
            } else {
                if (playerFragment.track != null && !playerFragment.localIsPlaying && selectedTrack.getTitle() == playerFragment.track.getTitle()) {

                } else {
                    int flag = 0;
                    for (int i = 0; i < favouriteTracks.getFavourite().size(); i++) {
                        UnifiedTrack ut = favouriteTracks.getFavourite().get(i);
                        if (!ut.getType() && ut.getStreamTrack().getTitle().equals(selectedTrack.getTitle())) {
                            flag = 1;
                            isFavourite = true;
                            break;
                        }
                    }
                    if (flag == 0) {
                        isFavourite = false;
                    }
                    playerFragment.localIsPlaying = false;
                    playerFragment.track = selectedTrack;
                    frag.refresh();
                }
            }
            if (!isQueueVisible)
                showPlayer();
        } else {
            PlayerFragment frag = playerFragment;
            playerFragment.localIsPlaying = false;
            playerFragment.track = selectedTrack;
            int flag = 0;
            for (int i = 0; i < favouriteTracks.getFavourite().size(); i++) {
                UnifiedTrack ut = favouriteTracks.getFavourite().get(i);
                if (!ut.getType() && ut.getStreamTrack().getTitle().equals(selectedTrack.getTitle())) {
                    flag = 1;
                    isFavourite = true;
                    break;
                }
            }
            if (flag == 0) {
                isFavourite = false;
            }
            frag.refresh();
        }

        if (playerFragment != null && playerFragment.snappyRecyclerView != null) {
            playerFragment.snappyRecyclerView.getAdapter().notifyDataSetChanged();
            playerFragment.snappyRecyclerView.setTransparency();
        }

        QueueFragment qFrag = (QueueFragment) fragmentManager.findFragmentByTag("queue");
        if (qFrag != null) {
            qFrag.updateQueueAdapter();
        }

        UnifiedTrack track = new UnifiedTrack(false, null, playerFragment.track);
        for (int i = 0; i < recentlyPlayed.getRecentlyPlayed().size(); i++) {
            if (!recentlyPlayed.getRecentlyPlayed().get(i).getType() && recentlyPlayed.getRecentlyPlayed().get(i).getStreamTrack().getTitle().equals(track.getStreamTrack().getTitle())) {
                recentlyPlayed.getRecentlyPlayed().remove(i);
//                rAdapter.notifyItemRemoved(i);
                break;
            }
        }
        recentlyPlayed.getRecentlyPlayed().add(0, track);
        if (recentlyPlayed.getRecentlyPlayed().size() > 50) {
            recentlyPlayed.getRecentlyPlayed().remove(50);
        }
        recentsRecycler.setVisibility(View.VISIBLE);
        recentsNothingText.setVisibility(View.INVISIBLE);
        continuePlayingList.clear();
        for (int i = 0; i < Math.min(10, recentlyPlayed.getRecentlyPlayed().size()); i++) {
            continuePlayingList.add(recentlyPlayed.getRecentlyPlayed().get(i));
        }
        rAdapter.notifyDataSetChanged();
        refreshHeaderImages();

        RecentsFragment rFrag = (RecentsFragment) fragmentManager.findFragmentByTag("recent");
        if (rFrag != null && rFrag.rtAdpater != null) {
            rFrag.rtAdpater.notifyDataSetChanged();
        }

    }


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
        Log.e(TAG, "numSongs = " + numSongs); // numSongs = 7
        int numRecents = recentlyPlayed.getRecentlyPlayed().size();
        Log.e(TAG, "numRecents = " + numRecents); // E/HomeActivity: numRecents = 4
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
                Log.e(TAG, "UnifiedTrack ut = " + ut);
                // UnifiedTrack ut = UnifiedTrack{type=true, localTrack=LocalTrack{id=764, title='我不配', artist='周杰伦', album='我很忙', path='/storage/emulated/0/netease/cloudmusic/Music/周杰伦 - 我不配.mp3', duration=288810}, streamTrack=null}
                Log.e(TAG, "UnifiedTrack ut.getLocalTrack().getPath() = " + ut.getLocalTrack().getPath());
                // UnifiedTrack ut.getLocalTrack().getPath() = /storage/emulated/0/netease/cloudmusic/Music/周杰伦 - 我不配.mp3
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
                Log.e(TAG, "大于10 ut = " + ut);
                if (ut.getType()) {
                    imgLoader.DisplayImage(ut.getLocalTrack().getPath(), imgView[i]);
                    Log.e(TAG, "ut.getLocalTrack().getPath() = " + ut.getLocalTrack().getPath() + "," + "图片 = " + imgView[i]);
                    // E/HomeActivity: ut.getLocalTrack().getPath() = /storage/emulated/0/CST/fragment_music/Souvevirs d'Enfance-Richard Clayderman.mp3,
                    // 图片 = android.support.v7.widget.AppCompatImageView{18df0f3 V.ED..... ......ID 0,0-216,187 #7f0900f7 app:id/home_header_img_1}
                } else {
                    imgLoader.DisplayImage(ut.getStreamTrack().getArtworkURL(), imgView[i]);
                }
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

    /**
     * 返回值跳转？！
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                try {
                    final Uri imageUri = data.getData();
                    String path = imageUri.getPath();
                    Toast.makeText(this, path + "", Toast.LENGTH_SHORT).show();
                    final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                    selectedImage = BitmapFactory.decodeStream(imageStream);

                    EditLocalSongFragment editSongFragment = (EditLocalSongFragment) getSupportFragmentManager().findFragmentByTag("Edit");
                    if (editSongFragment != null) {
                        editSongFragment.updateCoverArt(selectedImage, imageUri);
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 界面停止时进行异步操作，提交数据
     * 可以做下测试进行多界面打开，本界面的状态；
     */
    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "主界面 onPause");
        new SaveVersionCode().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new SaveData().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new SaveSettings().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        new SaveQueue().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

        try {
            prefsEditor.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存版本号的异步任务
     */
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

    /**
     * 保存到队列的异步任务
     */
    public static class SaveQueue extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if (!isSaveQueueRunning) {
                isSaveQueueRunning = true;
                try {
                    String json3 = gson.toJson(queue);
                    prefsEditor.putString("queue", json3);
                    String json6 = gson.toJson(queueCurrentIndex);
                    prefsEditor.putString("queueCurrentIndex", json6);
                } catch (Exception e) {

                }
                isSaveQueueRunning = false;
            }
            return null;
        }
    }

    /**
     * 保存最近的异步任务
     */
    public static class SaveRecents extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            if (!isSaveRecentsRunning) {
                isSaveRecentsRunning = true;
                try {
                    String json4 = gson.toJson(recentlyPlayed);
                    prefsEditor.putString("recentlyPlayed", json4);
                } catch (Exception e) {

                }
                isSaveRecentsRunning = false;
            }
            return null;
        }
    }

    /**
     * 保存喜欢的异步任务
     */
    public static class SaveFavourites extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if (!isSaveFavouritesRunning) {
                isSaveFavouritesRunning = true;
                try {
                    String json5 = gson.toJson(favouriteTracks);
                    prefsEditor.putString("favouriteTracks", json5);
                } catch (Exception e) {

                }
                isSaveFavouritesRunning = false;
            }
            return null;
        }
    }

    /**
     * 我的异步任务
     */
    public class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            // 2/19 暂时注释掉
            updatePoints();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            main.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    playerFragment.mVisualizerView.updateVisualizer(mBytes);
                    if (playerFragment.mVisualizerView.bmp != null) {
                        if (navImageView != null) {
                            try {
                                Bitmap croppedBmp = Bitmap.createBitmap(playerFragment.mVisualizerView.bmp, 0, (int) (75 * ratio), screen_width, screen_width);
                                navImageView.setImageBitmap(croppedBmp);
                            } catch (Exception | OutOfMemoryError e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }
    }

    private void updatePoints() {
        try {
            playerFragment.mVisualizerView.outerRadius = (float) (Math.min(playerFragment.mVisualizerView.width, playerFragment.mVisualizerView.height) * 0.42);
            playerFragment.mVisualizerView.normalizedPosition = ((float) (playerFragment.mMediaPlayer.getCurrentPosition()) / (float) (playerFragment.durationInMilliSec));
            if (mBytes == null) {
                return;
            }
            playerFragment.mVisualizerView.angle = (float) (Math.PI - playerFragment.mVisualizerView.normalizedPosition * playerFragment.mVisualizerView.TAU);
            playerFragment.mVisualizerView.color = 0;
            playerFragment.mVisualizerView.lnDataDistance = 0;
            playerFragment.mVisualizerView.distance = 0;
            playerFragment.mVisualizerView.size = 0;
            playerFragment.mVisualizerView.volume = 0;
            playerFragment.mVisualizerView.power = 0;
        } catch (Exception e) {

        }

        float x, y;

        int midx = (int) (playerFragment.mVisualizerView.width / 2);
        int midy = (int) (playerFragment.mVisualizerView.height / 2);

        // calculate min and max amplitude for current byte array
        float max = Integer.MIN_VALUE, min = Integer.MAX_VALUE;
        for (int a = 16; a < (mBytes.length / 2); a++) {
            float amp = mBytes[(a * 2)] * mBytes[(a * 2)] + mBytes[(a * 2) + 1] * mBytes[(a * 2) + 1];
            if (amp > max) {
                max = amp;
            }
            if (amp < min) {
                min = amp;
            }
        }

        /**
         * Number Fishing is all that is used here to get the best looking DNA
         * Number fishing is HOW YOU WIN AT LIFE. -- paullewis :)
         * **/

        for (int a = 16; a < (mBytes.length / 2); a++) {

            if (max <= 10.0) {
                break;
            }

            // scale the amplitude to the range [0,1]
            float amp = mBytes[(a * 2) + 0] * mBytes[(a * 2) + 0] + mBytes[(a * 2) + 1] * mBytes[(a * 2) + 1];
            if (max != min)
                amp = (amp - min) / (max - min);
            else {
                amp = 0;
            }

            playerFragment.mVisualizerView.volume = (amp);

            // converting polar to cartesian (distance calculated afterwards acts as radius for polar co-ords)
            x = (float) Math.sin(playerFragment.mVisualizerView.angle);
            y = (float) Math.cos(playerFragment.mVisualizerView.angle);

            // filtering low amplitude
            if (playerFragment.mVisualizerView.volume < minAudioStrength) {
                continue;
            }

            // color ( value of hue inn HSV ) calculated based on current progress of the song or audio clip
            playerFragment.mVisualizerView.color = (float) (playerFragment.mVisualizerView.normalizedPosition - 0.12 + Math.random() * 0.24);
            playerFragment.mVisualizerView.color = Math.round(playerFragment.mVisualizerView.color * 360);
            seekBarColor = (float) (playerFragment.mVisualizerView.normalizedPosition);
            seekBarColor = Math.round(seekBarColor * 360);

            // calculating distance from center ( 'r' in polar coordinates)
            playerFragment.mVisualizerView.lnDataDistance = (float) ((Math.log(a - 4) / playerFragment.mVisualizerView.LOG_MAX) - playerFragment.mVisualizerView.BASE);
            playerFragment.mVisualizerView.distance = playerFragment.mVisualizerView.lnDataDistance * playerFragment.mVisualizerView.outerRadius;


            // size of the circle to be rendered at the calculated position
            playerFragment.mVisualizerView.size = ratio * ((float) (4.5 * playerFragment.mVisualizerView.volume * playerFragment.mVisualizerView.MAX_DOT_SIZE + Math.random() * 2));

            // alpha also based on volume ( amplitude )
            playerFragment.mVisualizerView.alpha = (float) (playerFragment.mVisualizerView.volume * 0.09);

            // final cartesian coordinates for drawing on canvas
            x = x * playerFragment.mVisualizerView.distance;
            y = y * playerFragment.mVisualizerView.distance;


            float[] hsv = new float[3];
            hsv[0] = playerFragment.mVisualizerView.color;
            hsv[1] = (float) 0.9;
            hsv[2] = (float) 0.9;

            // setting color of the Paint
            playerFragment.mVisualizerView.mForePaint.setColor(Color.HSVToColor(hsv));

            if (playerFragment.mVisualizerView.size >= 8.0 && playerFragment.mVisualizerView.size < 29.0) {
                playerFragment.mVisualizerView.mForePaint.setAlpha(17);
            } else if (playerFragment.mVisualizerView.size >= 29.0 && playerFragment.mVisualizerView.size <= 60.0) {
                playerFragment.mVisualizerView.mForePaint.setAlpha(9);
            } else if (playerFragment.mVisualizerView.size > 60.0) {
                playerFragment.mVisualizerView.mForePaint.setAlpha(3);
            } else {
                playerFragment.mVisualizerView.mForePaint.setAlpha((int) (playerFragment.mVisualizerView.alpha * 1000));
            }

            // Draw to the *temp* canvas, this is done to deal with gaps in rendering, when canvas is out of focus
            cacheCanvas.drawCircle(midx + x, midy + y, playerFragment.mVisualizerView.size, playerFragment.mVisualizerView.mForePaint);

        }
    }


    /**
     * 保存设置的异步任务
     */
    public static class SaveSettings extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if (!isSaveSettingsRunning) {
                isSaveSettingsRunning = true;
                try {
                    String json8 = gson.toJson(settings);
                    prefsEditor.putString("settings", json8);
                } catch (Exception e) {

                }
                isSaveSettingsRunning = false;
            }
            return null;
        }
    }

    // 保存均衡器的异步任务
    public static class SaveEqualizer extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            if (!isSaveEqualizerRunning) {
                isSaveEqualizerRunning = true;
                try {
                    String json2 = gson.toJson(equalizerModel);
                    prefsEditor.putString("equalizer", json2);
                } catch (Exception e) {

                }
                isSaveEqualizerRunning = false;
            }
            return null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(headSetReceiver);
        RefWatcher refWatcher = MusicDNAApplication.getRefWatcher(this);
        refWatcher.watch(this);

        TelephonyManager mgr = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        if (mgr != null) {
            mgr.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
        }
        if (bound) {
            myService.setCallbacks(null); // unregister
            unbindService(serviceConnection);
            bound = false;
        }
    }

    /**
     * 保存dna的异步任务
     */
    public class SaveData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            try {
                String json6 = gson.toJson(queueCurrentIndex);
                prefsEditor.putString("queueCurrentIndex", json6);
            } catch (Exception e) {

            }
            return null;
        }
    }

    // 需要添加判断，带修正
//    long time;
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
//            if (event.getEventTime() - time < 2000) {
//                finish();
//            } else {
//                time = event.getEventTime();
//            }
//        }
//        return true;
//    }

}
