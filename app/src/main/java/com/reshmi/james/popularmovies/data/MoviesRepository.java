package com.reshmi.james.popularmovies.data;

import android.content.ContentResolver;
import android.support.annotation.NonNull;
import android.util.Log;

import com.reshmi.james.popularmovies.data.network.RestEndpointInterface;
import com.reshmi.james.popularmovies.data.network.model.Movie;
import com.reshmi.james.popularmovies.data.network.model.MoviesResponse;
import com.reshmi.james.popularmovies.data.network.model.ReviewResponse;
import com.reshmi.james.popularmovies.data.network.model.TrailerResponse;
import com.reshmi.james.popularmovies.util.ProviderUtils;

import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesRepository implements MoviesDataSource {

    private static final String TAG="MoviesRepository";
    private static MoviesRepository sInstance;
    private final RestEndpointInterface mApiService;
    private final ContentResolver mContentResolver;
    private final String mApiKey;

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
                    //noinspection ConstantConditions
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
                    //noinspection ConstantConditions
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
    public void insertFavoriteMovie(@NonNull final Movie movie) {
        new ProviderUtils.InsertMovieTask(mContentResolver).execute(movie);
    }

    @Override
    public void deleteFavoriteMovie(long movieId) {
        new ProviderUtils.DeleteMovieTask(mContentResolver).execute(movieId);
    }

    @Override
    public void getMovie(long movieId, final MovieStatusCallback callback) {
        new ProviderUtils.GetMovieTask(mContentResolver,callback).execute(movieId);
    }

    @Override
    public void getTrailers(final long id, @NonNull final GetTrailersCallback callback) {
        Call<TrailerResponse> call = mApiService.getMovieTrailers(id,mApiKey);
        call.enqueue(new Callback<TrailerResponse>() {
            @Override
            public void onResponse(@NonNull Call<TrailerResponse> call, @NonNull Response<TrailerResponse> response) {
                try{
                    //noinspection ConstantConditions
                    callback.onTrailersLoaded(Arrays.asList(response.body().getTrailers()));
                }
                catch (Exception e){
                    Log.e(TAG,response.message());
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(@NonNull Call<TrailerResponse> call, @NonNull Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getReviews(long id, @NonNull final GetReviewsCallback callback) {

        Call<ReviewResponse> call = mApiService.getMovieReviews(id,mApiKey);
        call.enqueue(new Callback<ReviewResponse>() {
            @Override
            public void onResponse(@NonNull Call<ReviewResponse> call, @NonNull Response<ReviewResponse> response) {
                try{
                    //noinspection ConstantConditions
                    callback.onReviewsLoaded(Arrays.asList(response.body().getResults()));
                }
                catch (Exception e){
                    Log.e(TAG,response.message());
                    callback.onDataNotAvailable();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ReviewResponse> call, @NonNull Throwable t) {
                callback.onDataNotAvailable();
            }
        });
    }


}
