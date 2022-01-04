package com.example.healthappdemo;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;

public class myViewHolderCart extends RecyclerView.ViewHolder {

    TextView mCartTitle, mCartPrice, mCartQuantity, mDeleteProductCart;
    LottieAnimationView mDeleteCart;

    public myViewHolderCart(@NonNull View itemView) {
        super(itemView);

        mCartTitle = itemView.findViewById(R.id.TV_ShoppingCart_title);
        mCartPrice = itemView.findViewById(R.id.TV_ShoppingCart_price);
        mCartQuantity = itemView.findViewById(R.id.TV_ShoppingCart_Quantity);
        mDeleteProductCart = itemView.findViewById(R.id.TV_cartProduct_Delete);


    }
}
