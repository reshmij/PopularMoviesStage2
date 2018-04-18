package com.reshmi.james.popularmovies;

import android.content.Context;

import com.reshmi.james.popularmovies.data.MoviesRepository;
import com.reshmi.james.popularmovies.data.network.RestApiClient;
import com.reshmi.james.popularmovies.data.network.RestEndpointInterface;

public final class Injector {

    private Injector(){}

    public static MoviesRepository getMoviesRepository(Context context){
        return MoviesRepository.getInstance(getApiService(),context.getContentResolver(),context.getString(R.string.api_key));
    }

    private static RestEndpointInterface getApiService() {
        return RestApiClient.getClient().create(RestEndpointInterface.class);
    }
}
