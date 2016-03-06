package com.popular_movies;

import android.net.Uri;

/**
 * Created by Gurpreet on 2/25/2016.
 */
public class UriBuilder {
    public static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";
    public static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    public static String[] imageSize = {"w92", "w154", "w185", "w342", "w500", "w780"};
    static final String sortParam = "sort_by";
    static final String keyParam = "api_key";
    static final String HIGHEST_RATED="vote_average.desc";
    static final String MOST_POPULAR="popularity.desc";
    static final String KEY = BuildConfig.TMDB_API_KEY;
    Uri.Builder builder;

    public UriBuilder( String url, String sortBy) {
        //String key = context.getString(R.string.key);
        builder = Uri.parse(url).buildUpon();
        builder.appendQueryParameter(sortParam, sortBy);
        builder.appendQueryParameter(keyParam, KEY);
    }

    @Override
    public String toString() {
        return builder.toString();
    }
}
