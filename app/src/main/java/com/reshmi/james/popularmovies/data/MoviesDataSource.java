package com.reshmi.james.popularmovies.data;

import android.database.Cursor;
import android.support.annotation.NonNull;

import com.reshmi.james.popularmovies.data.network.model.Movie;
import com.reshmi.james.popularmovies.data.network.model.Review;
import com.reshmi.james.popularmovies.data.network.model.Trailer;

import java.util.List;

@SuppressWarnings("unused")
public interface MoviesDataSource {

    interface GetMoviesCallback{
        void onMoviesLoaded(List<Movie> movies);
        void onDataNotAvailable();
    }

    interface GetTrailersCallback{
        void onTrailersLoaded(List<Trailer> trailers);
        void onDataNotAvailable();
    }

    interface GetReviewsCallback{
        void onReviewsLoaded(List<Review> reviews);
        void onDataNotAvailable();
    }

    interface MovieStatusCallback{
        void onStatusUpdate(Cursor cursor);
    }
    void getPopularMovies(@NonNull  GetMoviesCallback callback);
    void getTopRatedMovies(@NonNull  GetMoviesCallback callback);

    void insertFavoriteMovie(@NonNull Movie movie );
    void deleteFavoriteMovie(long movieId);
    void getMovie(long movieId , MovieStatusCallback callback);

    void getTrailers(long id, @NonNull  GetTrailersCallback callback);
    void getReviews(long id, @NonNull  GetReviewsCallback callback);
}
