package com.reshmi.james.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.reshmi.james.popularmovies.adapter.PopularMoviesGridAdapter;
import com.reshmi.james.popularmovies.model.Movie;
import com.reshmi.james.popularmovies.model.MoviesResponse;
import com.reshmi.james.popularmovies.rest.RestApiClient;
import com.reshmi.james.popularmovies.rest.RestEndpointInterface;
import com.reshmi.james.popularmovies.util.Utils;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by reshmijames on 3/14/18.
 */

public class PopularMoviesFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static String TAG = "PopularMoviesFragment";
    RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_popular_movies, container, false);
        setupRecyclerView();

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mRecyclerView.getContext());
        loadFromSharedPreferences(sharedPreferences, getString(R.string.sort_order_pref_key));
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        return mRecyclerView;
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

    private void onError() {
        Toast.makeText(getContext(), R.string.error_detail, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG, "shared preference changed");
        if(key.equals(getString(R.string.sort_order_pref_key))){
            loadFromSharedPreferences(sharedPreferences, key);
        }
    }

    private void loadFromSharedPreferences(SharedPreferences sp, String key){
        String sortOrder = sp.getString( key, getString(R.string.sort_by_popularity_value));
        if(sortOrder.equals(getString(R.string.sort_by_popularity_value))){
            loadPopularMovies();
        }
        else{
            loadTopRatedMovies();
        }
    }

    private void loadPopularMovies(){
        RestEndpointInterface apiService =
                RestApiClient.getClient().create(RestEndpointInterface.class);

        Call<MoviesResponse> call = apiService.getPopularMovies(getString(R.string.api_key));
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                PopularMoviesGridAdapter adapter = (PopularMoviesGridAdapter)mRecyclerView.getAdapter();
                adapter.setData(response.body());
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                onError();
            }
        });
    }

    private void loadTopRatedMovies(){
        RestEndpointInterface apiService =
                RestApiClient.getClient().create(RestEndpointInterface.class);

        Call<MoviesResponse> call = apiService.getTopRatedMovies(getString(R.string.api_key));
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                PopularMoviesGridAdapter adapter = (PopularMoviesGridAdapter)mRecyclerView.getAdapter();
                adapter.setData(response.body());
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                onError();
            }
        });
    }
}
