package com.popular_movies.domain;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by Gurpreet on 2/21/2016.
 */
public class MovieData implements Parcelable {
    public String title;
    public String description;
    public String thumbnailURL;
    public String wideThumbnailURL;
    public String userRatings;
    public Date releaseDate;
    public int id;

    public MovieData(String title, String description, String thumbnailURL, String wideThumbnailURL, String userRatings, Date release_date, int id) {
        this.title = title;
        this.description = description;
        this.thumbnailURL = thumbnailURL;
        this.wideThumbnailURL = wideThumbnailURL;
        this.userRatings = userRatings;
        this.releaseDate = release_date;
        this.id=id;
    }

    public MovieData(Parcel in) {
        title = in.readString();
        description = in.readString();
        thumbnailURL = in.readString();
        wideThumbnailURL = in.readString();
        userRatings = in.readString();
        releaseDate = new Date(in.readLong());;
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
        dest.writeString(wideThumbnailURL);
        dest.writeString(userRatings);
        dest.writeLong(releaseDate.getTime());
        dest.writeInt(id);
    }
}
