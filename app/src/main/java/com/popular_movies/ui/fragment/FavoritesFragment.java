package com.popular_movies.ui.fragment;

import android.content.res.Configuration;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.popular_movies.MainActivity;
import com.popular_movies.R;
import com.popular_movies.database.MovieProviderHelper;
import com.popular_movies.ui.adapter.MovieAdapter;


public class FavoritesFragment extends Fragment {
    RecyclerView recyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.favorites_list, container, false);
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
            if (getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
            else
                recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
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
    public void onStart() {
        super.onStart();
        MovieAdapter movieAdapter = new MovieAdapter(getContext(), MovieProviderHelper.getInstance().getAllFavouriteMovies());
        recyclerView.setAdapter(movieAdapter);
    }
}
