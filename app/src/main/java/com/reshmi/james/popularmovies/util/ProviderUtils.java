package com.reshmi.james.popularmovies.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;

import com.reshmi.james.popularmovies.data.MoviesDataSource;
import com.reshmi.james.popularmovies.data.database.MovieDbContract;
import com.reshmi.james.popularmovies.data.database.MovieDbContract.MovieEntry;
import com.reshmi.james.popularmovies.data.network.model.Movie;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public final class ProviderUtils {

    private ProviderUtils(){

    }

    public static class InsertMovieTask extends AsyncTask<Movie, Void, Void> {

        private final WeakReference<ContentResolver> mContentResolverReference;
        public InsertMovieTask(ContentResolver contentResolver){
            mContentResolverReference = new WeakReference<>(contentResolver);
        }

        @Override
        protected Void doInBackground(Movie... params) {
            Movie movie = params[0];
            ContentResolver contentResolver = mContentResolverReference.get();
            contentResolver.insert(MovieDbContract.MovieEntry.CONTENT_URI, ProviderUtils.getContentValues(movie));
            return null;
        }
    }

    public static class DeleteMovieTask extends AsyncTask<Long, Void, Void> {

        private final WeakReference<ContentResolver> mContentResolverReference;
        public DeleteMovieTask(ContentResolver contentResolver){
            mContentResolverReference = new WeakReference<>(contentResolver);
        }

        @Override
        protected Void doInBackground(Long... params) {
            long movieId = params[0];
            ContentResolver contentResolver = mContentResolverReference.get();
            String selection = MovieDbContract.MovieEntry.COLUMN_NAME_MOVIE_ID+"=?";
            String[] selectionArgs = {String.valueOf(movieId)};
            contentResolver.delete(MovieDbContract.MovieEntry.CONTENT_URI,selection, selectionArgs);
            return null;
        }
    }

    public static class GetMovieTask extends AsyncTask<Long, Void, Cursor> {

        private final WeakReference<ContentResolver> mContentResolverReference;
        final MoviesDataSource.MovieStatusCallback mCallback;
        public GetMovieTask(ContentResolver contentResolver, final MoviesDataSource.MovieStatusCallback callback){
            mContentResolverReference = new WeakReference<>(contentResolver);
            mCallback = callback;
        }

        @Override
        protected Cursor doInBackground(Long... params) {
            long movieId = params[0];
            ContentResolver contentResolver = mContentResolverReference.get();
            String selection = MovieDbContract.MovieEntry.COLUMN_NAME_MOVIE_ID+"=?";
            String[] selectionArgs = {String.valueOf(movieId)};
            return contentResolver.query(MovieDbContract.MovieEntry.CONTENT_URI, null, selection, selectionArgs, null);
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            super.onPostExecute(cursor);
            mCallback.onStatusUpdate(cursor);
        }
    }

    public static List<Movie> parseMovieResponse(Cursor cursor){

        int size = cursor.getCount();
        if(size!=0) {
            List<Movie> movies = new ArrayList<>();
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

    private static ContentValues getContentValues(Movie movie){
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
