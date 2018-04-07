package com.reshmi.james.popularmovies.ui.popmovies;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.reshmi.james.popularmovies.Injector;
import com.reshmi.james.popularmovies.R;
import com.reshmi.james.popularmovies.data.LoaderProvider;
import com.reshmi.james.popularmovies.ui.settings.SettingsActivity;

public class PopularMoviesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LoaderProvider loaderProvider = new LoaderProvider(this);
        PopularMoviesFragment popularMoviesFragment = (PopularMoviesFragment)getSupportFragmentManager().findFragmentById(R.id.pop_movies_fragment);
        PopularMoviesPresenter popularMoviesPresenter = new PopularMoviesPresenter(popularMoviesFragment,
                Injector.getMoviesRepository(getApplicationContext()),
                loaderProvider,
                getSupportLoaderManager());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.pop_movies_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_settings){
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
