package com.example.healthappdemo;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;

public class myViewHolderClaimHistory extends RecyclerView.ViewHolder {

    TextView ClaimDateSubmit;
    TextView ClaimRequestedAmount;
    TextView mBt_deleteClaimHistory;
    TextView mBt_ClaimHistoryView;

    public myViewHolderClaimHistory(@NonNull View itemView) {
        super(itemView);

        mBt_ClaimHistoryView = itemView.findViewById(R.id.TV_ClaimHistory_View);
        mBt_deleteClaimHistory = itemView.findViewById(R.id.TV_ClaimHistory_Delete);
        ClaimDateSubmit = itemView.findViewById(R.id.TV_Claim_date);
        ClaimRequestedAmount = itemView.findViewById(R.id.TV_Claimant_request_amount);
    }

}
