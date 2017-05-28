package com.popular_movies.ui.main;

import com.popular_movies.domain.MovieResponse;

/**
 * Created by Gurpreet on 21-01-2017.
 */

public class MainPresenter {

    public interface View {
        void onLatestMoviesRetreivalSuccess(MovieResponse movieResponse);
        void onLatestMoviesRetreivalFailure(Throwable throwable);

        void onNowPlayingMoviesRetreivalSuccess(MovieResponse movieResponse);
        void onNowPlayingMoviesRetreivalFailure(Throwable throwable);

        void onPopularMoviesRetreivalSuccess(MovieResponse movieResponse);
        void onPopularMoviesRetreivalFailure(Throwable throwable);

        void onTopRatedMoviesRetreivalSuccess(MovieResponse movieResponse);
        void onTopRatedMoviesRetreivalFailure(Throwable throwable);

        void onUpcomingMoviesRetreivalSuccess(MovieResponse movieResponse);
        void onUpcomingMoviesRetreivalFailure(Throwable throwable);
    }

    interface Presenter {
        void fetchLatestMovies();
        void fetchNowPlayingMovies();
        void fetchPopularMovies();
        void fetchTopRatedMovies();
        void fetchUpcomingMovies();

    }
}
