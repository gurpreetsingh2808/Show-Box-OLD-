package com.popular_movies.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeClipBounds;
import android.transition.Explode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.popular_movies.BuildConfig;
import com.popular_movies.R;
import com.popular_movies.ui.fragment.FavoritesFragment;
import com.popular_movies.ui.fragment.ListFragment;
import com.popular_movies.util.AppUtils;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity  {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static boolean mIsDualPane;
    Toolbar toolbar;

    public void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());
            getWindow().setSharedElementExitTransition(new ChangeClipBounds());
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
        setContentView(R.layout.activity_main);

        View detailView = findViewById(R.id.movie_detail);
        mIsDualPane = detailView != null && detailView.getVisibility() == View.VISIBLE;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content, ListFragment.getInstance(BuildConfig.MOVIE_TYPE_TOP_RATED))
                    .commit();
        }


        final BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        toolbar.setTitle(item.getTitle());
                        switch (item.getItemId()) {
                            case R.id.action_popular:
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.main_content, ListFragment.getInstance(BuildConfig.MOVIE_TYPE_POPULAR))
                                        .commit();
                                return true;

                            case R.id.action_top_rated:
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.main_content, ListFragment.getInstance(BuildConfig.MOVIE_TYPE_TOP_RATED))
                                        .commit();
                                return true;

                            case R.id.action_favorite:
                                getSupportFragmentManager().beginTransaction()
                                        .replace(R.id.main_content, new FavoritesFragment())
                                        .commit();
                                return true;

                        }
                        return true;
                    }
                });
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}


