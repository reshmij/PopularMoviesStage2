package com.reshmi.james.popularmovies.util;

public final class FormatUtils {

    private static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/";
    private static final String POSTER_SIZE="w185/";

    private FormatUtils(){}

    public static String getCompleteUrl(String relativeUrl){
        return BASE_POSTER_URL + POSTER_SIZE + relativeUrl;
    }

    public static String formatRatingString(float rating){
        return rating+"/"+10;
    }
}
