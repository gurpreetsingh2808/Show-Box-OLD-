package com.popular_movies.widget;

import com.popular_movies.domain.MovieData;

/**
 * Created by Gurpreet on 19-01-2017.
 */
public class WidgetItem extends MovieData {

    String mId;
    String mTitle;
    String mReleaseDate;
    String mPicture;

    public WidgetItem(String mId, String mTitle, String mReleaseDate, String mPicture) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mReleaseDate = mReleaseDate;
        this.mPicture = mPicture;
    }
}
