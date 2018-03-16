package com.reshmi.james.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.reshmi.james.popularmovies.model.Movie;
import com.reshmi.james.popularmovies.model.MoviesResponse;
import com.reshmi.james.popularmovies.rest.RestApiClient;
import com.reshmi.james.popularmovies.rest.RestEndpointInterface;
import com.reshmi.james.popularmovies.util.Utils;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by reshmijames on 3/14/18.
 */

public class PopularMoviesFragment extends Fragment {

    private static String TAG = "PopularMoviesFragment";
    RecyclerView mRecyclerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRecyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_popular_movies, container, false);
        setupRecyclerView();
        return mRecyclerView;
    }

    private void setupRecyclerView() {
        mRecyclerView.setLayoutManager(new GridLayoutManager(mRecyclerView.getContext(),2));
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        mRecyclerView.setAdapter(new PopularMoviesGridAdapter());
    }

    @Override
    public void onStart() {
        super.onStart();

        RestEndpointInterface apiService =
                RestApiClient.getClient().create(RestEndpointInterface.class);

        Call<MoviesResponse> call = apiService.getPopularMovies(getString(R.string.api_key));
        call.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {

                PopularMoviesGridAdapter adapter = (PopularMoviesGridAdapter)mRecyclerView.getAdapter();
                adapter.setData(response.body());
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                onError();
            }
        });

    }

    private void onError() {
        Toast.makeText(getContext(), R.string.error_detail, Toast.LENGTH_SHORT).show();
    }

    public static class PopularMoviesGridAdapter extends RecyclerView.Adapter<PopularMoviesGridAdapter.PopularMoviesViewHolder> implements View.OnClickListener{
         MoviesResponse moviesResponse=null;

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
