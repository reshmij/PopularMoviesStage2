package com.reshmi.james.popularmovies.data.network.model;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings({"CanBeFinal", "unused"})
public class ReviewResponse {

    @SerializedName("id")
    private long id;
    @SerializedName("page")
    private int page;
    @SerializedName("results")
    private Review[] results;

    public ReviewResponse(long id, int page, Review[] results) {
        this.id = id;
        this.page = page;
        this.results = results;
    }

    public long getId() {
        return id;
    }

    public int getPage() {
        return page;
    }

    public Review[] getResults() {
        return results;
    }
}
