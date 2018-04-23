package com.reshmi.james.popularmovies.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.Toast;

import com.reshmi.james.popularmovies.data.database.MovieDbContract;
import com.reshmi.james.popularmovies.data.database.MovieDbHelper;
import com.reshmi.james.popularmovies.data.network.model.Movie;
import com.reshmi.james.popularmovies.data.network.model.MoviesResponse;

import java.util.ArrayList;
import java.util.List;

import static com.reshmi.james.popularmovies.data.database.MovieDbContract.MovieEntry;


final class TestUtils {

    private TestUtils(){}

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

    @SuppressWarnings("unused")
    public static void testDb(Context context) {

        MovieDbHelper dbHelper = new MovieDbHelper(context);

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues cv = new ContentValues();
        cv.put(MovieEntry.COLUMN_NAME_VOTE_AVERAGE, 6.2);
        cv.put(MovieEntry.COLUMN_NAME_MOVIE_ID, 337167);
        cv.put(MovieEntry.COLUMN_NAME_TITLE, "Fifty Shades Freed");
        cv.put(MovieEntry.COLUMN_NAME_OVERVIEW, "Believing they have left behind shadowy figures from their past, newlyweds Christian and Ana fully embrace an inextricable connection and shared life of luxury. But just as she steps into her role as Mrs. Grey and he relaxes into an unfamiliar stability, new threats could jeopardize their happy ending before it even begins.");
        cv.put(MovieEntry.COLUMN_NAME_RELEASE_DATE, "2018-02-07");
        cv.put(MovieEntry.COLUMN_NAME_POSTER_PATH, "/jjPJ4s3DWZZvI4vw8Xfi4Vqa1Q8.jpg");
        long rowId = db.insert(MovieDbContract.MovieEntry.TABLE_NAME, null, cv);


        if (rowId == -1) Toast.makeText(context, "Error inserting data", Toast.LENGTH_SHORT).show();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                BaseColumns._ID,
                MovieEntry.COLUMN_NAME_TITLE,
                MovieEntry.COLUMN_NAME_POSTER_PATH,
                MovieEntry.COLUMN_NAME_VOTE_AVERAGE
        };

        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                MovieDbContract.MovieEntry.COLUMN_NAME_TITLE + " DESC";

        Cursor cursor = db.query(
                MovieDbContract.MovieEntry.TABLE_NAME,   // The table to query
                null,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        Toast.makeText(context, "Found " + cursor.getCount() + " item(s) in db", Toast.LENGTH_SHORT).show();

        List<Movie> movies = ProviderUtils.parseMovieResponse(cursor);
        if(movies!=null) {
            Log.d("cursor", "Found " + movies.size() + " movies.");
        }
        cursor.close();
    }
}
