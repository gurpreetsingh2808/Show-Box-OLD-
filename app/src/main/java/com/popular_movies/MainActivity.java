package com.popular_movies;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeClipBounds;
import android.transition.Explode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.popular_movies.framework.UriBuilder;

public class MainActivity extends AppCompatActivity {
    private static final String KEY_MENU_ITEM = "MENU_ITEM";
    public static boolean mIsDualPane;
    private int menuitem;

    public void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());
            getWindow().setSharedElementExitTransition(new ChangeClipBounds());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupWindowAnimations();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View detailView = findViewById(R.id.movie_detail);
        mIsDualPane = detailView != null && detailView.getVisibility() == View.VISIBLE;
        if (savedInstanceState != null && savedInstanceState.getInt(KEY_MENU_ITEM) != 0)
            menuitem = savedInstanceState.getInt(KEY_MENU_ITEM);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (savedInstanceState == null)
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.main_content, ListFragment.getInstance(UriBuilder.POPULAR))
                    .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (menuitem != 0) {
            menu.findItem(menuitem).setChecked(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.isChecked())
            return super.onOptionsItemSelected(item);
        else {
            item.setChecked(true);
            getSupportActionBar().setTitle(item.getTitle());

            //if (item.getItemId() == R.id.action_favorites || item.getItemId() == R.id.action_popluar || item.getItemId() == R.id.action_rated)
                menuitem = item.getItemId();
            switch (item.getItemId()) {
                case R.id.action_popular:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_content, ListFragment.getInstance(UriBuilder.POPULAR))
                            .commit();
                    return true;
                case R.id.action_top_rated:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_content, ListFragment.getInstance(UriBuilder.TOP_RATED))
                            .commit();
                    return true;
                case R.id.action_favorite:
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.main_content, new FavoritesFragment())
                            .commit();
                    return true;
                default:
                    return super.onOptionsItemSelected(item);
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_MENU_ITEM, menuitem);
    }
}


