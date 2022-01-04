package com.example.healthappdemo;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PharmacyAdapter extends RecyclerView.Adapter<PharmacyAdapter.PharmacyViewHolder> {

    Activity mActivity;
    ArrayList<PharmacyItem> MedicalPharmacyItem;
    onItemClickListener mListenerPharmacy;


    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        mListenerPharmacy = listener;
    }



    public PharmacyAdapter(ArrayList<PharmacyItem> mMedicalPharmacyItem, onItemClickListener listener, Activity activity) {
        mActivity = activity;
        mListenerPharmacy = listener;
        MedicalPharmacyItem = mMedicalPharmacyItem;

    }


    @NonNull
    @Override
    public PharmacyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pharmacy_item_layout, parent, false);
        return new PharmacyViewHolder(v, mListenerPharmacy, mActivity);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull PharmacyViewHolder holder, int position) {
        PharmacyItem currentItem = MedicalPharmacyItem.get(position);

        holder.mImg_Pharmacy_Image.setImageResource(currentItem.getmNavImagePharmacyItem());
        holder.mTV_Pharmacy_Product_Title.setText(currentItem.getmPharmacyTitle());
        holder.mTV_Pharmacy_Product_Price.setText("RM "+ currentItem.getmPrice());
        holder.mTV_Pharmacy_Product_Desc.setText(currentItem.getmPharmacyDesc());

    }

    @Override
    public int getItemCount() {
        return MedicalPharmacyItem.size();
    }

    public static class PharmacyViewHolder extends RecyclerView.ViewHolder {

        ImageView mImg_Pharmacy_Image;
        TextView mTV_Pharmacy_Product_Title;
        TextView mTV_Pharmacy_Product_Price;
        TextView mTV_Pharmacy_Product_Desc;

        public PharmacyViewHolder(@NonNull View itemView, onItemClickListener listener, Activity activity) {
            super(itemView);

            mImg_Pharmacy_Image = itemView.findViewById(R.id.Img_Pharmacy_Image);
            mTV_Pharmacy_Product_Title = itemView.findViewById(R.id.TV_Pharmacy_Product_Title);
            mTV_Pharmacy_Product_Price = itemView.findViewById(R.id.TV_Pharmacy_Product_Price);
            mTV_Pharmacy_Product_Desc = itemView.findViewById(R.id.TV_Pharmacy_Product_Desc);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                   listener.onItemClick(getAdapterPosition());

                    if (listener != null) {
                        int position = getAdapterPosition();
                        listener.onItemClick(position);
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }



                }
            });

        }
    }
}
