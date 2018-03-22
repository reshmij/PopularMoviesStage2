package com.reshmi.james.popularmovies.model;

import com.google.gson.annotations.SerializedName;


public class TrailerResponse {

    @SerializedName("id")
    long id;

    @SerializedName("results")
    Trailer[] trailers;

    public TrailerResponse(long id, Trailer[] trailers) {
        this.id = id;
        this.trailers = trailers;
    }

    public long getId() {
        return id;
    }

    public Trailer[] getTrailers() {
        return trailers;
    }
}
