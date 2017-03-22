package com.popular_movies.mvp.presenter;

import com.popular_movies.domain.MovieResponse;

/**
 * Created by Gurpreet on 21-01-2017.
 */

public class TopRatedMoviesPresenter {

    public interface View {
        //  handle response
        void onTopRatedMoviesRetreivalSuccess(MovieResponse movieResponse);

        //  handle failure
        void onTopRatedMoviesRetreivalFailure(Throwable throwable);
    }

    interface Presenter {
        void fetchTopRatedMovies();
    }
}
