package com.reshmi.james.popularmovies.ui.popmovies;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;


import com.reshmi.james.popularmovies.data.LoaderProvider;
import com.reshmi.james.popularmovies.data.MoviesDataSource;
import com.reshmi.james.popularmovies.data.MoviesRepository;
import com.reshmi.james.popularmovies.data.network.model.Movie;
import com.reshmi.james.popularmovies.util.ProviderUtils;

import java.util.List;

public class PopularMoviesPresenter implements  PopularMoviesContract.Presenter,
        MoviesDataSource.GetMoviesCallback,
        LoaderManager.LoaderCallbacks<Cursor> {

    private static final String TAG = "PopularMoviesPresenter";
    private static final int ID_MOVIE_LOADER = 104;

    private final PopularMoviesContract.View mMoviesView;
    private final MoviesRepository mMoviesRepository;
    private final LoaderProvider mLoaderProvider;
    private final LoaderManager mLoaderManager;

    public PopularMoviesPresenter(@NonNull PopularMoviesContract.View moviesView,@NonNull MoviesRepository moviesRepository, @NonNull LoaderProvider loaderProvider, @NonNull LoaderManager loaderManager) {
        this.mMoviesView = moviesView;
        this.mMoviesRepository = moviesRepository;
        this.mLoaderProvider = loaderProvider;
        this.mLoaderManager = loaderManager;
        moviesView.setPresenter(this);
    }

    @Override
    public void loadPopularMovies() {
        Log.d(TAG, "loadPopularMovies");

        if(mMoviesView.isNetworkConnected()){
            mMoviesView.showProgressIndicator();
            mMoviesRepository.getPopularMovies(this);
        }
        else{
            mMoviesView.showConnectionErrorMessage();
        }
    }

    @Override
    public void loadTopRatedMovies() {
        Log.d(TAG, "loadPopularMovies");

        if(mMoviesView.isNetworkConnected()){
            mMoviesView.showProgressIndicator();
            mMoviesRepository.getTopRatedMovies(this);
        }
        else{
            mMoviesView.showConnectionErrorMessage();
        }
    }

    @Override
    public void loadFavoriteMovies() {
        Log.d(TAG, "loadFromDatabase");

        if (mLoaderManager.getLoader(ID_MOVIE_LOADER) == null) {
            mLoaderManager.initLoader(ID_MOVIE_LOADER, null, this);
        } else {
            mLoaderManager.restartLoader(ID_MOVIE_LOADER, null, this);
        }
    }

    ///////////// Callbacks from the movie repository ///////////////////////
    @Override
    public void onMoviesLoaded(List<Movie> movies) {
        mMoviesView.showMovies(movies);
    }

    @Override
    public void onDataNotAvailable() {
        mMoviesView.showErrorMessage();
    }

    ///////////// Loader Callback Methods ////////////////////////////////
    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        mMoviesView.showProgressIndicator();
        return mLoaderProvider.createMovieLoader();
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        List<Movie> movies = ProviderUtils.parseMovieResponse(data);
        mMoviesView.showMovies(movies);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mMoviesView.showMovies(null);
    }

    @Override
    public void start() {

    }
}
