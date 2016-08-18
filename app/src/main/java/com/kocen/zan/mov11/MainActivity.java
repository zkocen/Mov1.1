package com.kocen.zan.mov11;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getName(); //log

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //fake arraylist
        ArrayList<Movie> movies1 = new ArrayList<Movie>();
        movies1.add(new Movie(0, "Star Trek"));
        movies1.add(new Movie(32, "MIB 2"));
        movies1.add(new Movie(2, "Independence Day"));
        movies1.add(new Movie(3, "Men in Black"));
        movies1.add(new Movie(4, "Terminator 2"));
        movies1.add(new Movie(5, "American Beauty"));
        movies1.add(new Movie(6, "Apolo 13"));


        // Create an {@link MovieAdapter}, whose data source is a list of
        // {@link Movie}s. The adapter knows how to create list item views for each item
        // in the list.
        MovieAdapter adapter = new MovieAdapter(this, movies1);

        //get the ref to the GridView link in layout
        ListView movieListView = (ListView) findViewById(R.id.list);
        movieListView.setAdapter(adapter);
    }
}
