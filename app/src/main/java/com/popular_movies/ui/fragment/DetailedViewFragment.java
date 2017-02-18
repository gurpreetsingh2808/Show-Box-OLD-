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
import com.popular_movies.BuildConfig;
import com.popular_movies.R;
import com.popular_movies.database.MovieProviderHelper;
import com.popular_movies.domain.MovieData;
import com.popular_movies.domain.Trailer;
import com.popular_movies.domain.TrailerResponse;
import com.popular_movies.util.DateConvert;
import com.popular_movies.framework.ImageLoader;
import com.popular_movies.mvp.presenter.TrailerPresenter;
import com.popular_movies.mvp.presenter.TrailerPresenterImpl;
import com.popular_movies.ui.activity.MainActivity;
import com.popular_movies.ui.activity.ReviewActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

public class DetailedViewFragment extends Fragment implements TrailerPresenter.View {

    private static final String TAG = DetailedViewFragment.class.getSimpleName();
    //  toolbar
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    //  textview
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.releaseDate)
    TextView releaseDate;
    @BindView(R.id.synopsis)
    TextView synopsis;
    @BindView(R.id.userRatings)
    TextView userRatings;

    //  image view
    @BindView(R.id.toolbarImage)
    ImageView toolbarImage;
    @BindView(R.id.poster)
    ImageView poster;

    //  collapsing toolbar layout
    @BindView(R.id.toolbar_layout)
    CollapsingToolbarLayout collapsingToolbar;
    //  appbar layout
    @BindView(R.id.app_bar)
    AppBarLayout appBarLayout;

    //  circular progress bar
    @BindView(R.id.indicator)
    CircleIndicator indicator;

    //  fab
    @BindView(R.id.fab)
    FloatingActionButton favoritesButton;
    //  button
    @BindView(R.id.buttonUserReviews)
    Button btnUserReview;

    private static final String KEY_MOVIE = "KEY_MOVIE";
    AppBarLayout.OnOffsetChangedListener mListener;
    String trailerKey = null;
    MovieData movieData;
    private Cursor cursor;
    private InterstitialAd mInterstitialAd;
    private View view;

    public static DetailedViewFragment getInstance(Parcelable movie) {
        DetailedViewFragment detailsFragment = new DetailedViewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_MOVIE, movie);
        //bundle.putParcelable(detailsFragment.getString(R.string.key_movie), movie);
        detailsFragment.setArguments(bundle);
        return detailsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_detailed_view, container, false);
        ButterKnife.bind(this, view);

        initializeAd();
        movieData = getArguments().getParcelable(getString(R.string.key_movie));
        if (!MainActivity.mIsDualPane) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        }

        mListener = new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d(TAG, "" + collapsingToolbar.getHeight() + verticalOffset);
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

        appBarLayout.addOnOffsetChangedListener(mListener);

        title.setText(movieData.getOriginal_title());
        releaseDate.setText(DateConvert.convert(movieData.getRelease_date()));
        synopsis.setText(movieData.getOverview());
        userRatings.setText(movieData.getVote_average());
        btnUserReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAd();
            }
        });

        ImageLoader.loadBackdropImage(getContext(), movieData.getBackdrop_path(), toolbarImage);
        toolbarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trailerKey != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(BuildConfig.BASE_URL_TRAILER + trailerKey));
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.trailer_error), Toast.LENGTH_SHORT).show();
                }
            }
        });

        ImageLoader.loadPosterImage(getContext(), movieData.getPoster_path(), poster);
        favoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MovieProviderHelper.getInstance().doesMovieExist(movieData.getId())) {
                    favoritesButton.setImageResource(R.drawable.ic_favorite);
                    //  delete movie from database
                    MovieProviderHelper.getInstance().delete(movieData.getId());
                    Snackbar.make(view, getString(R.string.removed) + " " + movieData.getOriginal_title() +
                            " " + getString(R.string.from_favourite), Snackbar.LENGTH_LONG)
                            .show();
                } else {
                    favoritesButton.setImageResource(R.drawable.ic_favorite_brown_24px);
                    //  add movie to database
                    MovieProviderHelper.getInstance().insert(movieData);
                    Snackbar.make(view, getString(R.string.added) + " " + movieData.getOriginal_title() +
                            " " + getString(R.string.to_favourite), Snackbar.LENGTH_LONG)
                            .show();
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
        if (!mInterstitialAd.isLoaded()) {
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
        intent.putExtra(getString(R.string.key_movie_id), movieData.getId());
        startActivity(intent);
    }


    public TextView getTitle() {
        return title;
    }


    @Override
    public void onTrailersRetreivalSuccess(TrailerResponse trailerResponse) {
        for (Trailer trailer : trailerResponse.getResults()) {
            if (trailer.getSite().equalsIgnoreCase(getString(R.string.youtube))) {
                trailerKey = trailer.getKey();
                break;
            }
        }
    }

    @Override
    public void onTrailersRetreivalFailure(Throwable throwable) {
        Snackbar.make(view, getString(R.string.connection_error), Snackbar.LENGTH_LONG)
                .show();
    }
}
