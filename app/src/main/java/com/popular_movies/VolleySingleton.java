package com.popular_movies;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by Gurpreet Singh on 10/25/2015.
 */
public class VolleySingleton {
    private static VolleySingleton ourInstance = new VolleySingleton();
    private RequestQueue mRequestQueue;

    public static VolleySingleton getInstance() {
        return ourInstance;
    }

    private VolleySingleton() {
        mRequestQueue= Volley.newRequestQueue(MyApplication.getInstance().getApplicationContext());
    }

    public RequestQueue getmRequestQueue()  {
        return mRequestQueue;
    }

}
