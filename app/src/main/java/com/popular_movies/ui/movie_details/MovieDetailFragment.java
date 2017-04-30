package com.popular_movies.ui.movie_details;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.florent37.diagonallayout.DiagonalLayout;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.popular_movies.BuildConfig;
import com.popular_movies.R;
import com.popular_movies.database.MovieProviderHelper;
import com.popular_movies.domain.MovieData;
import com.popular_movies.domain.Review;
import com.popular_movies.domain.ReviewResponse;
import com.popular_movies.domain.Trailer;
import com.popular_movies.domain.TrailerResponse;
import com.popular_movies.util.AppUtils;
import com.popular_movies.util.DateConvert;
import com.popular_movies.framework.ImageLoader;
import com.popular_movies.ui.main.MainActivity;
import com.popular_movies.ui.activity.ReviewActivity;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;

public class MovieDetailFragment extends Fragment implements MovieDetailPresenter.View, ReviewsAdapter.NavigateReviewListener, TrailerAdapter.TrailerClickListener {

    private static final String TAG = MovieDetailFragment.class.getSimpleName();
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

    //  circular progress bar
    @BindView(R.id.indicator)
    CircleIndicator indicator;

    //  recycler view
    @BindView(R.id.rvReviews)
    RecyclerView rvReviews;

    //  progress bar
    @BindView(R.id.pbReviews)
    ProgressBar pbReviews;
    @BindView(R.id.pbTrailers)
    ProgressBar pbTrailers;

    //  favoite icon
    @BindView(R.id.ivFavorite)
    AppCompatImageView ivFavorite;
    //  button
    @BindView(R.id.buttonUserReviews)
    Button btnUserReview;

    @BindView(R.id.diagonalLayout)
    DiagonalLayout diagonalLayout;
    @BindView(R.id.dsvTrailers)
    DiscreteScrollView dsvTrailers;

    private static final String KEY_MOVIE = "KEY_MOVIE";
    MovieData movieData;
    private InterstitialAd mInterstitialAd;
    private View view;
    private ReviewsAdapter reviewsAdapter;
    private LinearLayoutManager layoutManagerReview;

    public static MovieDetailFragment getInstance(Parcelable movie) {
        MovieDetailFragment detailsFragment = new MovieDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_MOVIE, movie);
        //bundle.putParcelable(detailsFragment.getString(R.string.key_movie), movie);
        detailsFragment.setArguments(bundle);
        return detailsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            // getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            //       WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getActivity().getWindow().setStatusBarColor(getResources().getColor(android.R.color.transparent));

        }
*/
        view = inflater.inflate(R.layout.fragment_detailed_view, container, false);
        ButterKnife.bind(this, view);

        initializeAd();
        diagonalLayout.setVisibility(View.VISIBLE);
        movieData = getArguments().getParcelable(getString(R.string.key_movie));
        layoutManagerReview = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        rvReviews.setLayoutManager(layoutManagerReview);

        if (!AppUtils.isTablet(getContext())) {
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
            if (((AppCompatActivity) getActivity()).getSupportActionBar() != null) {
                ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("");
               // ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

        }

        if (movieData != null) {
            title.setText(movieData.getOriginal_title());

            //releaseDate.append(" " + DateConvert.convert(movieData.getRelease_date()));
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
            ////ImageLoader.loadPosterImage(getContext(), movieData.getPoster_path(), toolbarImage, 4);

            ivFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (MovieProviderHelper.getInstance().doesMovieExist(movieData.getId())) {
                        ivFavorite.setImageResource(R.drawable.ic_favorite_border);
                        //  delete movie from database
                        MovieProviderHelper.getInstance().delete(movieData.getId());
                        Snackbar.make(view, getString(R.string.removed) + " " + movieData.getOriginal_title() +
                                " " + getString(R.string.from_favourite), Snackbar.LENGTH_LONG)
                                .show();
                    } else {
                        ivFavorite.setImageResource(R.drawable.ic_favorite_filled);
                        //  add movie to database
                        MovieProviderHelper.getInstance().insert(movieData);
                        Snackbar.make(view, getString(R.string.added) + " " + movieData.getOriginal_title() +
                                " " + getString(R.string.to_favourite), Snackbar.LENGTH_LONG)
                                .show();
                    }
                }
            });

            MovieDetailPresenterImpl movieDetailPresenterImpl = new MovieDetailPresenterImpl(this, getActivity());
            movieDetailPresenterImpl.fetchTrailers(movieData.getId());
            movieDetailPresenterImpl.fetchReviews(movieData.getId());

        }
        if (MovieProviderHelper.getInstance().doesMovieExist(movieData.getId())) {
            ivFavorite.setImageResource(R.drawable.ic_favorite_filled);
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

    private void viewTrailerInYoutube(String trailerKey) {
        if (trailerKey != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(BuildConfig.BASE_URL_TRAILER + trailerKey));
            startActivity(intent);
        } else {
            Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.trailer_error),
                    Snackbar.LENGTH_SHORT).show();
        }
    }


    public TextView getTitle() {
        return title;
    }


    @Override
    public void onReviewsRetreivalSuccess(ReviewResponse reviewResponse) {
        List<Review> reviewList = reviewResponse.getResults();
        if (reviewList != null) {
            SnapHelper snapHelper = new LinearSnapHelper();
            snapHelper.attachToRecyclerView(rvReviews);
            reviewsAdapter = new ReviewsAdapter(getContext(), reviewList, this);
            rvReviews.setAdapter(reviewsAdapter);
        }
        /////////////////////////////////////////////
        pbReviews.setVisibility(View.GONE);
    }

    @Override
    public void onReviewsRetreivalFailure(Throwable throwable) {
        Snackbar.make(getActivity().findViewById(android.R.id.content), getString(R.string.connection_error) , Snackbar.LENGTH_LONG)
                .show();
        pbReviews.setVisibility(View.GONE);
    }

    @Override
    public void onTrailersRetreivalSuccess(TrailerResponse trailerResponse) {
        pbTrailers.setVisibility(View.GONE);
        List<Trailer> listTrailers = new ArrayList<>();
        if(getContext() != null) {
            for (Trailer trailer : trailerResponse.getResults()) {
                if (trailer.getSite().equalsIgnoreCase(getContext().getString(R.string.youtube))) {
                    listTrailers.add(trailer);
                }
            }
            dsvTrailers.setAdapter(new TrailerAdapter(listTrailers, this));
            dsvTrailers.setItemTransformer(new ScaleTransformer.Builder()
                    .setMinScale(0.8f)
                    .build());
        }
    }

    @Override
    public void onTrailersRetreivalFailure(Throwable throwable) {
        pbTrailers.setVisibility(View.GONE);
        Snackbar.make(view, getString(R.string.connection_error), Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void onNextClick(int pos) {
            rvReviews.smoothScrollToPosition(pos + 1);
    }

    @Override
    public void onPreviousClick(int pos) {
        if(pos > 0) {
            rvReviews.smoothScrollToPosition(pos - 1);
        }
    }

    @Override
    public void onTrailerClick(String key) {
        viewTrailerInYoutube(key);
    }
}
