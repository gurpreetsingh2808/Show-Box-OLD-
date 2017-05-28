package com.popular_movies.service.movie;

import android.app.Activity;

import com.popular_movies.domain.Genre;
import com.popular_movies.domain.GenreResponse;
import com.popular_movies.domain.MovieResponse;
import com.popular_movies.domain.ReviewResponse;
import com.popular_movies.domain.TrailerResponse;
import com.popular_movies.service.ResourceBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Gurpreet on 21-01-2017.
 */

public class MovieServiceImpl implements MovieService {

    @Override
    public void getMovies(String movieType, String pageNumber, final Activity activity, final GetMoviesCallback getMoviesCardCalback) {
        MovieResource movieResource = ResourceBuilder.buildResource(MovieResource.class, activity);
        Call<MovieResponse> call = movieResource.getMovies(movieType, pageNumber);
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
    public void getPopularMovies(Activity activity, final GetMoviesCallback getPopularMoviesCardCalback) {
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
    public void getTopRatedMovies(Activity activity, final GetMoviesCallback getTopRatedMoviesCardCalback) {
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

    @Override
    public void getUpcomingMovies(Activity activity, final GetMoviesCallback getUpcomingMoviesCardCalback) {
        MovieResource movieResource = ResourceBuilder.buildResource(MovieResource.class, activity);
        Call<MovieResponse> call = movieResource.getUpcomingMovies();
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.body() != null && response.isSuccessful())
                    getUpcomingMoviesCardCalback.onSuccess(response.body());
                else
                    getUpcomingMoviesCardCalback.onFailure(new Throwable("Error"));
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                if (!call.isCanceled()) {
                    //SnackBarManager.renderFailureSnackBar(activity, null);
                    getUpcomingMoviesCardCalback.onFailure(t);
                }
            }
        });
    }

    @Override
    public void getNowPlayingMovies(Activity activity, final GetMoviesCallback getNowPlayingMoviesCardCalback) {
        MovieResource movieResource = ResourceBuilder.buildResource(MovieResource.class, activity);
        Call<MovieResponse> call = movieResource.getNowPlayingMovies();
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.body() != null && response.isSuccessful())
                    getNowPlayingMoviesCardCalback.onSuccess(response.body());
                else
                    getNowPlayingMoviesCardCalback.onFailure(new Throwable("Error"));
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                if (!call.isCanceled()) {
                    //SnackBarManager.renderFailureSnackBar(activity, null);
                    getNowPlayingMoviesCardCalback.onFailure(t);
                }
            }
        });
    }

    @Override
    public void getLatestMovies(Activity activity, final GetMoviesCallback getMoviesCallback) {
        MovieResource movieResource = ResourceBuilder.buildResource(MovieResource.class, activity);
        Call<MovieResponse> call = movieResource.getLatestMovies();
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.body() != null && response.isSuccessful())
                    getMoviesCallback.onSuccess(response.body());
                else
                    getMoviesCallback.onFailure(new Throwable("Error"));
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                if (!call.isCanceled()) {
                    getMoviesCallback.onFailure(t);
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
                    getReviewsCallback.onFailure(t);
                }
            }
        });
    }

    @Override
    public void getSearchResults(String query, Activity activity, final GetMoviesCallback getMoviesCallback) {
        MovieResource movieResource = ResourceBuilder.buildResource(MovieResource.class, activity);
        Call<MovieResponse> call = movieResource.getSearchResults(query);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                if (response.body() != null && response.isSuccessful())
                    getMoviesCallback.onSuccess(response.body());
                else
                    getMoviesCallback.onFailure(new Throwable("Error"));
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                if (!call.isCanceled()) {
                    getMoviesCallback.onFailure(t);
                }
            }
        });
    }

    @Override
    public void getGenre(Activity activity, final FetchGenresCallback fetchGenresCallback) {
        MovieResource movieResource = ResourceBuilder.buildResource(MovieResource.class, activity);
        Call<GenreResponse> call = movieResource.getGenres();
        call.enqueue(new Callback<GenreResponse>() {
            @Override
            public void onResponse(Call<GenreResponse> call, Response<GenreResponse> response) {
                if (response.body() != null && response.isSuccessful())
                    fetchGenresCallback.onSuccess(response.body());
                else
                    fetchGenresCallback.onFailure(new Throwable("Error"));
            }

            @Override
            public void onFailure(Call<GenreResponse> call, Throwable t) {
                if (!call.isCanceled()) {
                    fetchGenresCallback.onFailure(t);
                }
            }
        });
    }

}
