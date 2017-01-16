package com.popular_movies.ui.activity;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.popular_movies.R;
import com.popular_movies.VolleySingleton;
import com.popular_movies.domain.ReviewData;
import com.popular_movies.JsonParser;
import com.popular_movies.ui.adapter.ReviewsAdapter;
import com.popular_movies.framework.UriBuilder;

import org.json.JSONObject;

import java.util.ArrayList;

public class ReviewActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    ArrayList<ReviewData> reviewDataArrayList = new ArrayList<>();
    private RecyclerView recyclerViewReviews;
    private ReviewsAdapter reviewsAdapter;
    Toolbar toolbar;
    private static final String STATE_REVIEWS = "state_reviews";
    RequestQueue mRequestQueue = VolleySingleton.getInstance().getmRequestQueue();
    private static ReviewActivity instance;
    ProgressBar progressBar;
    SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        //getSupportActionBar().setTitle("User Reviews");
        instance =this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        recyclerViewReviews = (RecyclerView) findViewById(R.id.recyclerReview);
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));

        reviewsAdapter = new ReviewsAdapter(this);
        recyclerViewReviews.setAdapter(reviewsAdapter);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh);
        refreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.progress_colors));
        refreshLayout.setOnRefreshListener(this);
        onRefresh();

        if (savedInstanceState != null) {
            reviewDataArrayList = savedInstanceState.getParcelableArrayList(STATE_REVIEWS);
            reviewsAdapter.setReviewsList(reviewDataArrayList);
        } else {
            UriBuilder uri = new UriBuilder(UriBuilder.BASE_URL + "/" + getIntent().getIntExtra("ID",0) ,
                    UriBuilder.REVIEWS);
            sendReviewsRequest(uri.toString());
        }
    }

    @Override
    public void onRefresh() {
        UriBuilder uri = new UriBuilder(UriBuilder.BASE_URL + "/" + getIntent().getIntExtra("ID",0) ,
                UriBuilder.REVIEWS);
        sendReviewsRequest(uri.toString());
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    void sendReviewsRequest(String url) {

        recyclerViewReviews.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        reviewDataArrayList = JsonParser.parseReviewDetails(response);
                        reviewsAdapter.setReviewsList(reviewDataArrayList);
                        recyclerViewReviews.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                        refreshLayout.setRefreshing(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ReviewActivity.this, "There was an error retreiving the request", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
        mRequestQueue.add(request);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }
}
