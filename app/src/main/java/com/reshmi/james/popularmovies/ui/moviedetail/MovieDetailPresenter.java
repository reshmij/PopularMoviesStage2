package com.reshmi.james.popularmovies.ui.moviedetail;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.reshmi.james.popularmovies.data.MoviesDataSource;
import com.reshmi.james.popularmovies.data.MoviesRepository;
import com.reshmi.james.popularmovies.data.network.model.Movie;
import com.reshmi.james.popularmovies.data.network.model.Review;
import com.reshmi.james.popularmovies.data.network.model.Trailer;

import java.util.List;

public class MovieDetailPresenter implements MovieDetailContract.Presenter{

    private static final String TAG = "MovieDetailPresenter";
    private final MovieDetailContract.View mMovieDetailView;
    private final MoviesRepository mMoviesRepository;

    public MovieDetailPresenter( @NonNull MovieDetailContract.View movieDetailView, @NonNull MoviesRepository moviesRepository) {
        Log.d(TAG, "MovieDetailPresenter created");

        mMovieDetailView = movieDetailView;
        mMoviesRepository = moviesRepository;
        mMovieDetailView.setPresenter(this);
    }

    @Override
    public void start() {
        Log.d(TAG, "MovieDetailPresenter start");
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
        mMoviesRepository.deleteFavoriteMovie(movie.getId());
    }

    @Override
    public void checkMovieStatusAndConfigureButton(Movie movie) {
        mMoviesRepository.getMovie(movie.getId(), new MoviesDataSource.MovieStatusCallback() {
            @Override
            public void onStatusUpdate(Cursor cursor) {
                //If cursor returns at least one row, then movie is in the database, it was marked as favorite
                boolean isMovieMarkedAsFavorite = cursor.getCount() > 0;
                //Configure favorite button text accordingly
                mMovieDetailView.setFavoriteButtonText(isMovieMarkedAsFavorite);
                cursor.close();
            }
        });
    }
}
