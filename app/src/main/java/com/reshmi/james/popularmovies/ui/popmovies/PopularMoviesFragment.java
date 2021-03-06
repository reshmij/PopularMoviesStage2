package com.reshmi.james.popularmovies.ui.popmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.reshmi.james.popularmovies.R;
import com.reshmi.james.popularmovies.data.network.model.Movie;
import com.reshmi.james.popularmovies.ui.moviedetail.MovieDetailActivity;
import com.reshmi.james.popularmovies.util.ConnectionUtils;
import com.reshmi.james.popularmovies.util.DisplayUtils;

import java.util.List;

public class PopularMoviesFragment extends Fragment implements PopularMoviesContract.View, PopularMoviesGridAdapter.ListItemClickListener{

    private static final String TAG = "PopularMoviesFragment";
    private static final String KEY_INSTANCE_STATE_RV_POSITION = "scroll_position";
    private RecyclerView mRecyclerView;
    private List<Movie> mMovies;
    private ProgressBar mLoadingIndicator;
    private TextView mErrorMessageView;
    private LinearLayoutManager mLayoutManager;
    private Parcelable mLayoutManagerSavedState;

    private PopularMoviesContract.Presenter mPresenter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_popular_movies, container, false);
        setupUI(root);
        return root;
    }

    @Override
    public void onPause() {
        super.onPause();
        //Save the current state of the recycler view
        mLayoutManagerSavedState = mLayoutManager.onSaveInstanceState();
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mRecyclerView.getContext());
        loadPreferredMovieList(sharedPreferences, getString(R.string.sort_order_pref_key));
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the current state of the recycler view
        outState.putParcelable(KEY_INSTANCE_STATE_RV_POSITION,mLayoutManager.onSaveInstanceState());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(savedInstanceState!=null){
            //Restore the last saved state of the recycler view.
            mLayoutManagerSavedState = savedInstanceState.getParcelable(KEY_INSTANCE_STATE_RV_POSITION);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mLayoutManagerSavedState);
        }
    }

    private void setupUI(View root) {
        mRecyclerView = root.findViewById(R.id.pop_movies_grid);
        mLayoutManager = new GridLayoutManager(mRecyclerView.getContext(), DisplayUtils.calculateNoOfColumns(getContext()));
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mRecyclerView.setAdapter(new PopularMoviesGridAdapter(this));

        mErrorMessageView = root.findViewById(R.id.pop_movies_error_message);
        mLoadingIndicator = root.findViewById(R.id.pop_movies_loading_indicator);
    }

    private void loadPreferredMovieList(SharedPreferences sp, String key){
        Log.d(TAG, "loadPreferredMovieList");

        String sortPreference = sp.getString( key, getString(R.string.sort_by_popularity_value));
        if(sortPreference.equals(getString(R.string.sort_by_popularity_value))){
            loadPopularMovies();
        }
        else if(sortPreference.equals(getString(R.string.sort_by_rating_value))){
            loadTopRatedMovies();
        }
        else{
            loadFavoriteMovies();
        }
    }

    private void loadFavoriteMovies() {
        mPresenter.loadFavoriteMovies();
    }

    private void loadPopularMovies(){
        mPresenter.loadPopularMovies();
    }

    private void loadTopRatedMovies(){
        mPresenter.loadTopRatedMovies();
    }

    @Override
    public void onListItemClick(int position) {

        try {
            Movie movie = mMovies.get(position);

            Context context = getContext();
            Intent intent = new Intent(context, MovieDetailActivity.class);
            intent.putExtra(MovieDetailActivity.MOVIE_KEY, movie);
            //noinspection ConstantConditions
            context.startActivity(intent);

        }
        catch(Exception e){
            Log.e(TAG,"Error loading movie details");
            e.printStackTrace();
        }
    }

    @Override
    public void showProgressIndicator() {
        mLoadingIndicator.setVisibility(View.VISIBLE);

        mRecyclerView.setVisibility(View.INVISIBLE);
        mErrorMessageView.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showMovies(List<Movie> movies) {
        mRecyclerView.setVisibility(View.VISIBLE);
        mErrorMessageView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);

        mMovies = movies;
        PopularMoviesGridAdapter adapter = (PopularMoviesGridAdapter) mRecyclerView.getAdapter();
        adapter.setData(mMovies);

        //Restore the saved state of the list, if any, after loading the data
        if(mLayoutManagerSavedState!=null){
            mLayoutManager.onRestoreInstanceState(mLayoutManagerSavedState);
        }
    }

    @Override
    public void showErrorMessage() {
        mErrorMessageView.setVisibility(View.VISIBLE);
        mErrorMessageView.setText(R.string.no_data);

        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean isNetworkConnected() {
        return ConnectionUtils.isOnline(getContext());
    }

    @Override
    public void showConnectionErrorMessage() {
        mErrorMessageView.setVisibility(View.VISIBLE);
        mErrorMessageView.setText(R.string.connection_error);

        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingIndicator.setVisibility(View.INVISIBLE);
    }

    @Override
    public void setPresenter(PopularMoviesContract.Presenter presenter) {
        mPresenter = presenter;
    }
}
