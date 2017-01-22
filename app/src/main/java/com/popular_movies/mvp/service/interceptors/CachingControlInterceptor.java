package com.popular_movies.mvp.service.interceptors;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Gurpreet on 12/13/2016.
 */

public class CachingControlInterceptor implements Interceptor {

    private static final String TAG = CachingControlInterceptor.class.getSimpleName();

    private static final String CACHE_CONTROL = "Cache-Control";
    private static final String PRAGMA = "Pragma";

    public Cache setupCache(Context context) {
        //setup cache
        File httpCacheDirectory = new File(context.getCacheDir(), "http-cache");
        int cacheSize = 10 * 1024 * 1024; // 10 MiB

        return new Cache(httpCacheDirectory, cacheSize);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();


        CacheControl cacheControl = new CacheControl.Builder()
                .onlyIfCached()
                .maxAge(120, TimeUnit.SECONDS)
                .maxStale(1, TimeUnit.DAYS)
                .build();

        request = request.newBuilder()
                .removeHeader("Pragma")
                .cacheControl(cacheControl)
                .build();

        Response originalResponse = chain.proceed(request);

        return originalResponse.newBuilder()
                .header(CACHE_CONTROL, cacheControl.toString())
                .removeHeader(PRAGMA)
                .build();
    }
}

