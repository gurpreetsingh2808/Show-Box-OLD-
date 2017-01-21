package com.popular_movies.mvp.presenter;

import com.popular_movies.domain.TrailerResponse;

/**
 * Created by Gurpreet on 21-01-2017.
 */

public class TrailerPresenter {

    public interface View {
        //  handle response
        void onTrailersRetreivalSuccess(TrailerResponse trailerResponse);

        //  handle failure
        void onTrailersRetreivalFailure(Throwable throwable);
    }

    interface Presenter {
        void fetchTrailers(int movieId);
    }
}
