package com.popular_movies.mvp.presenter;

import android.app.Activity;

import com.popular_movies.domain.MovieResponse;
import com.popular_movies.mvp.service.movie.MovieService;
import com.popular_movies.mvp.service.movie.MovieServiceImpl;

/**
 * Created by Gurpreet on 21-01-2017.
 */

public class TopRatedMoviesPresenterImpl implements TopRatedMoviesPresenter.Presenter {

    private final TopRatedMoviesPresenter.View view;
    private final Activity activity;
    private MovieService movieService;

    public TopRatedMoviesPresenterImpl(TopRatedMoviesPresenter.View view, Activity activity) {
        this.view = view;
        this.activity = activity;
        this.movieService = new MovieServiceImpl();
    }

    @Override
    public void fetchTopRatedMovies() {
        movieService.getTopRatedMovies(activity, new MovieService.GetMoviesCallback() {
            @Override
            public void onSuccess(MovieResponse movieResponse) {
                view.onTopRatedMoviesRetreivalSuccess(movieResponse);
            }

            @Override
            public void onFailure(Throwable throwable) {
                view.onTopRatedMoviesRetreivalFailure(throwable);
            }
        });
    }
}
