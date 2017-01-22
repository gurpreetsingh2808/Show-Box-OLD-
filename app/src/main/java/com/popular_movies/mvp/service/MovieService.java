package com.popular_movies.mvp.service;

import android.app.Activity;

import com.popular_movies.BuildConfig;
import com.popular_movies.domain.MovieData;
import com.popular_movies.domain.MovieResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Gurpreet on 20-01-2017.
 */

public interface MovieService {

    public interface MovieResource {
        @GET("movie/popular?api_key="+ BuildConfig.TMDB_API_KEY)
        Call<MovieResponse> getPopularMovies();

        @GET("movie/top_rated?api_key="+ BuildConfig.TMDB_API_KEY)
        Call<MovieResponse> getTopRatedMovies();
    }

    /**
     * Get popular movies model
     */
    void getPopularMovies(Activity activity, GetPopularMoviesCallback getPopularMoviesCardCalback);

    interface GetPopularMoviesCallback {
        void onSuccess(MovieResponse movieResponse);

        void onFailure(Throwable throwable);
    }

    /**
     * Get top rated movies model
     */
    void getTopRatedMovies(Activity activity, GetTopRatedMoviesCallback getTopRatedMoviesCardCalback);

    interface GetTopRatedMoviesCallback {
        void onSuccess(MovieResponse movieResponse);

        void onFailure(Throwable throwable);
    }
}
