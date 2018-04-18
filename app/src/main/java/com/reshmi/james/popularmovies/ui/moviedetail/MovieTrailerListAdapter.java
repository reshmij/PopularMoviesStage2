package com.reshmi.james.popularmovies.ui.moviedetail;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reshmi.james.popularmovies.R;
import com.reshmi.james.popularmovies.data.network.model.Trailer;
import com.reshmi.james.popularmovies.data.network.model.TrailerResponse;

import java.util.List;

public class MovieTrailerListAdapter extends RecyclerView.Adapter<MovieTrailerListAdapter.MovieTrailerViewHolder> implements View.OnClickListener{

    List<Trailer> mTrailers;

    @NonNull
    @Override
    public MovieTrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_list_item,parent, false);
        return new MovieTrailerViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieTrailerViewHolder holder, int position) {
        try {
            Trailer trailer = mTrailers.get(position);
            holder.mView.setTag(trailer);
            holder.mView.setOnClickListener(this);

            holder.mTrailerName.setText(trailer.getName());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if(mTrailers!=null) {
            return mTrailers.size();
        }
        return 0;
    }

    public void setData(List<Trailer> trailers){
        mTrailers = trailers;
        notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {
        Trailer trailer = (Trailer)view.getTag();
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + trailer.getKey()));
        Intent webIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://www.youtube.com/watch?v=" + trailer.getKey()));
        Context context = view.getContext();
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    public static class MovieTrailerViewHolder extends RecyclerView.ViewHolder{

        final TextView mTrailerName;
        final View mView;


        public MovieTrailerViewHolder(View view) {
            super(view);
            mView = view;
            mTrailerName = view.findViewById(R.id.trailer_name);
        }
    }
}
