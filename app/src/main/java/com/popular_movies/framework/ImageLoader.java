package com.popular_movies.framework;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.popular_movies.BuildConfig;
import com.popular_movies.R;
import com.squareup.picasso.Picasso;

/**
 * Created by Gurpreet on 22-01-2017.
 */

public class ImageLoader {

    private static final String TAG = ImageLoader.class.getSimpleName();
    //private static final String BASE_URL_IMAGE = "http://image.tmdb.org/t/p/w185";
    private static String[] imageSize = {"w92", "w154", "w185", "w342", "w500", "w780"};

    public static void loadPosterImage(Context context, String url, ImageView imageView, int quality) {
        Picasso.with(context).load(BuildConfig.BASE_URL_IMAGE + imageSize[quality] + url)
                .placeholder(R.drawable.no_img_preview).into(imageView);
    }

    public static void loadPosterImage(Context context, String url, ImageView imageView) {
        Picasso.with(context).load(BuildConfig.BASE_URL_IMAGE + imageSize[2] + url)
                .placeholder(R.drawable.no_img_preview).into(imageView);
    }

    public static void loadBackdropImage(Context context, String url, ImageView imageView) {
        Picasso.with(context).load(BuildConfig.BASE_URL_IMAGE + imageSize[5] + url)
                .placeholder(R.drawable.no_img_preview).into(imageView);
    }

    public static void loadBackdropImage(Context context, String url, ImageView imageView, int quality) {
        Picasso.with(context).load(BuildConfig.BASE_URL_IMAGE + imageSize[quality] + url)
                .placeholder(R.drawable.no_img_preview).into(imageView);
    }

}
