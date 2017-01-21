package com.popular_movies.util;

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
                .setDefaultFontPath("fonts/Brandon_reg.otf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );
    }
}
