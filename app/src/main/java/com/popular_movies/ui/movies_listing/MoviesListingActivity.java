package com.popular_movies.ui.movies_listing;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.popular_movies.R;
import com.popular_movies.ui.main.MainFragment;
import com.popular_movies.util.AppUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MoviesListingActivity extends AppCompatActivity {

    //  toolbar
    @BindView(R.id.MovieListing_ToolBar)
    Toolbar toolbar;

    @BindView(R.id.tvToolbarTitleMoviesListing)
    TextView tvToolbarTitle;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUtils.initializeCalligraphy();
        setContentView(R.layout.activity_movies_listing);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        //  type MUAT BE PASSED THROUGH INTENT
        if (savedInstanceState == null) {
            if(getIntent() != null && getIntent().hasExtra(getString(R.string.key_title))) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movies_listing, MovieListFragment.getInstance(getIntent().getStringExtra(getString(R.string.key_movie_type))))
                        .commit();
                tvToolbarTitle.setText(getIntent().getStringExtra(getString(R.string.key_title)));
            }
        }
    }

    @OnClick(R.id.ivBack)
    public void goBack() {
        finish();
    }
}
