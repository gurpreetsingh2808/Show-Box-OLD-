package com.popular_movies.ui.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.popular_movies.R;
import com.popular_movies.domain.Review;
import com.popular_movies.domain.ReviewResponse;
import com.popular_movies.mvp.presenter.ReviewPresenter;
import com.popular_movies.mvp.presenter.ReviewPresenterImpl;
import com.popular_movies.ui.adapter.ReviewsAdapter;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, ReviewPresenter.View {

    ArrayList<Review> reviewDataArrayList = new ArrayList<>();
    private RecyclerView recyclerViewReviews;
    private ReviewsAdapter reviewsAdapter;
    Toolbar toolbar;
    private static final String STATE_REVIEWS = "state_reviews";
    ProgressBar progressBar;
    SwipeRefreshLayout refreshLayout;
    private ReviewPresenterImpl reviewPresenterImpl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        //getSupportActionBar().setTitle("User Reviews");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        reviewPresenterImpl = new ReviewPresenterImpl(this, this);

        recyclerViewReviews = (RecyclerView) findViewById(R.id.recyclerReview);
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.progress_colors));
        refreshLayout.setOnRefreshListener(this);
        onRefresh();

        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList(STATE_REVIEWS) != null) {
            reviewDataArrayList = savedInstanceState.getParcelableArrayList(STATE_REVIEWS);
            if (recyclerViewReviews.getAdapter() != null) {
                recyclerViewReviews.swapAdapter(reviewsAdapter, false);
            } else {
                recyclerViewReviews.setAdapter(reviewsAdapter);
            }
        } else {
            reviewPresenterImpl.fetchReviews(getIntent().getIntExtra("ID", 0));
        }
    }

    @Override
    public void onRefresh() {
        reviewPresenterImpl.fetchReviews(getIntent().getIntExtra("ID", 0));
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
        Toast.makeText(ReviewActivity.this, "There was an error retreiving the request", Toast.LENGTH_SHORT).show();
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (reviewDataArrayList != null) {
            outState.putParcelableArrayList(STATE_REVIEWS, reviewDataArrayList);
        }
    }
}
