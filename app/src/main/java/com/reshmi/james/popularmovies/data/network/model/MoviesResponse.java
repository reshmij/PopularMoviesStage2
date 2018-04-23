package com.reshmi.james.popularmovies.data.network.model;


import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

@SuppressWarnings({"CanBeFinal", "unused"})
public class MoviesResponse {

    @SerializedName("results")
    private Movie[] results;
    @SerializedName("page")
    private String page;
    @SerializedName("total_pages")
    private String totalPages;
    @SerializedName("total_results")
    private String totalResults;

    public MoviesResponse(Movie[] results) {
        this.results = results;
    }
    public Movie[] getResults() {
        return results;
    }

    @Override
    public String toString() {
        return "PopularMoviesResponse{" +
                "results=" + Arrays.toString(results) +
                ", page='" + page + '\'' +
                ", totalPages='" + totalPages + '\'' +
                ", totalResults='" + totalResults + '\'' +
                '}';
    }
}
