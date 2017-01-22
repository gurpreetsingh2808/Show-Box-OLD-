package com.popular_movies.framework;

import android.content.Context;
import android.widget.ImageView;

import com.popular_movies.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Gurpreet on 22-01-2017.
 */

public class ImageLoader {

    public static void loadPosterImage(Context context, String url, ImageView imageView) {
        Picasso.with(context).load(" " + url)
                .placeholder(R.drawable.no_img_preview).into(imageView);
    }

    public static void loadBackdropImage(Context context, String url, ImageView imageView) {
        Picasso.with(context).load(" " + url)
                .placeholder(R.drawable.no_img_preview).into(imageView);
    }

}
