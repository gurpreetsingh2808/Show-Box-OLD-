package com.popular_movies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Gurpreet on 1/17/2016.
 */
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private LayoutInflater inflater;
    private ClickListener clickListener;
    ArrayList<MovieItem> movieItemArrayList = new ArrayList<>();


    public CustomAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setMoviesList(ArrayList<MovieItem> listMovies) {
        this.movieItemArrayList = listMovies;
        notifyItemRangeChanged(0, listMovies.size());
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.movie_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        MovieItem current = movieItemArrayList.get(position);
        holder.title.setText(current.getTitle());
        Picasso.with(MainActivity.getContext()).load(current.getThumbnailURL()).into(holder.thumbnail);
    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return movieItemArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        ImageView thumbnail;

        public MyViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.movie_title);
            thumbnail = (ImageView) itemView.findViewById(R.id.movie_thumbnail);

        }

        @Override
        public void onClick(View v) {
            if(clickListener!=null) {
                clickListener.itemClicked(v, getPosition());
            }
        }
    }

    public interface ClickListener {
        void itemClicked(View view, int position);
    }
}
