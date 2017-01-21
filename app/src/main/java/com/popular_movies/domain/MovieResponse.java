package com.popular_movies.domain;

import java.util.List;

/**
 * Created by Gurpreet on 21-01-2017.
 */

public class MovieResponse {

    private Integer page;
    private List<MovieData> results;
    private Integer total_results;
    private Integer total_pages;


    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public List<MovieData> getResults() {
        return results;
    }

    public void setResults(List<MovieData> results) {
        this.results = results;
    }

    public Integer getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(Integer total_pages) {
        this.total_pages = total_pages;
    }

    public Integer getTotal_results() {
        return total_results;
    }

    public void setTotal_results(Integer total_results) {
        this.total_results = total_results;
    }
}
