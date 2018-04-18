package com.reshmi.james.popularmovies.ui.moviedetail;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reshmi.james.popularmovies.R;
import com.reshmi.james.popularmovies.data.network.model.Review;
import com.reshmi.james.popularmovies.data.network.model.ReviewResponse;

import java.util.List;

public class MovieReviewsListAdapter extends RecyclerView.Adapter<MovieReviewsListAdapter.MovieReviewViewHolder> {

    List<Review> mReviews;
    Context mContext;

    @NonNull
    @Override
    public MovieReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View root = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_list_item,parent, false);
        return new MovieReviewViewHolder(root);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieReviewViewHolder holder, int position) {
        try {
            Review review = mReviews.get(position);
            holder.mReviewAuthor.setText(mContext.getString(R.string.author,review.getAuthor()));
            holder.mReviewContent.setText(review.getContent());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        if(mReviews!=null) {
            return mReviews.size();
        }
        return 0;
    }

    public void setData(List<Review> reviews){
        mReviews = reviews;
        notifyDataSetChanged();
    }

    public static class MovieReviewViewHolder extends RecyclerView.ViewHolder{
        final TextView mReviewAuthor;
        final TextView mReviewContent;

        public MovieReviewViewHolder(View view) {
            super(view);
            mReviewAuthor = view.findViewById(R.id.movie_review_item_author);
            mReviewContent = view.findViewById(R.id.movie_review_item_content);
        }
    }
}
