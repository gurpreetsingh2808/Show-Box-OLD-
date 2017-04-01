package com.popular_movies.ui.main;

import android.app.Activity;

import com.popular_movies.domain.MovieResponse;
import com.popular_movies.mvp.service.movie.MovieService;
import com.popular_movies.mvp.service.movie.MovieServiceImpl;

/**
 * Created by Gurpreet on 21-01-2017.
 */

public class MainPresenterImpl implements MainPresenter.Presenter {

    private final MainPresenter.View view;
    private final Activity activity;
    private MovieService movieService;

    public MainPresenterImpl(MainPresenter.View view, Activity activity) {
        this.view = view;
        this.activity = activity;
        this.movieService = new MovieServiceImpl();
    }

    @Override
    public void fetchLatestMovies() {
        movieService.getLatestMovies(activity, new MovieService.GetMoviesCallback() {
            @Override
            public void onSuccess(MovieResponse movieResponse) {
                view.onLatestMoviesRetreivalSuccess(movieResponse);
            }

            @Override
            public void onFailure(Throwable throwable) {
                view.onLatestMoviesRetreivalFailure(throwable);
            }
        });
    }

    @Override
    public void fetchNowPlayingMovies() {
        movieService.getNowPlayingMovies(activity, new MovieService.GetMoviesCallback() {
            @Override
            public void onSuccess(MovieResponse movieResponse) {
                view.onNowPlayingMoviesRetreivalSuccess(movieResponse);
            }

            @Override
            public void onFailure(Throwable throwable) {
                view.onNowPlayingMoviesRetreivalFailure(throwable);
            }
        });
    }

    @Override
    public void fetchPopularMovies() {
        movieService.getPopularMovies(activity, new MovieService.GetMoviesCallback() {
            @Override
            public void onSuccess(MovieResponse movieResponse) {
                view.onPopularMoviesRetreivalSuccess(movieResponse);
            }

            @Override
            public void onFailure(Throwable throwable) {
                view.onPopularMoviesRetreivalFailure(throwable);
            }
        });
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

    @Override
    public void fetchUpcomingMovies() {
        movieService.getUpcomingMovies(activity, new MovieService.GetMoviesCallback() {
            @Override
            public void onSuccess(MovieResponse movieResponse) {
                view.onUpcomingMoviesRetreivalSuccess(movieResponse);
            }

            @Override
            public void onFailure(Throwable throwable) {
                view.onUpcomingMoviesRetreivalFailure(throwable);
            }
        });
    }
}
