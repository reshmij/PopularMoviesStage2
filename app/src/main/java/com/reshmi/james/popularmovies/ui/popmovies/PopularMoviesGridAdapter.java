package com.reshmi.james.popularmovies.ui.popmovies;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.reshmi.james.popularmovies.R;
import com.reshmi.james.popularmovies.data.network.model.Movie;
import com.reshmi.james.popularmovies.data.network.model.MoviesResponse;
import com.reshmi.james.popularmovies.util.FormatUtils;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PopularMoviesGridAdapter extends RecyclerView.Adapter<PopularMoviesGridAdapter.PopularMoviesViewHolder> {

    public interface ListItemClickListener{
        public void onListItemClick(View view, int position);
    }

    private static final String TAG = "PopMoviesGridAdapter";
    private List<Movie> movies;
    public static ListItemClickListener sListener;

    public PopularMoviesGridAdapter(ListItemClickListener listener){
        try {
            sListener = listener;
        }
        catch (ClassCastException e){
            Log.e(TAG,"Calling class does not implement the ListItemClickListener");
        }
    }

    @NonNull
    @Override
    public PopularMoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_movies_grid_item_layout,parent, false);
        return new PopularMoviesViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull PopularMoviesViewHolder holder, int position) {
        try {

            Movie movie = movies.get(position);
            String posterPath = FormatUtils.getCompleteUrl(movie.getPosterPath());
            Picasso.get().load(posterPath).into(holder.mMoviePoster);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        try{
            return movies.size();
        }
        catch(Exception e) {
            Log.e(TAG, "List data not set");
            return 0;
        }
    }

    public void setData(List<Movie> movies){
        this.movies = movies;
        notifyDataSetChanged();
    }

    public static class PopularMoviesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        final ImageView mMoviePoster;
        final View mView;

        public PopularMoviesViewHolder(View view) {
            super(view);
            mView = view;
            mMoviePoster = view.findViewById(R.id.movie_poster);
            mView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            try {
                sListener.onListItemClick(view, this.getAdapterPosition());
            }
            catch (Exception e){
                e.printStackTrace();
                Log.e(TAG, "Error handling item click");
            }
        }
    }
}
