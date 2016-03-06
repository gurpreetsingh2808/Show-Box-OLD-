package com.popular_movies;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.transition.ChangeClipBounds;
import android.transition.Explode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements CustomAdapter.ClickListener{

    private static MainActivity instance;
    //private String url="https://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&api_key=76b802b090caa26230f414433db80485\n";
    RequestQueue mRequestQueue = VolleySingleton.getInstance().getmRequestQueue();
    UriBuilder uri = new UriBuilder(UriBuilder.BASE_URL , UriBuilder.MOST_POPULAR);
    String url = uri.toString();
    ArrayList<MovieItem> movieItemArrayList = new ArrayList<>();
    private RecyclerView recyclerViewMovie;
    private  CustomAdapter customAdapter;

    public static Context getContext() {
        return instance.getApplicationContext();
    }

    public void setupWindowAnimations() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            //getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());
            getWindow().setSharedElementExitTransition(new ChangeClipBounds());
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            recyclerViewMovie.setLayoutManager(new GridLayoutManager(this, 5));
        }
        else if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerViewMovie.setLayoutManager(new GridLayoutManager(this, 3));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setupWindowAnimations();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            instance=this;
            recyclerViewMovie = (RecyclerView) findViewById(R.id.recyclerMovie);
            //recyclerViewMovie.setHasFixedSize(true);
            if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                recyclerViewMovie.setLayoutManager(new GridLayoutManager(this, 5));
            }
            else
                recyclerViewMovie.setLayoutManager(new GridLayoutManager(this, 3));

            customAdapter = new CustomAdapter(this);
            customAdapter.setClickListener(this);
            recyclerViewMovie.setAdapter(customAdapter);
            recyclerViewMovie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this, DetailedView.class));
                }
            });

            sendJsonRequest(url);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("MainActivity","exception "+e);
        }
    }

    /*
    // sending request to fetch data from tmdb
    */
    void sendJsonRequest(String url) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //parsing request
                        Log.d("menu", "parsing req");
                        movieItemArrayList = parseJSONResponse(response);
                        customAdapter.setMoviesList(movieItemArrayList);
                        Log.d("menu", "req parsed");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "There was an error retreiving the request", Toast.LENGTH_SHORT).show();
                    }
                });
        Log.d("menu", "json request created");
        mRequestQueue.add(request);
        Log.d("menu", "req added to queue");
    }

    private ArrayList<MovieItem> parseJSONResponse(JSONObject response) {

        ArrayList<MovieItem> listVideos = new ArrayList<>();
        Log.d("menu", "parse req");

        if (response == null || response.length() == 0) {
            return listVideos;
        }

        try {
            Log.d("menu", "try");
            JSONArray jsonArrayResult = response.getJSONArray("results");
            int len = jsonArrayResult.length();

            Log.d("menu", "for loop");
            for (int i = 0; i < len; i++) {

                    JSONObject itemsObject = jsonArrayResult.getJSONObject(i);

//////////////////    one array list
                    MovieItem movieItem = new MovieItem();
                    movieItem.setTitle(itemsObject.getString("original_title"));
                    movieItem.setDescription(itemsObject.getString("overview"));
                    movieItem.setReleaseDate(itemsObject.getString("release_date"));
                    movieItem.setUserRatings(itemsObject.getString("vote_average") + "/10");
                    movieItem.setThumbnailURL(UriBuilder.BASE_IMAGE_URL + UriBuilder.imageSize[2] + itemsObject.getString("poster_path"));
                    movieItem.setWideThumbnailURL(UriBuilder.BASE_IMAGE_URL + UriBuilder.imageSize[3] + itemsObject.getString("backdrop_path"));


                    //Log.d("menu", "title " + movieItem.getTitle());
                    //Log.d("menu", "desc " + movieItem.getDescription());
                    //Log.d("menu", "url " + movieItem.getThumbnailURL());

                    listVideos.add(movieItem);

                    //Log.d("menu", "arraylist title " + listVideos.get(i).getTitle());
                    //Log.d("menu", "arraylist desc " + listVideos.get(i).getDescription());
                    //Log.d("menu", "arraylist url " + listVideos.get(i).getThumbnailURL());

            }
        }catch (JSONException e) {
            Log.d("menu", "json exception " + e);
        }
        return listVideos;
    }


    @Override
    public void itemClicked(View view, int position) {


        Intent intent = new Intent(MainActivity.this, DetailedView.class);
        intent.putExtra("TITLE", movieItemArrayList.get(position).getTitle());
        intent.putExtra("DESCRIPTION", movieItemArrayList.get(position).getDescription());
        intent.putExtra("THUMBNAIL_URL", movieItemArrayList.get(position).getThumbnailURL());
        intent.putExtra("WIDE_THUMBNAIL_URL", movieItemArrayList.get(position).getWideThumbnailURL());
        intent.putExtra("RELEASE_DATE", movieItemArrayList.get(position).getReleaseDate());
        intent.putExtra("USER_RATINGS", movieItemArrayList.get(position).getUserRatings());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setTransitionName("poster");
            ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view.findViewById(R.id.movie_thumbnail), view.getTransitionName());
            startActivity(intent, compat.toBundle());
        }
        else
            startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_most_popular) {
            UriBuilder uri = new UriBuilder(UriBuilder.BASE_URL , UriBuilder.MOST_POPULAR);
            String url = uri.toString();
            sendJsonRequest(url);
            return true;
        }
        else if (id == R.id.action_highest_rated) {
            UriBuilder uri = new UriBuilder(UriBuilder.BASE_URL , UriBuilder.HIGHEST_RATED);
            String url = uri.toString();
            sendJsonRequest(url);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
