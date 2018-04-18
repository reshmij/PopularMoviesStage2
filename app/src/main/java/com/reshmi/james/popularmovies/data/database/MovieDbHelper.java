package com.reshmi.james.popularmovies.data.database;


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.reshmi.james.popularmovies.data.database.MovieDbContract.MovieEntry;
import com.reshmi.james.popularmovies.data.network.model.Movie;

public class MovieDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "FavoriteMovies.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + MovieEntry.TABLE_NAME + " (" +
                    MovieEntry._ID + " INTEGER PRIMARY KEY," +
                    MovieEntry.COLUMN_NAME_MOVIE_ID + " INTEGER," +
                    MovieEntry.COLUMN_NAME_TITLE + " TEXT," +
                    MovieEntry.COLUMN_NAME_OVERVIEW + " TEXT," +
                    MovieEntry.COLUMN_NAME_RELEASE_DATE + " TEXT," +
                    MovieEntry.COLUMN_NAME_VOTE_AVERAGE + " REAL, " +
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

    public boolean isMovieMarkedAsFavorite(Movie movie){
        SQLiteDatabase db = getReadableDatabase();
        String[] projection = {
                MovieEntry.COLUMN_NAME_MOVIE_ID
        };
        String selection = MovieEntry.COLUMN_NAME_MOVIE_ID + " = ?";
        String[] selectionArgs = { String.valueOf(movie.getId()) };
        Cursor cursor = db.query(MovieEntry.TABLE_NAME, projection,selection,selectionArgs,null,null,null);
        return (cursor.getCount()>0);
    }
}
