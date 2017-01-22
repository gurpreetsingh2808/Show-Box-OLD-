package com.popular_movies.widget;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.popular_movies.R;
import com.popular_movies.domain.MovieData;
import com.popular_movies.domain.MovieDataTable;


/**
 * Created by Gurpreet on 16-01-2017.
 */

public class StackWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new StackRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

 class StackRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    // private static final int mCount = 10;
    private int mCount = 0;
    private List<MovieData> mWidgetItems = new ArrayList<MovieData>();
    private Context mContext;
    private int mAppWidgetId;

    private String mID;
    private String mReleaseDate;
    private String mTitle;
    private String mPicture;

    public StackRemoteViewsFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    public void onCreate() {

        Uri uri = MovieDataTable.CONTENT_URI;
        String[] projection = {MovieDataTable.FIELD_COL_ID, MovieDataTable.FIELD_COL_TITLE,
                MovieDataTable.FIELD_COL_RELEASE_DATE, MovieDataTable.FIELD_COL_POSTER_PATH };
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = null;

        Cursor cursor = this.mContext.getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);

        if (cursor != null && cursor.getCount() > 0) {
           //     cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();

                mID = cursor.getString(cursor.getColumnIndex(MovieDataTable.FIELD_COL_ID));
                //mName = cursor.getString(cursor.getColumnIndex(EmployeeDatabase.COLUMN_LASTNAME)) + ", " + cursor.getString(cursor.getColumnIndex(EmployeeDatabase.COLUMN_FIRSTNAME));
                mTitle = cursor.getString(cursor.getColumnIndex(MovieDataTable.FIELD_COL_TITLE));
                mReleaseDate = cursor.getString(cursor.getColumnIndex(MovieDataTable.FIELD_COL_RELEASE_DATE));
                mPicture = cursor.getString(cursor.getColumnIndex(MovieDataTable.FIELD_COL_POSTER_PATH));

                mWidgetItems.add(new WidgetItem(mID, mTitle, mReleaseDate, mPicture));
                mCount = mCount + 1;

            }
        }

        cursor.close();

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        mWidgetItems.clear();
    }

    public int getCount() {
        return mCount;
    }

    public RemoteViews getViewAt(int position) {


        RemoteViews remoteViews = new RemoteViews(this.mContext.getPackageName(), R.layout.widget_movie);
        // Set the text
        remoteViews.setTextViewText(R.id.tvMovieName, mTitle);
        remoteViews.setTextViewText(R.id.tvReleaseDate, mReleaseDate);
        Bundle extras = new Bundle();

        extras.putInt(StackWidgetProvider.MOVIE_ID, mWidgetItems.get(position).getId());
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);
        remoteViews.setOnClickFillInIntent(R.id.tvMovieName, fillInIntent);

        InputStream is;
        try {
            System.out.println("Loading view " + position);

            is = mContext.getAssets().open("pics/" + mWidgetItems.get(position).getPoster_path());
            Bitmap bit = BitmapFactory.decodeStream(is);

            remoteViews.setImageViewBitmap(R.id.ivMovieIcon, bit);

        } catch (IOException e) {
            e.printStackTrace();
        }

        return remoteViews;
    }

    public RemoteViews getLoadingView() {
        return null;
    }

    public int getViewTypeCount() {
        return 1;
    }

    public long getItemId(int position) {
        return position;
    }

    public boolean hasStableIds() {
        return true;
    }

    public void onDataSetChanged() {

    }
}

