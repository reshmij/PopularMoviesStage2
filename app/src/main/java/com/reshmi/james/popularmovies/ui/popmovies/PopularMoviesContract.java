package com.reshmi.james.popularmovies.ui.popmovies;

import com.reshmi.james.popularmovies.data.network.model.Movie;
import com.reshmi.james.popularmovies.ui.BasePresenter;
import com.reshmi.james.popularmovies.ui.BaseView;

import java.util.List;

public interface PopularMoviesContract {

    interface View extends BaseView<Presenter>{

        void showProgressIndicator();

        void showMovies(List<Movie> movies);

        void showErrorMessage();

        boolean isNetworkConnected();

        void showConnectionErrorMessage();

        String getApiKey();

    }

    interface Presenter extends BasePresenter{

        void loadPopularMovies();

        void loadTopRatedMovies();

        void loadFavoriteMovies();
    }
}
