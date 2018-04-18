package com.reshmi.james.popularmovies.util;

import android.content.ContentValues;
import android.database.Cursor;

import com.reshmi.james.popularmovies.data.database.MovieDbContract.MovieEntry;
import com.reshmi.james.popularmovies.data.network.model.Movie;

import java.util.ArrayList;
import java.util.List;

public final class ProviderUtils {

    private ProviderUtils(){

    }

    public static List<Movie> parseMovieResponse(Cursor cursor){

        int size = cursor.getCount();
        if(size!=0) {
            List<Movie> movies = new ArrayList<Movie>();
            cursor.moveToFirst();
            for (int i = 0; i < size; i++) {

                movies.add(new Movie(
                        cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_MOVIE_ID)),
                        cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_TITLE)),
                        cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_POSTER_PATH)),
                        cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_OVERVIEW)),
                        cursor.getFloat(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_VOTE_AVERAGE)),
                        cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_RELEASE_DATE))
                ));

                cursor.moveToNext();
            }

            return movies;
        }

        return null;
    }

    public static ContentValues getContentValues(Movie movie){
        ContentValues cv = new ContentValues();
        cv.put(MovieEntry.COLUMN_NAME_MOVIE_ID, movie.getId());
        cv.put(MovieEntry.COLUMN_NAME_TITLE, movie.getTitle());
        cv.put(MovieEntry.COLUMN_NAME_POSTER_PATH, movie.getPosterPath());
        cv.put(MovieEntry.COLUMN_NAME_OVERVIEW, movie.getOverview());
        cv.put(MovieEntry.COLUMN_NAME_VOTE_AVERAGE, movie.getVoteAverage());
        cv.put(MovieEntry.COLUMN_NAME_RELEASE_DATE, movie.getReleaseDate());
        return cv;
    }
}
