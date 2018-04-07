package com.reshmi.james.popularmovies.data.network.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by reshmijames on 3/21/18.
 */

public class ReviewResponse {

    @SerializedName("id")
    long id;
    @SerializedName("page")
    int page;
    @SerializedName("results")
    Review[] results;

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
