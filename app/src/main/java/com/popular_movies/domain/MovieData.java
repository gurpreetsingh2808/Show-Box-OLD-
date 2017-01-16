package com.popular_movies.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

import ckm.simple.sql_provider.annotation.SimpleSQLColumn;
import ckm.simple.sql_provider.annotation.SimpleSQLTable;

/**
 * Created by Gurpreet on 2/21/2016.
 */

@SimpleSQLTable(table = "movieData", provider = "MovieProvider")

public class MovieData implements Parcelable {

    @SimpleSQLColumn("col_id")
    public int id;
    @SimpleSQLColumn("col_title")
    public String title;
    @SimpleSQLColumn("col_description")
    public String description;
    @SimpleSQLColumn("col_thumbnail")
    public String thumbnailURL;
    @SimpleSQLColumn("col_backdrop")
    public String backdropImage;
    @SimpleSQLColumn("col_rating")
    public String userRatings;
    @SimpleSQLColumn("col_releaseDate")
    public Date releaseDate;

    public MovieData() {
    }

    public MovieData(String title, String description, String thumbnailURL, String wideThumbnailURL, String userRatings, Date release_date, int id) {
        this.title = title;
        this.description = description;
        this.thumbnailURL = thumbnailURL;
        this.backdropImage = wideThumbnailURL;
        this.userRatings = userRatings;
        this.releaseDate = release_date;
        this.id = id;
    }

    public MovieData(Parcel in) {
        title = in.readString();
        description = in.readString();
        thumbnailURL = in.readString();
        backdropImage = in.readString();
        userRatings = in.readString();
        releaseDate = new Date(in.readLong());
        ;
        id = in.readInt();
    }

    public static final Creator<MovieData> CREATOR = new Creator<MovieData>() {
        @Override
        public MovieData createFromParcel(Parcel in) {
            return new MovieData(in);
        }

        @Override
        public MovieData[] newArray(int size) {
            return new MovieData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(thumbnailURL);
        dest.writeString(backdropImage);
        dest.writeString(userRatings);
        dest.writeLong(releaseDate.getTime());
        dest.writeInt(id);
    }
}
