package com.kocen.zan.mov11;


import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity
        implements LoaderCallbacks<List<Movie>> {

        /**
     * Constant value for the movie loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int MOVIE_LOADER_ID = 1;

    private static final String LOG_TAG = MainActivity.class.getName(); //log
    String key = BuildConfig.MOVIESDB_KEY_ZAN;
    private final String POPMDB_REQUEST_URL =
            "http://api.themoviedb.org/3/discover/movie?sort_by=popularity.desc&" +
                    "api_key=" + key;
    public static final String KEY_TITLE = "title";
    public static final String KEY_REL_DATE ="release_date";
    public static final String KEY_POSTER = "movie poster";
    public static final String KEY_VOTE_AVG = "average vote";
    public static final String KEY_PLOT = "plot";


    /**
     * Adapter for the list of movies
     */
    private MovieAdapter mAdapter;

    /**
     * TextView that is displayed when the list is empty
     */
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get the ref to the GridView link in layout
        GridView movieGridView = (GridView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);

        // Create a new adapter that takes an empty list of earthquakes as input
        mAdapter = new MovieAdapter(this, new ArrayList<Movie>());


        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        movieGridView.setAdapter(mAdapter);

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

//        Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager conMan = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = conMan.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            loaderManager.initLoader(MOVIE_LOADER_ID, null, MainActivity.this);
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
        return new MovieLoader(MainActivity.this, POPMDB_REQUEST_URL);
    }


    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> movies   ) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No earthquakes found."
        mEmptyStateTextView.setText(R.string.no_earthquakes);

        // Clear the adapter of previous earthquake data
        mAdapter.clear();

        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (movies != null && !movies.isEmpty()) {
            mAdapter.addAll(movies);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }
}
