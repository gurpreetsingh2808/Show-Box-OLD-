package com.popular_movies.ui.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.AutoTransition;
import android.transition.Explode;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;

import com.google.firebase.crash.FirebaseCrash;
import com.popular_movies.ui.fragment.DetailedViewFragment;
import com.popular_movies.R;

public class MovieDetailActivity extends AppCompatActivity {
    public static final String TAG = MovieDetailActivity.class.getSimpleName();
    public static final String KEY_MOVIE = "MOVIE";

    public void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new AutoTransition());
            getWindow().setReturnTransition(new Explode());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupWindowAnimations();
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
              //      WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);

        }
        setContentView(R.layout.activity_movie_detail);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            Log.d("moviedetail", "action bar not null");
            getSupportActionBar().setTitle(DetailedViewFragment.getInstance(getIntent().getParcelableExtra(KEY_MOVIE)).getTitle().toString());
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detailFragment, DetailedViewFragment.getInstance(getIntent().getParcelableExtra(KEY_MOVIE)), "TAG")
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
