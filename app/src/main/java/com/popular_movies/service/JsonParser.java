package com.popular_movies.service;

import android.os.Parcel;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.popular_movies.framework.UriBuilder;
import com.popular_movies.ui.adapter.MovieAdapter;
import com.popular_movies.ui.fragment.ListFragment;
import com.popular_movies.R;
import com.popular_movies.domain.MovieData;
import com.popular_movies.domain.ReviewData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Gurpreet on 4/9/2016.
 */
public class JsonParser {
    static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    //private static ListFragment listFragment;

    public static ArrayList<MovieData> parseMovieDetails(JSONObject response, final ListFragment listFragment) throws ParseException{
        ArrayList<MovieData> listVideos = new ArrayList<>();
        //this.listFragment = listFragment;

        if (response == null || response.length() == 0) {
            return listVideos;
        }
        try {

            JSONArray jsonArrayResult = response.getJSONArray("results");
            int len = jsonArrayResult.length();

            Log.d("menu", "for loop");
            for (int i = 0; i < len; i++) {

                JSONObject itemsObject = jsonArrayResult.getJSONObject(i);

                String title = (itemsObject.getString("original_title"));
                String description = (itemsObject.getString("overview"));
                Date releaseDate = sdf.parse(itemsObject.getString("release_date"));
                String userRatings = (itemsObject.getString("vote_average") + "/10");
                String thumbnailURL = (UriBuilder.BASE_URL_IMAGE + UriBuilder.imageSize[2] + itemsObject.getString("poster_path"));
                String wideThumbnailURL = (UriBuilder.BASE_URL_IMAGE + UriBuilder.imageSize[3] + itemsObject.getString("backdrop_path"));
                int id = (itemsObject.getInt("id"));
                MovieData movieItem = new MovieData(title, description, thumbnailURL, wideThumbnailURL, userRatings, releaseDate, id);
                listVideos.add(movieItem);

                listFragment.adapter = new MovieAdapter(listFragment.getContext(), listVideos);
                if (listFragment.recyclerView.getAdapter() != null) {
                    listFragment.recyclerView.swapAdapter(listFragment.adapter, false);
                } else {
                    listFragment.recyclerView.setAdapter(listFragment.adapter);
                }
                listFragment.refreshLayout.setRefreshing(false);
                listFragment.progressBar.setVisibility(View.GONE);
                listFragment.recyclerView.setVisibility(View.VISIBLE);

            }
        }catch (JSONException e) {
            Log.d("menu", "json exception " + e);
            Snackbar.make(listFragment.getView(), listFragment.getString(R.string.connection_error), Snackbar.LENGTH_SHORT).setAction("Try again", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listFragment.onRefresh();
                }
            }).show();
        }
        return listVideos;
    }

    public static String parseTrailer(JSONObject response){
        Log.d("json",""+response);
        try {
            if (response == null || response.length() == 0) {
                return null;
            }
            JSONArray jsonArrayResult = response.getJSONArray("results");
            int len = jsonArrayResult.length();
            for (int i = 0; i < len; i++) {
                JSONObject itemsObject = jsonArrayResult.getJSONObject(i);
                if (itemsObject.getString("site").equals("YouTube")) {
                    Log.d("json",""+itemsObject.getString("key"));
                    return itemsObject.getString("key");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<ReviewData> parseReviewDetails(JSONObject response) {
        ArrayList<ReviewData> listVideos = new ArrayList<>();

        if (response == null || response.length() == 0) {
            return listVideos;
        }
        try {
            JSONArray jsonArrayResult = response.getJSONArray("results");
            int len = jsonArrayResult.length();

            for (int i = 0; i < len; i++) {

                JSONObject itemsObject = jsonArrayResult.getJSONObject(i);
                ReviewData reviewData = new ReviewData(Parcel.obtain());
                reviewData.setAuthor(itemsObject.getString("author"));
                reviewData.setContent(itemsObject.getString("content"));
                listVideos.add(reviewData);
            }
        }catch (JSONException e) {
            e.printStackTrace();
            Log.d("menu", "json exception " + e);
        }
        return listVideos;
    }

}
