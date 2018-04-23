package com.reshmi.james.popularmovies.data;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.reshmi.james.popularmovies.data.database.MovieDbContract;

public class LoaderProvider {

    private final Context mContext;

    public LoaderProvider(Context context) {
        this.mContext = context;
    }

    public Loader<Cursor> createMovieLoader(){
        return new CursorLoader(mContext,
                MovieDbContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }
}
