package com.reshmi.james.popularmovies.ui.moviedetail;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.reshmi.james.popularmovies.R;
import com.reshmi.james.popularmovies.data.database.MovieDbContract.MovieEntry;
import com.reshmi.james.popularmovies.data.database.MovieDbHelper;
import com.reshmi.james.popularmovies.data.network.model.Movie;
import com.reshmi.james.popularmovies.data.network.model.Review;
import com.reshmi.james.popularmovies.data.network.model.ReviewResponse;
import com.reshmi.james.popularmovies.data.network.model.Trailer;
import com.reshmi.james.popularmovies.data.network.model.TrailerResponse;
import com.reshmi.james.popularmovies.util.ConnectionUtils;
import com.reshmi.james.popularmovies.util.ProviderUtils;
import com.reshmi.james.popularmovies.data.network.RestApiClient;
import com.reshmi.james.popularmovies.data.network.RestEndpointInterface;
import com.reshmi.james.popularmovies.util.FormatUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailFragment extends Fragment implements View.OnClickListener, MovieDetailContract.View {

    private static final String TAG = "MovieDetailFragment";
    public static final String MOVIE_DETAIL = "movie_detail";
    MovieDbHelper mMovieDbHelper;
    Movie mMovie;
    MovieDetailContract.Presenter mPresenter;
    RecyclerView mTrailerList;
    RecyclerView mReviewList;

    public static MovieDetailFragment newInstance(Movie movie) {
        MovieDetailFragment f = new MovieDetailFragment();

        Bundle args = new Bundle();
        args.putParcelable(MovieDetailFragment.MOVIE_DETAIL, movie);
        f.setArguments(args);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Log.d(TAG, "onCreateView");
        mMovieDbHelper = new MovieDbHelper(getContext());

        Bundle args = getArguments();
        mMovie = args != null ? (Movie) args.getParcelable(MOVIE_DETAIL) : null;
        populateUI(root,mMovie);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        loadDetails();
    }

    private void loadDetails() {
        mPresenter.loadTrailers(mMovie.getId());
        mPresenter.loadReviews(mMovie.getId());
    }

    private void populateUI(View root, Movie movie){

        if(movie!=null) {
            TextView title = root.findViewById(R.id.movie_detail_title);
            TextView releaseDate = root.findViewById(R.id.movie_detail_release_date);
            TextView synopsis = root.findViewById(R.id.movie_detail_synopsis);
            TextView userRating = root.findViewById(R.id.movie_detail_user_rating);
            ImageView thumbnail = root.findViewById(R.id.movie_detail_thumbnail);

            title.setText(movie.getOriginalTitle());
            releaseDate.setText(movie.getReleaseDate());
            synopsis.setText(movie.getOverview());
            userRating.setText(FormatUtils.formatRatingString(movie.getVoteAverage()));

            String posterPath = FormatUtils.getCompleteUrl(movie.getPosterPath());
            Picasso.get().load(posterPath).into(thumbnail);

            mTrailerList= root.findViewById(R.id.movie_detail_trailer_list);
            mTrailerList.setLayoutManager(new LinearLayoutManager(getContext()));
            mTrailerList.setAdapter(new MovieTrailerListAdapter());
            mTrailerList.setHasFixedSize(true);
            mTrailerList.setNestedScrollingEnabled(false);

            mReviewList = root.findViewById(R.id.movie_detail_review_list);
            mReviewList.setLayoutManager(new LinearLayoutManager(getContext()));
            mReviewList.setAdapter(new MovieReviewsListAdapter());
            mReviewList.setHasFixedSize(true);
            mReviewList.setNestedScrollingEnabled(false);

            Button favoriteButton = root.findViewById(R.id.movie_detail_add_favorite_btn);
            if(mMovieDbHelper.isMovieMarkedAsFavorite(movie)){
                favoriteButton.setText(getString(R.string.remove_from_favorites));
            }
            else{
                favoriteButton.setText(getString(R.string.mark_as_favorite));
            }
            favoriteButton.setOnClickListener(this);
        }
    }


    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.movie_detail_add_favorite_btn){

            Button button = (Button) view;
            if(button.getText().equals(getString(R.string.mark_as_favorite))){
                mPresenter.insertFavorite(mMovie);
                button.setText(getString(R.string.remove_from_favorites));
                Toast.makeText(getContext(),getString(R.string.marked_as_favorite), Toast.LENGTH_SHORT).show();
            }
            else{
                mPresenter.deleteFromFavorites(mMovie);
                button.setText(getString(R.string.mark_as_favorite));
                Toast.makeText(getContext(),getString(R.string.removed_from_favorites), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public Movie getMovie(){
        return mMovie;
    }

    @Override
    public void setPresenter(MovieDetailContract.Presenter presenter) {
        Log.d(TAG, "setPresenter");
        mPresenter = presenter;
    }

    @Override
    public void populateTrailers(List<Trailer> trailers) {
        MovieTrailerListAdapter adapter = (MovieTrailerListAdapter) mTrailerList.getAdapter();
        adapter.setData(trailers);
    }

    @Override
    public void popularReviews(List<Review> reviews) {
        MovieReviewsListAdapter adapter = (MovieReviewsListAdapter) mReviewList.getAdapter();
        adapter.setData(reviews);
    }

    @Override
    public void showErrorMessage() {
        Toast.makeText(getContext(),getString(R.string.error_detail), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showConnectionErrorMessage() {
        ConnectionUtils.onConnectionError(getContext());
    }

    @Override
    public boolean isNetworkConnected() {
        return ConnectionUtils.isOnline(getContext());
    }
}
