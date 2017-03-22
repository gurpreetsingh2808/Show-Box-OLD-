package com.popular_movies.mvp.presenter;

import com.popular_movies.domain.MovieResponse;

/**
 * Created by Gurpreet on 21-01-2017.
 */

public class LatestMoviesPresenter {

    public interface View {
        //  handle response
        void onLatestMoviesRetreivalSuccess(MovieResponse movieResponse);

        //  handle failure
        void onLatestMoviesRetreivalFailure(Throwable throwable);
    }

    interface Presenter {
        void fetchLatestMovies();
    }
}
