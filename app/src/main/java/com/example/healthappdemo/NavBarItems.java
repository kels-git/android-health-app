package com.example.healthappdemo;

public class NavBarItems {
    private final int mNavImageResource;
    private final String mNavTextItem;

    public NavBarItems(int NavImageResource, String NavTextItem){
        this.mNavImageResource = NavImageResource;
        this.mNavTextItem = NavTextItem;

    }

   public int getmNavImageResource(){
        return mNavImageResource;
   }

   public String getmNavTextItem(){
        return mNavTextItem;
   }
}
