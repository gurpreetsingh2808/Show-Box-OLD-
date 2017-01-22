package com.popular_movies.database;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import com.popular_movies.MyApplication;
import com.popular_movies.domain.MovieData;
import com.popular_movies.domain.MovieDataTable;

import java.util.List;

/**
 * Created by Gurpreet on 16-01-2017.
 */

public class MovieProviderHelper {

    private static final String TAG = MovieProviderHelper.class.getSimpleName();
    private static ContentResolver contentResolver;
    private static MovieProviderHelper movieProviderHelper;


    /**
     * Private constructor to disallow instantiation from outside
     *
     * @param context
     */
    private MovieProviderHelper(Context context) {
        contentResolver = context.getContentResolver();
    }

    /**
     * Obtain the instance of shared preference
     *
     * @return
     */
    public static MovieProviderHelper getInstance() {
        if (movieProviderHelper == null) {
            movieProviderHelper = new MovieProviderHelper(MyApplication.getInstance().getApplicationContext());
        }
        return movieProviderHelper;
    }

    public Cursor getCursor() {
        if (contentResolver == null) {
            getInstance();
        }
        return contentResolver.query(MovieDataTable.CONTENT_URI, null, null, null, null);
    }

    public Cursor getFilledCursor() {
        if (contentResolver == null) {
            getInstance();
        }
        return contentResolver.query(MovieDataTable.CONTENT_URI,
                new String[]{MovieDataTable.FIELD_COL_POSTER_PATH, MovieDataTable.FIELD_COL_TITLE,
                        MovieDataTable.FIELD_COL_RELEASE_DATE},
                null,
                null,
                null);
    }


    public void insert(MovieData movieData) {
        contentResolver.insert(MovieDataTable.CONTENT_URI,
                MovieDataTable.getContentValues(movieData, false));
    }


    public void delete(int id) {
        contentResolver.delete(MovieDataTable.CONTENT_URI,
                MovieDataTable.FIELD_COL_ID + "=?", new String[]{String.valueOf(id)});
    }

    public Boolean doesMovieExist(int id) {
        List<MovieData> listMovies = MovieDataTable.getRows(getCursor(), false);
        for (MovieData movieData : listMovies) {
            Log.d(TAG, "doesMovieExist: movie id "+movieData.getId());
            if(movieData.getId() == id) {
                return true;
            }
        }
        return false;
    }

    public List<MovieData> getAllFavouriteMovies() {
        return MovieDataTable.getRows(getCursor(), true);
    }
}
