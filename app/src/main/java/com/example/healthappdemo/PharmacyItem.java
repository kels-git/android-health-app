package com.example.healthappdemo;

import android.os.Parcel;
import android.os.Parcelable;

public class PharmacyItem implements Parcelable {

    private int mNavImagePharmacyItem;
    private String mPharmacyTitle;
    private String mPrice;
    private String mPharmacyDesc;
    private String mPharmacyQuantity;

    public  PharmacyItem(){

    }


    public PharmacyItem(int NavImagePharmacyItem, String PharmacyTitle, String Price, String PharmacyDesc, String Quantity ){
        this.mNavImagePharmacyItem = NavImagePharmacyItem;
        this.mPharmacyTitle = PharmacyTitle;
        this.mPrice = Price;
        this.mPharmacyDesc = PharmacyDesc;
        this.mPharmacyQuantity = Quantity;

    }

    protected PharmacyItem(Parcel in) {
        mNavImagePharmacyItem = in.readInt();
        mPharmacyTitle = in.readString();
        mPrice = in.readString();
        mPharmacyDesc = in.readString();
        mPharmacyQuantity = in.readString();
    }

    public static final Creator<PharmacyItem> CREATOR = new Creator<PharmacyItem>() {
        @Override
        public PharmacyItem createFromParcel(Parcel in) {
            return new PharmacyItem(in);
        }

        @Override
        public PharmacyItem[] newArray(int size) {
            return new PharmacyItem[size];
        }
    };

    public int getmNavImagePharmacyItem() {
        return mNavImagePharmacyItem;
    }

    public void setmNavImagePharmacyItem(int mNavImagePharmacyItem) {
        this.mNavImagePharmacyItem = mNavImagePharmacyItem;
    }

    public String getmPharmacyTitle() {
        return mPharmacyTitle;
    }

    public void setmPharmacyTitle(String mPharmacyTitle) {
        this.mPharmacyTitle = mPharmacyTitle;
    }

    public String getmPrice() {
        return mPrice;
    }

    public void setmPrice(String mPrice) {
        this.mPrice = mPrice;
    }

    public String getmPharmacyDesc() {
        return mPharmacyDesc;
    }

    public void setmPharmacyDesc(String mPharmacyDesc) {
        this.mPharmacyDesc = mPharmacyDesc;
    }

    public String getmPharmacyQuantity() {
        return mPharmacyQuantity;
    }

    public void setmPharmacyQuantity(String mPharmacyQuantity) {
        this.mPharmacyQuantity = mPharmacyQuantity;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mNavImagePharmacyItem);
        parcel.writeString(mPharmacyTitle);
        parcel.writeString(mPrice);
        parcel.writeString(mPharmacyDesc);
        parcel.writeString(mPharmacyQuantity);
    }
}
