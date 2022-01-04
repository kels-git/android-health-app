package com.example.healthappdemo;

import android.os.Parcel;
import android.os.Parcelable;

public class Policy_DataModel implements Parcelable {

     String mLifeCoverage;
     String mCriticalIllness;
     String mPaymentMethod;
     String mPaymentAmount;
     String mCategory;
    String mPolicyNumber;

    public Policy_DataModel(){
        //for firebase
    }

    public Policy_DataModel(String LifeCoverage, String CriticalIllness, String PaymentMethod,
                            String PaymentAmount, String Category, String PolicyNumber )
    {
        this.mLifeCoverage = LifeCoverage;
        this.mCriticalIllness = CriticalIllness;
        this.mPaymentMethod = PaymentMethod;
        this.mPaymentAmount = PaymentAmount;
        this.mCategory = Category;
        this.mPolicyNumber = PolicyNumber;
    }

    protected Policy_DataModel(Parcel in) {
        mLifeCoverage = in.readString();
        mCriticalIllness = in.readString();
        mPaymentMethod = in.readString();
        mPaymentAmount = in.readString();
        mCategory = in.readString();
        mPolicyNumber = in.readString();
    }

    public static final Creator<Policy_DataModel> CREATOR = new Creator<Policy_DataModel>() {
        @Override
        public Policy_DataModel createFromParcel(Parcel in) {
            return new Policy_DataModel(in);
        }

        @Override
        public Policy_DataModel[] newArray(int size) {
            return new Policy_DataModel[size];
        }
    };

    public String getmLifeCoverage() {
        return mLifeCoverage;
    }

    public void setmLifeCoverage(String mLifeCoverage) {
        this.mLifeCoverage = mLifeCoverage;
    }

    public String getmCriticalIllness() {
        return mCriticalIllness;
    }

    public void setmCriticalIllness(String mCriticalIllness) {
        this.mCriticalIllness = mCriticalIllness;
    }

    public String getmPaymentMethod() {
        return mPaymentMethod;
    }

    public void setmPaymentMethod(String mPaymentMethod) {
        this.mPaymentMethod = mPaymentMethod;
    }

    public String getmPaymentAmount() {
        return mPaymentAmount;
    }

    public void setmPaymentAmount(String mPaymentAmount) {
        this.mPaymentAmount = mPaymentAmount;
    }

    public String getmCategory() {
        return mCategory;
    }

    public void setmCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public String getmPolicyNumber() {
        return mPolicyNumber;
    }

    public void setmPolicyNumber(String mPolicyNumber) {
        this.mPolicyNumber = mPolicyNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mLifeCoverage);
        parcel.writeString(mCriticalIllness);
        parcel.writeString(mPaymentMethod);
        parcel.writeString(mPaymentAmount);
        parcel.writeString(mCategory);
        parcel.writeString(mPolicyNumber);
    }
}
