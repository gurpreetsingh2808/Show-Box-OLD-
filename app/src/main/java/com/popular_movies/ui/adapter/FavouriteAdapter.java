package com.popular_movies.ui.adapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.popular_movies.R;
import com.popular_movies.domain.MovieData;
import com.popular_movies.domain.MovieDataTable;
import com.popular_movies.framework.ImageLoader;
import com.popular_movies.ui.main.MainActivity;
import com.popular_movies.ui.moviedetail.MovieDetailActivity;
import com.popular_movies.ui.moviedetail.MovieDetailFragment;

import java.util.List;

/**
 * Created by Gurpreet on 1/17/2016.
 */
public class FavouriteAdapter extends RecyclerView.Adapter<MyViewHolder> {

    private static String TAG = FavouriteAdapter.class.getSimpleName();
    private List<MovieData> movieItemArrayList;
    private LayoutInflater inflater;
    private Context context;
    private Cursor dataCursor;
    public static ClickListener clickListener;


    public FavouriteAdapter(Context context, Cursor cursor) {
        dataCursor = cursor;
        this.context = context;
        inflater = LayoutInflater.from(context);
       /* if (MainActivity.mIsDualPane) {
            ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail, DetailedViewFragment.getInstance(movieItemArrayList.get(0)))
                    .commit();
        }*/
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.movie_item_horizontal, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        dataCursor.moveToPosition(position);

        String title = dataCursor.getString(dataCursor.getColumnIndex(MovieDataTable.FIELD_COL_TITLE));
        String description = dataCursor.getString(dataCursor.getColumnIndex(MovieDataTable.FIELD_COL_OVERVIEW));
        String posterPath = dataCursor.getString(dataCursor.getColumnIndex(MovieDataTable.FIELD_COL_POSTER_PATH));
        String backdrop = dataCursor.getString(dataCursor.getColumnIndex(MovieDataTable.FIELD_COL_BACKDROP));
        String voteAverage = dataCursor.getString(dataCursor.getColumnIndex(MovieDataTable.FIELD_COL_VOTE_AVERAGE));
        String releaseDate = dataCursor.getString(dataCursor.getColumnIndex(MovieDataTable.FIELD_COL_RELEASE_DATE));
        Log.d(TAG, "onBindViewHolder: release date "+releaseDate);
        String id = dataCursor.getString(dataCursor.getColumnIndex(MovieDataTable.FIELD_COL_ID));

        holder.title.setText(title);
        ImageLoader.loadPosterImage(context, posterPath, holder.thumbnail);

        final MovieData movieData = new MovieData(title, description, posterPath, backdrop, voteAverage, new java.sql.Date(Long.valueOf(releaseDate)), Integer.valueOf(id));

        if (!MainActivity.mIsDualPane) {
            holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MovieDetailActivity.class);
                    intent.putExtra(context.getString(R.string.key_movie), movieData);
                        context.startActivity(intent);
                }
            });
        } else {
            holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top)
                            .replace(R.id.movie_detail, MovieDetailFragment.getInstance(movieData))
                            .commit();
                }
            });
        }
    }

    public Cursor swapCursor(Cursor cursor) {
        if (dataCursor == cursor) {
            return null;
        }
        Cursor oldCursor = dataCursor;
        this.dataCursor = cursor;
        if (cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
         return (dataCursor == null) ? 0 : dataCursor.getCount();
    }

    public interface ClickListener {
        void itemClicked(View view, int position);
    }
}
