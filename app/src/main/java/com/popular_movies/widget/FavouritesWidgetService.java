package com.popular_movies.widget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.widget.RemoteViews;

import com.popular_movies.R;
import com.popular_movies.domain.MovieDataTable;

import java.io.InputStream;
import java.util.Random;

/**
 * Created by Gurpreet on 17-01-2017.
 */

public class FavouritesWidgetService extends Service {

    private int mID;
    private String mReleaseDate;
    private String mTitle;
    private String mPicture;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this.getApplicationContext());

        int[] allWidgetIds = intent.getIntArrayExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS);

        ComponentName thisWidget = new ComponentName(getApplicationContext(), MovieWidgetProvider.class);
        int[] allWidgetIds2 = appWidgetManager.getAppWidgetIds(thisWidget);

        for (int widgetId : allWidgetIds) {

            // Select a random employee
            mID = (new Random().nextInt(11) + 1);

            Uri uri = ContentUris.withAppendedId(MovieDataTable.CONTENT_URI, mID);
            String[] projection = {MovieDataTable.FIELD_COL_POSTER_PATH, MovieDataTable.FIELD_COL_TITLE,
                    MovieDataTable.FIELD_COL_RELEASE_DATE };
            String selection = null;
            String[] selectionArgs = null;
            String sortOrder = null;

            Cursor cursor = this.getApplicationContext().getContentResolver().query(uri, projection, selection, selectionArgs, sortOrder);

            if (cursor != null && cursor.getCount() > 0) {
                cursor.moveToFirst();
                //mName = cursor.getString(cursor.getColumnIndex(EmployeeDatabase.COLUMN_LASTNAME)) + ", " + cursor.getString(cursor.getColumnIndex(EmployeeDatabase.COLUMN_FIRSTNAME));
                mTitle = cursor.getString(cursor.getColumnIndex(MovieDataTable.FIELD_COL_TITLE));
                mReleaseDate = cursor.getString(cursor.getColumnIndex(MovieDataTable.FIELD_COL_RELEASE_DATE));
                mPicture = cursor.getString(cursor.getColumnIndex(MovieDataTable.FIELD_COL_POSTER_PATH));
            } else {
                mTitle = "empty cursor";
            }

            cursor.close();

            RemoteViews remoteViews = new RemoteViews(this.getApplicationContext().getPackageName(), R.layout.widget_movie);

            // Set the text
            remoteViews.setTextViewText(R.id.tvMovieName, mTitle);
            remoteViews.setTextViewText(R.id.tvReleaseDate, mReleaseDate);
            //remoteViews.setTextViewText(R.id.appwidget_layout_department, mDepartment);

            /*InputStream is;
            try {
                is = getAssets().open("pics/" + mPicture );
                Bitmap bit = BitmapFactory.decodeStream(is);

                remoteViews.setImageViewBitmap(R.id.appwidget_layout_picture, bit);
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            // Register an onClickListener
            Intent clickIntent = new Intent(this.getApplicationContext(), MovieWidgetProvider.class);

            clickIntent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            clickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, allWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, clickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.widgetRoot, pendingIntent);
            appWidgetManager.updateAppWidget(widgetId, remoteViews);
            //

        }
        stopSelf();

        super.onStart(intent, startId);

        return super.onStartCommand(intent, flags, startId);
    }
}
