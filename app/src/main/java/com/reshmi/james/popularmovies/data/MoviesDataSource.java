package com.reshmi.james.popularmovies.data;

import android.support.annotation.NonNull;

import com.reshmi.james.popularmovies.data.network.model.Movie;
import com.reshmi.james.popularmovies.data.network.model.Review;
import com.reshmi.james.popularmovies.data.network.model.Trailer;

import java.util.List;

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

    void getPopularMovies(@NonNull  GetMoviesCallback callback);
    void getTopRatedMovies(@NonNull  GetMoviesCallback callback);

    void insertFavoriteMovie(@NonNull Movie movie );
    void deleteFavoriteMovie(@NonNull String selection, String[] selectionArgs);
    void getTrailers(long id, @NonNull  GetTrailersCallback callback);
    void getReviews(long id, @NonNull  GetReviewsCallback callback);
}
