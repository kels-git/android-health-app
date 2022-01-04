package com.example.healthappdemo;
public class ProfileItems {

     String mProfileTitle;
     int mProfileCount;

    public ProfileItems(String ProfileTitle, int ProfileCount){
        this.mProfileTitle = ProfileTitle;
        this.mProfileCount = ProfileCount;

    }

    public String getmProfileTitle(){
        return mProfileTitle;
    }

    public int getmProfileCount(){
        return mProfileCount;
    }
}
