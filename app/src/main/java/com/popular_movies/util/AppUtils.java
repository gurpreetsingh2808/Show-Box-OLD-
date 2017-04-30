package com.popular_movies.util;

import android.content.Context;
import android.content.res.Configuration;

import com.popular_movies.R;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

/**
 * Created by Gurpreet on 22-01-2017.
 */

public class AppUtils {

    /**
     * This method initializes calligraphy(fonts) in the system
     */
    public static void initializeCalligraphy() {
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Brandon_Regular.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }

    public static Boolean isTablet(Context context) {
        return context.getResources().getBoolean(R.bool.isTablet);
    }

    public static Boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }
}
