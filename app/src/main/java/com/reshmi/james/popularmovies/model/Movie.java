package com.reshmi.james.popularmovies.model;

/**
 * Created by reshmijames on 3/14/18.
 */

public class Movie {

    String originalTitle;
    String posterPath;

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public Movie(String originalTitle, String posterPath) {
        this.originalTitle = originalTitle;
        this.posterPath = posterPath;
    }
}
