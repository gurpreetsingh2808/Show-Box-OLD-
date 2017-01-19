package com.popular_movies.ui.fragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.popular_movies.ui.activity.MainActivity;
import com.popular_movies.R;
import com.popular_movies.service.VolleySingleton;
import com.popular_movies.domain.MovieData;
import com.popular_movies.service.JsonParser;
import com.popular_movies.ui.adapter.MovieAdapter;
import com.popular_movies.framework.UriBuilder;

import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;


public class ListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener /*, MovieAdapter.ClickListener*/ {
    public static final String KEY_DATA = "DATA";
    public static final String KEY_TITLE = "title";
    public ArrayList<MovieData> movieDataList = new ArrayList<>();
    public RecyclerView recyclerView;
    public MovieAdapter adapter;
    public ProgressBar progressBar;
    public SwipeRefreshLayout refreshLayout;

    RequestQueue mRequestQueue = VolleySingleton.getInstance().getmRequestQueue();
    //private String url="https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=76b802b090caa26230f414433db80485\n";

    static UriBuilder uri;
    static String url;

    public ListFragment() {

    }

    public static ListFragment getInstance(String title) {
        ListFragment fragment = new ListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        fragment.setArguments(bundle);
        uri = new UriBuilder(UriBuilder.BASE_URL, title);
        url = uri.toString();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.list_layout, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view);
        if (MainActivity.mIsDualPane) {

            DisplayMetrics metrics = getResources().getDisplayMetrics();
            int width = (int) (metrics.widthPixels / metrics.density);
            //For Tabs
            boolean isTablet = getResources().getBoolean(R.bool.isTablet);
            width = isTablet ? (width / 2) : width;
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), width / 140));
        }
        else {

            if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
            } else
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        }
        refreshLayout = (SwipeRefreshLayout) layout.findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(this);
        refreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.progress_colors));
        progressBar = (ProgressBar) layout.findViewById(R.id.progressBar);
        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList(KEY_DATA) != null) {
            movieDataList = savedInstanceState.getParcelableArrayList(KEY_DATA);
            progressBar.setVisibility(View.GONE);
            adapter = new MovieAdapter(getContext(), movieDataList);

            if (recyclerView.getAdapter() != null) {
                recyclerView.swapAdapter(adapter, false);
            } else {
                recyclerView.setAdapter(adapter);
            }
        } else {
            sendMoviesRequest(url);
        }
        return layout;
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
            outState.putParcelableArrayList(KEY_DATA, (ArrayList<? extends Parcelable>) movieDataList);
        }
    }

    @Override
    public void onRefresh() {
        sendMoviesRequest(url);
    }

    /*
    // sending request to fetch data from tmdb
    */

    void sendMoviesRequest(String url) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            movieDataList = JsonParser.parseMovieDetails(response, ListFragment.this);
                            Log.d("listfragment", "" + movieDataList);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        adapter.setMoviesList(movieDataList);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "There was an error retreiving the request", Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
        mRequestQueue.add(request);
    }

}
