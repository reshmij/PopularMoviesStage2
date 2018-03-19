package com.reshmi.james.popularmovies;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.reshmi.james.popularmovies.model.Movie;
import com.reshmi.james.popularmovies.util.Utils;
import com.squareup.picasso.Picasso;

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
        }
    }
}
