package com.popular_movies.mvp.presenter;

import com.popular_movies.domain.MovieResponse;

/**
 * Created by Gurpreet on 21-01-2017.
 */

public class UpcomingMoviesPresenter {

    public interface View {
        //  handle response
        void onUpcomingMoviesRetreivalSuccess(MovieResponse movieResponse);

        //  handle failure
        void onUpcomingMoviesRetreivalFailure(Throwable throwable);
    }

    interface Presenter {
        void fetchUpcomingMovies();
    }
}
