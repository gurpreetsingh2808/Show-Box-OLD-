package com.popular_movies.mvp.service.interceptors;

import android.app.Activity;

import com.popular_movies.domain.MovieResponse;
import com.popular_movies.domain.MovieResponse;
import com.popular_movies.mvp.ResourceBuilder;
import com.popular_movies.mvp.service.MovieService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gurpreet on 21-01-2017.
 */

public class MovieServiceImpl implements MovieService {
    
    @Override
    public void getPopularMovies(final Activity activity, final GetPopularMoviesCallback getPopularMoviesCardCalback) {
        MovieResource movieResource = ResourceBuilder.buildResource(MovieResource.class, activity);
        Call<MovieResponse> call = movieResource.getPopularMovies();
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.body() != null && response.isSuccessful())
                    getPopularMoviesCardCalback.onSuccess(response.body());
                else
                    getPopularMoviesCardCalback.onFailure(new Throwable("Error"));
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                if (!call.isCanceled()) {
                    //SnackBarManager.renderFailureSnackBar(activity, null);
                    getPopularMoviesCardCalback.onFailure(t);
                }
            }
        });
    }

    @Override
    public void getTopRatedMovies(final Activity activity, final GetTopRatedMoviesCallback getTopRatedMoviesCardCalback) {
        MovieResource movieResource = ResourceBuilder.buildResource(MovieResource.class, activity);
        Call<MovieResponse> call = movieResource.getTopRatedMovies();
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.body() != null && response.isSuccessful())
                    getTopRatedMoviesCardCalback.onSuccess(response.body());
                else
                    getTopRatedMoviesCardCalback.onFailure(new Throwable("Error"));
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                if (!call.isCanceled()) {
                    //SnackBarManager.renderFailureSnackBar(activity, null);
                    getTopRatedMoviesCardCalback.onFailure(t);
                }
            }
        });
    }
}
