package com.popular_movies.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.popular_movies.MainActivity;
import com.popular_movies.R;

/**
 * Created by Gurpreet on 16-01-2017.
 */
public class MovieWidgetProvider extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            // Create an Intent to launch Activity
            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

            // Get the layout for the App Widget and attach an on-click listener
            // to the button
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_movie);
            views.setOnClickPendingIntent(R.id.widgetRoot, pendingIntent);

            //this changes the image in the imageview btnToggle, remoteViews lets you use setText
            // and a couple of other to manipulate certain widget items
            //  UPDATE NEW DATA EVERYTIME FROM CONTENT PROVIDER OR DATABASE
            views.setImageViewResource(R.id.ivMovieIcon, R.drawable.no_img_preview);
            views.setTextViewText(R.id.tvMovieName, "aaa");
            views.setTextViewText(R.id.tvReleaseDate, "123");

            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
