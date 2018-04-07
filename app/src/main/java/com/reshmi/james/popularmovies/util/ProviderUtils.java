package com.reshmi.james.popularmovies.util;

import android.content.ContentValues;
import android.database.Cursor;

import com.reshmi.james.popularmovies.data.database.MovieDbContract.MovieEntry;
import com.reshmi.james.popularmovies.data.network.model.Movie;
import com.reshmi.james.popularmovies.data.network.model.MoviesResponse;

import java.util.ArrayList;
import java.util.List;

public class ProviderUtils {

    private ProviderUtils(){

    }

    public static List<Movie> parseMovieResponse(Cursor cursor){

        int size = cursor.getCount();
        if(size!=0) {
            List<Movie> movies = new ArrayList<Movie>();
            cursor.moveToFirst();
            for (int i = 0; i < size; i++) {
                movies.add(new Movie(
                        cursor.getFloat(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_VOTE_AVERAGE)),
                        cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_BACKDROP_PATH)),
                        cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_ADULT))>0,
                        cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_MOVIE_ID)),
                        cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_TITLE)),
                        cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_OVERVIEW)),
                        cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_ORIGINAL_LANGUAGE)),
                        null,
                        cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_RELEASE_DATE)),
                        cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_ORIGINAL_TITLE)),
                        cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_VOTE_COUNT)),
                        cursor.getString(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_POSTER_PATH)),
                        cursor.getInt(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_VIDEO))>0,
                        cursor.getDouble(cursor.getColumnIndex(MovieEntry.COLUMN_NAME_POPULARITY))
                ));

                cursor.moveToNext();
            }

            return movies;
        }

        return null;
    }

    public static ContentValues getContentValues(Movie movie){
        ContentValues cv = new ContentValues();
        cv.put(MovieEntry.COLUMN_NAME_VOTE_AVERAGE, movie.getVoteAverage());
        cv.put(MovieEntry.COLUMN_NAME_POSTER_PATH, movie.getPosterPath());
        cv.put(MovieEntry.COLUMN_NAME_BACKDROP_PATH, movie.getBackdropPath());
        cv.put(MovieEntry.COLUMN_NAME_TITLE, movie.getTitle());

        cv.put(MovieEntry.COLUMN_NAME_ORIGINAL_TITLE, movie.getOriginalTitle());
        cv.put(MovieEntry.COLUMN_NAME_RELEASE_DATE, movie.getReleaseDate());
        cv.put(MovieEntry.COLUMN_NAME_OVERVIEW, movie.getOverview());
        cv.put(MovieEntry.COLUMN_NAME_ADULT, movie.isAdult()?1:0);

        cv.put(MovieEntry.COLUMN_NAME_MOVIE_ID, movie.getId());
        cv.put(MovieEntry.COLUMN_NAME_VOTE_COUNT, movie.getVoteCount());
        cv.put(MovieEntry.COLUMN_NAME_VIDEO, movie.isVideo()?1:0);
        cv.put(MovieEntry.COLUMN_NAME_POPULARITY, movie.getPopularity());

        return cv;
    }
}
