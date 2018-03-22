package com.reshmi.james.popularmovies;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.reshmi.james.popularmovies.adapter.MovieReviewsListAdapter;
import com.reshmi.james.popularmovies.adapter.MovieTrailerListAdapter;
import com.reshmi.james.popularmovies.model.Movie;
import com.reshmi.james.popularmovies.model.ReviewResponse;
import com.reshmi.james.popularmovies.model.TrailerResponse;
import com.reshmi.james.popularmovies.rest.RestApiClient;
import com.reshmi.james.popularmovies.rest.RestEndpointInterface;
import com.reshmi.james.popularmovies.util.Utils;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailFragment extends Fragment {

    public static final String MOVIE_DETAIL = "movie_detail";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        Bundle args = getArguments();
        Movie movie = args != null ? (Movie) args.getParcelable(MOVIE_DETAIL) : null;
        populateUI(root,movie);
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

}
