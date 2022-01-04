package com.example.healthappdemo;


import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainNavAdapter extends RecyclerView.Adapter<MainNavAdapter.NavViewHolder> {


    //initialize variable
    ArrayList<NavBarItems> mdrawerArrayList;
    Activity mActivity;
    onItemClickListener mListener;


    public interface onItemClickListener {
        void onItemClick(int position);
    }


    public void setOnItemClickListener(onItemClickListener listener) {
        mListener = listener;
    }


    //create a constructor
    public MainNavAdapter(ArrayList<NavBarItems> drawerArrayList, onItemClickListener listener, Activity activity) {
        mActivity = activity;

        mdrawerArrayList = drawerArrayList;
    }

    @NonNull
    @Override
    public NavViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_drawer_main, parent, false);
        return new NavViewHolder(v, mActivity);
    }

    @Override
    public void onBindViewHolder(@NonNull NavViewHolder holder, int position) {
        NavBarItems currentNavItem = mdrawerArrayList.get(position);

        holder.mNavImageView.setImageResource(currentNavItem.getmNavImageResource());
        holder.mNavTextView.setText(currentNavItem.getmNavTextItem());

    }

    @Override
    public int getItemCount() {
        return mdrawerArrayList.size();
    }

    public static class NavViewHolder extends RecyclerView.ViewHolder {
        public ImageView mNavImageView;
        public TextView mNavTextView;

        public NavViewHolder(@NonNull View itemView,  Activity activity) {
            super(itemView);

            mNavImageView = itemView.findViewById(R.id.Img_nav_drawer);
            mNavTextView = itemView.findViewById(R.id.TV_nav_drawer);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();

                    switch (position) {

                        case 0:
                            activity.startActivity(new Intent(activity, NewsFeedActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            activity.overridePendingTransition(0, 0);
                            break;

                        case 1:
                            activity.startActivity(new Intent(activity, FollowUserActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            activity.overridePendingTransition(0, 0);
                            break;
                        case 2:

                            activity.startActivity(new Intent(activity, StepTrackerActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            activity.overridePendingTransition(0, 0);
                            break;
                        case 3:
                            activity.startActivity(new Intent(activity, WeightCheckActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            activity.overridePendingTransition(0, 0);
                            break;

                        case 4:
                            activity.startActivity(new Intent(activity, CovidDemoActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            activity.overridePendingTransition(0, 0);
                            break;


                        case 5:
                            activity.startActivity(new Intent(activity, ExtraRecruitersActivity.class)
                                    .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                            activity.overridePendingTransition(0, 0);
                            break;


                    }

                    //listener.onItemClick(getAdapterPosition());
//                    if(listener != null){
//                        int position = getAdapterPosition();
//                        listener.onItemClick(position);
//                        if(position != RecyclerView.NO_POSITION ){
//                            listener.onItemClick(position);
//                        }
//                    }

                }
            });
        }
    }


}
