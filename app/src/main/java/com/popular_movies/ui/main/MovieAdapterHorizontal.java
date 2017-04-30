package com.popular_movies.ui.main;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.popular_movies.R;
import com.popular_movies.domain.MovieData;
import com.popular_movies.framework.ImageLoader;
import com.popular_movies.ui.MovieItemClickListener;
import com.popular_movies.ui.movie_details.MovieDetailFragment;
import com.popular_movies.util.AppUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gurpreet on 1/17/2016.
 */
public class MovieAdapterHorizontal extends RecyclerView.Adapter<MovieAdapterHorizontal.ViewHolder> {

    private static String TAG = MovieAdapterHorizontal.class.getSimpleName();
    private List<MovieData> movieItemArrayList;
    private LayoutInflater inflater;
    private Context context;
    MovieItemClickListener clickListener;

    public MovieAdapterHorizontal(Context context, List<MovieData> movieDataList) {
        if (context != null) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.movieItemArrayList = movieDataList;
        } else {
            Log.e(TAG, "MovieAdapterHorizontal: context is null");
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.movie_item_horizontal, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final MovieData movieData = movieItemArrayList.get(position);
        holder.setData(movieData);
    }


    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.movie_title)
        TextView title;
        @BindView(R.id.movie_thumbnail)
        ImageView thumbnail;
        //public MovieAdapterHorizontal.ClickListener clickListener;


        ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.itemClicked(v, getPosition(), movieItemArrayList.get(getAdapterPosition()));
            }
        }

        private void setData(final MovieData movieData) {
            title.setText(movieData.getOriginal_title());
            ImageLoader.loadPosterImage(context, movieData.getPoster_path(), thumbnail);
        }
    }

    public void setClickListener(MovieItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return movieItemArrayList.size();
    }

}