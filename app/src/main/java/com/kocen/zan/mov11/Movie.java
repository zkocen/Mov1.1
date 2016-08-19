package com.kocen.zan.mov11;

/**
 * Created by zan on 18/08/2016.
 */
public class Movie {
    private int id;
    private String title;
    private String imageUrl;
    private String relDate;
    private String voteAvg;
    private String plot;

    public Movie(int id, String title, String imageUrl,
                 String relDate, String voteAvg, String plot){
        this.id = id;
        this.title = title;
        this.imageUrl = imageUrl;
        this.relDate = relDate;
        this.voteAvg = voteAvg;
        this.plot = plot;
    }

    public String getTitle(){
        return title;
    }

    public int getId(){
        return id;
    }

    public String getImageUrl(){
        return imageUrl;
    }

    public String getRelDate(){
        return relDate;
    }

    public String getVoteAvg(){
        return voteAvg;
    }

    public String getPlot(){
        return plot;
    }
}
