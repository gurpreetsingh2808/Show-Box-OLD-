package com.popular_movies.ui.movies_listing;

import android.content.Context;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.popular_movies.R;
import com.popular_movies.domain.MovieData;
import com.popular_movies.framework.ImageLoader;
import com.popular_movies.ui.MovieItemClickListener;
import com.popular_movies.ui.main.MainActivity;
import com.popular_movies.ui.movie_details.MovieDetailFragment;
import com.popular_movies.util.AppUtils;
import com.popular_movies.util.DateConvert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gurpreet on 1/17/2016.
 */
public class MovieListingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static String TAG = MovieListingAdapter.class.getSimpleName();
    private List<MovieData> movieItemArrayList;
    private LayoutInflater inflater;
    private Context context;
    private MovieItemClickListener clickListener;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;
    private Map<Integer, String> mapGenre = new HashMap<>();

    public MovieListingAdapter(Context context, List<MovieData> movieDataList) {
        if (context != null) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.movieItemArrayList = movieDataList;
            /*if (AppUtils.isTablet(context)) {
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail, MovieDetailFragment.getInstance(movieItemArrayList.get(0)))
                        .commit();
            }*/
        } else {
            Log.e(TAG, "MovieAdapterHorizontal: context is null");
        }
    }

    @Override
    public int getItemViewType(int position) {
        return movieItemArrayList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = inflater.inflate(R.layout.item_movie_listing, parent, false);
            return new MovieViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = inflater.inflate(R.layout.item_pagination_loader, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MovieViewHolder) {
            final MovieData movieData = movieItemArrayList.get(position);
            MovieViewHolder movieViewHolder = (MovieViewHolder) holder;
            movieViewHolder.setData(movieData);

        } else if (holder instanceof LoadingViewHolder) {
            LoadingViewHolder loadingViewHolder = (LoadingViewHolder) holder;
            //loadingViewHolder.progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemCount() {
        return movieItemArrayList == null ? 0 : movieItemArrayList.size();
    }

    public void setGenre(Map<Integer,String> mapGenre) {
        this.mapGenre = mapGenre;
    }

    public void addAll(List<MovieData> results) {
        movieItemArrayList.addAll(results);
        notifyDataSetChanged();
    }

    public void add(MovieData movie) {
        movieItemArrayList.add(movie);
        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                notifyItemInserted(movieItemArrayList.size() - 1);
            }
        };

        handler.post(r);
    }

    public void remove() {
        movieItemArrayList.remove(movieItemArrayList.size() - 1);
        notifyItemRemoved(movieItemArrayList.size());
    }

    class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.movie_title)
        TextView title;
        @BindView(R.id.tvReleaseYear)
        TextView tvReleaseYear;
        @BindView(R.id.movie_thumbnail)
        ImageView thumbnail;
        @BindView(R.id.tvRating)
        TextView tvRating;
        @BindView(R.id.ivPopularity)
        ImageView ivPopularity;
        @BindView(R.id.tvPopularity)
        TextView tvPopularity;
        @BindView(R.id.ivAdult)
        ImageView ivAdult;
        @BindView(R.id.tvGenre)
        TextView tvGenre;
        //public MovieAdapterHorizontal.ClickListener clickListener;

        MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.itemClicked(v, getAdapterPosition(), movieItemArrayList.get(getAdapterPosition()));
            }
        }

        private void setData(final MovieData movieData) {
            //  set title
            title.setText(movieData.getOriginal_title());
            //  set poster/movie image
            ImageLoader.loadPosterImage(context, movieData.getPoster_path(), thumbnail);
            //  set rating
            tvRating.setText(movieData.getVote_average());
            //  set popularity icon
            if(Float.valueOf(movieData.getPopularity()) < 100f ) {
                ivPopularity.setImageResource(R.drawable.ic_popularity_low);
            }
            else if(Float.valueOf(movieData.getPopularity()) >= 100f && Float.valueOf(movieData.getPopularity()) < 200f) {
                ivPopularity.setImageResource(R.drawable.ic_popularity_ok);
            }
            else if(Float.valueOf(movieData.getPopularity()) >= 200f) {
                ivPopularity.setImageResource(R.drawable.ic_popularity_high);
            }
            int roundedPopularity = Math.round(Float.valueOf(movieData.getPopularity()));
            //  set popularity text
            tvPopularity.setText(String.valueOf(roundedPopularity));
            //  set release date
            tvReleaseYear.setText(DateConvert.convert(movieData.getRelease_date()));
            //  set adult image
            if(movieData.getAdult()) {
                ivAdult.setVisibility(View.VISIBLE);
            }

            for (int i = 0; i < movieData.getGenre_ids().length; i++) {
                Log.d(TAG, "genre "+movieData.getGenre_ids()[i]);
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < movieData.getGenre_ids().length; i++) {
                for (Map.Entry<Integer, String> genre : mapGenre.entrySet()) {
                    Log.d(TAG, "genre "+genre.getValue());
                    if (movieData.getGenre_ids()[i].intValue() == genre.getKey()) {
                        Log.d(TAG, "setData: genre matched");
                        sb.append(genre.getValue());
                        break;
                    }
                }
                if(i != movieData.getGenre_ids().length-1) {
                    sb.append(", ");
                }
            }
            tvGenre.setText(sb);
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.pbPagination)
        ProgressBar progressBar;

        LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setClickListener(MovieItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

}