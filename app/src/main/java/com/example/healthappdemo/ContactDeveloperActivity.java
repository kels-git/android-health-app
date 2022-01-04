package com.example.healthappdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.eazegraph.lib.charts.BarChart;
import org.eazegraph.lib.models.BarModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactDeveloperActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    RecyclerView mRecyclerView;
    ImageView btMenu, btProfile, btSetting;

    MainNavAdapter.onItemClickListener mListener;

    CircleImageView mDeveloperPhoto;
    TextView mDevOccupation, mDevName, mDevEmail, mDevGithub,
            mDevAddress, mLinkedln, mDevPhone, mAboutDev, mDevResume;

    BarChart mBarCharDev, mBarChartDevTwo;

    TextView mUniDate, mUniFullName, mUniProgram, mMajor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_developer);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        btMenu = findViewById(R.id.Menu_bar_btn);
        btSetting = findViewById(R.id.Settings_Menu_bar_btn);
        btProfile = findViewById(R.id.Profile_Menu_bar_btn);
        mRecyclerView = findViewById(R.id.recycler_view);

        mDeveloperPhoto = findViewById(R.id.developer_photo);
        mDevOccupation = findViewById(R.id.TV_dev_occupation);
        mDevName = findViewById(R.id.TV_dev_name);
        mDevGithub = findViewById(R.id.TV_dev_github);
        mDevEmail = findViewById(R.id.TV_dev_email);
        mDevAddress = findViewById(R.id.TV_dev_Address);
        mLinkedln = findViewById(R.id.TV_dev_linkedln);
        mDevPhone = findViewById(R.id.TV_Dev_PhoneNumber);
        mAboutDev = findViewById(R.id.TV_about_dev);
        mBarCharDev = findViewById(R.id.barchart);
        mBarChartDevTwo = findViewById(R.id.barchart_two);
        mDevResume = findViewById(R.id.TV_dev_Resume);

        mUniDate = findViewById(R.id.TV_Uni_Date_1);
        mUniFullName = findViewById(R.id.TV_Uni_fullName_1);
        mUniProgram = findViewById(R.id.TV_Uni_Program_1);
        mMajor = findViewById(R.id.TV_Uni_Major_1);


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

        btProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactDeveloperActivity.this, UserProfileActivity.class);
                startActivity(intent); // start same activity
                finish(); // destroy older activity
            }
        });

        btSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ContactDeveloperActivity.this, UserSettingsActivity.class);
                startActivity(intent); // start same activity
            }
        });

      developerDetails();
    }


    @SuppressLint("SetTextI18n")
    private void developerDetails() {

        Picasso.get()
                .load(R.drawable.developerpic)
                .into(mDeveloperPhoto);
        mDevName.setText("Osazuwa Ogiemwanye");
        mDevOccupation.setText("Web & App Developer");
        mDevEmail.setText("osazuwaogiemwanye@gmail.com");
        mDevAddress.setText("Puchong South, 43400 Puchong, Selangor");
       mDevPhone.setText("+601133939812");

        mLinkedln.setText("View Linkedln");

        mDevGithub.setText("View GitHub");


        //TODO: go to github
     gotoGithub();

        //TODO: go to github
       gotoLinkedln();

       mAboutDev.setText(
               "A passionate coder, fantastic troubleshooter, enthusiastic Software Developer, fast learner and having the" +
               " ability to make progressive skills in implementing, developing, and testing software to meet specific project requirements. " +
               "key strength include being able to make an " +
               "immediate impact on a project and having an eagerness to evolve existing systems and technology when developing software.");

       //TODO: for bar char
        getFirstExpertiseArea();


     //TODO: for bar chat 2
        getSecondExpertiseArea();

        //TODO: download resume
        downloadResume();


        //TODO: University
        DisplayUni();
    }

    @SuppressLint("SetTextI18n")
    private void DisplayUni() {
        mUniDate.setText("(UNITAR) 2018" + "|" + "2021");
        mUniFullName.setText("UNITAR INTERNATIONAL UNIVERSITY");
        mUniProgram.setText("BACHELOR OF INFORMATION TECHNOLOGY");
        mMajor.setText("Software Engineering");


    }

    private void downloadResume() {
        mDevResume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoUrl("https://2ab5a0ff-b9ed-4f2e-90b5-81ab0c83a69d.filesusr.com/ugd/09db13_da4b981757314a54b70a1ab9a0e0b8c1.pdf");
            }
        });
    }

    private void getSecondExpertiseArea() {

        mBarChartDevTwo.addBar(new BarModel("RT", 60,  Color.parseColor("#FF7733")));
        mBarChartDevTwo.addBar(new BarModel("ASDK", 100,  Color.parseColor("#15D300")));
        mBarChartDevTwo.addBar(new BarModel("VSC", 100, Color.parseColor("#EBEDF0")));
        mBarChartDevTwo.addBar(new BarModel("FB", 90, Color.parseColor("#FFE600")));
        mBarChartDevTwo.addBar(new BarModel("PGS",60, Color.parseColor("#4033FF")));


        mBarChartDevTwo.startAnimation();
    }

    private void getFirstExpertiseArea() {

        mBarCharDev.addBar(new BarModel("HTML", 100,  Color.parseColor("#FFBB86FC")));
        mBarCharDev.addBar(new BarModel("CSS", 100,  Color.parseColor("#F0FF33")));
        mBarCharDev.addBar(new BarModel("JAVA", 80, Color.parseColor("#00AEEF")));
        mBarCharDev.addBar(new BarModel("JS", 70, Color.parseColor("#15D300")));
        mBarCharDev.addBar(new BarModel("RN",60, Color.parseColor("#FF03DAC5")));
        //mBarCharDev.addBar(new BarModel("ASDK",100, 0xFF563458));

        mBarCharDev.startAnimation();
    }

    @SuppressLint("SetTextI18n")
    private void gotoLinkedln() {
        mLinkedln.setOnClickListener(view -> gotoUrl("https://www.linkedin.com/in/osazuwa-ogiemwanye-9983121b4"));
    }

    private void gotoGithub(){
        mDevGithub.setOnClickListener(view -> gotoUrl("https://github.com/kelvin888-cloud"));
    }

    private void gotoUrl(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
    }

    @Override
    protected void onPause() {
        super.onPause();
        NewsFeedActivity.closeDrawer(mDrawerLayout);
    }
}