package com.popular_movies.ui.movies_listing;

import android.app.Activity;

import com.popular_movies.domain.Genre;
import com.popular_movies.domain.GenreResponse;
import com.popular_movies.domain.MovieResponse;
import com.popular_movies.service.movie.MovieService;
import com.popular_movies.service.movie.MovieServiceImpl;

import java.util.List;

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
    public void fetchGenres() {
        movieService.getGenre(activity, new MovieService.FetchGenresCallback() {
            @Override
            public void onSuccess(GenreResponse genreResponse) {
                view.onGenresRetreivalSuccess(genreResponse);
            }

            @Override
            public void onFailure(Throwable throwable) {
                view.onGenresRetreivalFailure(throwable);
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
