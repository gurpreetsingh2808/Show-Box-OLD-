/*
package com.popular_movies.ui.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.popular_movies.R;
import com.popular_movies.domain.MovieData;
import com.popular_movies.domain.MovieResponse;
import com.popular_movies.mvp.presenter.MoviesPresenter;
import com.popular_movies.mvp.presenter.MoviesPresenterImpl;
import com.popular_movies.ui.adapter.MovieAdapterHorizontal;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MovieListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        MoviesPresenter.View */
/*, MovieAdapterHorizontal.ClickListener*//*
 {

    //  recycler view
    @BindView(R.id.rvMoviesList)
    public RecyclerView rvMoviesList;

    //  progress bar
    @BindView(R.id.pbMoviesList)
    ProgressBar pbMoviesList;

    public ArrayList<MovieData> movieDataList = new ArrayList<>();
    public MovieAdapterHorizontal adapterHorizontal;
    // public SwipeRefreshLayout refreshLayout;
    private static final String KEY_TITLE = "KEY_TITLE";
    static String movieType;
    private View view;
    private MoviesPresenterImpl moviesPresenterImpl;

    public MovieListFragment() {

    }

    public static MovieListFragment getInstance(String title) {
        MovieListFragment fragment = new MovieListFragment();
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
        ButterKnife.bind(this, view);

        rvMoviesList.setLayoutManager(new GridLayoutManager(getContext(), 3));
        rvMoviesList.setHasFixedSize(true);
        rvMoviesList.setNestedScrollingEnabled(false);
        */
/*
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.progress_colors));
    *//*


        moviesPresenterImpl = new MoviesPresenterImpl(this, getActivity());
        pbMoviesList.setVisibility(View.VISIBLE);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList(getString(R.string.key_data)) != null) {
            movieDataList = savedInstanceState.getParcelableArrayList(getString(R.string.key_data));
            pbMoviesList.setVisibility(View.GONE);
            setHorizontalAdapter(movieDataList, rvMoviesList);
        } else {
            moviesPresenterImpl.fetchMovies(movieType);
        }
    }

    private void setHorizontalAdapter(List<MovieData> listMovies, RecyclerView recyclerView) {

        adapterHorizontal = new MovieAdapterHorizontal(getContext(), listMovies);
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
            rvMoviesList.setLayoutManager(new GridLayoutManager(getContext(), 5));
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            rvMoviesList.setLayoutManager(new GridLayoutManager(getContext(), 3));
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
        moviesPresenterImpl.fetchMovies(movieType);
    }

    @Override
    public void onMoviesRetreivalSuccess(MovieResponse movieResponse) {
        pbMoviesList.setVisibility(View.GONE);
        setHorizontalAdapter(movieResponse.getResults(), rvMoviesList);
    }

    @Override
    public void onMoviesRetreivalFailure(Throwable throwable) {
        pbMoviesList.setVisibility(View.GONE);
        Snackbar.make(view, getString(R.string.connection_error), Snackbar.LENGTH_LONG)
                .show();
    }
}
*/
