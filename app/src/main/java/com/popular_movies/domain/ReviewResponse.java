package com.popular_movies.domain;

import java.util.List;

/**
 * Created by Gurpreet on 21-01-2017.
 */

public class ReviewResponse {

    private Integer id;
    private List<Review> results;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Review> getResults() {
        return results;
    }

    public void setResults(List<Review> results) {
        this.results = results;
    }
}
