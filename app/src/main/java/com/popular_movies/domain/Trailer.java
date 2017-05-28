package com.popular_movies.domain;

import android.support.annotation.NonNull;

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

    private Trailer(String id, String key, String site, String type) {
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

    public static class Builder {

        private String id;
        private String name;
        private String key;
        private String site;
        private Integer size;
        private String type;

        public Builder setId(@NonNull String id) {
            this.id = id;
            return this;
        }

        public Builder setKey(@NonNull String key) {
            this.key = key;
            return this;
        }

        public Builder setSite(@NonNull String site) {
            this.site = site;
            return this;
        }

        public Builder setType(@NonNull String type) {
            this.type = type;
            return this;
        }

        public Builder setName(@NonNull String name) {
            this.name = name;
            return this;
        }

        public Builder setSize(@NonNull Integer size) {
            this.size = size;
            return this;
        }

        public Trailer build() {
            return new Trailer(id, key, site, type);
        }

    }
}
