package com.reshmi.james.popularmovies.ui.moviedetail;

import com.reshmi.james.popularmovies.data.network.model.Review;
import com.reshmi.james.popularmovies.data.network.model.Trailer;
import com.reshmi.james.popularmovies.ui.BaseView;

import java.util.List;

public interface MovieDetailContract {

    interface View extends BaseView<Presenter>{
        void setPresenter(Presenter presenter);
        void populateTrailers(List<Trailer> trailers);
        void popularReviews(List<Review> reviews);
        void showNoTrailersMessage();
        void showNoReviewsMessage();
        void showConnectionErrorMessage();
        boolean isNetworkConnected();
        void setFavoriteButtonText( boolean addFavorite);
    }

    @SuppressWarnings("unused")
    interface Presenter{
        @SuppressWarnings("unused")
        void start();
        void loadReviews();
        void loadTrailers();
        void insertFavorite();
        void deleteFromFavorites();
        void checkMovieStatusAndConfigureButton();
    }
}
