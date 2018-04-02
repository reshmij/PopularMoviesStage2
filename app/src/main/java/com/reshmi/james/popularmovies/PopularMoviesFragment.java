package com.reshmi.james.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.reshmi.james.popularmovies.adapter.PopularMoviesGridAdapter;
import com.reshmi.james.popularmovies.database.MovieDbContract;
import com.reshmi.james.popularmovies.database.MovieDbContract.MovieEntry;
import com.reshmi.james.popularmovies.database.MovieDbHelper;
import com.reshmi.james.popularmovies.model.Movie;
import com.reshmi.james.popularmovies.model.MoviesResponse;
import com.reshmi.james.popularmovies.provider.ProviderUtils;
import com.reshmi.james.popularmovies.rest.RestApiClient;
import com.reshmi.james.popularmovies.rest.RestEndpointInterface;
import com.reshmi.james.popularmovies.util.TestUtils;
import com.reshmi.james.popularmovies.util.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopularMoviesFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "PopularMoviesFragment";
    private static final int ID_MOVIE_LOADER = 104;
    private RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_popular_movies, container, false);
        setupRecyclerView();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mRecyclerView.getContext());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        return mRecyclerView;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mRecyclerView.getContext());
        loadFromSharedPreferences(sharedPreferences, getString(R.string.sort_order_pref_key));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mRecyclerView.getContext());
        sp.unregisterOnSharedPreferenceChangeListener(this);
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(mRecyclerView.getContext(),2));
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mRecyclerView.setAdapter(new PopularMoviesGridAdapter());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG, "Shared preference changed");
        if(key.equals(getString(R.string.sort_order_pref_key))){
            loadFromSharedPreferences(sharedPreferences, key);
        }
    }

    private void loadFromSharedPreferences(SharedPreferences sp, String key){
        String sortOrder = sp.getString( key, getString(R.string.sort_by_popularity_value));
        if(sortOrder.equals(getString(R.string.sort_by_popularity_value))){
            loadPopularMovies();
        }
        else if(sortOrder.equals(getString(R.string.sort_by_rating_value))){
            loadTopRatedMovies();
        }
        else{
            loadFromDatabase();
        }
    }

    private void loadFromDatabase() {
        getActivity().getSupportLoaderManager().initLoader( ID_MOVIE_LOADER, null, this);
    }

    private void loadPopularMovies(){

        final Context context = mRecyclerView.getContext();
        if(!Utils.isOnline(context)){
            Utils.onConnectionError(context);
            return;
        }

        RestEndpointInterface apiService =
                RestApiClient.getClient().create(RestEndpointInterface.class);

        Call<MoviesResponse> call = apiService.getPopularMovies(getString(R.string.api_key));
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                PopularMoviesGridAdapter adapter = (PopularMoviesGridAdapter)mRecyclerView.getAdapter();
                adapter.setData(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                Log.e(TAG,"Error loading popular movies");
                Utils.onError(context);
            }
        });
    }

    private void loadTopRatedMovies(){

        final Context context = mRecyclerView.getContext();
        if(!Utils.isOnline(context)){
            Utils.onConnectionError(context);
            return;
        }

        RestEndpointInterface apiService =
                RestApiClient.getClient().create(RestEndpointInterface.class);

        Call<MoviesResponse> call = apiService.getTopRatedMovies(getString(R.string.api_key));
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(@NonNull Call<MoviesResponse> call, @NonNull Response<MoviesResponse> response) {
                PopularMoviesGridAdapter adapter = (PopularMoviesGridAdapter)mRecyclerView.getAdapter();
                adapter.setData(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                Log.e(TAG,"Error loading topRated movies");
                Utils.onError(context);
            }
        });
    }


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(getContext(),
                MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        if(data.getCount()>0) {
            PopularMoviesGridAdapter adapter = (PopularMoviesGridAdapter) mRecyclerView.getAdapter();
            adapter.setData(ProviderUtils.parseMovieResponse(data));
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {
        PopularMoviesGridAdapter adapter = (PopularMoviesGridAdapter) mRecyclerView.getAdapter();
        adapter.setData(null);
    }
}
