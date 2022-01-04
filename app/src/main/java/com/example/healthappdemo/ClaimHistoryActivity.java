package com.example.healthappdemo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class ClaimHistoryActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    RecyclerView mRecyclerView, mRecyclerViewClaimHistory;
    ImageView btMenu, btProfile, btSetting;

    String USER_ID;
    String USER_CLAIMS;
    MainNavAdapter.onItemClickListener mListener;
    Dialog dialog, secondDialog, ClaimHistoryDialog;
    TextView DialogTitle, DialogMessage, No_ClaimDisplay;
    Button YesDialog_btn, NoDialog_btn, YesDialogSecondBt;
    LinearLayout mLinear_ClaimHistoryWrapper;

    Dialog dialog_three;
    TextView mmDialogTitle,
            mmDialogMessage, mmCancelDialog_btn, mmProceedDialog_btn;

    FirestoreRecyclerOptions <ClaimsHistory_DataModel> options;
    FirestoreRecyclerAdapter<ClaimsHistory_DataModel, myViewHolderClaimHistory> myAdapter;

    TextView mTV_tableHead_Dialog_title,mTV_table_first_title, mTV_table_second_title,
            mTV_table_third_title, mTV_table_fourth_title, mTV_table_fifth_title, mTV_table_sixth_title;

    TextView  mTV_table_first,mTV_table_second, mTV_table_third, mTV_table_fourth, mTV_table_fifth, mTV_table_sixth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim_history);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        btMenu = findViewById(R.id.Menu_bar_btn);
        btSetting = findViewById(R.id.Settings_Menu_bar_btn);
        btProfile = findViewById(R.id.Profile_Menu_bar_btn);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerViewClaimHistory = findViewById(R.id.recycler_view_claim_history);
        No_ClaimDisplay = findViewById(R.id.TV_NoHistoryDisplay);
        mLinear_ClaimHistoryWrapper = findViewById(R.id.LinearLayout_CheckEmptyClaim);


        //set layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //set the adapter for drawers
        mRecyclerView.setAdapter(new MainNavAdapter(NewsFeedActivity.drawerArrayList, mListener, this));

        //set onclick to open drawer menu
        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open drawer
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //set onclick to navigate profile activity
        btProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClaimHistoryActivity.this, UserProfileActivity.class);
                startActivity(intent); // start same activity
                finish(); // destroy older activity
            }
        });

        //set onclick to navigate setting activity
        btSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClaimHistoryActivity.this, UserSettingsActivity.class);
                startActivity(intent); // start same activity
                finish(); // destroy older activity
            }
        });


        mRecyclerViewClaimHistory.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerViewClaimHistory.setHasFixedSize(false);

            readData();
            checkClaimDocument();

        NewsFeedActivity newsFeedActivity = new NewsFeedActivity();
        newsFeedActivity.mAboutHA = findViewById(R.id.TV_menu_about_HA);
        newsFeedActivity.mAboutHA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                displayDialogAboutApp();

            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void displayDialogAboutApp() {
        dialog_three = new Dialog(ClaimHistoryActivity.this);
        dialog_three.setContentView(R.layout.dialog_custom);
        dialog_three.setCancelable(true);
        dialog_three.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        dialog_three.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        mmDialogTitle = dialog_three.findViewById(R.id.Dialog_title);
        mmDialogMessage = dialog_three.findViewById(R.id.Dialog_Message);
        mmCancelDialog_btn = dialog_three.findViewById(R.id.Btn_Cancel);
        mmProceedDialog_btn = dialog_three.findViewById(R.id.Btn_Proceed);

        mmCancelDialog_btn.setVisibility(View.GONE);

        mmProceedDialog_btn.setText("Visit Developer Page");

        mmDialogTitle.setText("About Health App");
        mmDialogMessage.setText(
                "Health App is a personal side project carried out by Osazuwa Ogiemwanye, a software developer. Health app includes " +
                        "a step counter, Bmi Calculator, covid info, news feed , and follow users section." + "Also included is extra " +
                        "for IT recruiters a basic for Insurance company on eclaim submission and many more. Basically this app is met " +
                        "for IT recruiters to display my little " + "skills in mobile app development Android native (Java) and integrated " +
                        "with flexible backend (Firebase)");
        dialog_three.show();


        //confirm the dialog box and navigate to the payment section
        proceedToDeveloperScreen();
    }

    private void proceedToDeveloperScreen() {
        mmProceedDialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_three.dismiss();
                Intent navigate_dev_screen = new Intent(ClaimHistoryActivity.this, ContactDeveloperActivity.class);
                startActivity(navigate_dev_screen);

            }
        });
    }

    private void checkClaimDocument() {
        FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
        if (AppUser != null  ) {
            USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            USER_CLAIMS = FirebaseAuth.getInstance().getCurrentUser().getUid();

            CollectionReference ColRef = FirebaseFirestore.getInstance()
                    .collection("REGISTERED USERS").document(USER_ID)
                    .collection("USER CLAIMS");
            ColRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                    if(queryDocumentSnapshots.isEmpty()){
                        No_ClaimDisplay.setVisibility(View.VISIBLE);
                        No_ClaimDisplay.setText("No Claims Submitted "+ "\n"+ "at moment");
                        mLinear_ClaimHistoryWrapper.setVisibility(View.VISIBLE);
                        mRecyclerViewClaimHistory.setVisibility(View.GONE);
                    }else{
                        No_ClaimDisplay.setVisibility(View.GONE);
                        mLinear_ClaimHistoryWrapper.setVisibility(View.GONE);
                        mRecyclerViewClaimHistory.setVisibility(View.VISIBLE);
                    }

                }
            });
        }
    }

    private void readData() {
        FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
        if (AppUser != null  ) {
            USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            USER_CLAIMS = FirebaseAuth.getInstance().getCurrentUser().getUid();

            Query query = FirebaseFirestore.getInstance()
                    .collection("REGISTERED USERS").document(USER_ID)
                    .collection("USER CLAIMS").orderBy("mUserSubmitClaimDate", Query.Direction.DESCENDING);

            options = new FirestoreRecyclerOptions.Builder<ClaimsHistory_DataModel>()
                    .setQuery(query, ClaimsHistory_DataModel.class)
                    .build();

            myAdapter = new FirestoreRecyclerAdapter<ClaimsHistory_DataModel, myViewHolderClaimHistory>(options) {

                @SuppressLint("SetTextI18n")
                @Override
                protected void onBindViewHolder(@NonNull myViewHolderClaimHistory holder, int position, @NonNull ClaimsHistory_DataModel model) {
                    holder.ClaimDateSubmit.setText(model.getmUserSubmitClaimDate());
                    holder.ClaimRequestedAmount.setText("RM " + model.getmUserAmount_request() + ".00");

                    holder.mBt_deleteClaimHistory.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            _displayDeleteDialogMsg(holder);
                        }
                    });

                    holder.mBt_ClaimHistoryView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            viewItem(holder.getAdapterPosition());
                        }
                    });
                }

                @NonNull
                @Override
                public myViewHolderClaimHistory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                    View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_claim_history, parent, false);
                    return new myViewHolderClaimHistory(v);

                }
            };
                mRecyclerViewClaimHistory.setAdapter(myAdapter);
        }
    }


    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    private void _displayDeleteDialogMsg(myViewHolderClaimHistory holder) {
        dialog = new Dialog(ClaimHistoryActivity.this);
        dialog.setContentView(R.layout.dialog_custom);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        DialogTitle = dialog.findViewById(R.id.Dialog_title);
        DialogMessage = dialog.findViewById(R.id.Dialog_Message);
        NoDialog_btn = dialog.findViewById(R.id.Btn_Cancel);
        YesDialog_btn = dialog.findViewById(R.id.Btn_Proceed);
        YesDialog_btn.setText("Yes");
        NoDialog_btn.setText("No");
        DialogTitle.setText("E-CLAIM DELETE");
        DialogMessage.setText("Do you wish to delete this e-claim details? Please note you cannot retrieve these details after deleting! ");
        dialog.show();

        //No proceed to the payment-demo page
        No_Btn_DialogBox();

        //yes -cancel the check quotation and return to main page
        Yes_Btn_DialogBox(holder);

    }

    private void No_Btn_DialogBox() {
        NoDialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do nothing
                dialog.dismiss();
            }
        });
    }

    private void Yes_Btn_DialogBox(myViewHolderClaimHistory holder) {
        YesDialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteItem(holder.getAdapterPosition());
               // ClaimHistoryDialog.dismiss();
                dialog.dismiss();
                checkClaimDocument();
            }
        });

    }

    public void viewItem(int position){
        myAdapter.getSnapshots().getSnapshot(position).getReference().addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(value != null){
                    String getSubmitDate = value.getString("mUserSubmitClaimDate");
                    String getPolicyType = value.getString("mUserPolicy_type");
                    String getClaimType = value.getString("mUserClaim_type");
                    String getDocDiagnose = value.getString("mUserDoc_diagnose");
                    String getAmountReceipt = value.getString("mUserAmount_request");

                    ClaimHistoryDialog = new Dialog(ClaimHistoryActivity.this);
                    ClaimHistoryDialog.setContentView(R.layout.dialog_custom_table);
                    ClaimHistoryDialog.setCancelable(true);
                    ClaimHistoryDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
                    ClaimHistoryDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    mTV_tableHead_Dialog_title = ClaimHistoryDialog.findViewById(R.id.TV_tableHead_Dialog_title);
                    mTV_table_first_title = ClaimHistoryDialog.findViewById(R.id.TV_table_first_title);
                    mTV_table_second_title = ClaimHistoryDialog.findViewById(R.id.TV_table_second_title);
                    mTV_table_third_title = ClaimHistoryDialog.findViewById(R.id.TV_table_third_title);
                    mTV_table_fourth_title = ClaimHistoryDialog.findViewById(R.id.TV_table_fourth_title);
                    mTV_table_fifth_title = ClaimHistoryDialog.findViewById(R.id.TV_table_fifth_title);

                    mTV_table_first = ClaimHistoryDialog.findViewById(R.id.TV_table_first);
                    mTV_table_second = ClaimHistoryDialog.findViewById(R.id.TV_table_second);
                    mTV_table_third = ClaimHistoryDialog.findViewById(R.id.TV_table_third);
                    mTV_table_fourth = ClaimHistoryDialog.findViewById(R.id.TV_table_fourth);
                    mTV_table_fifth= ClaimHistoryDialog.findViewById(R.id.TV_table_fifth);

                    YesDialogSecondBt = ClaimHistoryDialog.findViewById(R.id.Btn_Dismiss);

                    mTV_tableHead_Dialog_title.setText("E-CLAIM DETAILS");
                    mTV_table_first_title.setText("Date"); mTV_table_first.setText(getSubmitDate);
                    mTV_table_second_title.setText("Policy Type"); mTV_table_second.setText(getPolicyType);
                    mTV_table_third_title.setText("Claim Type"); mTV_table_third.setText(getClaimType);
                    mTV_table_fourth_title.setText("Doctor Diagnose"); mTV_table_fourth.setText(getDocDiagnose);
                    mTV_table_fifth_title.setText("Request Amount"); mTV_table_fifth.setText("RM" + getAmountReceipt +".00");

                    YesDialogSecondBt.setVisibility(View.GONE);

                    ClaimHistoryDialog.show();
                }

            }
        });
    }


    public void deleteItem(int position){
        myAdapter.getSnapshots().getSnapshot(position).getReference().delete();
    }



    @Override
    protected void onStart() {
        super.onStart();
        myAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myAdapter.stopListening();

    }

    @Override
    protected void onPause() {
        super.onPause();
        NewsFeedActivity.closeDrawer(mDrawerLayout);
    }



}