package com.popular_movies.mvp.service.movie;

import android.app.Activity;

import com.popular_movies.BuildConfig;
import com.popular_movies.domain.MovieData;
import com.popular_movies.domain.MovieResponse;
import com.popular_movies.domain.Trailer;
import com.popular_movies.domain.TrailerResponse;

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
        @GET("movie/{movieType}?api_key="+ BuildConfig.TMDB_API_KEY)
        Call<MovieResponse> getMovies(@Path("movieType") String movieType);

        @GET ("movie/{id}/videos?api_key="+ BuildConfig.TMDB_API_KEY)
        Call<TrailerResponse> getTrailers(@Path("id") int id);
/*
        @GET ("movie/{id}/reviews?api_key="+BuildConfig.TMDB_API_KEY)
        Call<Review> getReview(@Path("id") int id);
        */
    }

    /**
     * Get movies model
     */
    void getMovies(String movieType, Activity activity, GetMoviesCallback getMoviesCardCalback);

    interface GetMoviesCallback {
        void onSuccess(MovieResponse movieResponse);

        void onFailure(Throwable throwable);
    }

    /**
     * Get movie trailers model
     */
    void getTrailers(int id, Activity activity, GetTrailersCallback getTrailersCallback);

    interface GetTrailersCallback {
        void onSuccess(TrailerResponse trailerResponse);

        void onFailure(Throwable throwable);
    }
}
