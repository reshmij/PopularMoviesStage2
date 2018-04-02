package com.reshmi.james.popularmovies.model;


import com.google.gson.annotations.SerializedName;

import java.util.Arrays;

public class MoviesResponse {

    @SerializedName("results")
    private Movie[] results;
    @SerializedName("page")
    private String page;
    @SerializedName("total_pages")
    private String totalPages;
    @SerializedName("total_results")
    private String totalResults;

    public MoviesResponse() {
    }

    public MoviesResponse(Movie[] results) {
        this.results = results;
    }
    public Movie[] getResults() {
        return results;
    }

    public void setResults(Movie[] results) {
        this.results = results;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(String totalPages) {
        this.totalPages = totalPages;
    }

    public String getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(String totalResults) {
        this.totalResults = totalResults;
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
