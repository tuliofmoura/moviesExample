package com.example.movies.application;

import android.app.Application;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.movies.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

/**
 * Created by tuliomoura on 10/20/15.
 */
public class MoviesApplication extends Application {

    private final int CONNECT_TIMEOUT = 10 * 1000; // 10 seconds
    private final int READ_TIMEOUT = 20 * 1000; // 20 seconds
    private final int DISK_CACHE_SIZE = 50 * 1024 * 1024; // 50MB

    private static RequestQueue mRequestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader();
        initVolley();
    }

    private void initImageLoader() {
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.drawable.loading)
                .showImageOnFail(R.drawable.no_image)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .diskCacheSize(DISK_CACHE_SIZE)
                .defaultDisplayImageOptions(options)
                .imageDownloader(new BaseImageDownloader(this, CONNECT_TIMEOUT, READ_TIMEOUT))
                .build();
        ImageLoader.getInstance().init(config);
    }

    private void initVolley() {
        mRequestQueue = Volley.newRequestQueue(this);
    }

    public static synchronized RequestQueue getRequestQueue() {
        return mRequestQueue;
    }
}
