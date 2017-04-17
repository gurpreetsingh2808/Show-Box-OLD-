package com.popular_movies.mvp.presenter;

import com.popular_movies.domain.MovieData;
import com.popular_movies.domain.MovieResponse;

import java.util.List;

/**
 * Created by Gurpreet on 21-01-2017.
 */

public class MoviesPresenter {

    public interface View {
        //  handle response
        void onMoviesRetreivalSuccess(MovieResponse movieResponse);

        //  handle failure
        void onMoviesRetreivalFailure(Throwable throwable);
    }

    interface Presenter {
        void fetchMovies(String movieType, String pageNumber);

        void getSearchResults(String query);
    }
}
