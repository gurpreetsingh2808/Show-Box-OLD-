package com.popular_movies.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class MoviesDbHelper extends SQLiteOpenHelper {
    static final String TABLE_FAVORITES = "favorites";
    static final String COLUMN_ID = "_id";
    static final String COLUMN_TITLE = "title";
    static final String COLUMN_THUMBNAIL = "thumbnailUrl";
    static final String COLUMN_DESCRIPTION = "description";
    static final String COLUMN_RATING = "userRatings";
    static final String COLUMN_RELEASE = "releaseDate";
    static final String COLUMN_WIDE_THUMBNAIL = "wideThumbnailUrl";
    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "Movies.db";
    static final String DATABASE_CREATE = "create table "
            + TABLE_FAVORITES + "(" +
            COLUMN_ID + " integer primary key, "
            + COLUMN_TITLE + " text not null, "
            + COLUMN_THUMBNAIL + " text not null, "
            + COLUMN_DESCRIPTION + " text not null, "
            + COLUMN_RATING + " text not null, "
            + COLUMN_RELEASE + " integer not null, "
            + COLUMN_WIDE_THUMBNAIL + " text not null);";
    private static MoviesDbHelper mInstance = null;

    private MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static MoviesDbHelper getInstance(Context context) {
        if (mInstance == null)
            return new MoviesDbHelper(context.getApplicationContext());
        else return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MoviesDbHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVORITES);
        onCreate(db);
    }
}
