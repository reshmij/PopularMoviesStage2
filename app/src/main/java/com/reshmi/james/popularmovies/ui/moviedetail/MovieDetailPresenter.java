package com.reshmi.james.popularmovies.ui.moviedetail;

import android.support.annotation.NonNull;
import android.util.Log;

import com.reshmi.james.popularmovies.data.MoviesDataSource;
import com.reshmi.james.popularmovies.data.MoviesRepository;
import com.reshmi.james.popularmovies.data.database.MovieDbContract;
import com.reshmi.james.popularmovies.data.network.model.Movie;
import com.reshmi.james.popularmovies.data.network.model.Review;
import com.reshmi.james.popularmovies.data.network.model.Trailer;

import java.util.List;

public class MovieDetailPresenter implements MovieDetailContract.Presenter{

    private static String TAG = "MovieDetailPresenter";
    MovieDetailContract.View mMovieDetailView;
    MoviesRepository mMoviesRepository;

    public MovieDetailPresenter( @NonNull MovieDetailContract.View movieDetailView, @NonNull MoviesRepository moviesRepository) {
        Log.d(TAG, "MovieDetailPresenter created");

        mMovieDetailView = movieDetailView;
        mMoviesRepository = moviesRepository;
        mMovieDetailView.setPresenter(this);
    }

    @Override
    public void start() {
    }

    @Override
    public void loadTrailers(long id) {
        if(mMovieDetailView.isNetworkConnected()) {
            mMoviesRepository.getTrailers(id, new MoviesDataSource.GetTrailersCallback() {
                @Override
                public void onTrailersLoaded(List<Trailer> trailers) {
                    mMovieDetailView.populateTrailers(trailers);
                }

                @Override
                public void onDataNotAvailable() {
                    mMovieDetailView.showNoTrailersMessage();
                }
            });
        }
        else{
            mMovieDetailView.showConnectionErrorMessage();
        }
    }

    @Override
    public void loadReviews(long id) {
        if(mMovieDetailView.isNetworkConnected()) {
            mMoviesRepository.getReviews(id, new MoviesDataSource.GetReviewsCallback() {
                @Override
                public void onReviewsLoaded(List<Review> reviews) {
                    mMovieDetailView.popularReviews(reviews);
                }

                @Override
                public void onDataNotAvailable() {
                    mMovieDetailView.showNoReviewsMessage();
                }
            });
        }
        else{
            mMovieDetailView.showConnectionErrorMessage();
        }
    }

    @Override
    public void insertFavorite(Movie movie) {
        mMoviesRepository.insertFavoriteMovie(movie);
    }

    @Override
    public void deleteFromFavorites(Movie movie) {
        String selection = MovieDbContract.MovieEntry.COLUMN_NAME_MOVIE_ID+"=?";
        String[] selectionArgs = {String.valueOf(movie.getId())};
        mMoviesRepository.deleteFavoriteMovie(selection,selectionArgs);
    }

}
