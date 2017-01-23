package com.popular_movies;

import android.app.Application;
import android.content.Context;

/**
 * Created by Gurpreet Singh on 10/25/2015.
 */
public class MyApplication extends Application {

    private static MyApplication myApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        myApplication = this;
    }

    public static MyApplication getInstance(){
        return myApplication;
    }

}
