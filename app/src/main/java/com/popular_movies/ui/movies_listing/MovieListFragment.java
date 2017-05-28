package com.popular_movies.ui.movies_listing;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.popular_movies.R;
import com.popular_movies.domain.Genre;
import com.popular_movies.domain.GenreResponse;
import com.popular_movies.domain.MovieData;
import com.popular_movies.domain.MovieResponse;
import com.popular_movies.ui.MovieItemClickListener;
import com.popular_movies.ui.main.MainActivity;
import com.popular_movies.ui.movie_details.MovieDetailActivity;
import com.popular_movies.ui.movie_details.MovieDetailFragment;
import com.popular_movies.util.AppUtils;
import com.popular_movies.util.pagination.EndlessRecyclerOnScrollListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MovieListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        MoviesPresenter.View, MovieItemClickListener
/*, MovieAdapterHorizontal.ClickListener*/
 {

    //  recycler view
    @BindView(R.id.rvMoviesList)
    public RecyclerView rvMoviesList;

    //  progress bar
    @BindView(R.id.pbMoviesList)
    ProgressBar pbMoviesList;

     private String TAG = MovieListFragment.class.getSimpleName();
    public ArrayList<MovieData> movieDataList = new ArrayList<>();
    public MovieListingAdapter adapterHorizontal;
    // public SwipeRefreshLayout refreshLayout;
    private static final String KEY_TITLE = "KEY_TITLE";
    static String movieType;
    private View view;
    private MoviesPresenterImpl moviesPresenterImpl;
     private Integer pageNumber = 1;
     private LinearLayoutManager layoutManager;
     private Boolean loadingInProgress = false;
     private Map<Integer, String> mapGenre = new HashMap<>();

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
        view = inflater.inflate(R.layout.fragment_movies_list, container, false);
        ButterKnife.bind(this, view);
        moviesPresenterImpl = new MoviesPresenterImpl(this, getActivity());
        pbMoviesList.setVisibility(View.VISIBLE);

        layoutManager = new LinearLayoutManager(getContext());
        rvMoviesList.setLayoutManager(layoutManager);
        rvMoviesList.setHasFixedSize(true);
        rvMoviesList.setNestedScrollingEnabled(false);

/*
        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.progress_colors));
    */

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*if (savedInstanceState != null && savedInstanceState.getParcelableArrayList(getString(R.string.key_data)) != null) {
            movieDataList = savedInstanceState.getParcelableArrayList(getString(R.string.key_data));
            pbMoviesList.setVisibility(View.GONE);
            setHorizontalAdapter(movieDataList, rvMoviesList);
        } else {*/
//            moviesPresenterImpl.fetchMovies(movieType, String.valueOf(pageNumber));
       // }
        moviesPresenterImpl.fetchGenres();
        rvMoviesList.addOnScrollListener(new EndlessRecyclerOnScrollListener(layoutManager) {
            @Override
            public void onLoadMore() {
                pageNumber++;
                adapterHorizontal.add(null);
                loadingInProgress = true;
                moviesPresenterImpl.fetchMovies(movieType, String.valueOf(pageNumber));
            }

            @Override
            public Boolean isLoading() {
                // Indicate whether new page loading is in progress or not
                return loadingInProgress;
            }
        });
    }

    private void setHorizontalAdapter(List<MovieData> listMovies, RecyclerView recyclerView) {
        adapterHorizontal = new MovieListingAdapter(getContext(), listMovies);
        adapterHorizontal.setClickListener(this);
        if (recyclerView.getAdapter() != null) {
            recyclerView.swapAdapter(adapterHorizontal, false);
        } else {
            recyclerView.setAdapter(adapterHorizontal);
        }
        adapterHorizontal.setGenre(mapGenre);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rvMoviesList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            rvMoviesList.setLayoutManager(new LinearLayoutManager(getContext()));
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
        pageNumber = 1;
        moviesPresenterImpl.fetchMovies(movieType, String.valueOf(pageNumber));
    }

    @Override
    public void onMoviesRetreivalSuccess(MovieResponse movieResponse) {
        pbMoviesList.setVisibility(View.GONE);
        if(adapterHorizontal == null) {
            setHorizontalAdapter(movieResponse.getResults(), rvMoviesList);
        }
        else {
            loadingInProgress = false;
            adapterHorizontal.remove();
            adapterHorizontal.addAll(movieResponse.getResults());
        }
    }

    @Override
    public void onMoviesRetreivalFailure(Throwable throwable) {
        if(adapterHorizontal == null) {
            pbMoviesList.setVisibility(View.GONE);
        }
        else {
            loadingInProgress = false;
            adapterHorizontal.remove();
        }
        Snackbar.make(view, getString(R.string.connection_error), Snackbar.LENGTH_LONG)
                .show();
    }

     @Override
     public void onGenresRetreivalSuccess(GenreResponse genreResponse) {
         for (Genre genre : genreResponse.getGenres()) {
             mapGenre.put(genre.getId(), genre.getName());
             Log.e(TAG, "genre: id "+genre.getId() + ", name "+genre.getName());
         }
         moviesPresenterImpl.fetchMovies(movieType, String.valueOf(pageNumber));
     }

     @Override
     public void onGenresRetreivalFailure(Throwable throwable) {
         Log.e(TAG, "onGenresRetreivalFailure: ");
         moviesPresenterImpl.fetchMovies(movieType, String.valueOf(pageNumber));
     }

     @Override
     public void itemClicked(View view, int position, MovieData movieData) {
         //if (!AppUtils.isTablet(getContext())) {
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
       /*  }
         else {
             getActivity().getSupportFragmentManager().beginTransaction()
                     .setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top)
                     .replace(R.id.movie_detail, MovieDetailFragment.getInstance(movieData))
                     .commit();
         }*/

     }
 }

