package com.popular_movies.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.popular_movies.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gurpreet on 4/17/2016.
 */
public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    @BindView(R.id.movie_title)
    TextView title;
    @BindView(R.id.movie_thumbnail)
    ImageView thumbnail;
    //public MovieAdapterHorizontal.ClickListener clickListener;


    public MyViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(MovieAdapterHorizontal.clickListener!=null) {
            MovieAdapterHorizontal.clickListener.itemClicked(v, getPosition());
        }
    }
}
