package com.popular_movies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.popular_movies.database.FavoritesDataSource;
import com.popular_movies.domain.MovieData;
import com.popular_movies.framework.DateConvert;
import com.popular_movies.framework.JsonParser;
import com.popular_movies.framework.UriBuilder;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import me.relex.circleindicator.CircleIndicator;

public class DetailedViewFragment extends Fragment {

    TextView title, releaseDate, synopsis, userRatings;
    ImageView poster;
    CircleIndicator indicator;
    AppBarLayout.OnOffsetChangedListener mListener;
    String trailerKey = null;
    RequestQueue mRequestQueue = VolleySingleton.getInstance().getmRequestQueue();
    private static final String LOGTAG = "DetailedView";
    MovieData movieData;
    FavoritesDataSource dataSource;
    FloatingActionButton favoritesButton;
    public static final String KEY_MOVIE = "movie";
    public static Toolbar toolbar;

    private static DetailedViewFragment instance;
    Button btnUserReview;

    public static DetailedViewFragment getInstance(Parcelable movie) {
        DetailedViewFragment detailsFragment = new DetailedViewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_MOVIE, movie);
        detailsFragment.setArguments(bundle);
        return detailsFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_detailed_view, container, false);

        instance = this;
        movieData = getArguments().getParcelable(KEY_MOVIE);
        Log.d("detailedview", "" + movieData);

        if (!MainActivity.mIsDualPane) {
            toolbar = (Toolbar) view.findViewById(R.id.toolbar);
            ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        }

        dataSource = new FavoritesDataSource(getActivity());
        dataSource.open(false);

        indicator = (CircleIndicator) view.findViewById(R.id.indicator);

        final CollapsingToolbarLayout collapsingToolbar = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);
        mListener = new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                Log.d("toolbar", "" + collapsingToolbar.getHeight() + verticalOffset);
                if (!MainActivity.mIsDualPane) {
                    if (collapsingToolbar.getHeight() + verticalOffset < 340) {
                        title.setVisibility(View.GONE);
                        collapsingToolbar.setTitle(movieData.title);
                        releaseDate.setVisibility(View.GONE);
                        userRatings.setVisibility(View.GONE);
                        poster.setVisibility(View.GONE);
                    } else {
                        title.setVisibility(View.VISIBLE);
                        releaseDate.setVisibility(View.VISIBLE);
                        userRatings.setVisibility(View.VISIBLE);
                        poster.setVisibility(View.VISIBLE);
                        collapsingToolbar.setTitle("");
                    }
                } else {
                    if (collapsingToolbar.getHeight() + verticalOffset >= 16) {
                        collapsingToolbar.setContentScrimColor(getResources().getColor(R.color.black));
                    }
                    if (collapsingToolbar.getHeight() + verticalOffset > 134) {
                        title.setVisibility(View.VISIBLE);
                        releaseDate.setVisibility(View.VISIBLE);
                        userRatings.setVisibility(View.VISIBLE);
                        poster.setVisibility(View.VISIBLE);
                        collapsingToolbar.setTitle("");
                    }
                }

            }
        };

        ((AppBarLayout) view.findViewById(R.id.app_bar)).addOnOffsetChangedListener(mListener);

        title = (TextView) view.findViewById(R.id.title);
        title.setText(movieData.title);

        releaseDate = (TextView) view.findViewById(R.id.releaseDate);
        releaseDate.setText(DateConvert.convert(movieData.releaseDate));

        synopsis = (TextView) view.findViewById(R.id.synopsis);
        synopsis.setText(movieData.description);

        userRatings = (TextView) view.findViewById(R.id.userRatings);
        userRatings.setText(movieData.userRatings);

        btnUserReview = (Button) view.findViewById(R.id.buttonUserReviews);
        btnUserReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ReviewActivity.class);
                intent.putExtra("ID", movieData.id);
                Log.d("detailed", "" + movieData.id);
                startActivity(intent);
            }
        });

        ImageView toolbarImage = (ImageView) view.findViewById(R.id.toolbarImage);
        Picasso.with(getActivity()).load(movieData.wideThumbnailURL).into(toolbarImage);
        toolbarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (trailerKey != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://www.youtube.com/watch?v=" + trailerKey));
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity(), "Trailer for this movie is not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        poster = (ImageView) view.findViewById(R.id.poster);
        Picasso.with(getActivity()).load(movieData.thumbnailURL)
                .placeholder(R.drawable.no_img_preview).into(poster);

        favoritesButton = (FloatingActionButton) view.findViewById(R.id.fab);
        favoritesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataSource.doesMovieExist(movieData.id)) {
                    favoritesButton.setImageResource(R.drawable.ic_favorite_grey_24px);
                    dataSource.removeMovie(movieData.id);
                    Snackbar.make(view, "Removed " + movieData.title + " from Favorites!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else {
                    favoritesButton.setImageResource(R.drawable.ic_favorite_brown_24px);
                    dataSource.insertMovie(movieData);
                    Snackbar.make(view, "Added " + movieData.title + " To Favorites!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        UriBuilder uri = new UriBuilder(UriBuilder.BASE_URL + "/" + movieData.id,
                UriBuilder.VIDEOS);
        String url = uri.toString();
        sendTrailerKeyRequest(url);

        dataSource = new FavoritesDataSource(getActivity());
        dataSource.open(false);
        if (dataSource.doesMovieExist(movieData.id)) {
            favoritesButton.setImageResource(R.drawable.ic_favorite_brown_24px);
        }
        return  view;
    }

    void sendTrailerKeyRequest(String url) {

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        trailerKey = JsonParser.parseTrailer(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), "There was an error retreiving the request", Toast.LENGTH_SHORT).show();
                    }
                });
        mRequestQueue.add(request);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (dataSource != null)
            dataSource.close();
    }

}
