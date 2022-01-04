package com.example.healthappdemo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ExtraRecruitersActivity extends AppCompatActivity {


    DrawerLayout mDrawerLayout;
    RecyclerView mRecyclerView;
    ImageView btMenu, btProfile, btSetting;
    MainNavAdapter.onItemClickListener mListener;
    RelativeLayout btn_ClaimScreen, btn_ClaimsHistory, btn_PharmacySection, btn_Clinics;
    Dialog dialog_three;
    TextView mmDialogTitle,
            mmDialogMessage, mmCancelDialog_btn, mmProceedDialog_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_recruiters);


        mDrawerLayout = findViewById(R.id.drawer_layout);
        btMenu = findViewById(R.id.Menu_bar_btn);
        btSetting = findViewById(R.id.Settings_Menu_bar_btn);
        btProfile = findViewById(R.id.Profile_Menu_bar_btn);
        mRecyclerView = findViewById(R.id.recycler_view);
        btn_ClaimScreen = findViewById(R.id.RelativeLayout_Claims);
        btn_ClaimsHistory = findViewById(R.id.RelativeLayout_ClaimsHistory);
        btn_PharmacySection = findViewById(R.id.Relativelayout_Pharmacy);
        btn_Clinics = findViewById(R.id.RelativeLayout_Clinic);



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
                Intent intent = new Intent(ExtraRecruitersActivity.this, UserProfileActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ExtraRecruitersActivity.this, UserSettingsActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btn_ClaimScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent navigate_stepCount_Page = new Intent(ExtraRecruitersActivity.this, ClaimsActivity.class);
                startActivity(navigate_stepCount_Page);
            }
        });

        btn_ClaimsHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent navigate_weightCheck_Page = new Intent(ExtraRecruitersActivity.this, ClaimHistoryActivity.class);
                startActivity(navigate_weightCheck_Page);
            }
        });


        btn_PharmacySection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent navigate_Covid_Screen = new Intent(ExtraRecruitersActivity.this, PharmacyActivity.class);
                startActivity(navigate_Covid_Screen);
            }
        });


        btn_Clinics.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent navigate_Covid_Screen = new Intent(ExtraRecruitersActivity.this, ContactDeveloperActivity.class);
                startActivity(navigate_Covid_Screen);
            }
        });



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
        dialog_three = new Dialog(ExtraRecruitersActivity.this);
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
                Intent navigate_dev_screen = new Intent(ExtraRecruitersActivity.this, ContactDeveloperActivity.class);
                startActivity(navigate_dev_screen);

            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        NewsFeedActivity.closeDrawer(mDrawerLayout);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }



}