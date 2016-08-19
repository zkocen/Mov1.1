package com.kocen.zan.mov11;

/**
 * Created by zan on 19/08/2016.
 */
public class Drawer {
    private String mostPopular;
    private String topRated;

    public Drawer(String mostPopular, String topRated){
        this.mostPopular = mostPopular;
        this.topRated = topRated;
    }

    public String getMostPopular(){
        return mostPopular;
    }
}
