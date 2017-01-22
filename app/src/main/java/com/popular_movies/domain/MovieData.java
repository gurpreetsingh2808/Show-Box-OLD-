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

    @SimpleSQLColumn("col_poster_path")
    private String poster_path;

    private Boolean adult;

    @SimpleSQLColumn("col_overview")
    private String overview;



    @SimpleSQLColumn("col_id")
    private int id;


    @SimpleSQLColumn("col_title")
    private String original_title;

    private String original_language;

    private String title;

    @SimpleSQLColumn("col_backdrop")
    private String backdrop_path;

    private String popularity;

    private String vote_count;

    private Boolean video;

    private String vote_average;





    private Integer[] genre_ids;

    @SimpleSQLColumn("col_rating")
    private String userRatings;

    @SimpleSQLColumn("col_releaseDate")
    private Date release_date;




    public MovieData() {
    }

    public MovieData(String title, String description, String thumbnailURL, String wideThumbnailURL, String userRatings, Date release_date, int id) {
        this.original_title = title;
        this.overview = description;
        this.poster_path = thumbnailURL;
        this.backdrop_path = wideThumbnailURL;
        this.userRatings = userRatings;
        this.release_date = release_date;
        this.id = id;
    }

    public MovieData(Parcel in) {
        original_title = in.readString();
        overview = in.readString();
        poster_path = in.readString();
        backdrop_path = in.readString();
        userRatings = in.readString();
        release_date = new Date(in.readLong());
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
        dest.writeString(original_title);
        dest.writeString(overview);
        dest.writeString(poster_path);
        dest.writeString(backdrop_path);
        dest.writeString(userRatings);
        if(release_date != null) {
            dest.writeLong(release_date.getTime());
        }
        dest.writeInt(id);
    }
}
