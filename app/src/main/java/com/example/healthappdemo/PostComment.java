package com.example.healthappdemo;

import android.os.Parcel;
import android.os.Parcelable;

public class PostComment implements Parcelable {

    int CommentPosition;
    String CommentPostID;
    String CommentDesc;
    String CommentPublisher;

    public PostComment() {

    }

    public PostComment(int commentPosition, String commentPostID, String commentDesc, String commentPublisher) {
        CommentPosition = commentPosition;
        CommentPostID = commentPostID;
        CommentDesc = commentDesc;
        CommentPublisher = commentPublisher;
    }

    protected PostComment(Parcel in) {
        CommentPosition = in.readInt();
        CommentPostID = in.readString();
        CommentDesc = in.readString();
        CommentPublisher = in.readString();
    }

    public static final Creator<PostComment> CREATOR = new Creator<PostComment>() {
        @Override
        public PostComment createFromParcel(Parcel in) {
            return new PostComment(in);
        }

        @Override
        public PostComment[] newArray(int size) {
            return new PostComment[size];
        }
    };

    public int getCommentPosition() {
        return CommentPosition;
    }

    public void setCommentPosition(int commentPosition) {
        CommentPosition = commentPosition;
    }

    public String getCommentPostID() {
        return CommentPostID;
    }

    public void setCommentPostID(String commentPostID) {
        CommentPostID = commentPostID;
    }

    public String getCommentDesc() {
        return CommentDesc;
    }

    public void setCommentDesc(String commentDesc) {
        CommentDesc = commentDesc;
    }

    public String getCommentPublisher() {
        return CommentPublisher;
    }

    public void setCommentPublisher(String commentPublisher) {
        CommentPublisher = commentPublisher;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(CommentPosition);
        parcel.writeString(CommentPostID);
        parcel.writeString(CommentDesc);
        parcel.writeString(CommentPublisher);
    }
}
