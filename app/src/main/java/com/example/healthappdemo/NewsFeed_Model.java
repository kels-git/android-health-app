package com.example.healthappdemo;

import android.os.Parcel;
import android.os.Parcelable;

public class NewsFeed_Model implements Parcelable {

    String PostID;
    String mNewsFeed_photoPost;
    String mNewsFeed_Desc;
    String mPostDate;
    String mDateNewsFeed;
    int   mLikedPost;
    int   mLikedHeart;



    public NewsFeed_Model(){

    }


    public NewsFeed_Model(String postID, String mNewsFeed_photoPost, String mNewsFeed_Desc, String mPostDate, String mDateNewsFeed,
                          int mLikedPost, int mLikedHeart) {
        PostID = postID;
        this.mNewsFeed_photoPost = mNewsFeed_photoPost;
        this.mNewsFeed_Desc = mNewsFeed_Desc;
        this.mPostDate = mPostDate;
        this.mDateNewsFeed = mDateNewsFeed;
        this.mLikedPost = mLikedPost;
        this.mLikedHeart = mLikedHeart;
    }

    protected NewsFeed_Model(Parcel in) {
        PostID = in.readString();
        mNewsFeed_photoPost = in.readString();
        mNewsFeed_Desc = in.readString();
        mPostDate = in.readString();
        mDateNewsFeed = in.readString();
        mLikedPost = in.readInt();
        mLikedHeart = in.readInt();
    }

    public static final Creator<NewsFeed_Model> CREATOR = new Creator<NewsFeed_Model>() {
        @Override
        public NewsFeed_Model createFromParcel(Parcel in) {
            return new NewsFeed_Model(in);
        }

        @Override
        public NewsFeed_Model[] newArray(int size) {
            return new NewsFeed_Model[size];
        }
    };

    public int getmLikedPost() {
        return mLikedPost;
    }

    public void setmLikedPost(int mLikedPost) {
        this.mLikedPost = mLikedPost;
    }

    public int getmLikedHeart() {
        return mLikedHeart;
    }

    public void setmLikedHeart(int mLikedHeart) {
        this.mLikedHeart = mLikedHeart;
    }

    public String getmDateNewsFeed() {
        return mDateNewsFeed;
    }

    public void setmDateNewsFeed(String mDateNewsFeed) {
        this.mDateNewsFeed = mDateNewsFeed;
    }

    public String getPostID() {
        return PostID;
    }

    public void setPostID(String postID) {
        this.PostID = postID;
    }

    public String getmNewsFeed_photoPost() {
        return mNewsFeed_photoPost;
    }

    public void setmNewsFeed_photoPost(String mNewsFeed_photoPost) {
        this.mNewsFeed_photoPost = mNewsFeed_photoPost;
    }

    public String getNewsFeed_Desc() {
        return mNewsFeed_Desc;
    }

    public void setNewsFeed_Desc(String newsFeed_Desc) {
        this.mNewsFeed_Desc = newsFeed_Desc;
    }

    public String getmPostDate() {
        return mPostDate;
    }

    public void setmPostDate(String mPostDate) {
        this.mPostDate = mPostDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(PostID);
        parcel.writeString(mNewsFeed_photoPost);
        parcel.writeString(mNewsFeed_Desc);
        parcel.writeString(mPostDate);
        parcel.writeString(mDateNewsFeed);
        parcel.writeInt(mLikedPost);
        parcel.writeInt(mLikedHeart);
    }
}
