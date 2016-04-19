package com.popular_movies.domain;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Gurpreet on 4/9/2016.
 */
public class ReviewData implements Parcelable{
    private String author;
    private String content;

    public ReviewData(Parcel in) {
        author = in.readString();
        content = in.readString();
    }

    public static final Creator<ReviewData> CREATOR = new Creator<ReviewData>() {
        @Override
        public ReviewData createFromParcel(Parcel in) {
            return new ReviewData(in);
        }

        @Override
        public ReviewData[] newArray(int size) {
            return new ReviewData[size];
        }
    };

    public String getAuthor() { return author; }

    public void setAuthor(String author) { this.author = author; }

    public String getContent() { return content; }

    public void setContent(String content) { this.content = content; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(author);
        dest.writeString(content);
    }
}
