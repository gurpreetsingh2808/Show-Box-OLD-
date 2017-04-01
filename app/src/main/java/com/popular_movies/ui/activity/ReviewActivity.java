package com.popular_movies.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.popular_movies.R;
import com.popular_movies.domain.Review;
import com.popular_movies.domain.ReviewResponse;
import com.popular_movies.ui.moviedetail.ReviewPresenter;
import com.popular_movies.ui.moviedetail.ReviewPresenterImpl;
import com.popular_movies.ui.adapter.ReviewsAdapter;
import com.popular_movies.util.AppUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ReviewActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, ReviewPresenter.View {

    //  toolbar
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    //  recycler view
    @BindView(R.id.recyclerReview)
    RecyclerView recyclerViewReviews;
    //  progress bar
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    //  swipe refresh layout
    @BindView(R.id.refresh)
    SwipeRefreshLayout refreshLayout;

    ArrayList<Review> reviewDataArrayList = new ArrayList<>();
    private ReviewsAdapter reviewsAdapter;
    private ReviewPresenterImpl reviewPresenterImpl;


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtils.initializeCalligraphy();
        setContentView(R.layout.activity_review);
        ButterKnife.bind(this);


        //getSupportActionBar().setTitle("User Reviews");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        reviewPresenterImpl = new ReviewPresenterImpl(this, this);
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));

        refreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.progress_colors));
        refreshLayout.setOnRefreshListener(this);
        onRefresh();

        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList(getString(R.string.state_reviews)) != null) {
            reviewDataArrayList = savedInstanceState.getParcelableArrayList(getString(R.string.state_reviews));
            if (recyclerViewReviews.getAdapter() != null) {
                recyclerViewReviews.swapAdapter(reviewsAdapter, false);
            } else {
                recyclerViewReviews.setAdapter(reviewsAdapter);
            }
        } else {
            reviewPresenterImpl.fetchReviews(getIntent().getIntExtra(getString(R.string.key_movie_id), 0));
        }
    }

    @Override
    public void onRefresh() {
        reviewPresenterImpl.fetchReviews(getIntent().getIntExtra(getString(R.string.key_movie_id), 0));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else
            return super.onOptionsItemSelected(item);
    }

    @Override
    public void onReviewsRetreivalSuccess(ReviewResponse reviewResponse) {
        List<Review> reviewList = reviewResponse.getResults();
        if (reviewList != null)
            recyclerViewReviews.setAdapter(new ReviewsAdapter(ReviewActivity.this, reviewList));
        recyclerViewReviews.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.INVISIBLE);
        refreshLayout.setRefreshing(false);
    }

    @Override
    public void onReviewsRetreivalFailure(Throwable throwable) {
        Snackbar.make(findViewById(android.R.id.content), getString(R.string.connection_error) , Snackbar.LENGTH_LONG)
                .show();
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (reviewDataArrayList != null) {
            outState.putParcelableArrayList(getString(R.string.state_reviews), reviewDataArrayList);
        }
    }
}
