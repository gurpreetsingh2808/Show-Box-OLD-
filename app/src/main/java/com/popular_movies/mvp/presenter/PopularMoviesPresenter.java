package com.popular_movies.mvp.presenter;

import com.popular_movies.domain.MovieResponse;

/**
 * Created by Gurpreet on 21-01-2017.
 */

public class PopularMoviesPresenter {

    public interface View {
        //  handle response
        void onPopularMoviesRetreivalSuccess(MovieResponse movieResponse);

        //  handle failure
        void onPopularMoviesRetreivalFailure(Throwable throwable);
    }

    interface Presenter {
        void fetchPopularMovies();
    }
}
