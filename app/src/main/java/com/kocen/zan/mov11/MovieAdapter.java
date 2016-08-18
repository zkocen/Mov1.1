package com.kocen.zan.mov11;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by zan on 18/08/2016.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param movies A List of AndroidFlavor objects to display in a list
     */
    public MovieAdapter(Activity context, ArrayList<Movie> movies) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, movies);
    }
    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position The position in the list of data that should be displayed in the
     *                 list item view.
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //check if existing view is being reused, othervise inflate view
        View itemListView = convertView;
        if (itemListView == null){
            itemListView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link Movie} object located at this position in the list
        Movie currentMovie = getItem(position);
        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView idTextView = (TextView) itemListView.findViewById(R.id.movie_id);
        // Get the id from the current Movie object and
        // set this text on the name TextView
        String id = Integer.toString(currentMovie.getId());//parse id int to string
        idTextView.setText(id);

        TextView titleTextView = (TextView) itemListView.findViewById(R.id.movie_title);
        titleTextView.setText(currentMovie.getTitle());

        return itemListView;
    }
}
