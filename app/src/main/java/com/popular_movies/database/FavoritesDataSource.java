package com.popular_movies.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.popular_movies.domain.MovieData;

import java.util.Date;


public class FavoritesDataSource {
    private SQLiteDatabase database;
    private MoviesDbHelper dbHelper;
    private final String TAG = "favorite";

    public FavoritesDataSource(Context context) {
        dbHelper = MoviesDbHelper.getInstance(context);
    }

    public void open(boolean readonly) {
        database = readonly ? dbHelper.getReadableDatabase() : dbHelper.getWritableDatabase();
    }

    public void close() {
        database.close();
    }

    private MovieData cursorToMovie(Cursor cursor) {
        int id = cursor.getInt(0);
        String title = cursor.getString(1);
        String thumbnail = cursor.getString(2);
        String description = cursor.getString(3);
        String rating = cursor.getString(4);
        Date release = new Date(cursor.getLong(5));
        String backdrop = cursor.getString(6);
        return new MovieData(title, description, thumbnail, backdrop, rating, release, id);
    }

    public long insertMovie(MovieData movie) {
        ContentValues values = new ContentValues();
        values.put(MoviesDbHelper.COLUMN_ID, movie.id);
        values.put(MoviesDbHelper.COLUMN_TITLE, movie.title);
        values.put(MoviesDbHelper.COLUMN_THUMBNAIL, movie.thumbnailURL);
        values.put(MoviesDbHelper.COLUMN_DESCRIPTION, movie.description);
        values.put(MoviesDbHelper.COLUMN_RATING, movie.userRatings);
        values.put(MoviesDbHelper.COLUMN_RELEASE, movie.releaseDate.getTime());
        values.put(MoviesDbHelper.COLUMN_WIDE_THUMBNAIL, movie.wideThumbnailURL);
        return database.insert(MoviesDbHelper.TABLE_FAVORITES, null, values);
    }

    public boolean doesMovieExist(int id) {
        String query = "select 1 from " + MoviesDbHelper.TABLE_FAVORITES + " where " + MoviesDbHelper.COLUMN_ID + " = " + id;
        Log.d(TAG, "QUERY "+query);
        Cursor c = database.rawQuery(query, null);
        Log.d(TAG, "CURSOR "+c);
        boolean b = c.moveToFirst();
        c.close();
        return b;
    }

    public int removeMovie(int id) {
       return database.delete(MoviesDbHelper.TABLE_FAVORITES, MoviesDbHelper.COLUMN_ID + " = " + id, null);
    }
    public Cursor getAllMovies(){
        return database.rawQuery("select * from "+MoviesDbHelper.TABLE_FAVORITES,null);
    }
}
