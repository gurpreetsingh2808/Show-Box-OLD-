package com.popular_movies.domain;

/**
 * Created by Gurpreet on 22-01-2017.
 */

public class Trailer {

    private String id;
    private String name;
    private String key;
    private String site;
    private Integer size;
    private String type;

    public Trailer() {
    }

    public Trailer(String id, String key, String site, String type) {
        this.id = id;
        this.key = key;
        this.site = site;
        this.type = type;
    }

    public String getId() {

        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
