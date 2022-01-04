package com.example.healthappdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class UserProfileActivity extends AppCompatActivity {
    DrawerLayout mDrawerLayout;
    RecyclerView mRecyclerView, mRecyclerViewProfile;
    ImageView btMenu, btProfile, btSetting,mImageProfileContainer;
    MainProfileAdapter mAdapter;
    static ArrayList<ProfileItems> ProfileArrayList = new ArrayList<>();
    String USER_ID;
    String ELIGIBILITY_CHECK_TWO;
    String ELIGIBILITY_CHECK;
    String USER_CLAIMS;
    TextView mProfileName, displayCount, mVaccinationStatusUserProfile, mProfileOccupation;
    MainNavAdapter.onItemClickListener mListener;

    String PhotoLink;
    String savePhoto;
    String PolicyHolder_FName, PolicyHolder_LName;
    ProgressBar mVaccinationBarStatus;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);


        mDrawerLayout = findViewById(R.id.drawer_layout);
        btMenu = findViewById(R.id.Menu_bar_btn);
        btSetting = findViewById(R.id.Settings_Menu_bar_btn);
        btProfile = findViewById(R.id.Profile_Menu_bar_btn);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerViewProfile = findViewById(R.id.recycler_view_profile);
        mProfileName = findViewById(R.id.profile_UserName);

        mImageProfileContainer = findViewById(R.id.roundedImageView);
        mVaccinationStatusUserProfile = findViewById(R.id.vaccination_profile_status);
        mProfileOccupation = findViewById(R.id.Profile_Occupation);
        mVaccinationBarStatus = findViewById(R.id.customProgressProfile);

        displayCount = findViewById(R.id.TV_pre_account);


        //set layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //mRecyclerViewProfile.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerViewProfile.setLayoutManager(linearLayoutManager);

        //set the adapter
        mRecyclerViewProfile.setAdapter(new MainProfileAdapter(ProfileArrayList));

        //set the adapter
        mRecyclerView.setAdapter(new MainNavAdapter(NewsFeedActivity.drawerArrayList, mListener, this  ));

        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open drawer
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        btProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, UserProfileActivity.class);
                startActivity(intent); // start same activity
                finish(); // destroy older activity
                overridePendingTransition(0, 0);

            }
        });

        btSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserProfileActivity.this, UserSettingsActivity.class);
                startActivity(intent); // start same activity
                finish(); // destroy older activity

            }
        });

        ProfileArrayList.clear();
        ProfileArrayList.add(new ProfileItems("Blood Test", 12));
        ProfileArrayList.add(new ProfileItems("Claim Submit", 32));
        ProfileArrayList.add(new ProfileItems("Pharmacy Buy", 60));

        SharedPreferences sharedPref = UserProfileActivity.this.getPreferences(Context.MODE_PRIVATE);
        String imagePathSaved = sharedPref.getString("ImagePath", PhotoLink); //null represents the default value.
        Picasso.get()
                .load(imagePathSaved)
                .into(mImageProfileContainer);


        displayUserName_AND_Photo();
        displayOccupation();

    }



    private void displayUserName_AND_Photo() {
        FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
        if (AppUser != null) {
            USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            DocumentReference documentReference = FirebaseFirestore.getInstance()
                    .collection("REGISTERED USERS").document(USER_ID);


            documentReference.getParent().document(USER_ID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    assert value != null;
                    PolicyHolder_FName = value.getString("mFirstName");
                    PolicyHolder_LName = value.getString("mLastName");
                    PhotoLink = value.getString("mPhotoUrl");
                    //String PolicyHolder_Gender = value.getString("Gender");
                    //String PolicyHolder_Occupation = value.getString("Occupation");


                    Picasso.get()
                            .load(PhotoLink)
                            .into(mImageProfileContainer);
                        mProfileName.setText( PolicyHolder_FName + " " + PolicyHolder_LName);


                }


            });


        }
    }

    private void displayOccupation() {
        FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
        if (AppUser != null) {
            USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            ELIGIBILITY_CHECK = FirebaseAuth.getInstance().getCurrentUser().getUid();
            ELIGIBILITY_CHECK_TWO = FirebaseAuth.getInstance().getCurrentUser().getUid();


            DocumentReference documentReference = FirebaseFirestore.getInstance()
                    .collection("REGISTERED USERS").document(USER_ID)
                    .collection("ELIGIBILITY CHECK ONE").document(ELIGIBILITY_CHECK)
                    .collection("ELIGIBILITY CHECK TWO").document(ELIGIBILITY_CHECK_TWO);

            documentReference.getParent().document(ELIGIBILITY_CHECK_TWO).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
                @Override
                public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                    assert value != null;

                    String PolicyHolder_Occupation = value.getString("Occupation");
                    String PolicyHolder_Vaccination = value.getString("Covid Status");
                    mProfileOccupation.setText(PolicyHolder_Occupation);

                    switch (PolicyHolder_Vaccination) {
                        case "positive Covid19":
                        case "Negative Covid19":
                            mVaccinationStatusUserProfile.setText("Not-Yet Vaccinated");
                            mVaccinationBarStatus.setProgress(0);

                            break;
                        case "Completely Vaccinated":
                            mVaccinationStatusUserProfile.setText(PolicyHolder_Vaccination);
                            mVaccinationBarStatus.setProgress(100);
                            mVaccinationBarStatus.setProgressDrawable(getDrawable(R.drawable.grad_gradient_green));

                            break;
                        case "Partially Vaccinated":
                            mVaccinationStatusUserProfile.setText(PolicyHolder_Vaccination);
                            mVaccinationBarStatus.setProgress(50);
                            mVaccinationBarStatus.setProgressDrawable(getDrawable(R.drawable.custom_progress_profile));
                            break;
                        default:
                            mVaccinationStatusUserProfile.setText(PolicyHolder_Vaccination);
                            mVaccinationBarStatus.setProgress(0);

                            break;
                    }

                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayUserName_AND_Photo();
        displayOccupation();

//        SharedPreferences sharedPref = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
//        PhotoLink = sharedPref.getString("ImagePath",PhotoLink);

    }

    @Override
    protected void onPause() {
        super.onPause();
        NewsFeedActivity.closeDrawer(mDrawerLayout);

//        SharedPreferences sharedPref = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString("ImagePath", PhotoLink);
//        editor.commit();




    }

    @Override
    protected void onResume() {
        super.onResume();

//        SharedPreferences sharedPref = getSharedPreferences("PREFERENCE", MODE_PRIVATE);
//        PhotoLink = sharedPref.getString("ImagePath",PhotoLink);
    }
}