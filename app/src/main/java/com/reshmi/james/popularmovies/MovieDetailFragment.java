package com.reshmi.james.popularmovies;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
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

import com.reshmi.james.popularmovies.adapter.MovieReviewsListAdapter;
import com.reshmi.james.popularmovies.adapter.MovieTrailerListAdapter;
import com.reshmi.james.popularmovies.database.MovieDbContract;
import com.reshmi.james.popularmovies.database.MovieDbContract.MovieEntry;
import com.reshmi.james.popularmovies.database.MovieDbHelper;
import com.reshmi.james.popularmovies.model.Movie;
import com.reshmi.james.popularmovies.model.ReviewResponse;
import com.reshmi.james.popularmovies.model.TrailerResponse;
import com.reshmi.james.popularmovies.provider.ProviderUtils;
import com.reshmi.james.popularmovies.rest.RestApiClient;
import com.reshmi.james.popularmovies.rest.RestEndpointInterface;
import com.reshmi.james.popularmovies.util.Utils;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "MovieDetailFragment";
    public static final String MOVIE_DETAIL = "movie_detail";
    private static final long INVALID_MOVIE_ID = -1;
    MovieDbHelper mMovieDbHelper;
    Movie mMovie;

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

        mMovieDbHelper = new MovieDbHelper(getContext());

        Bundle args = getArguments();
        mMovie = args != null ? (Movie) args.getParcelable(MOVIE_DETAIL) : null;
        populateUI(root,mMovie);

        return root;
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
            userRating.setText(Utils.formatRatingString(movie.getVoteAverage()));

            String posterPath = Utils.getCompleteUrl(movie.getPosterPath());
            Picasso.get().load(posterPath).into(thumbnail);

            RecyclerView trailerList = root.findViewById(R.id.movie_detail_trailer_list);
            trailerList.setLayoutManager(new LinearLayoutManager(getContext()));
            trailerList.setAdapter(new MovieTrailerListAdapter());
            trailerList.setHasFixedSize(true);
            trailerList.setNestedScrollingEnabled(false);
            loadTrailers( trailerList,movie);

            RecyclerView reviewList = root.findViewById(R.id.movie_detail_review_list);
            reviewList.setLayoutManager(new LinearLayoutManager(getContext()));
            reviewList.setAdapter(new MovieReviewsListAdapter());
            reviewList.setHasFixedSize(true);
            reviewList.setNestedScrollingEnabled(false);
            loadReviews( reviewList, movie);

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

    private void loadReviews(final RecyclerView reviewList, Movie movie) {
        final Context context = reviewList.getContext();
        if(!Utils.isOnline(context)){
            Utils.onConnectionError(context);
            return;
        }

        RestEndpointInterface apiService =
                RestApiClient.getClient().create(RestEndpointInterface.class);

        Call<ReviewResponse> call = apiService.getMovieReviews(movie.getId(), getString(R.string.api_key));
        call.enqueue(new Callback<ReviewResponse>() {

            @Override
            public void onResponse(Call<ReviewResponse> call, Response<ReviewResponse> response) {
                MovieReviewsListAdapter adapter = (MovieReviewsListAdapter) reviewList.getAdapter();
                adapter.setData(response.body());
            }

            @Override
            public void onFailure(Call<ReviewResponse> call, Throwable t) {

                t.printStackTrace();
                Utils.onError(context);
            }
        });
    }


    private void loadTrailers(final RecyclerView trailerList, Movie movie) {
        final Context context = trailerList.getContext();
        if(!Utils.isOnline(context)){
            Utils.onConnectionError(context);
            return;
        }

        RestEndpointInterface apiService =
                RestApiClient.getClient().create(RestEndpointInterface.class);

        Call<TrailerResponse> call = apiService.getMovieTrailers(movie.getId(), getString(R.string.api_key));
        call.enqueue(new Callback<TrailerResponse>() {

            @Override
            public void onResponse(Call<TrailerResponse> call, Response<TrailerResponse> response) {
                MovieTrailerListAdapter adapter = (MovieTrailerListAdapter) trailerList.getAdapter();
                adapter.setData(response.body());
            }

            @Override
            public void onFailure(Call<TrailerResponse> call, Throwable t) {
                t.printStackTrace();
                Utils.onError(context);
            }
        });
    }

    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.movie_detail_add_favorite_btn){

            Button b = (Button) view;
            if(b.getText().equals(getString(R.string.mark_as_favorite))){
                getContext().getContentResolver().insert(MovieEntry.CONTENT_URI, ProviderUtils.getContentValues(mMovie));
                b.setText(getString(R.string.remove_from_favorites));
                Toast.makeText(getContext(),getString(R.string.marked_as_favorite), Toast.LENGTH_SHORT).show();
            }
            else{
                String selection = MovieEntry.COLUMN_NAME_MOVIE_ID+"=?";
                String[] selectionArgs = {String.valueOf(mMovie.getId())};
                getContext().getContentResolver().delete(MovieEntry.CONTENT_URI,selection, selectionArgs);
                b.setText(getString(R.string.mark_as_favorite));
                Toast.makeText(getContext(),getString(R.string.removed_from_favorites), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public long getMovieId(){
        try {
            return mMovie.getId();
        }
        catch(Exception e){
            return INVALID_MOVIE_ID;
        }
    }
}
