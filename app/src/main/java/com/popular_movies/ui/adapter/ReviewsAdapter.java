package com.popular_movies.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.popular_movies.R;
import com.popular_movies.domain.Review;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gurpreet on 4/9/2016.
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyViewHolder> {

    private List<Review> reviewDataArrayList = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;
    private static final String TAG = ReviewsAdapter.class.getSimpleName();

    public ReviewsAdapter(Context context, List<Review> reviewList) {
        if(context != null) {
            this.context = context;
            this.reviewDataArrayList = reviewList;
            inflater = LayoutInflater.from(context);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.review_data, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Review current = reviewDataArrayList.get(position);
        holder.user.setText(current.getAuthor());
        holder.review.setText(current.getContent());
    }

    @Override
    public int getItemCount() {
        return reviewDataArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvUsername)
        TextView user;
        @BindView(R.id.tvReview)
        TextView review;

        MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
