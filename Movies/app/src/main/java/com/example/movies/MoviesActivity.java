package com.example.movies;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.movies.model.DatabaseOpenHelper;
import com.example.movies.model.MovieEntity;
import com.j256.ormlite.android.apptools.OpenHelperManager;

import java.util.ArrayList;

public class MoviesActivity extends AppCompatActivity {

    private static final int SEARCH_REQUEST_CODE = 796;

    private RecyclerView mMoviesList;
    private TextView mEmptyView;
    private MoviesAdapter mAdapter;
    private DatabaseOpenHelper mDbHelper;
    private MovieEntity.MovieDbHelper mMovieDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        setUpToolbar();
        setStatusBarColor();
        createDbHelper();
        initViews();
        getMovies();
    }

    private void setUpToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.hideOverflowMenu();
        setSupportActionBar(toolbar);
    }

    public void setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window w = getWindow();
            w.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            w.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }
    }

    private void initViews() {
        mMoviesList = (RecyclerView) findViewById(R.id.movies_list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mMoviesList.setLayoutManager(layoutManager);
        mEmptyView = (TextView) findViewById(R.id.movies_empty);
    }

    private void getMovies() {
        ArrayList<MovieEntity> myMovies = mMovieDbHelper.getAll();
        if (myMovies.size() > 0) {
            mAdapter = new MoviesAdapter(this, myMovies);
            mMoviesList.setAdapter(mAdapter);
            mMoviesList.setVisibility(View.VISIBLE);
            mEmptyView.setVisibility(View.GONE);
        } else {
            mMoviesList.setVisibility(View.GONE);
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        releaseDbHelper();
        super.onDestroy();
    }

    private void createDbHelper() {
        mDbHelper = OpenHelperManager.getHelper(this, DatabaseOpenHelper.class);
        mMovieDbHelper = new MovieEntity.MovieDbHelper(mDbHelper.getMovieDao());
    }

    private void releaseDbHelper() {
        if (mDbHelper != null)
            OpenHelperManager.releaseHelper();
        mDbHelper = null;
        mMovieDbHelper = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_movies, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_movies_add) {
            Intent searchIntent = new Intent(this, SearchActivity.class);
            startActivityForResult(searchIntent, SEARCH_REQUEST_CODE);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SEARCH_REQUEST_CODE && resultCode == RESULT_OK){
            getMovies();
        }
    }
}
