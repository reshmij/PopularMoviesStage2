package com.reshmi.james.popularmovies.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.reshmi.james.popularmovies.R;
import com.reshmi.james.popularmovies.model.Review;
import com.reshmi.james.popularmovies.model.ReviewResponse;

public class MovieReviewsListAdapter extends RecyclerView.Adapter<MovieReviewsListAdapter.MovieReviewViewHolder> {

    ReviewResponse mReviewResponse;
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
            Review review = mReviewResponse.getResults()[position];
            holder.mReviewAuthor.setText(mContext.getString(R.string.author,review.getAuthor()));
            holder.mReviewContent.setText(review.getContent());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {

        if(mReviewResponse!=null) {
            return mReviewResponse.getResults().length;
        }
        return 0;
    }

    public void setData(ReviewResponse reviewResponse){

        mReviewResponse = reviewResponse;
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
