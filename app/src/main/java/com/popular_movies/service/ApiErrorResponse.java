package com.popular_movies.service;

/**
 * <code>ApiErrorResponse</code> represents an error which is sent by the backend api
 * as a result of an API invocation
 */
public class ApiErrorResponse {

    // The status_message of error from the backend
    private String status_message;

    // The backend stacktrace ( only for development)
    private Integer status_code;

    public String getStatus_message() {
        return status_message;
    }

    public void setStatus_message(String status_message) {
        this.status_message = status_message;
    }

    public Integer getStatus_code() {
        return status_code;
    }

    public void setStatus_code(Integer status_code) {
        this.status_code = status_code;
    }
}

