package com.popular_movies.ui.moviedetail;

import com.popular_movies.domain.ReviewResponse;

/**
 * Created by Gurpreet on 21-01-2017.
 */

public class ReviewPresenter {

    public interface View {
        //  handle response
        void onReviewsRetreivalSuccess(ReviewResponse reviewResponse);

        //  handle failure
        void onReviewsRetreivalFailure(Throwable throwable);
    }

    interface Presenter {
        void fetchReviews(int movieId);
    }
}
