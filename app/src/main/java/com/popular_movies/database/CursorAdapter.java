package com.popular_movies.database;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.popular_movies.ui.activity.MovieDetail;
import com.popular_movies.framework.MyViewHolder;
import com.squareup.picasso.Picasso;

import java.util.Date;

import com.popular_movies.ui.fragment.DetailedViewFragment;
import com.popular_movies.MainActivity;
import com.popular_movies.domain.MovieData;
import com.popular_movies.R;

public class CursorAdapter extends CursorRecyclerViewAdapter<MyViewHolder> {
    LayoutInflater inflater;
    Context context;

    public CursorAdapter(Context context, Cursor cursor) {
        super(context, cursor);
        inflater = LayoutInflater.from(context);
        this.context = context;
        if(cursor.getCount() > 0){
            if(MainActivity.mIsDualPane) {
                cursor.moveToFirst();
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String thumbnail = cursor.getString(2);
                String description = cursor.getString(3);
                String rating = cursor.getString(4);
                Date release = new Date(cursor.getLong(5));
                String backdrop = cursor.getString(6);
                final MovieData movie = new MovieData(title, description, thumbnail, backdrop, rating, release, id);
                ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail, DetailedViewFragment.getInstance(movie))
                        .commit();
            }
        }
        else {
            //Snackbar.make(context, "Your favorites list is empty", Snackbar.LENGTH_SHORT).show();
            Toast.makeText(context, "Your favorites list is empty", Toast.LENGTH_SHORT).show();
        }
        //}
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, Cursor cursor) {
        int id = cursor.getInt(0);
        String title = cursor.getString(1);
        String thumbnail = cursor.getString(2);
        String description = cursor.getString(3);
        String rating = cursor.getString(4);
        Date release = new Date(cursor.getLong(5));
        String backdrop = cursor.getString(6);

        final MovieData movie = new MovieData(title, description, thumbnail, backdrop, rating, release, id);
        holder.title.setText(movie.title);
        String poster_url = movie.thumbnailURL;
        if (!(poster_url.isEmpty() || poster_url.equals("null"))) {
            Picasso.with(context).load(poster_url).error(R.drawable.no_img_preview).into(holder.thumbnail);
        }

        if (!MainActivity.mIsDualPane) {
            holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MovieDetail.class);
                    intent.putExtra(MovieDetail.KEY_MOVIE, movie);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        AppBarLayout barLayout = (AppBarLayout) ((AppCompatActivity) context).findViewById(R.id.actionbar);
                        ActivityOptions compat = ActivityOptions.makeSceneTransitionAnimation((AppCompatActivity) context, Pair.create((View) holder.thumbnail, "poster"), Pair.create((View) barLayout, "actionbar"));
                        context.startActivity(intent, compat.toBundle());
                    } else
                        context.startActivity(intent);
                }
            });
        } else {
            holder.thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.slide_in_bottom, R.anim.slide_out_top)
                            .replace(R.id.movie_detail, DetailedViewFragment.getInstance(movie))
                            .commit();
                }
            });
        }
        }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.movie_data, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }
}
