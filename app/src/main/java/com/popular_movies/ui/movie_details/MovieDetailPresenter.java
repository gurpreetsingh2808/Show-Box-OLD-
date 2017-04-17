package com.popular_movies.ui.movie_details;

import com.popular_movies.domain.ReviewResponse;
import com.popular_movies.domain.TrailerResponse;

/**
 * Created by Gurpreet on 21-01-2017.
 */

public class MovieDetailPresenter {

    public interface View {
        void onReviewsRetreivalSuccess(ReviewResponse reviewResponse);
        void onReviewsRetreivalFailure(Throwable throwable);

        void onTrailersRetreivalSuccess(TrailerResponse trailerResponse);
        void onTrailersRetreivalFailure(Throwable throwable);

    }

    interface Presenter {
        void fetchReviews(int movieId);
        void fetchTrailers(int movieId);

    }
}
