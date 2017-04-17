package com.popular_movies.ui.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.popular_movies.ui.main.MainActivity;
import com.popular_movies.ui.movie_details.MovieDetailActivity;
import com.popular_movies.ui.movie_details.MovieDetailFragment;

import java.util.List;

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
    private ClickListener clickListener;
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    public MovieListingAdapter(Context context, List<MovieData> movieDataList) {
        if (context != null) {
            this.context = context;
            inflater = LayoutInflater.from(context);
            this.movieItemArrayList = movieDataList;
            if (MainActivity.mIsDualPane) {
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail, MovieDetailFragment.getInstance(movieItemArrayList.get(0)))
                        .commit();
            }
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
        @BindView(R.id.movie_thumbnail)
        ImageView thumbnail;
        @BindView(R.id.tvRating)
        TextView tvRating;
        //public MovieAdapterHorizontal.ClickListener clickListener;

        MovieViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (clickListener != null) {
                clickListener.itemClicked(v, getPosition());
            }
        }

        private void setData(final MovieData movieData) {
            title.setText(movieData.getOriginal_title());
            ImageLoader.loadPosterImage(context, movieData.getPoster_path(), thumbnail);
            tvRating.setText(movieData.getVote_average());

            if (!MainActivity.mIsDualPane) {
                thumbnail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, MovieDetailActivity.class);
                        intent.putExtra(context.getString(R.string.key_movie), movieData);
                        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            AppBarLayout barLayout = (AppBarLayout) ((AppCompatActivity) context).findViewById(R.id.actionbar);
                            ActivityOptions compat = ActivityOptions.makeSceneTransitionAnimation((AppCompatActivity) context,
                                    Pair.create((View) thumbnail, context.getString(R.string.transition_name)),
                                    Pair.create((View) barLayout, context.getString(R.string.transition_name_action_bar)));
                            context.startActivity(intent, compat.toBundle());
                        } else*/
                            context.startActivity(intent);
                    }
                });
            } else {
                thumbnail.setOnClickListener(new View.OnClickListener() {
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
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.pbPagination)
        ProgressBar progressBar;

        LoadingViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void itemClicked(View view, int position);
    }
}