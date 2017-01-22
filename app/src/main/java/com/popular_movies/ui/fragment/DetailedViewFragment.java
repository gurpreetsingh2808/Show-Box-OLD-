package com.popular_movies.ui.fragment;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.popular_movies.R;
import com.popular_movies.database.MovieProviderHelper;
import com.popular_movies.domain.MovieData;
import com.popular_movies.domain.Trailer;
import com.popular_movies.domain.TrailerResponse;
import com.popular_movies.framework.DateConvert;
import com.popular_movies.framework.ImageLoader;
import com.popular_movies.mvp.presenter.TrailerPresenter;
import com.popular_movies.mvp.presenter.TrailerPresenterImpl;
import com.popular_movies.ui.activity.MainActivity;
import com.popular_movies.ui.activity.ReviewActivity;

import me.relex.circleindicator.CircleIndicator;

public class DetailedViewFragment extends Fragment implements TrailerPresenter.View {

    TextView title, releaseDate, synopsis, userRatings;
    ImageView poster;
    CircleIndicator indicator;
    AppBarLayout.OnOffsetChangedListener mListener;
    String trailerKey = null;
    private static final String TAG = DetailedViewFragment.class.getSimpleName();
    MovieData movieData;
    FloatingActionButton favoritesButton;
    public static final String KEY_MOVIE = "movie";
    Button btnUserReview;
    private Cursor cursor;
    private InterstitialAd mInterstitialAd;


    public static DetailedViewFragment getInstance(Parcelable movie) {
        DetailedViewFragment detailsFragment = new DetailedViewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_MOVIE, movie);
        detailsFragment.setArguments(bundle);
        return detailsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_detailed_view, container, false);

        initializeAd();
        movieData = getArguments().getParcelable(KEY_MOVIE);
        if (movieData != null) {
            Log.d("detailedview", "" + movieData.getId());
        }

        if (!MainActivity.mIsDualPane) {
            Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        }

        indicator = (CircleIndicator) view.findViewById(R.id.indicator);

        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);
        mListener = new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d("toolbar", "" + collapsingToolbar.getHeight() + verticalOffset);
                if (!MainActivity.mIsDualPane) {
                    if (collapsingToolbar.getHeight() + verticalOffset < 340) {
                        title.setVisibility(View.GONE);
                        collapsingToolbar.setTitle(movieData.getOriginal_title());
                        releaseDate.setVisibility(View.GONE);
                        userRatings.setVisibility(View.GONE);
                        poster.setVisibility(View.GONE);
                    } else {
                        title.setVisibility(View.VISIBLE);
                        releaseDate.setVisibility(View.VISIBLE);
                        userRatings.setVisibility(View.VISIBLE);
                        poster.setVisibility(View.VISIBLE);
                        collapsingToolbar.setTitle("");
                    }
                } else {
                    if (collapsingToolbar.getHeight() + verticalOffset >= 16) {
                        collapsingToolbar.setContentScrimColor(getResources().getColor(R.color.black));
                    }
                    if (collapsingToolbar.getHeight() + verticalOffset > 134) {
                        title.setVisibility(View.VISIBLE);
                        releaseDate.setVisibility(View.VISIBLE);
                        userRatings.setVisibility(View.VISIBLE);
                        poster.setVisibility(View.VISIBLE);
                        collapsingToolbar.setTitle("");
                    }
                }
            }
        };

        ((AppBarLayout) view.findViewById(R.id.app_bar)).addOnOffsetChangedListener(mListener);

        title = (TextView) view.findViewById(R.id.title);
        title.setText(movieData.getOriginal_title());

        releaseDate = (TextView) view.findViewById(R.id.releaseDate);
        releaseDate.setText(DateConvert.convert(movieData.getRelease_date()));

        synopsis = (TextView) view.findViewById(R.id.synopsis);
        synopsis.setText(movieData.getOverview());

        userRatings = (TextView) view.findViewById(R.id.userRatings);
        userRatings.setText(movieData.getVote_average());

        btnUserReview = (Button) view.findViewById(R.id.buttonUserReviews);
        btnUserReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAd();
            }
        });

        ImageView toolbarImage = (ImageView) view.findViewById(R.id.toolbarImage);
        ImageLoader.loadBackdropImage(getContext(), movieData.getBackdrop_path(), toolbarImage);
        toolbarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trailerKey != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://www.youtube.com/watch?v=" + trailerKey));
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Trailer for this movie is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        poster = (ImageView) view.findViewById(R.id.poster);
        ImageLoader.loadPosterImage(getContext(), movieData.getPoster_path(), poster);


        favoritesButton = (FloatingActionButton) view.findViewById(R.id.fab);
        favoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MovieProviderHelper.getInstance().doesMovieExist(movieData.getId())) {
                    favoritesButton.setImageResource(R.drawable.ic_favorite);
                    //  delete movie from database
                    MovieProviderHelper.getInstance().delete(movieData.getId());
                    Snackbar.make(view, "Removed " + movieData.getOriginal_title() + " from Favorites!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    favoritesButton.setImageResource(R.drawable.ic_favorite_brown_24px);
                    //  add movie to database
                    MovieProviderHelper.getInstance().insert(movieData);
                    Snackbar.make(view, "Added " + movieData.getOriginal_title() + " To Favorites!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        TrailerPresenterImpl trailerPresenterImpl = new TrailerPresenterImpl(this, getActivity());
        trailerPresenterImpl.fetchTrailers(movieData.getId());

        if (MovieProviderHelper.getInstance().doesMovieExist(movieData.getId())) {
            favoritesButton.setImageResource(R.drawable.ic_favorite_brown_24px);
        }
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!mInterstitialAd.isLoaded()) {
            requestNewInterstitial();
        }
    }

    private void initializeAd() {
        //  initialize interstitial ad
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id));
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                moveToReviewActivity();
            }

            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                Log.d(TAG, "onAdLoaded: woohoo! add loaded successfully");
            }
        });
    }


    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                //.addTestDevice("BC6C6B77CEF61830841859B30835E10C")
                .build();

        mInterstitialAd.loadAd(adRequest);
    }


    private void showAd() {
        //  check whether app is loaded or not
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        } else {
            moveToReviewActivity();
        }
    }

    private void moveToReviewActivity() {
        Intent intent = new Intent(getActivity(), ReviewActivity.class);
        intent.putExtra("ID", movieData.getId());
        startActivity(intent);
    }


    public TextView getTitle() {
        return title;
    }


    @Override
    public void onTrailersRetreivalSuccess(TrailerResponse trailerResponse) {
        for (Trailer trailer : trailerResponse.getResults()) {
            if (trailer.getSite().equalsIgnoreCase("YouTube")) {
                trailerKey = trailer.getKey();
                break;
            }
        }
    }

    @Override
    public void onTrailersRetreivalFailure(Throwable throwable) {
        Toast.makeText(getActivity(), "There was an error retreiving the request", Toast.LENGTH_SHORT).show();
    }
}
