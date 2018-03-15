package com.reshmi.james.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by reshmijames on 3/14/18.
 */

public class MovieDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        Intent intent = getIntent();
        String movieTitle = intent != null? intent.getStringExtra("movieName"): "";
        TextView textView = (TextView) findViewById(R.id.detail_movie_title);
        textView.setText(movieTitle);
    }
}
