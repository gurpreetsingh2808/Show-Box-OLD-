package com.popular_movies.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Movie;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.popular_movies.R;
import com.popular_movies.database.MovieProvider;
import com.popular_movies.database.MovieProviderHelper;
import com.squareup.picasso.Picasso;


/**
 * Created by Gurpreet on 16-01-2017.
 */


public class FavouriteWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ListRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}

class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final String TAG = ListRemoteViewsFactory.class.getSimpleName();
    private Context context;
    private Intent intent;
    private int AppWidgetId;
    private Cursor mCursor;

    public ListRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        this.intent = intent;
        this.AppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        Log.d(TAG, "ListRemoteViewsFactory: constructor");
    }

    private void loadCursor() {
        mCursor = MovieProviderHelper.getInstance().getFilledCursor();
        if(mCursor == null)
            Log.d(TAG, "loadCursor: cursor is null");
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "onCreate: widget service started");
        loadCursor();
    }

    @Override
    public void onDataSetChanged() {
        if (mCursor != null)
            mCursor.close();
        loadCursor();
    }

    @Override
    public void onDestroy() {
        mCursor.close();
    }

    @Override
    public int getCount() {
        return mCursor == null ? 0 : mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_movie);
        mCursor.moveToPosition(position);
       /* Picasso.with(context).load(mCursor.getString(mCursor.getColumnIndex("col_title")))
                .error(R.drawable.no_img_preview)
                .into(R.id.ivMovieIcon);*/
        //views.setImageViewResource(R.id.ivMovieIcon, R.drawable.no_img_preview);
        Log.d(TAG, "getViewAt: title "+mCursor.getString(mCursor.getColumnIndex("col_title")));
        views.setTextViewText(R.id.tvMovieName, mCursor.getString(mCursor.getColumnIndex("col_title")));
        views.setTextViewText(R.id.tvReleaseDate, mCursor.getString(mCursor.getColumnIndex("col_releaseDate")));
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}

