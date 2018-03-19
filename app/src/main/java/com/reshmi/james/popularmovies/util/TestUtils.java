package com.reshmi.james.popularmovies.util;

import com.reshmi.james.popularmovies.model.Movie;
import com.reshmi.james.popularmovies.model.MoviesResponse;

import java.util.ArrayList;


 class TestUtils {


    @SuppressWarnings("unused")
    public static MoviesResponse loadPopularMovies ( ){

        ArrayList<Movie> movies = new ArrayList<>();

        movies.add(new Movie("Zootopia","https://image.tmdb.org/t/p/w185/sM33SANp9z6rXW8Itn7NnG1GOEs.jpg"));
        movies.add(new Movie("Thor: Ragnarok","https://image.tmdb.org/t/p/w185/rzRwTcFvttcN1ZpX2xv4j3tSdJu.jpg"));
        movies.add(new Movie("Black Panther","https://image.tmdb.org/t/p/w185/uxzzxijgPIY7slzFvMotPv8wjKA.jpg"));
        movies.add(new Movie("The shape of water","https://image.tmdb.org/t/p/w185/k4FwHlMhuRR5BISY2Gm2QZHlH5Q.jpg"));
        movies.add(new Movie("Star Wars: The Last Jedi","https://image.tmdb.org/t/p/w185/kOVEVeg59E0wsnXmF9nrh6OmWII.jpg"));

        return new MoviesResponse(movies.toArray(new Movie[movies.size()]));

    }

}
