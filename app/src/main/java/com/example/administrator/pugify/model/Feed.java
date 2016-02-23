package com.example.administrator.pugify.model;

import android.util.Log;

import java.util.concurrent.RecursiveTask;

/**
 * Created by Administrator on 10/12/2015.
 */
public class Feed {

    private String objectId;
    private String pugId;
    private String pugName;
    private String imageLink;
    private String description;
    private int rate;
    private boolean reported;

    public String getObjectId(){
        return this.objectId;
    }
    public String getPugId(){
        return this.pugId;
    }
    public String getPugName(){
        return this.pugName;
    }
    public String getImageLink(){
        return this.imageLink;
    }
    public String getDescription(){
        return this.description;
    }
    public int getRate(){
        return this.rate;
    }
    public void addRate(){
        this.rate++;
    }
    public boolean getReported(){ return this.reported; }

    public Feed(String pugId, String pugName, String imageLink, String description, int rate){
        this.pugId = pugId;
        this.pugName = pugName;
        this.imageLink = imageLink;
        this.description = description;
        this.rate = rate;
    }
    public Feed(String objectId, String pugId, String pugName, String imageLink, String description, int rate, boolean reported){
        this.objectId = objectId;
        this.pugId = pugId;
        this.pugName = pugName;
        this.imageLink = imageLink;
        this.description = description;
        this.rate = rate;
        this.reported = reported;
    }



}
