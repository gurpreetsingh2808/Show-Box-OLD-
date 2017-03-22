package com.popular_movies.ui.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.popular_movies.R;
import com.popular_movies.domain.MovieData;
import com.popular_movies.domain.MovieResponse;
import com.popular_movies.mvp.presenter.NowPlayingMoviesPresenter;
import com.popular_movies.mvp.presenter.NowPlayingMoviesPresenterImpl;
import com.popular_movies.mvp.presenter.PopularMoviesPresenter;
import com.popular_movies.mvp.presenter.PopularMoviesPresenterImpl;
import com.popular_movies.mvp.presenter.TopRatedMoviesPresenter;
import com.popular_movies.mvp.presenter.TopRatedMoviesPresenterImpl;
import com.popular_movies.mvp.presenter.UpcomingMoviesPresenter;
import com.popular_movies.mvp.presenter.UpcomingMoviesPresenterImpl;
import com.popular_movies.ui.activity.MainActivity;
import com.popular_movies.ui.adapter.MovieAdapterHorizontal;
import com.popular_movies.ui.adapter.MovieAdapterVertical;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class HomeFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        TopRatedMoviesPresenter.View, PopularMoviesPresenter.View, UpcomingMoviesPresenter.View, NowPlayingMoviesPresenter.View /*, MovieAdapterHorizontal.ClickListener*/{

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

    public ArrayList<MovieData> movieDataList = new ArrayList<>();
    public MovieAdapterHorizontal adapterHorizontal;
    private MovieAdapterVertical adapterVertical;
    // public SwipeRefreshLayout refreshLayout;
    private static final String KEY_TITLE = "KEY_TITLE";
    static String movieType;
    private View view;
    private TopRatedMoviesPresenterImpl topRatedMoviesPresenterImpl;
    private PopularMoviesPresenterImpl popularMoviesPresenterImpl;
    private NowPlayingMoviesPresenterImpl nowPlayingMoviesPresenterImpl;
    private UpcomingMoviesPresenterImpl upcomingMoviesPresenterImpl;

    public HomeFragment() {

    }

    public static HomeFragment getInstance(String title) {
        HomeFragment fragment = new HomeFragment();
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
        view = inflater.inflate(R.layout.list_layout, container, false);
        ButterKnife.bind(this,view);

        if (MainActivity.mIsDualPane) {

            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int width = (int) (metrics.widthPixels / metrics.density);
            //For Tabs
            boolean isTablet = getResources().getBoolean(R.bool.isTablet);
            //width = isTablet ? (width / 2) : width;
            //recyclerView.setLayoutManager(new GridLayoutManager(getContext(), width / 140));
            rvTopRated.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            rvPopular.setLayoutManager(new LinearLayoutManager(getContext()));
            rvNowPlaying.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            rvUpcoming.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {

            rvTopRated.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            rvPopular.setLayoutManager(new LinearLayoutManager(getContext()));
            rvNowPlaying.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            rvUpcoming.setLayoutManager(new LinearLayoutManager(getContext()));
        }
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

        topRatedMoviesPresenterImpl = new TopRatedMoviesPresenterImpl(this, getActivity());
        popularMoviesPresenterImpl = new PopularMoviesPresenterImpl(this, getActivity());
        nowPlayingMoviesPresenterImpl = new NowPlayingMoviesPresenterImpl(this, getActivity());
        upcomingMoviesPresenterImpl = new UpcomingMoviesPresenterImpl(this, getActivity());

        pbTopRated.setVisibility(View.VISIBLE);
        pbPopular.setVisibility(View.VISIBLE);
        pbNowPlaying.setVisibility(View.VISIBLE);
        pbUpcoming.setVisibility(View.VISIBLE);

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
            nowPlayingMoviesPresenterImpl.fetchNowPlayingMovies();
            upcomingMoviesPresenterImpl.fetchUpcomingMovies();
            popularMoviesPresenterImpl.fetchPopularMovies();
            topRatedMoviesPresenterImpl.fetchTopRatedMovies();
        }
    }

    private void setVerticalAdapter(List<MovieData> listMovies, RecyclerView recyclerView) {
        List<MovieData> movieDataList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            movieDataList.add(listMovies.get(i));
        }
        adapterVertical = new MovieAdapterVertical(getContext(), movieDataList);
        if (recyclerView.getAdapter() != null) {
            recyclerView.swapAdapter(adapterVertical, false);
        } else {
            recyclerView.setAdapter(adapterVertical);
        }
    }

    private void setHorizontalAdapter(List<MovieData> listMovies, RecyclerView recyclerView) {
        List<MovieData> movieDataList  = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            movieDataList.add(listMovies.get(i));
        }
        adapterHorizontal = new MovieAdapterHorizontal(getContext(), movieDataList);
        if (recyclerView.getAdapter() != null) {
            recyclerView.swapAdapter(adapterHorizontal, false);
        } else {
            recyclerView.setAdapter(adapterHorizontal);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
         //   recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
         //   recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (movieDataList != null) {
            outState.putParcelableArrayList(getString(R.string.key_data),  movieDataList);
        }
    }

    @Override
    public void onRefresh() {
        popularMoviesPresenterImpl.fetchPopularMovies();
        topRatedMoviesPresenterImpl.fetchTopRatedMovies();
        pbPopular.setVisibility(View.VISIBLE);
    }

    @Override
    public void onTopRatedMoviesRetreivalSuccess(MovieResponse movieResponse) {
        pbTopRated.setVisibility(View.GONE);
        setHorizontalAdapter(movieResponse.getResults(), rvTopRated);
    }

    @Override
    public void onTopRatedMoviesRetreivalFailure(Throwable throwable) {
        pbTopRated.setVisibility(View.GONE);
        Snackbar.make(view, getString(R.string.connection_error) , Snackbar.LENGTH_LONG)
                .show();
        //progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onPopularMoviesRetreivalSuccess(MovieResponse movieResponse) {
        setVerticalAdapter(movieResponse.getResults(),rvPopular);
        ////////// refreshLayout.setRefreshing(false);
        pbPopular.setVisibility(View.GONE);
    }

    @Override
    public void onPopularMoviesRetreivalFailure(Throwable throwable) {
        Snackbar.make(view, getString(R.string.connection_error) , Snackbar.LENGTH_LONG)
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
    public void onNowPlayingMoviesRetreivalSuccess(MovieResponse movieResponse) {
        pbNowPlaying.setVisibility(View.GONE);
        setHorizontalAdapter(movieResponse.getResults(), rvNowPlaying);
    }

    @Override
    public void onNowPlayingMoviesRetreivalFailure(Throwable throwable) {
        pbNowPlaying.setVisibility(View.GONE);
    }
}
