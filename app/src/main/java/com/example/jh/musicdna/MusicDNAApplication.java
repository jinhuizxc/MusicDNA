package com.example.jh.musicdna;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import io.fabric.sdk.android.Fabric;

/**
 * Created by jinhui on 2018/2/13.
 * Email:1004260403@qq.com
 */

public class MusicDNAApplication extends Application {

    private RefWatcher refWatcher;

    public RefWatcher getRefWatcher() {
        return refWatcher;
    }

    // get方法
//    public RefWatcher getRefWatcher(Context context) {
//        MusicDNAApplication application = (MusicDNAApplication) context.getApplicationContext();
//        return application.refWatcher;
//    }

    @Override
    public void onCreate() {
        super.onCreate();

        Fabric.with(this, new Crashlytics());
        refWatcher = LeakCanary.install(this);
    }
}
