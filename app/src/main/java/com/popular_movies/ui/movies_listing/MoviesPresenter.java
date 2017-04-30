package com.popular_movies.ui.movies_listing;

import com.popular_movies.domain.Genre;
import com.popular_movies.domain.GenreResponse;
import com.popular_movies.domain.MovieData;
import com.popular_movies.domain.MovieResponse;

import java.util.List;

/**
 * Created by Gurpreet on 21-01-2017.
 */

public class MoviesPresenter {

    public interface View {
        void onMoviesRetreivalSuccess(MovieResponse movieResponse);
        void onMoviesRetreivalFailure(Throwable throwable);
        void onGenresRetreivalSuccess(GenreResponse genreResponse);
        void onGenresRetreivalFailure(Throwable throwable);
    }

    interface Presenter {
        void fetchMovies(String movieType, String pageNumber);
        void fetchGenres();
        void getSearchResults(String query);
    }
}
