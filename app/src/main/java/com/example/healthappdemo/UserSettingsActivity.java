package com.example.healthappdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.paperdb.Paper;

public class UserSettingsActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    RecyclerView mRecyclerView;
    ImageView btMenu, btProfile, btSetting;

    String USER_ID;
    TextView Logout, email_dev, mProfileEdit;

    MainNavAdapter.onItemClickListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_settings);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        btMenu = findViewById(R.id.Menu_bar_btn);
        btSetting = findViewById(R.id.Settings_Menu_bar_btn);
        btProfile = findViewById(R.id.Profile_Menu_bar_btn);
        mRecyclerView = findViewById(R.id.recycler_view);
        Logout = findViewById(R.id.BT_Logout);
        email_dev = findViewById(R.id.BT_Contact_Developer_email);
        mProfileEdit = findViewById(R.id.Profile_Edit);

        //set layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        //set the adapter
        mRecyclerView.setAdapter(new MainNavAdapter(NewsFeedActivity.drawerArrayList, mListener, this  ));


        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open drawer
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        btSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSettingsActivity.this, UserSettingsActivity.class);
                startActivity(intent); // start same activity
                finish(); // destroy older activity
                overridePendingTransition(0, 0);

            }
        });

        btProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UserSettingsActivity.this, UserProfileActivity.class);
                startActivity(intent); // start same activity
                finish(); // destroy older activity


            }
        });


        mProfileEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent navigate_Edit_Profile = new Intent(UserSettingsActivity.this, UserProfileEditActivity.class);
                startActivity(navigate_Edit_Profile);
            }
        });

        Paper.init(this);
        signOut();

        developerEmail();
    }

    @SuppressLint("SetTextI18n")
    private void developerEmail() {
        email_dev.setText("osazuwaogiemwanye@gmail.com");
    }

    private void signOut() {
        FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
        if (AppUser != null) {
            USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Logout.setOnClickListener(view -> {

                Paper.book().destroy();
                Intent LogoutActivity = new Intent(UserSettingsActivity.this, MainActivity.class);
                startActivity(LogoutActivity);
                finish();
                onDestroy();
                //FirebaseAuth.getInstance().signOut();


            });

        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        NewsFeedActivity.closeDrawer(mDrawerLayout);
    }


}
