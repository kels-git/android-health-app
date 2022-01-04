package com.example.healthappdemo;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainProfileAdapter extends RecyclerView.Adapter<MainProfileAdapter.ProfileViewHolder> {

    ArrayList<ProfileItems> ProfileArrayList;

    public MainProfileAdapter(ArrayList<ProfileItems> mProfileArrayList) {
        ProfileArrayList = mProfileArrayList;
    }


    @NonNull
    @Override
    public ProfileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_profile, parent, false);
        return new MainProfileAdapter.ProfileViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProfileViewHolder holder, int position) {
        ProfileItems currentProfileItem = ProfileArrayList.get(position);

        holder.mProfileTitlePage.setText(currentProfileItem.getmProfileTitle());
        holder.mProfileCountPage.setText(String.valueOf(currentProfileItem.getmProfileCount()));

    }

    @Override
    public int getItemCount() {
        return ProfileArrayList.size();
    }

    public static class ProfileViewHolder extends RecyclerView.ViewHolder{

        public TextView mProfileCountPage;
        public TextView mProfileTitlePage;

        public ProfileViewHolder(@NonNull View itemView) {
            super(itemView);
            mProfileTitlePage = itemView.findViewById(R.id.profile_container_title);
            mProfileCountPage = itemView.findViewById(R.id.profile_container_count);
        }
    }
}
