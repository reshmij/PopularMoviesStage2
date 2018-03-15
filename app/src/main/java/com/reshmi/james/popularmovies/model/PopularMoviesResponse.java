package com.reshmi.james.popularmovies.model;

import java.util.ArrayList;

/**
 * Created by reshmijames on 3/14/18.
 */

public class PopularMoviesResponse {

    ArrayList<Movie> movies;

    public PopularMoviesResponse(ArrayList<Movie> movies) {
        this.movies = movies;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public void setMovies(ArrayList<Movie> movies) {
        this.movies = movies;
    }


}
