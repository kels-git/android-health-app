package com.example.healthappdemo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;

public class StepCounter_Model implements Parcelable {

    String mStepID;
    String mStepDate;
    String mStepDay;
    String mStepTime;
    String mStepCount ;
    String mStepCountMeter;
    String mStepCalories;
    String mStepDuration;

    public StepCounter_Model(){
        //
    }

    public StepCounter_Model(String StepDate, String StepDay,String StepTime, String StepCount,
                             String StepCountMeter,String StepCalories, String StepDuration ) {

        this.mStepDate = StepDate;
        this.mStepDay = StepDay;
        this.mStepTime = StepTime;
        this.mStepCount = StepCount;
        this.mStepCountMeter = StepCountMeter;
        this.mStepCalories = StepCalories;
        this.mStepDuration = StepDuration;
    }

    protected StepCounter_Model(Parcel in) {
        mStepID = in.readString();
        mStepDate = in.readString();
        mStepDay = in.readString();
        mStepTime = in.readString();
        mStepCount = in.readString();
        mStepCountMeter = in.readString();
        mStepCalories = in.readString();
        mStepDuration = in.readString();
    }

    public static final Creator<StepCounter_Model> CREATOR = new Creator<StepCounter_Model>() {
        @Override
        public StepCounter_Model createFromParcel(Parcel in) {
            return new StepCounter_Model(in);
        }

        @Override
        public StepCounter_Model[] newArray(int size) {
            return new StepCounter_Model[size];
        }
    };

    @Exclude
    public String getmStepID() {
        return mStepID;
    }

    public void setmStepID(String mStepID) {
        this.mStepID = mStepID;
    }

    public String getmStepDate() {
        return mStepDate;
    }

    public void setmStepDate(String mStepDate) {
        this.mStepDate = mStepDate;
    }

    public String getmStepDay() {
        return mStepDay;
    }

    public void setmStepDay(String mStepDay) {
        this.mStepDay = mStepDay;
    }

    public String getmStepTime() {
        return mStepTime;
    }

    public void setmStepTime(String mStepTime) {
        this.mStepTime = mStepTime;
    }

    public String getmStepCount() {
        return mStepCount;
    }

    public void setmStepCount(String mStepCount) {
        this.mStepCount = mStepCount;
    }

    public String getmStepCountMeter() {
        return mStepCountMeter;
    }

    public void setmStepCountMeter(String mStepCountMeter) {
        this.mStepCountMeter = mStepCountMeter;
    }

    public String getmStepCalories() {
        return mStepCalories;
    }

    public void setmStepCalories(String mStepCalories) {
        this.mStepCalories = mStepCalories;
    }

    public String getmStepDuration() {
        return mStepDuration;
    }

    public void setmStepDuration(String mStepDuration) {
        this.mStepDuration = mStepDuration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mStepID);
        parcel.writeString(mStepDate);
        parcel.writeString(mStepDay);
        parcel.writeString(mStepTime);
        parcel.writeString(mStepCount);
        parcel.writeString(mStepCountMeter);
        parcel.writeString(mStepCalories);
        parcel.writeString(mStepDuration);
    }
}
