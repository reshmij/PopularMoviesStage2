package com.reshmi.james.popularmovies.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

import com.reshmi.james.popularmovies.R;

public class Utils {

    private static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/";
    private static final String POSTER_SIZE="w185/";

    public static String getCompleteUrl(String relativeUrl){
        return BASE_POSTER_URL + POSTER_SIZE + relativeUrl;
    }

    public static String formatRatingString(float rating){
        return rating+"/"+10;
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public static boolean isOnline(Context context){
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm!=null && cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }

    public static void onConnectionError(Context context){
        Toast.makeText( context, R.string.connection_error, Toast.LENGTH_LONG).show();
    }

    public static void onError(Context context){
        Toast.makeText( context, R.string.error_detail, Toast.LENGTH_LONG).show();
    }


}
