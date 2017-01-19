package com.popular_movies.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.popular_movies.R;
import com.popular_movies.domain.ReviewData;

import java.util.ArrayList;

/**
 * Created by Gurpreet on 4/9/2016.
 */
public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyViewHolder> {

    ArrayList<ReviewData> reviewDataArrayList = new ArrayList<>();
    private LayoutInflater inflater;
    private static final String TAG = ReviewsAdapter.class.getSimpleName();

    public ReviewsAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setReviewsList(ArrayList<ReviewData> listReviews) {
        if(listReviews != null) {
            this.reviewDataArrayList = listReviews;
            notifyItemRangeChanged(0, listReviews.size());
        }
        else {
            Log.e(TAG, "setReviewsList: list reviews is null");
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.review_data, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        ReviewData current = reviewDataArrayList.get(position);
        holder.user.setText(current.getAuthor());
        holder.review.setText(current.getContent());
    }

    @Override
    public int getItemCount() {
        return reviewDataArrayList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView user, review;

        public MyViewHolder(View itemView) {
            super(itemView);
            user = (TextView) itemView.findViewById(R.id.tvUsername);
            review = (TextView) itemView.findViewById(R.id.tvReview);

        }
    }
}
