package com.example.healthappdemo;

public class User_Model {

    String mID;
    String mPhotoUrl;
    String mEmail;
    String mPassword;
    String mFirstName;
    String mLastName;
    String mUserName;

    public User_Model() {

    }

    public User_Model(String mID, String mPhotoUrl, String mEmail, String mPassword,
                      String mFirstName, String mLastName, String mUserName) {
        this.mID = mID;
        this.mPhotoUrl = mPhotoUrl;
        this.mEmail = mEmail;
        this.mPassword = mPassword;
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
        this.mUserName = mUserName;
    }

    public String getmID() {
        return mID;
    }

    public void setmID(String mID) {
        this.mID = mID;
    }

    public String getmPhotoUrl() {
        return mPhotoUrl;
    }

    public void setmPhotoUrl(String mPhotoUrl) {
        this.mPhotoUrl = mPhotoUrl;
    }

    public String getmEmail() {
        return mEmail;
    }

    public void setmEmail(String mEmail) {
        this.mEmail = mEmail;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }

    public String getmFirstName() {
        return mFirstName;
    }

    public void setmFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getmLastName() {
        return mLastName;
    }

    public void setmLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getmUserName() {
        return mUserName;
    }

    public void setmUserName(String mUserName) {
        this.mUserName = mUserName;
    }
}
