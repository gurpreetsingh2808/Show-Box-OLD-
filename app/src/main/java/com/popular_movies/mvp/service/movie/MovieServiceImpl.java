package com.popular_movies.mvp.service.movie;

import android.app.Activity;

import com.popular_movies.domain.MovieResponse;
import com.popular_movies.domain.ReviewResponse;
import com.popular_movies.domain.TrailerResponse;
import com.popular_movies.mvp.ResourceBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gurpreet on 21-01-2017.
 */

public class MovieServiceImpl implements MovieService {

    @Override
    public void getMovies(String movieType, final Activity activity, final GetMoviesCallback getMoviesCardCalback) {
        MovieResource movieResource = ResourceBuilder.buildResource(MovieResource.class, activity);
        Call<MovieResponse> call = movieResource.getMovies(movieType);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.body() != null && response.isSuccessful())
                    getMoviesCardCalback.onSuccess(response.body());
                else
                    getMoviesCardCalback.onFailure(new Throwable("Error"));
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                if (!call.isCanceled()) {
                    //SnackBarManager.renderFailureSnackBar(activity, null);
                    getMoviesCardCalback.onFailure(t);
                }
            }
        });
    }

    @Override
    public void getTrailers(int id, Activity activity, final GetTrailersCallback getTrailersCallback) {

        MovieResource movieResource = ResourceBuilder.buildResource(MovieResource.class, activity);
        Call<TrailerResponse> call = movieResource.getTrailers(id);
        call.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                if (response.body() != null && response.isSuccessful())
                    getTrailersCallback.onSuccess(response.body());
                else
                    getTrailersCallback.onFailure(new Throwable("Error"));
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                if (!call.isCanceled()) {
                    //SnackBarManager.renderFailureSnackBar(activity, null);
                    getTrailersCallback.onFailure(t);
                }
            }
        });
    }

    @Override
    public void getReviews(int id, Activity activity, final GetReviewsCallback getReviewsCallback) {
        MovieResource movieResource = ResourceBuilder.buildResource(MovieResource.class, activity);
        Call<ReviewResponse> call = movieResource.getReview(id);
        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                if (response.body() != null && response.isSuccessful())
                    getReviewsCallback.onSuccess(response.body());
                else
                    getReviewsCallback.onFailure(new Throwable("Error"));
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {
                if (!call.isCanceled()) {
                    //SnackBarManager.renderFailureSnackBar(activity, null);
                    getReviewsCallback.onFailure(t);
                }
            }
        });
    }

}
