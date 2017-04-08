package com.popular_movies.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.popular_movies.R;
import com.popular_movies.domain.Trailer;
import com.popular_movies.framework.ImageLoader;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Gurpreet on 08-04-2017.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private List<Trailer> data;
    private TrailerClickListener mListener;

    public TrailerAdapter(List<Trailer> data, TrailerClickListener mListener) {
        this.data = data;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.item_trailer, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.setData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ivTrailerImage)
        ImageView ivTrailerImage;
        @BindView(R.id.tvTrailerTitle)
        TextView tvTrailerTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ivTrailerImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mListener != null) {
                        mListener.onTrailerClick(data.get(getAdapterPosition()).getKey());
                    }
                }
            });
        }

        private void setData(Trailer trailer) {
            ImageLoader.loadTrailer(itemView.getContext(),
                    trailer.getKey(),
                    ivTrailerImage);
            tvTrailerTitle.setText(trailer.getName());
        }
    }

    /**
     * interface to view trailer on click
     */
    public interface TrailerClickListener {
        void onTrailerClick(String key);
    }
}
