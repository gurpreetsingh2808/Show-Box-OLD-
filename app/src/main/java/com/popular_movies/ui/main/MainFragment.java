package com.popular_movies.ui.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.popular_movies.R;
import com.popular_movies.domain.MovieData;
import com.popular_movies.domain.MovieResponse;
import com.popular_movies.ui.MovieItemClickListener;
import com.popular_movies.ui.movie_details.MovieDetailActivity;
import com.popular_movies.ui.movie_details.MovieDetailFragment;
import com.popular_movies.ui.movies_listing.MoviesListingActivity;
import com.popular_movies.util.AppUtils;
import com.popular_movies.util.constants.IntentKeys;
import com.popular_movies.util.constants.TitleKeyValues;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        MainPresenter.View, View.OnClickListener,  MovieItemClickListener  {

    //  recycler view
    @BindView(R.id.rvTopRated)
    public RecyclerView rvTopRated;
    @BindView(R.id.rvPopular)
    public RecyclerView rvPopular;
    @BindView(R.id.rvNowPlaying)
    public RecyclerView rvNowPlaying;
    @BindView(R.id.rvUpcoming)
    public RecyclerView rvUpcoming;

    //  progress bar
    @BindView(R.id.pbPopular)
    ProgressBar pbPopular;
    @BindView(R.id.pbTopRated)
    ProgressBar pbTopRated;
    @BindView(R.id.pbNowPlaying)
    ProgressBar pbNowPlaying;
    @BindView(R.id.pbUpcoming)
    ProgressBar pbUpcoming;

    @BindView(R.id.tvNowPlayingViewAll)
    TextView tvNowPlayingViewAll;
    @BindView(R.id.tvUpcomingViewAll)
    TextView tvUpcomingViewAll;
    @BindView(R.id.tvTopRatedViewAll)
    TextView tvTopRatedViewAll;
    @BindView(R.id.tvPopularViewAll)
    TextView tvPopularViewAll;

    public ArrayList<MovieData> movieDataList = new ArrayList<>();
    public MovieAdapterHorizontal adapterHorizontal;
    private MovieAdapterVertical adapterVertical;
    // public SwipeRefreshLayout refreshLayout;
    private static final String KEY_TITLE = "KEY_TITLE";
    static String movieType;
    private View view;
    private MainPresenterImpl mainPresenterImpl;

    public MainFragment() {

    }

    public static MainFragment getInstance(String title) {
        MainFragment fragment = new MainFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        //bundle.putString(fragment.getActivity().getString(R.string.key_title), title);
        fragment.setArguments(bundle);
        movieType = title;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.layout_main, container, false);
        ButterKnife.bind(this, view);

            rvPopular.setLayoutManager(new LinearLayoutManager(getContext()));
            rvUpcoming.setLayoutManager(new LinearLayoutManager(getContext()));
        rvTopRated.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        rvNowPlaying.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        rvPopular.setHasFixedSize(true);
        rvPopular.setNestedScrollingEnabled(false);
        rvTopRated.setHasFixedSize(true);
        rvTopRated.setNestedScrollingEnabled(false);
        rvNowPlaying.setHasFixedSize(true);
        rvNowPlaying.setNestedScrollingEnabled(false);
        rvUpcoming.setHasFixedSize(true);
        rvUpcoming.setNestedScrollingEnabled(false);
        /*
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.progress_colors));
    */

        mainPresenterImpl = new MainPresenterImpl(this, getActivity());

        pbTopRated.setVisibility(View.VISIBLE);
        pbPopular.setVisibility(View.VISIBLE);
        pbNowPlaying.setVisibility(View.VISIBLE);
        pbUpcoming.setVisibility(View.VISIBLE);

        tvNowPlayingViewAll.setOnClickListener(this);
        tvTopRatedViewAll.setOnClickListener(this);
        tvUpcomingViewAll.setOnClickListener(this);
        tvPopularViewAll.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList(getString(R.string.key_data)) != null) {
            movieDataList = savedInstanceState.getParcelableArrayList(getString(R.string.key_data));
            pbTopRated.setVisibility(View.GONE);
            setHorizontalAdapter(movieDataList, rvTopRated);
        } else {
            mainPresenterImpl.fetchNowPlayingMovies();
            mainPresenterImpl.fetchUpcomingMovies();
            mainPresenterImpl.fetchTopRatedMovies();
            mainPresenterImpl.fetchPopularMovies();
            //mainPresenterImpl.fetchLatestMovies();
        }
    }

    private void setVerticalAdapter(List<MovieData> listMovies, RecyclerView recyclerView) {
        List<MovieData> movieDataList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            movieDataList.add(listMovies.get(i));
        }
        adapterVertical = new MovieAdapterVertical(getContext(), movieDataList);
        adapterVertical.setClickListener(this);
        if (recyclerView.getAdapter() != null) {
            recyclerView.swapAdapter(adapterVertical, false);
        } else {
            recyclerView.setAdapter(adapterVertical);
        }
    }

    private void setHorizontalAdapter(List<MovieData> listMovies, RecyclerView recyclerView) {
        List<MovieData> movieDataList = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            movieDataList.add(listMovies.get(i));
        }
        adapterHorizontal = new MovieAdapterHorizontal(getContext(), movieDataList);
        adapterHorizontal.setClickListener(this);
        if (recyclerView.getAdapter() != null) {
            recyclerView.swapAdapter(adapterHorizontal, false);
        } else {
            recyclerView.setAdapter(adapterHorizontal);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
            if (AppUtils.isTablet(getContext()) && newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                rvPopular.setLayoutManager(new GridLayoutManager(getContext(), 2));
                rvUpcoming.setLayoutManager(new GridLayoutManager(getContext(), 2));
            } else {
                rvPopular.setLayoutManager(new LinearLayoutManager(getContext()));
                rvUpcoming.setLayoutManager(new LinearLayoutManager(getContext()));
            }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (movieDataList != null) {
            outState.putParcelableArrayList(getString(R.string.key_data), movieDataList);
        }
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void onTopRatedMoviesRetreivalSuccess(MovieResponse movieResponse) {
        pbTopRated.setVisibility(View.GONE);
        setHorizontalAdapter(movieResponse.getResults(), rvTopRated);
    }

    @Override
    public void onTopRatedMoviesRetreivalFailure(Throwable throwable) {
        pbTopRated.setVisibility(View.GONE);
        Snackbar.make(view, getString(R.string.connection_error), Snackbar.LENGTH_LONG)
                .show();
        //progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onPopularMoviesRetreivalSuccess(MovieResponse movieResponse) {
        setVerticalAdapter(movieResponse.getResults(), rvPopular);
        ////////// refreshLayout.setRefreshing(false);
        pbPopular.setVisibility(View.GONE);
    }

    @Override
    public void onPopularMoviesRetreivalFailure(Throwable throwable) {
        Snackbar.make(view, getString(R.string.connection_error), Snackbar.LENGTH_LONG)
                .show();
        //////////// refreshLayout.setRefreshing(false);
        pbPopular.setVisibility(View.GONE);
    }

    @Override
    public void onUpcomingMoviesRetreivalSuccess(MovieResponse movieResponse) {
        setVerticalAdapter(movieResponse.getResults(), rvUpcoming);
        pbUpcoming.setVisibility(View.GONE);
    }

    @Override
    public void onUpcomingMoviesRetreivalFailure(Throwable throwable) {
        pbUpcoming.setVisibility(View.GONE);
    }

    @Override
    public void onLatestMoviesRetreivalSuccess(MovieResponse movieResponse) {

    }

    @Override
    public void onLatestMoviesRetreivalFailure(Throwable throwable) {

    }

    @Override
    public void onNowPlayingMoviesRetreivalSuccess(MovieResponse movieResponse) {
        pbNowPlaying.setVisibility(View.GONE);
        setHorizontalAdapter(movieResponse.getResults(), rvNowPlaying);
    }

    @Override
    public void onNowPlayingMoviesRetreivalFailure(Throwable throwable) {
        pbNowPlaying.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvNowPlayingViewAll:
                openMoviesListing(IntentKeys.KEY_NOW_PLAYING, TitleKeyValues.now_playing);
                break;
            case R.id.tvUpcomingViewAll:
                openMoviesListing(IntentKeys.KEY_UPCOMING, TitleKeyValues.upcoming);
                break;
            case R.id.tvTopRatedViewAll:
                openMoviesListing(IntentKeys.KEY_TOP_RATED, TitleKeyValues.top_rated);
                break;
            case R.id.tvPopularViewAll:
                openMoviesListing(IntentKeys.KEY_POPULAR, TitleKeyValues.popular);
                break;
        }
    }

    private void openMoviesListing(String movieType, String title) {
        Intent intent = new Intent(getActivity(), MoviesListingActivity.class);
        intent.putExtra(getString(R.string.key_movie_type), movieType);
        intent.putExtra(getString(R.string.key_title), title);
        startActivity(intent);
    }


    @Override
    public void itemClicked(View view, int position, MovieData movieData) {
        if (AppUtils.isTablet(getContext()) && AppUtils.isLandscape(getContext())) {
            getActivity().getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top)
                    .replace(R.id.movie_detail, MovieDetailFragment.getInstance(movieData))
                    .commit();
        }
        else {
            Intent intent = new Intent(getActivity(), MovieDetailActivity.class);
            intent.putExtra(getString(R.string.key_movie), movieData);
                        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            AppBarLayout barLayout = (AppBarLayout) ((AppCompatActivity) context).findViewById(R.id.actionbar);
                            ActivityOptions compat = ActivityOptions.makeSceneTransitionAnimation((AppCompatActivity) context,
                                    Pair.create((View) thumbnail, context.getString(R.string.transition_name)),
                                    Pair.create((View) barLayout, context.getString(R.string.transition_name_action_bar)));
                            context.startActivity(intent, compat.toBundle());
                        } else*/
            startActivity(intent);
        }

    }
}
