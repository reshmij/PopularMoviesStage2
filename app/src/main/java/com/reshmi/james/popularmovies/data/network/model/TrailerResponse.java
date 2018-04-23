package com.reshmi.james.popularmovies.data.network.model;

import com.google.gson.annotations.SerializedName;


@SuppressWarnings({"CanBeFinal", "unused"})
public class TrailerResponse {

    @SerializedName("id")
    private long id;

    @SerializedName("results")
    private Trailer[] trailers;

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
