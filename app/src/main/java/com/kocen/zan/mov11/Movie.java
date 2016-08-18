package com.kocen.zan.mov11;

/**
 * Created by zan on 18/08/2016.
 */
public class Movie {
    private int id;
    private String title;

    public Movie(int id, String title){
        this.id = id;
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public int getId(){
        return id;
    }
}
