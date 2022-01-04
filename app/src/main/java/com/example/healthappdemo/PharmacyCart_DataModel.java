package com.example.healthappdemo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.firestore.Exclude;

public class PharmacyCart_DataModel implements Parcelable {

    String ProductID, ProductCartName, ProductCartPrice, ProductCartQuantity, ProductDate,ProductTime,  ProductDesc  ;
    int mImageProduct;

    public PharmacyCart_DataModel(){

    }

    protected PharmacyCart_DataModel(Parcel in) {
        ProductID = in.readString();
        ProductCartName = in.readString();
        ProductCartPrice = in.readString();
        ProductCartQuantity = in.readString();
        ProductDate = in.readString();
        ProductTime = in.readString();
        ProductDesc = in.readString();
        mImageProduct = in.readInt();
    }

    public static final Creator<PharmacyCart_DataModel> CREATOR = new Creator<PharmacyCart_DataModel>() {
        @Override
        public PharmacyCart_DataModel createFromParcel(Parcel in) {
            return new PharmacyCart_DataModel(in);
        }

        @Override
        public PharmacyCart_DataModel[] newArray(int size) {
            return new PharmacyCart_DataModel[size];
        }
    };

    @Exclude
    public String getProductID() {
        return ProductID;
    }

    public void setProductID(String productID) {
        ProductID = productID;
    }

    public PharmacyCart_DataModel(String productCartName, String ProductCartPrice, String ProductCartQuantity,
                                  String ProductDate, String ProductTime, String ProductDesc, int ImageProduct){
        this.ProductCartName = productCartName;
        this.ProductCartPrice = ProductCartPrice;
        this.ProductCartQuantity = ProductCartQuantity;
        this.ProductDate = ProductDate;
        this.ProductTime = ProductTime;
        this.ProductDesc = ProductDesc;
        this.mImageProduct = ImageProduct;
    }

    public String getProductCartName() {
        return ProductCartName;
    }

    public void setProductCartName(String productCartName) {
        ProductCartName = productCartName;
    }

    public String getProductCartPrice() {
        return ProductCartPrice;
    }

    public void setProductCartPrice(String productCartPrice) {
        ProductCartPrice = productCartPrice;
    }

    public String getProductCartQuantity() {
        return ProductCartQuantity;
    }

    public void setProductCartQuantity(String productCartQuantity) {
        ProductCartQuantity = productCartQuantity;
    }

    public String getProductDate() {
        return ProductDate;
    }

    public void setProductDate(String productDate) {
        ProductDate = productDate;
    }

    public String getProductTime() {
        return ProductTime;
    }

    public void setProductTime(String productTime) {
        ProductTime = productTime;
    }

    public String getProductDesc() {
        return ProductDesc;
    }

    public void setProductDesc(String productDesc) {
        ProductDesc = productDesc;
    }

    public int getmImageProduct() {
        return mImageProduct;
    }

    public void setmImageProduct(int mImageProduct) {
        this.mImageProduct = mImageProduct;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ProductID);
        parcel.writeString(ProductCartName);
        parcel.writeString(ProductCartPrice);
        parcel.writeString(ProductCartQuantity);
        parcel.writeString(ProductDate);
        parcel.writeString(ProductTime);
        parcel.writeString(ProductDesc);
        parcel.writeInt(mImageProduct);
    }
}
