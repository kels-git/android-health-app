package com.example.healthappdemo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthProvider;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FollowUserActivity extends AppCompatActivity {

    Dialog dialog_three;
    DrawerLayout mDrawerLayout;
    RecyclerView mRecyclerView;
    ImageView btMenu, btProfile, btSetting;

    MainNavAdapter.onItemClickListener mListener;
    TextView mmDialogTitle,
            mmDialogMessage, mmCancelDialog_btn, mmProceedDialog_btn;

    RecyclerView mRecyclerViewFollow;
    FirestoreRecyclerOptions<User_Model> options;
    FirestoreRecyclerAdapter<User_Model, myViewHolderFollowUser> mUserFollowAdapter;
    String FOLLOW_USERS;
    String USER_ID;


    SharedPreferences sharedPreferences;
    public static final String SHARED_PREF_NAME = "myPref";
    public static final String KEY_BUTTON = "buttonSave";
    public static final String KEY_USERID = "buttonUSERID";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_users);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        btMenu = findViewById(R.id.Menu_bar_btn);
        btSetting = findViewById(R.id.Settings_Menu_bar_btn);
        btProfile = findViewById(R.id.Profile_Menu_bar_btn);
        mRecyclerView = findViewById(R.id.recycler_view);

        mRecyclerViewFollow = findViewById(R.id.RecyclerView_follow_user);


        //set layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //set the adapter
        mRecyclerView.setAdapter(new MainNavAdapter(NewsFeedActivity.drawerArrayList, mListener, this));


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
                Intent intent = new Intent(FollowUserActivity.this, UserProfileActivity.class);
                startActivity(intent); // start same activity
                finish(); // destroy older activity
            }
        });

        btSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FollowUserActivity.this, UserSettingsActivity.class);
                startActivity(intent); // start same activity
            }
        });

        mRecyclerViewFollow.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        mRecyclerViewFollow.setHasFixedSize(false);
        mRecyclerViewFollow.setAnimation(null);


        readData();

        NewsFeedActivity newsFeedActivity = new NewsFeedActivity();
        newsFeedActivity.mAboutHA = findViewById(R.id.TV_menu_about_HA);
        newsFeedActivity.mAboutHA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                displayDialogAboutApp();

            }
        });

    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    private void displayDialogAboutApp() {

        dialog_three = new Dialog(FollowUserActivity.this);
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
                Intent navigate_dev_screen = new Intent(FollowUserActivity.this, ContactDeveloperActivity.class);
                startActivity(navigate_dev_screen);

            }
        });
    }


    private void readData() {
        options = new FirestoreRecyclerOptions.Builder<User_Model>()
                .setQuery(FirebaseFirestore.getInstance()
                        .collection("REGISTERED USERS"), User_Model.class)
                .build();

        mUserFollowAdapter = new FirestoreRecyclerAdapter<User_Model, myViewHolderFollowUser>(options) {
            @Override
            protected void onBindViewHolder(@NonNull myViewHolderFollowUser holder, int position, @NonNull User_Model model) {


                holder.Btn_Follow.setVisibility(View.VISIBLE);
                holder.mUserName.setText(model.getmUserName());
                holder.mFirstName.setText(model.getmFirstName());
                holder.mLastName.setText(model.getmLastName());
                Glide.with(FollowUserActivity.this).load(model.getmPhotoUrl()).into(holder.mFollowImage);


                //check if the user is added and follow
                isFollowingUser(holder.Btn_Follow, model, position);

                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                assert firebaseUser != null;
                if (model.getmID().equals(firebaseUser.getUid())) {
                    holder.Btn_Follow.setVisibility(View.GONE);
                }


                holder.Btn_Follow.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onClick(View view) {
                        int position = holder.getAdapterPosition();
                        FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
                        if (AppUser != null) {
                            USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            FOLLOW_USERS = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            CollectionReference colRef = FirebaseFirestore.getInstance()
                                    .collection("REGISTERED USERS").document(USER_ID)
                                    .collection("FOLLOWING");

                            if (holder.Btn_Follow.getText().toString().equals("follow")) {

                                addFollowUserToFirestore(holder.Btn_Follow, model, position);
                            }else if(holder.Btn_Follow.getText().toString().equals("following")){
                                deleteFollowUserToFirestore(holder.Btn_Follow, model, position);

                            }


                        }
                    }

                });

            }

            @NonNull
            @Override
            public myViewHolderFollowUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
                return new myViewHolderFollowUser(view);
            }
        };

        mRecyclerViewFollow.setAdapter(mUserFollowAdapter);

    }


    private void deleteFollowUserToFirestore(TextView btn_follow, User_Model model, int position) {
        FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
        if (AppUser != null) {
            USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FOLLOW_USERS = FirebaseAuth.getInstance().getCurrentUser().getUid();

            CollectionReference colRef = FirebaseFirestore.getInstance()
                    .collection("REGISTERED USERS").document(USER_ID)
                    .collection("FOLLOWING");

            Query q1 = colRef.whereEqualTo("follow_user position", position);
            q1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    boolean isExisting = false;
                    for (DocumentSnapshot ds : queryDocumentSnapshots) {
                        int userFollowPosition;
                        userFollowPosition = Objects.requireNonNull(ds.getLong("follow_user position")).intValue();

                        if (userFollowPosition == position) {
                            isExisting = true;
                            model.setmID(ds.getId());
                            colRef.document(model.getmID()).delete();

                            btn_follow.setText("follow");

                        }

                    }
                    overridePendingTransition(0, 0);
                    Intent intent = new Intent(FollowUserActivity.this, FollowUserActivity.class);
                    overridePendingTransition(0, 0);
                    startActivity(intent); // start same activity
                    overridePendingTransition(0, 0);
                    finish(); // destroy older activity
                    overridePendingTransition(0, 0);



                    if (!isExisting) {
                        addFollowUserToFirestore(btn_follow, model, position);
                    }
                }
            });



        }
    }


    // first add to fireStore
    private void addFollowUserToFirestore(TextView btn_follow, User_Model model, int position) {

        FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
        if (AppUser != null) {
            USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FOLLOW_USERS = FirebaseAuth.getInstance().getCurrentUser().getUid();

            CollectionReference colRef = FirebaseFirestore.getInstance()
                    .collection("REGISTERED USERS").document(USER_ID)
                    .collection("FOLLOWING");

            Map<String, Object> following = new HashMap<>();
            following.put("following user", model.getmID());
            following.put("follow_user position", position);

            colRef.add(following).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @SuppressLint({"SetTextI18n"})
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    btn_follow.setText("following");

                }
            });
        }
    }


    private void isFollowingUser(TextView btn_follow, User_Model model, int position) {

        FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
        if (AppUser != null) {
            USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            FOLLOW_USERS = FirebaseAuth.getInstance().getCurrentUser().getUid();

            CollectionReference colRef = FirebaseFirestore.getInstance()
                    .collection("REGISTERED USERS").document(USER_ID)
                    .collection("FOLLOWING");



            Query q1 = colRef.whereEqualTo("follow_user position", position);
            q1.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                    boolean isExisting = false;
                    for (DocumentSnapshot ds : queryDocumentSnapshots) {
                        int userFollowPosition;
                        userFollowPosition = Objects.requireNonNull(ds.getLong("follow_user position")).intValue();

                        if (userFollowPosition == position) {
                            isExisting = true;
                            btn_follow.setText("following");

                        }

                    }

                    if (!isExisting) {
                        btn_follow.setText("follow");
                    }


                }
            });

        }

    }


    @Override
    protected void onPause() {
        super.onPause();

        NewsFeedActivity.closeDrawer(mDrawerLayout);
        mUserFollowAdapter.stopListening();


    }

    @Override
    protected void onResume() {
        super.onResume();

        mUserFollowAdapter.startListening();
    }

}