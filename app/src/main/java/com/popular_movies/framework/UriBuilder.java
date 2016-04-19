package com.popular_movies.framework;

import android.net.Uri;

import com.popular_movies.BuildConfig;

/**
 * Created by Gurpreet on 2/25/2016.
 */
public class UriBuilder {
    public static final String BASE_URL = "https://api.themoviedb.org/3/movie";
    public static final String BASE_URL_IMAGE = "http://image.tmdb.org/t/p/";

    public static String[] imageSize = {"w92", "w154", "w185", "w342", "w500", "w780"};
    public static final String TOP_RATED = "top_rated";
    public static final String POPULAR = "popular";
    public static final String VIDEOS = "videos";
    public static final String REVIEWS = "reviews";

    static final String KEYPARAM= "api_key";
    static final String KEY = BuildConfig.TMDB_API_KEY;
    Uri.Builder builder;

    public UriBuilder( String url, String path) {
        builder = Uri.parse(url).buildUpon();
        builder.appendEncodedPath(path);
        builder.appendQueryParameter(KEYPARAM, KEY);
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
