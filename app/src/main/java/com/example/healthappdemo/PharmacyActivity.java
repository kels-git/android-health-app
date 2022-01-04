package com.example.healthappdemo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class PharmacyActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    RecyclerView mRecyclerView, mRecyclerViewPharmacy;
    ImageView btMenu, btProfile, btSetting;
    FloatingActionButton mCartDisplay;
    PharmacyAdapter mPharmacyAdapter;
    PharmacyAdapter.onItemClickListener mListenerPharmacy;
    Uri mImageUri;
    Dialog dialog_three;
    TextView mmDialogTitle,
            mmDialogMessage, mmCancelDialog_btn, mmProceedDialog_btn;

    static  ArrayList<PharmacyItem> MedicalPharmacyItem = new ArrayList<>();

    MainNavAdapter.onItemClickListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        btMenu = findViewById(R.id.Menu_bar_btn);
        btSetting = findViewById(R.id.Settings_Menu_bar_btn);
        btProfile = findViewById(R.id.Profile_Menu_bar_btn);
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerViewPharmacy = findViewById(R.id.recycler_view_pharmacy);
        mCartDisplay = findViewById(R.id.cart_pharmacy_screen);

        mPharmacyAdapter = new PharmacyAdapter(MedicalPharmacyItem, mListenerPharmacy, PharmacyActivity.this);

        //set layout manager

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        mRecyclerViewPharmacy.setLayoutManager(gridLayoutManager);
        mRecyclerViewPharmacy.setAdapter(mPharmacyAdapter);


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
                Intent intent = new Intent(PharmacyActivity.this, UserProfileActivity.class);
                startActivity(intent); // start same activity
                finish(); // destroy older activity


            }
        });

        btSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PharmacyActivity.this, UserSettingsActivity.class);
                startActivity(intent); // start same activity


            }
        });

        mCartDisplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent navigate_to_cart = new Intent(PharmacyActivity.this, CartActivity.class);
                startActivity(navigate_to_cart);
            }
        });



        //clear array list
        MedicalPharmacyItem.clear();

        MedicalPharmacyItem.add(new PharmacyItem(R.drawable.norvasc5mgtablet2,"Norvasc 5mg Tablet","27",
                " Used to treat high blood pressure (hypertension)", "1" ));

        MedicalPharmacyItem.add(new PharmacyItem(R.drawable.lipitor40mg,"Lipitor 10mg Tablet","108",
                "Used to lower blood levels of “bad” cholesterol", "1" ));

        MedicalPharmacyItem.add(new PharmacyItem(R.drawable.acustop,"Acustop 40mg Plaster","98",
                "used temporarily to relieve pain during acute gout attacks.", "1"));

        MedicalPharmacyItem.add(new PharmacyItem(R.drawable.acentin5g,"Acetin Sachet 5g","2",
                "works by thinning the mucus (phlegm)", "1" ));

        MedicalPharmacyItem.add(new PharmacyItem(R.drawable.artelacsplasheyedrop,"Artelac EyeDrop 0.5ml","88",
                "relieves, soothes and revitalizes dry eye symptoms", "1" ));

        MedicalPharmacyItem.add(new PharmacyItem(R.drawable.panadolmedicine,"Panadol 500mg","20",
                "relieves headache, cold, period pain, muscle pain","1" ));


        _ClickOnProduct();


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

        dialog_three = new Dialog(PharmacyActivity.this);
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
                Intent navigate_dev_screen = new Intent(PharmacyActivity.this, ContactDeveloperActivity.class);
                startActivity(navigate_dev_screen);

            }
        });
    }

    private void _ClickOnProduct() {
        mPharmacyAdapter.setOnItemClickListener(new PharmacyAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent navigateProductScreen = new Intent(PharmacyActivity.this, PharmacyProductActivity.class);
                navigateProductScreen.putExtra("PharmacyItemProduct", MedicalPharmacyItem.get(position));
                startActivity(navigateProductScreen);
            }
        });

    }


    @Override
    protected void onPause() {
        super.onPause();
        NewsFeedActivity.closeDrawer(mDrawerLayout);
    }

}