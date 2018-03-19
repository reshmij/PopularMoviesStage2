package com.reshmi.james.popularmovies.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.reshmi.james.popularmovies.MovieDetailActivity;
import com.reshmi.james.popularmovies.R;
import com.reshmi.james.popularmovies.model.Movie;
import com.reshmi.james.popularmovies.model.MoviesResponse;
import com.reshmi.james.popularmovies.util.Utils;
import com.squareup.picasso.Picasso;

public class PopularMoviesGridAdapter extends RecyclerView.Adapter<PopularMoviesGridAdapter.PopularMoviesViewHolder> implements View.OnClickListener{
    private MoviesResponse moviesResponse=null;

    @NonNull
    @Override
    public PopularMoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_movies_grid_item_layout,parent, false);
        return new PopularMoviesViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularMoviesViewHolder holder, int position) {

        try {

            Movie movie = moviesResponse.getResults()[position];
            holder.mView.setTag(movie);
            holder.mView.setOnClickListener(this);

            String posterPath = Utils.getCompleteUrl(movie.getPosterPath());
            Picasso.get().load(posterPath).into(holder.mMoviePoster);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if(moviesResponse!=null) {
            return moviesResponse.getResults().length;
        }
        return 0;
    }

    public void setData(MoviesResponse response){

        this.moviesResponse = response;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {

        Context context = view.getContext();
        Movie movie = (Movie)view.getTag();
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.MOVIE_KEY, movie);
        context.startActivity(intent);

    }

    public static class PopularMoviesViewHolder extends RecyclerView.ViewHolder{

        final ImageView mMoviePoster;
        final View mView;


        public PopularMoviesViewHolder(View view) {
            super(view);
            mView = view;
            mMoviePoster = view.findViewById(R.id.movie_poster);

        }
    }
}
