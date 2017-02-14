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
import com.popular_movies.mvp.presenter.MoviesPresenter;
import com.popular_movies.mvp.presenter.MoviesPresenterImpl;
import com.popular_movies.mvp.presenter.PopularMoviesPresenter;
import com.popular_movies.mvp.presenter.PopularMoviesPresenterImpl;
import com.popular_movies.mvp.presenter.TopRatedMoviesPresenter;
import com.popular_movies.mvp.presenter.TopRatedMoviesPresenterImpl;
import com.popular_movies.ui.activity.MainActivity;
import com.popular_movies.ui.adapter.MovieAdapterHorizontal;
import com.popular_movies.ui.adapter.MovieAdapterVertical;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        TopRatedMoviesPresenter.View, PopularMoviesPresenter.View /*, MovieAdapterHorizontal.ClickListener*/{

    public ArrayList<MovieData> movieDataList = new ArrayList<>();
    public RecyclerView rvTopRated;
    public RecyclerView rvPopular;
    public MovieAdapterHorizontal adapterHorizontal;
    private MovieAdapterVertical adapterVertical;
    private ProgressBar pbPopular;
    private ProgressBar pbTopRated;
    // public SwipeRefreshLayout refreshLayout;
    private static final String KEY_TITLE = "KEY_TITLE";
    static String movieType;
    private View view;
    private TopRatedMoviesPresenterImpl topRatedMoviesPresenterImpl;
    private PopularMoviesPresenterImpl popularMoviesPresenterImpl;

    public ListFragment() {

    }

    public static ListFragment getInstance(String title) {
        ListFragment fragment = new ListFragment();
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
        rvTopRated = (RecyclerView) view.findViewById(R.id.rvTopRated);
        rvPopular = (RecyclerView) view.findViewById(R.id.rvPopular);

        if (MainActivity.mIsDualPane) {

            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int width = (int) (metrics.widthPixels / metrics.density);
            //For Tabs
            boolean isTablet = getResources().getBoolean(R.bool.isTablet);
            //width = isTablet ? (width / 2) : width;
            //recyclerView.setLayoutManager(new GridLayoutManager(getContext(), width / 140));
            rvTopRated.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            rvPopular.setLayoutManager(new LinearLayoutManager(getContext()));
        } else {

            rvTopRated.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
            rvPopular.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        rvPopular.setHasFixedSize(true);
        rvPopular.setNestedScrollingEnabled(false);
        rvTopRated.setHasFixedSize(true);
        rvTopRated.setNestedScrollingEnabled(false);

        /*
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.progress_colors));
    */    pbTopRated = (ProgressBar) view.findViewById(R.id.pbTopRated);
          pbPopular = (ProgressBar) view.findViewById(R.id.pbPopular);

        topRatedMoviesPresenterImpl = new TopRatedMoviesPresenterImpl(this, getActivity());
        popularMoviesPresenterImpl = new PopularMoviesPresenterImpl(this, getActivity());
        pbTopRated.setVisibility(View.VISIBLE);
        pbPopular.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList(getString(R.string.key_data)) != null) {
            movieDataList = savedInstanceState.getParcelableArrayList(getString(R.string.key_data));
            pbTopRated.setVisibility(View.GONE);
            setHorizontalAdapter(movieDataList);
        } else {
            popularMoviesPresenterImpl.fetchPopularMovies();
            topRatedMoviesPresenterImpl.fetchTopRatedMovies();
        }
    }

    private void setVerticalAdapter(List<MovieData> listMovies) {
        List<MovieData> movieDataList = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            movieDataList.add(listMovies.get(i));
        }
        adapterVertical = new MovieAdapterVertical(getContext(), movieDataList);
        if (rvPopular.getAdapter() != null) {
            rvPopular.swapAdapter(adapterVertical, false);
        } else {
            rvPopular.setAdapter(adapterVertical);
        }
    }

    private void setHorizontalAdapter(List<MovieData> listMovies) {
        List<MovieData> movieDataList  = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            movieDataList.add(listMovies.get(i));
        }
        adapterHorizontal = new MovieAdapterHorizontal(getContext(), movieDataList);
        if (rvTopRated.getAdapter() != null) {
            rvTopRated.swapAdapter(adapterHorizontal, false);
        } else {
            rvTopRated.setAdapter(adapterHorizontal);
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
        setHorizontalAdapter(movieResponse.getResults());
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
        setVerticalAdapter(movieResponse.getResults());
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
}
