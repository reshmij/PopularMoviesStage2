package com.reshmi.james.popularmovies;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

public class PopularMoviesFragment extends Fragment implements PopularMoviesGridAdapter.ListItemClickListener,SharedPreferences.OnSharedPreferenceChangeListener, LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "PopularMoviesFragment";
    private static final int ID_MOVIE_LOADER = 104;
    private static final String SCROLL_POSITION = "scroll_position";
    private RecyclerView mRecyclerView;
    private boolean mDualPane;
    private MoviesResponse mMoviesResponse;
    private int mCurrentChoice = 0;


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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Check if we are in dual pane mode
        View detailsFrame = getActivity().findViewById(R.id.pop_movies_detail_view_container);
        mDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        if(savedInstanceState!=null){
            mCurrentChoice = savedInstanceState.getInt(SCROLL_POSITION);
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mRecyclerView.getContext());
        loadMovies(sharedPreferences, getString(R.string.sort_order_pref_key));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SCROLL_POSITION,mCurrentChoice);
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
        mRecyclerView.setAdapter(new PopularMoviesGridAdapter(this));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Log.d(TAG, "Shared preference changed");
        if(key.equals(getString(R.string.sort_order_pref_key))){
            loadMovies(sharedPreferences, key);
        }
    }

    private void loadMovies(SharedPreferences sp, String key){
        Log.d(TAG, "loadMovies");
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
        Log.d(TAG, "loadFromDatabase");
        getActivity().getSupportLoaderManager().restartLoader( ID_MOVIE_LOADER, null, this);
    }

    private void loadPopularMovies(){

        Log.d(TAG, "loadPopularMovies");
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
                populateMoviesList(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<MoviesResponse> call, @NonNull Throwable t) {
                Log.e(TAG,"Error loading popular movies");
                Utils.onError(context);
            }
        });
    }

    private void loadTopRatedMovies(){
        Log.d(TAG, "loadTopRatedMovies");
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
                populateMoviesList(response.body());
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

        Log.d(TAG, "onCreateLoader");
        return new CursorLoader(getContext(),
                MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        Log.d(TAG, "onLoadFinished");
        if(data.getCount()>0) {
            populateMoviesList(ProviderUtils.parseMovieResponse(data));
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader loader) {

        Log.d(TAG, "onLoaderReset");
        PopularMoviesGridAdapter adapter = (PopularMoviesGridAdapter) mRecyclerView.getAdapter();
        adapter.setData(null);
    }

    @Override
    public void onListItemClick(View view, int position) {

        try {
            Movie movie = mMoviesResponse.getResults()[position];
            mCurrentChoice = position;
            showDetails(movie);
        }
        catch(Exception e){
            Log.e(TAG,"Error loading movie details");
            e.printStackTrace();
        }
    }

    private void populateMoviesList(MoviesResponse moviesResponse){
        mMoviesResponse = moviesResponse;

        PopularMoviesGridAdapter adapter = (PopularMoviesGridAdapter) mRecyclerView.getAdapter();
        adapter.setData(mMoviesResponse);
        mRecyclerView.smoothScrollToPosition(mCurrentChoice);

        if(mDualPane){
            showDetails(mMoviesResponse.getResults()[mCurrentChoice]);
        }
    }

    private void showDetails(Movie movie){
        if(mDualPane){

            // Check what fragment is currently shown, replace if needed.
            MovieDetailFragment details = (MovieDetailFragment)
                    getFragmentManager().findFragmentById(R.id.pop_movies_detail_view_container);

            if (details == null || details.getMovieId() != movie.getId()) {
                // Make new fragment to show this selection.
                details = MovieDetailFragment.newInstance(movie);

                //Replace fragment with new one
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.pop_movies_detail_view_container,details);
                ft.commit();
            }
        }
        else{
            Context context = getContext();
            Intent intent = new Intent(context, MovieDetailActivity.class);
            intent.putExtra(MovieDetailActivity.MOVIE_KEY, movie);
            context.startActivity(intent);
        }
    }
}
