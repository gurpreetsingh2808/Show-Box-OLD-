package com.popular_movies.ui.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.AutoTransition;
import android.transition.Explode;
import android.transition.Slide;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.crash.FirebaseCrash;
import com.popular_movies.ui.fragment.DetailedViewFragment;
import com.popular_movies.R;
import com.popular_movies.util.AppUtils;

import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MovieDetailActivity extends AppCompatActivity {
    public static final String TAG = MovieDetailActivity.class.getSimpleName();


    public void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            //getWindow().setEnterTransition(new AutoTransition());
            //getWindow().setReturnTransition(new Explode());

            getWindow().setEnterTransition(new Slide());
            getWindow().setExitTransition(new Slide());
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupWindowAnimations();
        super.onCreate(savedInstanceState);
        AppUtils.initializeCalligraphy();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            //       WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.pureBlack));

        }

        setContentView(R.layout.activity_movie_detail);
        ButterKnife.bind(this);

        if(getSupportActionBar() != null) {
            Log.d("moviedetail", "action bar not null");
            getSupportActionBar().setTitle(DetailedViewFragment.getInstance(getIntent().
                    getParcelableExtra(getString(R.string.key_movie))).getTitle().toString());
        }

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detailFragment, DetailedViewFragment.getInstance(getIntent()
                            .getParcelableExtra(getString(R.string.key_movie))), getString(R.string.tag))
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
