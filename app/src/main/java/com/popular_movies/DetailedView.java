package com.popular_movies;

import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.AutoTransition;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailedView extends AppCompatActivity {

    TextView title, releaseDate, synopsis, userRatings;
    ImageView poster;
    Toolbar toolbar;
    AppBarLayout.OnOffsetChangedListener mListener;

    public void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new AutoTransition());
//            getWindow().setReturnTransition(new Explode());
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupWindowAnimations();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        mListener = new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d("toolbar",""+collapsingToolbar.getHeight() + verticalOffset);
                Log.d("toolbar",""+2 * ViewCompat.getMinimumHeight(collapsingToolbar));
                if(collapsingToolbar.getHeight() + verticalOffset < 340) {
                    title.setVisibility(View.GONE);
                    releaseDate.setVisibility(View.GONE);
                    userRatings.setVisibility(View.GONE);
                    poster.setVisibility(View.GONE);
                    getSupportActionBar().setTitle(getIntent().getStringExtra("TITLE"));
                    getSupportActionBar().setDisplayShowTitleEnabled(true);
                    Log.d("toolbar","works");
                } else {
                    title.setVisibility(View.VISIBLE);
                    releaseDate.setVisibility(View.VISIBLE);
                    userRatings.setVisibility(View.VISIBLE);
                    poster.setVisibility(View.VISIBLE);
                    collapsingToolbar.setTitle("");
                }
            }
        };

        ((AppBarLayout)findViewById(R.id.app_bar)).addOnOffsetChangedListener(mListener);


        title = (TextView)findViewById(R.id.title);
        title.setText(getIntent().getStringExtra("TITLE"));

        releaseDate = (TextView)findViewById(R.id.releaseDate);
        releaseDate.setText(getIntent().getStringExtra("RELEASE_DATE"));

        synopsis = (TextView)findViewById(R.id.synopsis);
        synopsis.setText(getIntent().getStringExtra("DESCRIPTION"));

        userRatings = (TextView)findViewById(R.id.userRatings);
        userRatings.setText(getIntent().getStringExtra("USER_RATINGS"));

        ImageView toolbarImage = (ImageView)findViewById(R.id.toolbarImage);
        Picasso.with(DetailedView.this).load(getIntent().getStringExtra("WIDE_THUMBNAIL_URL")).into(toolbarImage);

        poster = (ImageView)findViewById(R.id.poster);
        Picasso.with(DetailedView.this).load(getIntent().getStringExtra("THUMBNAIL_URL"))
                .placeholder(R.drawable.no_img_preview).into(poster);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

    }

}
