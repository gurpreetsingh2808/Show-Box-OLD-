package com.popular_movies.ui.moviedetail;

import android.app.Activity;

import com.popular_movies.domain.ReviewResponse;
import com.popular_movies.domain.TrailerResponse;
import com.popular_movies.mvp.service.movie.MovieService;
import com.popular_movies.mvp.service.movie.MovieServiceImpl;


/**
 * Created by Gurpreet on 21-01-2017.
 */


public class MovieDetailPresenterImpl implements MovieDetailPresenter.Presenter {

    private final MovieDetailPresenter.View view;
    private final Activity activity;
    private MovieService movieService;

    public MovieDetailPresenterImpl(MovieDetailPresenter.View view, Activity activity) {
        this.view = view;
        this.activity = activity;
        this.movieService = new MovieServiceImpl();
    }

    @Override
    public void fetchReviews(int movieId) {
        movieService.getReviews(movieId, activity, new MovieService.GetReviewsCallback() {
            @Override
            public void onSuccess(ReviewResponse reviewResponse) {
                view.onReviewsRetreivalSuccess(reviewResponse);
            }

            @Override
            public void onFailure(Throwable throwable) {
                view.onReviewsRetreivalFailure(throwable);
            }
        });
    }

    @Override
    public void fetchTrailers(int movieId) {
        movieService.getTrailers(movieId, activity, new MovieService.GetTrailersCallback() {
            @Override
            public void onSuccess(TrailerResponse trailerResponse) {
                view.onTrailersRetreivalSuccess(trailerResponse);
            }

            @Override
            public void onFailure(Throwable throwable) {
                view.onTrailersRetreivalFailure(throwable);
            }
        });
    }
}

