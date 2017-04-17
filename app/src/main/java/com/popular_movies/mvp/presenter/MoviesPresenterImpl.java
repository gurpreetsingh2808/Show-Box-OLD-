package com.popular_movies.mvp.presenter;

import android.app.Activity;

import com.popular_movies.domain.MovieResponse;
import com.popular_movies.mvp.service.movie.MovieService;
import com.popular_movies.mvp.service.movie.MovieServiceImpl;

/**
 * Created by Gurpreet on 21-01-2017.
 */

public class MoviesPresenterImpl implements MoviesPresenter.Presenter {

    private final MoviesPresenter.View view;
    private final Activity activity;
    private MovieService movieService;

    public MoviesPresenterImpl(MoviesPresenter.View view, Activity activity) {
        this.view = view;
        this.activity = activity;
        this.movieService = new MovieServiceImpl();
    }

    @Override
    public void fetchMovies(String movieType, String pageNumber) {
        movieService.getMovies(movieType, pageNumber, activity, new MovieService.GetMoviesCallback() {
            @Override
            public void onSuccess(MovieResponse movieResponse) {
                view.onMoviesRetreivalSuccess(movieResponse);
            }

            @Override
            public void onFailure(Throwable throwable) {
                view.onMoviesRetreivalFailure(throwable);
            }
        });
    }

    @Override
    public void getSearchResults(String query) {
        movieService.getSearchResults(query, activity, new MovieService.GetMoviesCallback() {
            @Override
            public void onSuccess(MovieResponse movieResponse) {
                view.onMoviesRetreivalSuccess(movieResponse);
            }

            @Override
            public void onFailure(Throwable throwable) {
                view.onMoviesRetreivalFailure(throwable);
            }
        });
    }
}
