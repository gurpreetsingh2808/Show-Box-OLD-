package com.popular_movies.mvp.presenter;

import android.app.Activity;

import com.popular_movies.domain.MovieResponse;
import com.popular_movies.domain.Trailer;
import com.popular_movies.domain.TrailerResponse;
import com.popular_movies.mvp.service.movie.MovieService;
import com.popular_movies.mvp.service.movie.MovieServiceImpl;


/**
 * Created by Gurpreet on 21-01-2017.
 */


public class TrailerPresenterImpl implements TrailerPresenter.Presenter {

    private final TrailerPresenter.View view;
    private final Activity activity;
    private MovieService movieService;

    public TrailerPresenterImpl(TrailerPresenter.View view, Activity activity) {
        this.view = view;
        this.activity = activity;
        this.movieService = new MovieServiceImpl();
    }

    @Override
    public void fetchTrailers(int movieId) {
        movieService.getTrailers(movieId, activity, new MovieService.GetTrailersCallback() {
            @Override
            public void onSuccess(TrailerResponse trailerResponse) {
                view.onTrailersRetreivalSuccess(trailerResponse);
            }

            @Override
            public void onFailure(Throwable throwable) {
                view.onTrailersRetreivalFailure(throwable);
            }
        });
    }
}

