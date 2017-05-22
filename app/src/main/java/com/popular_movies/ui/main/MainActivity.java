package com.popular_movies.ui.main;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeClipBounds;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.popular_movies.R;
import com.popular_movies.ui.favourites.FavoritesFragment;
import com.popular_movies.util.AppUtils;
import com.yalantis.guillotine.animation.GuillotineAnimation;
import com.yalantis.guillotine.interfaces.GuillotineListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private GuillotineAnimation guillotineAnimation;
    //  toolbar
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    //  root frame layout
    @BindView(R.id.root)
    FrameLayout root;
    //  view
    @BindView(R.id.content_hamburger)
    View contentHamburger;
    @Nullable
    @BindView(R.id.movie_detail)
    View detailView;
    //  textview for toolbar
    @BindView(R.id.tvToolbarTitleMain)
    TextView tvToolbarTitle;


    public void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Slide());
            getWindow().setExitTransition(new Slide());
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
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content, new MainFragment())
                    .commit();
        }

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(null);
            }
        }

        //  add guillotine menu to rootview
        addMenu();

    }

    /**
     * This method adds navigation menu to rootview
     */
    private void addMenu() {
        View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.navigation, null);
        root.addView(guillotineMenu);

        guillotineAnimation = new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)
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
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        //  to replace the single pane fragment with the dual pane one
        //  TODO : test it properly (cannot call after onsaveinstancestate)
        if (AppUtils.isTablet(this) && newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content, new MainFragment())
                    .commit();
        }
    }

    /**
     * Listener for menu item
     *
     * @param view
     */
    public void onNavigationItemSelected(View view) {
        guillotineAnimation.close();
        switch(view.getId()) {
            case R.id.llMovies :
                setFragment(new MainFragment());
                break;
            case R.id.llFavourites :
                setFragment(new FavoritesFragment());
                break;
        }
    }

    /**
     * This method sets the fragment for selected menu option
     *
     * @param fragment
     */
    private void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.main_content, fragment)
                .commit();
    }
}


