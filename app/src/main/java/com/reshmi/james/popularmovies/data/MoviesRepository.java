package com.reshmi.james.popularmovies.data;

import android.content.ContentResolver;
import android.support.annotation.NonNull;
import android.util.Log;

import com.reshmi.james.popularmovies.data.database.MovieDbContract;
import com.reshmi.james.popularmovies.data.network.RestEndpointInterface;
import com.reshmi.james.popularmovies.data.network.model.Movie;
import com.reshmi.james.popularmovies.data.network.model.MoviesResponse;
import com.reshmi.james.popularmovies.util.ProviderUtils;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesRepository implements MoviesDataSource {

    private static final String TAG="MoviesRepository";
    private static MoviesRepository sInstance;
    RestEndpointInterface mApiService;
    ContentResolver mContentResolver;
    String mApiKey;

    public static MoviesRepository getInstance(RestEndpointInterface apiService, ContentResolver contentResolver, String apiKey){
        if(sInstance == null){
            sInstance = new MoviesRepository(apiService, contentResolver, apiKey);
        }
        return sInstance;
    }

    private MoviesRepository(RestEndpointInterface apiService, ContentResolver contentResolver, String apiKey){
        mApiService = apiService;
        mContentResolver = contentResolver;
        mApiKey = apiKey;
    }

    @Override
    public void getPopularMovies(@NonNull final GetMoviesCallback callback) {
        Call<MoviesResponse> call = mApiService.getPopularMovies(mApiKey);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                try{
                    callback.onMoviesLoaded(Arrays.asList(response.body().getResults()));
                }
                catch (Exception e){
                    Log.e(TAG,response.message());
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getTopRatedMovies(@NonNull final GetMoviesCallback callback) {
        Call<MoviesResponse> call = mApiService.getTopRatedMovies(mApiKey);
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                try{
                    callback.onMoviesLoaded(Arrays.asList(response.body().getResults()));
                }
                catch (Exception e){
                    Log.e(TAG,response.message());
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void insertFavoriteMovie(@NonNull Movie movie) {
        mContentResolver.insert(MovieDbContract.MovieEntry.CONTENT_URI, ProviderUtils.getContentValues(movie));
    }

    @Override
    public void deleteFavoriteMovie(@NonNull String selection, String[] selectionArgs) {
        mContentResolver.delete(MovieDbContract.MovieEntry.CONTENT_URI,selection, selectionArgs);
    }
}
