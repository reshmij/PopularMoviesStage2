package com.reshmi.james.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.reshmi.james.popularmovies.data.database.MovieDbContract;
import com.reshmi.james.popularmovies.data.database.MovieDbHelper;


public class MovieContentProvider extends ContentProvider {

    private static final int MOVIES=100;
    private static final int MOVIE_WITH_ID=101;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(MovieDbContract.AUTHORITY, MovieDbContract.PATH_MOVIES, MOVIES);
        uriMatcher.addURI(MovieDbContract.AUTHORITY, MovieDbContract.PATH_MOVIES+"/#", MOVIE_WITH_ID);
        return uriMatcher;
    }

    private MovieDbHelper mDbHelper;
    
    @Override
    public boolean onCreate() {
        mDbHelper = new MovieDbHelper(getContext());
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        return db != null;
    }

    @Override
    public void shutdown() {
        mDbHelper.close();
        super.shutdown();
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);
        Cursor cursor;

        switch(match){
            case MOVIES:
                cursor = db.query(MovieDbContract.MovieEntry.TABLE_NAME,projection,selection, selectionArgs,  null,null,sortOrder);
                break;

            case MOVIE_WITH_ID:
                String mSelection = MovieDbContract.MovieEntry._ID +"=?";
                String[] mSelectionArgs = new String[]{uri.getPathSegments().get(1)};
                cursor = db.query( MovieDbContract.MovieEntry.TABLE_NAME, projection,mSelection, mSelectionArgs,null,null,sortOrder);
                break;


            default:
                throw new android.database.SQLException("Failed to query: Invalid Uri " + uri);
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIES:
                // directory
                return "vnd.android.cursor.dir" + "/" + MovieDbContract.AUTHORITY + "/" + MovieDbContract.PATH_MOVIES;
            case MOVIE_WITH_ID:
                // single item type
                return "vnd.android.cursor.item" + "/" + MovieDbContract.AUTHORITY + "/" + MovieDbContract.PATH_MOVIES;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        final SQLiteDatabase db = mDbHelper.getWritableDatabase();
        int match = sUriMatcher.match(uri);

        Uri returnUri;
        switch(match){
            case MOVIES: {
                    long id = db.insert(MovieDbContract.MovieEntry.TABLE_NAME, null, contentValues);
                    if (id > 0) {
                        returnUri = ContentUris.withAppendedId(MovieDbContract.MovieEntry.CONTENT_URI, id);
                    } else {
                        throw new android.database.SQLException("Failed to insert row into " + uri);
                    }
                }
                break;

            default:
                throw new android.database.SQLException("Failed to insert: Invalid Uri " + uri);
        }


        //noinspection ConstantConditions
        getContext().getContentResolver().notifyChange(uri, null);
        db.close();
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count;
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)){
            case MOVIES:
                count = db.delete(MovieDbContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case MOVIE_WITH_ID:
                String mSelection = MovieDbContract.MovieEntry._ID +"=?";
                String[] mSelectionArgs = new String[]{uri.getPathSegments().get(1)};
                count = db.delete( MovieDbContract.MovieEntry.TABLE_NAME, mSelection, mSelectionArgs);
                break;

            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        db.close();
        if(count!=0) {
            //noinspection ConstantConditions
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count;
        final SQLiteDatabase db = mDbHelper.getWritableDatabase();

        switch (sUriMatcher.match(uri)){
            case MOVIES:
                count = db.update(MovieDbContract.MovieEntry.TABLE_NAME, contentValues,selection, selectionArgs);
                break;

            case MOVIE_WITH_ID:
                String mSelection = MovieDbContract.MovieEntry._ID +"=?";
                String[] mSelectionArgs = new String[]{uri.getPathSegments().get(1)};
                count = db.update( MovieDbContract.MovieEntry.TABLE_NAME, contentValues,mSelection,mSelectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        //noinspection ConstantConditions
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }
}
