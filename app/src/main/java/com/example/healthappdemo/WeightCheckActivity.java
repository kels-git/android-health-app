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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class WeightCheckActivity extends AppCompatActivity {

    EditText mWeight, mHeight;
    TextView mTextTopBmi, mBMI_TextResult;
    LinearLayout mBtnCompute, mDisplayWeightResult, mBtnResetResult;

    DrawerLayout mDrawerLayout;
    ImageView btMenu, btProfile, btSetting;
    MainNavAdapter.onItemClickListener mListener;
    RecyclerView mRecyclerView;


    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight_check);

        mWeight = findViewById(R.id.et_weight);
        mHeight = findViewById(R.id.et_height);
        mTextTopBmi = findViewById(R.id.TV_bmi_textTop);
        mBMI_TextResult = findViewById(R.id.TV_BMI_result);
        mBtnCompute = findViewById(R.id.Btn_to_calculate_BMI);
        mBtnResetResult = findViewById(R.id.Btn_BMI_Reset);
        mDisplayWeightResult = findViewById(R.id.Linear_Display_weightResult);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        btMenu = findViewById(R.id.Menu_bar_btn);
        btSetting = findViewById(R.id.Settings_Menu_bar_btn);
        btProfile = findViewById(R.id.Profile_Menu_bar_btn);
        mRecyclerView = findViewById(R.id.recycler_view);


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
                Intent intent = new Intent(WeightCheckActivity.this, UserProfileActivity.class);
                startActivity(intent); // start same activity
                finish(); // destroy older activity


            }
        });

        //set onclick to navigate setting activity
        btSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WeightCheckActivity.this, UserSettingsActivity.class);
                startActivity(intent); // start same activity
                finish(); // destroy older activity


            }
        });

        mBtnCompute.setOnClickListener(view -> {
            String weightCheck = mWeight.getText().toString();
            String heightCheck = mHeight.getText().toString();

            if (weightCheck.isEmpty()) {
                Toast.makeText(WeightCheckActivity.this, "Please Enter Weight", Toast.LENGTH_SHORT).show();
                return;
            }
            if (heightCheck.isEmpty()) {
                Toast.makeText(WeightCheckActivity.this, "Please Enter Height", Toast.LENGTH_SHORT).show();
            }
             else {

                float weight = Float.parseFloat(weightCheck);
                float height = Float.parseFloat(heightCheck)/100;

                float bmiValue = bmi_calculate(weight, height);


                if(interpretBMI(bmiValue).matches("Extremely Under-Weight")
                || interpretBMI(bmiValue).matches("UnderWeight")
                || interpretBMI(bmiValue).matches("Over weight")
                || interpretBMI(bmiValue).matches("Weight Obesity")){

                    mDisplayWeightResult.setVisibility(View.VISIBLE);
                    mBMI_TextResult.setBackground(getDrawable(R.drawable.grad_error_message));
                    mBMI_TextResult.setPadding(32,32, 32, 32);
                    mBMI_TextResult.setText(interpretBMI(bmiValue));
                    mBtnResetResult.setVisibility(View.VISIBLE);
                    mBtnCompute.setVisibility(View.GONE);


                }else{
                    mDisplayWeightResult.setVisibility(View.VISIBLE);
                    mBMI_TextResult.setBackground(getDrawable(R.drawable.grad_success_message));
                    mBMI_TextResult.setPadding(32,32, 32, 32);
                    mBMI_TextResult.setText(interpretBMI(bmiValue));
                    mBtnResetResult.setVisibility(View.VISIBLE);
                    mBtnCompute.setVisibility(View.GONE);
                }
                mTextTopBmi.setText("Your BMI Result: " + " " + bmiValue);

            }


        });

        mBtnResetResult.setOnClickListener(view -> {
            mDisplayWeightResult.setVisibility(View.GONE);
            mBtnResetResult.setVisibility(View.GONE);
            mBtnCompute.setVisibility(View.VISIBLE);
            mWeight.setText("");
            mHeight.setText("");
        });

    }

    public float bmi_calculate(float weight, float height) {
        return weight / (height * height);
    }

    public String interpretBMI(float mbiValue) {
        if (mbiValue < 16) {
            return "Extremely Under-Weight";
        } else if (mbiValue < 18.5) {
            return "UnderWeight";
        } else if (mbiValue < 25) {
            return "Normal weight";
        } else if (mbiValue < 30) {
            return "Over weight";
        } else {
            return "Weight Obesity";
        }
    }


}