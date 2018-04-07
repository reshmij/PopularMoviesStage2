package com.reshmi.james.popularmovies.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.reshmi.james.popularmovies.data.database.MovieDbContract.MovieEntry;
import com.reshmi.james.popularmovies.data.network.model.Movie;

/**
 * Created by reshmijames on 3/22/18.
 */

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "FavoriteMovies.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                    MovieEntry._ID + " INTEGER PRIMARY KEY," +
                    MovieEntry.COLUMN_NAME_MOVIE_ID + " INTEGER," +
                    MovieEntry.COLUMN_NAME_TITLE + " TEXT," +
                    MovieEntry.COLUMN_NAME_BACKDROP_PATH + " TEXT," +
                    MovieEntry.COLUMN_NAME_OVERVIEW + " TEXT," +
                    MovieEntry.COLUMN_NAME_ORIGINAL_LANGUAGE + " TEXT," +
                    MovieEntry.COLUMN_NAME_RELEASE_DATE + " TEXT," +
                    MovieEntry.COLUMN_NAME_ORIGINAL_TITLE + " TEXT," +
                    MovieEntry.COLUMN_NAME_ADULT + " INTEGER, " +
                    MovieEntry.COLUMN_NAME_VIDEO + " INTEGER, " +
                    MovieEntry.COLUMN_NAME_VOTE_COUNT + " INTEGER, " +
                    MovieEntry.COLUMN_NAME_VOTE_AVERAGE + " REAL, " +
                    MovieEntry.COLUMN_NAME_POPULARITY + " REAL, " +
                    MovieEntry.COLUMN_NAME_POSTER_PATH + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME;

    public MovieDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }

    public void addToFavorites(Movie movie){

        ContentValues cv = new ContentValues();
        cv.put(MovieEntry.COLUMN_NAME_VOTE_AVERAGE, movie.getVoteAverage());
        cv.put(MovieEntry.COLUMN_NAME_BACKDROP_PATH, movie.getPosterPath());
        cv.put(MovieEntry.COLUMN_NAME_ADULT, movie.isAdult()?1:0);
        cv.put(MovieEntry.COLUMN_NAME_MOVIE_ID, movie.getId());
        cv.put(MovieEntry.COLUMN_NAME_TITLE, movie.getTitle());
        cv.put(MovieEntry.COLUMN_NAME_OVERVIEW, movie.getOverview());
        cv.put(MovieEntry.COLUMN_NAME_RELEASE_DATE, movie.getReleaseDate());
        cv.put(MovieEntry.COLUMN_NAME_ORIGINAL_TITLE, movie.getOriginalTitle());
        cv.put(MovieEntry.COLUMN_NAME_VOTE_COUNT, movie.getVoteCount());
        cv.put(MovieEntry.COLUMN_NAME_POSTER_PATH, movie.getPosterPath());
        cv.put(MovieEntry.COLUMN_NAME_VIDEO, movie.isVideo()?1:0);
        cv.put(MovieEntry.COLUMN_NAME_POPULARITY, movie.getPopularity());

        SQLiteDatabase db = getWritableDatabase();
        long rowId = db.insert(MovieEntry.TABLE_NAME, null, cv);

    }

    public void removeFromFavorites(Movie movie){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(MovieEntry.TABLE_NAME, MovieEntry.COLUMN_NAME_MOVIE_ID + "=" + movie.getId(), null);
    }

    public boolean isMovieMarkedAsFavorite(Movie movie){
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                MovieEntry.COLUMN_NAME_MOVIE_ID
        };
        // Filter results WHERE "title" = 'My Title'
        String selection = MovieEntry.COLUMN_NAME_MOVIE_ID + " = ?";
        String[] selectionArgs = { String.valueOf(movie.getId()) };


        Cursor cursor = db.query(MovieEntry.TABLE_NAME, projection,selection,selectionArgs,null,null,null);
        return (cursor.getCount()>0);
    }
}
