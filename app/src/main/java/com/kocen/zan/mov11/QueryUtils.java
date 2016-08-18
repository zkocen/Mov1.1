package com.kocen.zan.mov11;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zan on 18/08/2016.
 */
public class QueryUtils {

    private static final String LOG_TAG = QueryUtils.class.getName(); //log

    public static List<Movie> fetchMovieData(String requestUrl){
        URL url = createUrl(requestUrl);
        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG, "Can't makke http request: ", e);
        }
        List<Movie> movies = extractFeatureFromJson(jsonResponse);
        return movies;
    }

    private static List<Movie> extractFeatureFromJson(String movieJson) {
        if (TextUtils.isEmpty(movieJson)) {
            return null;
        }
        List<Movie> movies = new ArrayList<>(); //new array where I'll put movies from JSON
        try {
            JSONObject baseJsonResponse = new JSONObject(movieJson);
            // Extract the JSONArray associated with the key called "properties"
            JSONArray moviesArray = baseJsonResponse.getJSONArray("results");

            for (int i=0; i<moviesArray.length(); i++){
                JSONObject currentMovie = moviesArray.getJSONObject(i);

                int mId = currentMovie.getInt("id");
                String mTitle = currentMovie.getString("original_title");

                // Create a new {@link Movie} object with the id, title, ...
                // and url from the JSON response.
                Movie movie = new Movie(mId,mTitle);
                // Add the new {@link Movie} to the list of movies.
                movies.add(movie);
                Log.v(LOG_TAG, " " + movie.getTitle() );
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG,"Can't get JSON ", e);
        }
        return movies;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResp = "";
        if (url == null){
            return jsonResp;
        }
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000); //miliseconds
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                jsonResp = readFromStream(inputStream);
            }
        }catch (IOException e){
            Log.v(LOG_TAG, "IOExeption error: " + urlConnection.getResponseCode());
        } finally {
            if (urlConnection != null){
                urlConnection.disconnect();
            }
            if (inputStream != null){
                // function must handle java.io.IOException here
                inputStream.close();
            }
        }
        return jsonResp;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    private static URL createUrl(String requestUrl) {
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }
}
