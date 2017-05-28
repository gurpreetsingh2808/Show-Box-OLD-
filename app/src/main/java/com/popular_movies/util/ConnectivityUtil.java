package com.popular_movies.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * <code>ConnectivityUtil</code> provides various utility methods on checking internet connectivity
 *
 * @author Gurpreet Singh
 * @since 30-Mar-2017
 */
public class ConnectivityUtil {

    public static boolean isNetworkConnected(Context context) {

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnected();
    }
}
