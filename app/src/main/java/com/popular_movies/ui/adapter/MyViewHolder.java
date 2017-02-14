package com.popular_movies.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.popular_movies.R;

/**
 * Created by Gurpreet on 4/17/2016.
 */
public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView title;
    public ImageView thumbnail;
    //public MovieAdapterHorizontal.ClickListener clickListener;


    public MyViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
        title = (TextView) itemView.findViewById(R.id.movie_title);
        thumbnail = (ImageView) itemView.findViewById(R.id.movie_thumbnail);

    }

    @Override
    public void onClick(View v) {
        if(MovieAdapterHorizontal.clickListener!=null) {
            MovieAdapterHorizontal.clickListener.itemClicked(v, getPosition());
        }
    }
}
