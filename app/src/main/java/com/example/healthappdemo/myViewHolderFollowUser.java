package com.example.healthappdemo;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import de.hdodenhof.circleimageview.CircleImageView;

public class myViewHolderFollowUser extends RecyclerView.ViewHolder {
    TextView mUserName;
    TextView mFirstName;
    TextView mLastName;
    TextView Btn_Follow;
    CircleImageView mFollowImage;

    public myViewHolderFollowUser(@NonNull View itemView) {
        super(itemView);

        mUserName = itemView.findViewById(R.id.Tv_userName_follow_user);
        mFirstName = itemView.findViewById(R.id.Tv_firstName_follow_user);
        mLastName = itemView.findViewById(R.id.Tv_lastName_follow_user);
        Btn_Follow = itemView.findViewById(R.id.TV_btnFollowing);
        mFollowImage = itemView.findViewById(R.id.follow_profile_image);


    }

}
