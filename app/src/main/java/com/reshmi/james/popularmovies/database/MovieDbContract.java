package com.reshmi.james.popularmovies.database;

import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;

import com.reshmi.james.popularmovies.model.Movie;
import com.reshmi.james.popularmovies.model.MoviesResponse;

/**
 * Created by reshmijames on 3/22/18.
 */

public final class MovieDbContract {

    private MovieDbContract() {
    }

    public static final String AUTHORITY="com.reshmi.james.popularmovies";
    public static final Uri BASE_CONTENT_URI= Uri.parse("content://"+AUTHORITY);
    public static final String PATH_MOVIES = "movies";

    /* Inner class that defines the table contents */
    public static class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI=BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIES).build();

        public static final String TABLE_NAME = "movies";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_POSTER_PATH = "poster_path";
        public static final String COLUMN_NAME_VOTE_AVERAGE = "vote_average";
        public static final String COLUMN_NAME_BACKDROP_PATH = "backdrop_path";
        public static final String COLUMN_NAME_ADULT = "adult";
        public static final String COLUMN_NAME_MOVIE_ID = "id";
        public static final String COLUMN_NAME_OVERVIEW = "overview";
        public static final String COLUMN_NAME_ORIGINAL_LANGUAGE = "language";
        public static final String COLUMN_NAME_GENRE_IDS = "genre_ids";
        public static final String COLUMN_NAME_RELEASE_DATE = "release_date";
        public static final String COLUMN_NAME_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_NAME_VOTE_COUNT = "vote_count";
        public static final String COLUMN_NAME_VIDEO = "video";
        public static final String COLUMN_NAME_POPULARITY = "popularity";
    }

    public static MoviesResponse parseMovieResponse(Cursor cursor){

        int size = cursor.getCount();
        if(size!=0) {
            Movie[] movies = new Movie[size];
            cursor.moveToFirst();
            for (int i = 0; i < size; i++) {
                movies[i] = new Movie(
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
                );

                cursor.moveToNext();
            }

            return new MoviesResponse(movies);
        }

        return null;
    }

}
