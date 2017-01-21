package com.popular_movies.domain;

import java.util.List;

/**
 * Created by Gurpreet on 21-01-2017.
 */

public class TrailerResponse {

    private Integer id;
    private List<Trailer> results;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<Trailer> getResults() {
        return results;
    }

    public void setResults(List<Trailer> results) {
        this.results = results;
    }
}
