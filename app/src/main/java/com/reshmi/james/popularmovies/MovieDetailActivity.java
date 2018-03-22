package com.reshmi.james.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.reshmi.james.popularmovies.model.Movie;

public class MovieDetailActivity extends AppCompatActivity {

    public static final String MOVIE_KEY = "movie";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        MovieDetailFragment movieDetailFragment = new MovieDetailFragment();
        Intent intent = getIntent();
        if(intent!=null){
            Movie movie = getIntent().getParcelableExtra(MOVIE_KEY);
            Bundle args = new Bundle();
            args.putParcelable(MovieDetailFragment.MOVIE_DETAIL, movie);
            movieDetailFragment.setArguments(args);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.movie_detail_fragment_container, movieDetailFragment);
        ft.commit();
    }
}
