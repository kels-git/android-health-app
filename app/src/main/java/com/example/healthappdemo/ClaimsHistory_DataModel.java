package com.example.healthappdemo;

public class ClaimsHistory_DataModel {

    String mUserClaimantName;
    String mUserPolicy_type;
    String mUserClaim_type;
    String mUserDoc_diagnose;
    String mUserAmount_receipt;
    String mUserAmount_request;
    String mUserSubmitClaimDate;
    String mUserClaimPhotoLink;
    String mClaimSubmitCount;

    public ClaimsHistory_DataModel(){
        // needed for firebase;
    }

    public  ClaimsHistory_DataModel(String UserClaimantName, String UserPolicy_type, String UserClaim_type,
                                    String UserDoc_diagnose, String UserAmount_receipt, String UserAmount_request,
                                    String ClaimHistoryDate, String UserClaimPhotoLink, String ClaimSubmitCount
    ){
        mUserClaimantName = UserClaimantName;
        mUserPolicy_type = UserPolicy_type;
        mUserClaim_type = UserClaim_type;
        mUserDoc_diagnose = UserDoc_diagnose;
        mUserAmount_receipt = UserAmount_receipt;
        mUserAmount_request = UserAmount_request;
        mUserSubmitClaimDate = ClaimHistoryDate;
        mUserClaimPhotoLink = UserClaimPhotoLink;
        mClaimSubmitCount = ClaimSubmitCount;
    }

    public String getmUserClaimantName() {
        return mUserClaimantName;
    }

    public void setmUserClaimantName(String mUserClaimantName) {
        this.mUserClaimantName = mUserClaimantName;
    }

    public String getmUserPolicy_type() {
        return mUserPolicy_type;
    }

    public void setmUserPolicy_type(String mUserPolicy_type) {
        this.mUserPolicy_type = mUserPolicy_type;
    }

    public String getmUserClaim_type() {
        return mUserClaim_type;
    }

    public void setmUserClaim_type(String mUserClaim_type) {
        this.mUserClaim_type = mUserClaim_type;
    }

    public String getmUserDoc_diagnose() {
        return mUserDoc_diagnose;
    }

    public void setmUserDoc_diagnose(String mUserDoc_diagnose) {
        this.mUserDoc_diagnose = mUserDoc_diagnose;
    }

    public String getmUserAmount_receipt() {
        return mUserAmount_receipt;
    }

    public void setmUserAmount_receipt(String mUserAmount_receipt) {
        this.mUserAmount_receipt = mUserAmount_receipt;
    }

    public String getmUserAmount_request() {
        return mUserAmount_request;
    }

    public void setmUserAmount_request(String mUserAmount_request) {
        this.mUserAmount_request = mUserAmount_request;
    }

    public String getmUserSubmitClaimDate() {
        return mUserSubmitClaimDate;
    }

    public void setmUserSubmitClaimDate(String mUserSubmitClaimDate) {
        this.mUserSubmitClaimDate = mUserSubmitClaimDate;
    }

    public String getmUserClaimPhotoLink() {
        return mUserClaimPhotoLink;
    }

    public void setmUserClaimPhotoLink(String mUserClaimPhotoLink) {
        this.mUserClaimPhotoLink = mUserClaimPhotoLink;
    }

    public String getmClaimSubmitCount() {
        return mClaimSubmitCount;
    }

    public void setmClaimSubmitCount(String mClaimSubmitCount) {
        this.mClaimSubmitCount = mClaimSubmitCount;
    }
}
