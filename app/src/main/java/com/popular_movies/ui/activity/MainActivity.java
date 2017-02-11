package com.popular_movies.ui.activity;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeClipBounds;
import android.transition.Explode;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.popular_movies.BuildConfig;
import com.popular_movies.R;
import com.popular_movies.ui.fragment.FavoritesFragment;
import com.popular_movies.ui.fragment.ListFragment;
import com.popular_movies.util.AppUtils;
import com.yalantis.guillotine.animation.GuillotineAnimation;
import com.yalantis.guillotine.interfaces.GuillotineListener;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static boolean mIsDualPane;
    private Toolbar toolbar;
    private FrameLayout root;
    private View contentHamburger;
    private TextView tvToolbarTitle;


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

        root = (FrameLayout) findViewById(R.id.root);
        contentHamburger = (View) findViewById(R.id.content_hamburger);
        tvToolbarTitle = (TextView) findViewById(R.id.tvToolbarTitleMain);

        View detailView = findViewById(R.id.movie_detail);
        mIsDualPane = detailView != null && detailView.getVisibility() == View.VISIBLE;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content, ListFragment.getInstance(BuildConfig.MOVIE_TYPE_TOP_RATED))
                    .commit();
        }

        if (toolbar != null ) {
            setSupportActionBar(toolbar);
            if(getSupportActionBar() != null) {
                getSupportActionBar().setTitle(null);
            }
        }

        View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.navigation, null);
        root.addView(guillotineMenu);

        GuillotineAnimation guillotineAnimation = new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)
                //.setStartDelay(500)
                .setActionBarViewForAnimation(toolbar)
                .setClosedOnStart(true)
                .setGuillotineListener(new GuillotineListener() {
                    @Override
                    public void onGuillotineOpened() {
                        toolbar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onGuillotineClosed() {
                        toolbar.setVisibility(View.VISIBLE);
                    }
                })
                .build();


        final BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
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


