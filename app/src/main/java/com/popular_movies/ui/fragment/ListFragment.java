package com.popular_movies.ui.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.popular_movies.R;
import com.popular_movies.domain.MovieData;
import com.popular_movies.domain.MovieResponse;
import com.popular_movies.mvp.presenter.MoviesPresenter;
import com.popular_movies.mvp.presenter.MoviesPresenterImpl;
import com.popular_movies.ui.activity.MainActivity;
import com.popular_movies.ui.adapter.MovieAdapter;

import java.util.ArrayList;


public class ListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        MoviesPresenter.View /*, MovieAdapter.ClickListener*/ {

    public ArrayList<MovieData> movieDataList = new ArrayList<>();
    public RecyclerView recyclerView;
    public MovieAdapter adapter;
    public ProgressBar progressBar;
    public SwipeRefreshLayout refreshLayout;
    private MoviesPresenterImpl moviesPresenterImpl;
    private static final String KEY_TITLE = "KEY_TITLE";
    static String movieType;
    private View view;

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
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        if (MainActivity.mIsDualPane) {

            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int width = (int) (metrics.widthPixels / metrics.density);
            //For Tabs
            boolean isTablet = getResources().getBoolean(R.bool.isTablet);
            width = isTablet ? (width / 2) : width;
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), width / 140));
        } else {

            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
            } else
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        }
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.progress_colors));
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);

        moviesPresenterImpl = new MoviesPresenterImpl(this, getActivity());
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList(getString(R.string.key_data)) != null) {
            movieDataList = savedInstanceState.getParcelableArrayList(getString(R.string.key_data));
            progressBar.setVisibility(View.GONE);
            adapter = new MovieAdapter(getContext(), movieDataList);

            if (recyclerView.getAdapter() != null) {
                recyclerView.swapAdapter(adapter, false);
            } else {
                recyclerView.setAdapter(adapter);
            }
        } else {
            moviesPresenterImpl.fetchMovies(movieType);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
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
        moviesPresenterImpl.fetchMovies(movieType);
    }

    @Override
    public void onMoviesRetreivalSuccess(MovieResponse movieResponse) {
        adapter = new MovieAdapter(getActivity(), movieResponse.getResults());
        if (recyclerView.getAdapter() != null) {
            recyclerView.swapAdapter(adapter, false);
        } else {
            recyclerView.setAdapter(adapter);
        }
        refreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMoviesRetreivalFailure(Throwable throwable) {
        Snackbar.make(view, getString(R.string.connection_error) , Snackbar.LENGTH_LONG)
                .show();
        refreshLayout.setRefreshing(false);
        progressBar.setVisibility(View.GONE);
    }

}
