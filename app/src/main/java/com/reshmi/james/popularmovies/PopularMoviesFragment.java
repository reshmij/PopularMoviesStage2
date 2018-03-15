package com.reshmi.james.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.reshmi.james.popularmovies.R;
import com.reshmi.james.popularmovies.model.Movie;
import com.reshmi.james.popularmovies.model.PopularMoviesResponse;
import com.reshmi.james.popularmovies.util.TestUtils;
import com.squareup.picasso.Picasso;

/**
 * Created by reshmijames on 3/14/18.
 */

public class PopularMoviesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        RecyclerView rv = (RecyclerView) inflater.inflate(R.layout.fragment_popular_movies, container, false);
        setupRecyclerView(rv);
        return rv;
    }

    private void setupRecyclerView(RecyclerView recyclerView) {
        recyclerView.setLayoutManager(new GridLayoutManager(recyclerView.getContext(),2));
        recyclerView.setAdapter(new PopularMoviesGridAdapter(TestUtils.loadPopularMovies()));
    }


    public static class PopularMoviesGridAdapter extends RecyclerView.Adapter<PopularMoviesGridAdapter.PopularMoviesViewHolder> implements View.OnClickListener{
         PopularMoviesResponse popularMoviesResponse;

         @NonNull
         @Override
         public PopularMoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

             View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_movies_grid_item_layout,parent, false);
             PopularMoviesViewHolder vh = new PopularMoviesViewHolder(root);
             return vh;
         }

         @Override
         public void onBindViewHolder(@NonNull PopularMoviesViewHolder holder, int position) {

             try {

                 Movie movie = popularMoviesResponse.getMovies().get(position);
                 holder.mView.setTag(movie);
                 holder.mView.setOnClickListener(this);
                 Picasso.get().load(movie.getPosterPath()).into(holder.mMoviePoster);
             }
             catch(Exception e){
                 e.printStackTrace();
             }
         }

         @Override
         public int getItemCount() {
             return popularMoviesResponse.getMovies().size();
         }

         public PopularMoviesGridAdapter(PopularMoviesResponse response){
             popularMoviesResponse = response;
         }

        @Override
        public void onClick(View view) {

             Context context = view.getContext();
             Movie movie = (Movie)view.getTag();
             Intent intent = new Intent(context, MovieDetailActivity.class);
             intent.putExtra("movieName",movie.getOriginalTitle());
             context.startActivity(intent);

        }

        public static class PopularMoviesViewHolder extends RecyclerView.ViewHolder{

            ImageView mMoviePoster;
            View mView;


            public PopularMoviesViewHolder(View view) {
                super(view);
                mView = view;
                mMoviePoster = (ImageView) view.findViewById(R.id.movie_poster);

            }
        }
    }
}
