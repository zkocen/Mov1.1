package com.kocen.zan.mov11;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import static com.kocen.zan.mov11.MainActivity.KEY_PLOT;
import static com.kocen.zan.mov11.MainActivity.KEY_POSTER;
import static com.kocen.zan.mov11.MainActivity.KEY_REL_DATE;
import static com.kocen.zan.mov11.MainActivity.KEY_TITLE;
import static com.kocen.zan.mov11.MainActivity.KEY_VOTE_AVG;

public class DetailMovieActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_movie);

        //Movie details layout contains title, release date, movie poster,
        // vote average, and plot synopsis.

        String mTitle="";
        String mRelDate="";
        String mPoster ="";
        String mAvgVote = "";
        String mPlot = "";

        Intent intent = getIntent();
        if (null != intent){
            mTitle = intent.getStringExtra(KEY_TITLE);
            mRelDate = intent.getStringExtra(KEY_REL_DATE);
            mPoster = intent.getStringExtra(KEY_POSTER);
            mAvgVote = intent.getStringExtra(KEY_VOTE_AVG);
            mPlot = intent.getStringExtra(KEY_PLOT);
        }

        TextView titelTextView = (TextView) findViewById(R.id.details_mov_titleTxtViewId);
        titelTextView.setText(mTitle);

        TextView relDateView = (TextView)findViewById(R.id.release_dateTxtViewId);
        relDateView.setText("Relese date: " + mRelDate);

        ImageView posterDetView = (ImageView)findViewById(R.id.movie_poster_detailsTxtViewId);
        Picasso.with(DetailMovieActivity.this)
                .load(mPoster)
                .fit().centerCrop()
                .into(posterDetView);

        TextView avgVoteTextView = (TextView)findViewById(R.id.avgVoteTxtViewId);
        avgVoteTextView.setText("Average Score: " + mAvgVote);

        TextView plotTextView = (TextView)findViewById(R.id.plotTxtViewId);
        plotTextView.setText(mPlot);
    }
}
