package com.popular_movies;

/**
 * Created by Gurpreet on 2/21/2016.
 */
public class MovieItem {
    private String title;
    private String description;
    private String thumbnailURL;
    private String wideThumbnailURL;
    private String userRatings;
    private String releaseDate;

    public String getReleaseDate() { return releaseDate; }

    public void setReleaseDate(String releaseDate) { this.releaseDate = releaseDate; }

    public String getUserRatings() { return userRatings; }

    public void setUserRatings(String userRatings) { this.userRatings = userRatings; }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

    public void setThumbnailURL(String thumbnail) {
        this.thumbnailURL = thumbnail;
    }

    public String getWideThumbnailURL() {
        return wideThumbnailURL;
    }

    public void setWideThumbnailURL(String wideThumbnail) {this.wideThumbnailURL = wideThumbnail; }
}
