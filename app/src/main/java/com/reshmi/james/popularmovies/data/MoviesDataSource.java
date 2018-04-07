package com.reshmi.james.popularmovies.data;

import android.support.annotation.NonNull;

import com.reshmi.james.popularmovies.data.network.model.Movie;

import java.util.List;

public interface MoviesDataSource {

    interface GetMoviesCallback{
        void onMoviesLoaded(List<Movie> movies);
        void onDataNotAvailable();
    }

    interface GetMovieCallback{
        void onMovieLoaded(List<Movie> movies);
        void onDataNotAvailable();
    }

    void getPopularMovies(@NonNull  GetMoviesCallback callback);
    void getTopRatedMovies(@NonNull  GetMoviesCallback callback);

    void insertFavoriteMovie(@NonNull Movie movie );
    public void deleteFavoriteMovie(@NonNull String selection, String[] selectionArgs);
}
