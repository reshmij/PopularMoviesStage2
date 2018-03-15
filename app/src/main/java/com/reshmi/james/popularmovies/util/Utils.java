package com.reshmi.james.popularmovies.util;

import com.reshmi.james.popularmovies.rest.RestApiClient;

/**
 * Created by reshmijames on 3/15/18.
 */

public class Utils {

    private static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/";
    private static final String POSTER_SIZE="w185/";

    public static String getCompleteUrl(String relativeUrl){
        return BASE_POSTER_URL + POSTER_SIZE + relativeUrl;
    }
}
