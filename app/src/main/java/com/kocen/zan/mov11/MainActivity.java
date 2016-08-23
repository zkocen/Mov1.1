package com.kocen.zan.mov11;


import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements LoaderCallbacks<List<Movie>>, NavigationView.OnNavigationItemSelectedListener {

    /**
     * Constant value for the movie loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int MOVIE_LOADER_ID = 1;
    private static final int TOP_MOVIE_LOADER_ID = 2;

    private static final String LOG_TAG = MainActivity.class.getName(); //log
    String key = BuildConfig.MOVIESDB_KEY_ZAN;
    public static final String PAGE_NUM = "&page=";
    public static String page_num = "1";
    private final String POPMDB_REQUEST_URL =
            "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc"
                    + PAGE_NUM + page_num + "&api_key=" + key;
    private final String TOPRAT_REQUEST_URL =
    "http://api.themoviedb.org/3/movie/top_rated?sort_by=popularity.desc"
            + PAGE_NUM + page_num + "&api_key=" + key;
    public static final String KEY_TITLE = "title";
    public static final String KEY_REL_DATE ="release_date";
    public static final String KEY_POSTER = "movie poster";
    public static final String KEY_VOTE_AVG = "average vote";
    public static final String KEY_PLOT = "plot";


//    //fields for side drawer
//    Allow your user to change sort order via a setting:
//    The sort order can be by most popular, or by top rated

    /**
     * Adapter for the list of movies
     */
    private MovieAdapter mAdapter;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    private ArrayList<Movie> mMovies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get the ref to the GridView link in layout
        GridView movieGridView = (GridView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);

        // Create a new adapter that takes an empty list of movies as input
        mAdapter = new MovieAdapter(this, new ArrayList<Movie>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        movieGridView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

//         Set an item click listener on the GridView, which sends an intent to a new activity
//        which displays more info about the movie
        movieGridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current movie that was clicked on
                Movie currentMovie = mAdapter.getItem(position);
                Intent intent = new Intent(MainActivity.this, DetailMovieActivity.class);
                intent.putExtra(KEY_TITLE, currentMovie.getTitle());
                intent.putExtra(KEY_REL_DATE, currentMovie.getRelDate());
                intent.putExtra(KEY_POSTER, currentMovie.getImageUrl());
                intent.putExtra(KEY_VOTE_AVG, currentMovie.getVoteAvg());
                intent.putExtra(KEY_PLOT, currentMovie.getPlot());
                startActivity(intent);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, null, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView)findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);

//        Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager conMan = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = conMan.getActiveNetworkInfo();


        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            // Get a reference to the LoaderManager, in order to interact with loaders.


            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            getLoaderManager().initLoader(MOVIE_LOADER_ID, null, MainActivity.this);
            getLoaderManager().initLoader(TOP_MOVIE_LOADER_ID, null, MainActivity.this);

        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            Toast toast = Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_LONG);
            toast.show();
            movieGridView.setEmptyView(mEmptyStateTextView);
            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
     try {
         if (i == 1){
             return new MovieLoader(MainActivity.this, POPMDB_REQUEST_URL);
         }
         else if (i == 2){
            return new MovieLoader(MainActivity.this, TOPRAT_REQUEST_URL);
         }
     }catch (Exception e){
         Log.e(LOG_TAG, "Cannot create loader ", e);
     }
        return null;
    }


    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies   ) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No Movies found."
        mEmptyStateTextView.setText(R.string.no_movies);

        // Clear the adapter of previous movie data
        mAdapter.clear();


        // If there is a valid list of {@link Movie}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (movies != null && !movies.isEmpty()) {
            //load all movies
            mAdapter.addAll(movies);
            mMovies.addAll(movies);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
        getLoaderManager().destroyLoader(TOP_MOVIE_LOADER_ID);
        getLoaderManager().destroyLoader(MOVIE_LOADER_ID);
    }


    //    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings){
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.mostPopularMenuId) {
            mAdapter.clear();
            getLoaderManager().destroyLoader(TOP_MOVIE_LOADER_ID);
            getLoaderManager().restartLoader(MOVIE_LOADER_ID, null, this);
            mAdapter.notifyDataSetChanged();
        }
//        } else if (id == R.id.MostPopularByTopRatedMenuId){
//            mAdapter.clear();
//
//            getLoaderManager().destroyLoader(TOP_MOVIE_LOADER_ID);
//            Collections.sort(mMovies, movieComparator);
//            mAdapter.addAll(mMovies);
//            mAdapter.notifyDataSetChanged();
//        }
        else if (id == R.id.topRatedMenuId){

            mAdapter.clear();
            getLoaderManager().destroyLoader(MOVIE_LOADER_ID);
            onCreateLoader(TOP_MOVIE_LOADER_ID, null);
            getLoaderManager().getLoader(TOP_MOVIE_LOADER_ID);
            mAdapter.addAll(mMovies);
            mAdapter.notifyDataSetChanged();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



//    compare average raitings of movies and sort them from highest to lowest
//    Comparator<Movie> movieComparator = new Comparator<Movie>() {
//        @Override
//        public int compare(Movie movie1, Movie movie2) {
//            return Float.parseFloat(movie1.getVoteAvg()) > Float.parseFloat(movie2.getVoteAvg()) ? -1
//                    : Float.parseFloat(movie1.getVoteAvg()) > Float.parseFloat(movie2.getVoteAvg()) ? 1
//                    : 0;
//        }
//    };
}
