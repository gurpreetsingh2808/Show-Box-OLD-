package com.popular_movies.mvp.presenter;

import com.popular_movies.domain.MovieResponse;

/**
 * Created by Gurpreet on 21-01-2017.
 */

public class NowPlayingMoviesPresenter {

    public interface View {
        //  handle response
        void onNowPlayingMoviesRetreivalSuccess(MovieResponse movieResponse);

        //  handle failure
        void onNowPlayingMoviesRetreivalFailure(Throwable throwable);
    }

    interface Presenter {
        void fetchNowPlayingMovies();
    }
}
