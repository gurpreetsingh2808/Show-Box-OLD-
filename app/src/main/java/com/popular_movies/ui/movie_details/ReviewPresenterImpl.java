package com.popular_movies.ui.movie_details;

import android.app.Activity;

import com.popular_movies.domain.ReviewResponse;
import com.popular_movies.service.movie.MovieService;
import com.popular_movies.service.movie.MovieServiceImpl;


/**
 * Created by Gurpreet on 21-01-2017.
 */


public class ReviewPresenterImpl implements ReviewPresenter.Presenter {

    private final ReviewPresenter.View view;
    private final Activity activity;
    private MovieService movieService;

    public ReviewPresenterImpl(ReviewPresenter.View view, Activity activity) {
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
}

