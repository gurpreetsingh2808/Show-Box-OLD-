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

    @SimpleSQLColumn("col_release_date")
    private Date release_date;

    private Integer[] genre_ids;

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

    @SimpleSQLColumn("col_vote_average")
    private String vote_average;



    public MovieData() {
    }

    public String getPoster_path() {
        return poster_path;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Date getRelease_date() {
        return release_date;
    }

    public void setRelease_date(Date release_date) {
        this.release_date = release_date;
    }

    public Integer[] getGenre_ids() {
        return genre_ids;
    }

    public void setGenre_ids(Integer[] genre_ids) {
        this.genre_ids = genre_ids;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public String getPopularity() {
        return popularity;
    }

    public void setPopularity(String popularity) {
        this.popularity = popularity;
    }

    public String getVote_count() {
        return vote_count;
    }

    public void setVote_count(String vote_count) {
        this.vote_count = vote_count;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public String getVote_average() {
        return vote_average;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }
/*

    public MovieData(String title, String description, String thumbnailURL, String wideThumbnailURL, String userRatings, Date release_date, int id) {
        this.original_title = title;
        this.overview = description;
        this.poster_path = thumbnailURL;
        this.backdrop_path = wideThumbnailURL;
        this.userRatings = userRatings;
        this.release_date = release_date;
        this.id = id;
    }
*/

    public MovieData(Parcel in) {
        original_title = in.readString();
        overview = in.readString();
        poster_path = in.readString();
        backdrop_path = in.readString();
        vote_average = in.readString();
        release_date = new Date(in.readLong());
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
        dest.writeString(vote_average);
        if(release_date != null) {
            dest.writeLong(release_date.getTime());
        }
        dest.writeInt(id);
    }
}
