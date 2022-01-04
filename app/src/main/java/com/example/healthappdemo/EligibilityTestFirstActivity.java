package com.example.healthappdemo;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class EligibilityTestFirstActivity extends AppCompatActivity {

    String USER_ID;
    String ELIGIBILITY_CHECK;

    int progress_Status = 3;
    Handler mHandler = new Handler();
    Spinner mSP_newUser_register_gender;
    ProgressBar mProgressBar;
    DatePickerDialog.OnDateSetListener setListener;
    TextView mNew_User_DateOfBirth,  mDOB_error_id, mGender_error_id;
    LinearLayout mNavigate_TestPage_Two, mLinearLayout_display_error_personal_details;
    private static final String TAG = "TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eligibility_test_first);



        mNew_User_DateOfBirth = findViewById(R.id.user_register_DOB);
        mSP_newUser_register_gender = findViewById(R.id.sp_new_user_register_gender);

        mNavigate_TestPage_Two = findViewById(R.id.Btn_to_eligibility_Register_page_two);
        mProgressBar = findViewById(R.id.progressBarID_NEXT);

        //To display all error messages and the container
        mGender_error_id = findViewById(R.id.Gender_error_id);
        mDOB_error_id = findViewById(R.id.DOB_error_id);

        mLinearLayout_display_error_personal_details = findViewById(R.id.LinearLayout_display_error_personal_details);


        // Gender drop-down View
        ArrayAdapter<CharSequence> GenderAdapter = ArrayAdapter.createFromResource(EligibilityTestFirstActivity.this,
                R.array.array_gender, R.layout.spinner_custom_layout);
        GenderAdapter.setDropDownViewResource(R.layout.spinner_custom_drop_down);
        mSP_newUser_register_gender.setAdapter(GenderAdapter);

        //date of birth
        displayDateOfBirth();

        //gender spinner selector
        GenderSelector();


        //perform operations
        mNavigate_TestPage_Two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GenderSelector();

                String newUser_DOB = mNew_User_DateOfBirth.getText().toString();
                String newUser_Gender = mSP_newUser_register_gender.getSelectedItem().toString();

                if (newUser_DOB.length() == 0 ) {
                    mLinearLayout_display_error_personal_details.setVisibility(View.VISIBLE);
                    mDOB_error_id.setVisibility(View.VISIBLE);

                } else {
                    mLinearLayout_display_error_personal_details.setVisibility(View.GONE);
                    mDOB_error_id.setVisibility(View.GONE);
                }


                if(newUser_DOB.length() > 0 ){
                    mLinearLayout_display_error_personal_details.setVisibility(View.GONE);
                    mDOB_error_id.setVisibility(View.GONE);
                    registerTo_db_Eligibility_One( newUser_DOB, newUser_Gender);
                }
            }
        });
    }



    private void registerTo_db_Eligibility_One( String newUser_dob, String newUser_gender)
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progress_Status < 15){
                    progress_Status++;
                    android.os.SystemClock.sleep(200);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.VISIBLE);
                            mProgressBar.setProgress(progress_Status);
                        }
                    });
                }
                if(progress_Status == 15){
                    FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (AppUser != null) {
                        USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        ELIGIBILITY_CHECK = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        Map<String, Object> registerDOBandGender = new HashMap<>();
                        registerDOBandGender.put("Date of Birth", newUser_dob);
                        registerDOBandGender.put("Gender", newUser_gender);


                        DocumentReference documentReference = FirebaseFirestore.getInstance()
                                .collection("REGISTERED USERS").document(USER_ID)
                                .collection("ELIGIBILITY CHECK ONE").document(ELIGIBILITY_CHECK);

                        documentReference.set(registerDOBandGender).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "onSuccess: eligibility entered success " + ELIGIBILITY_CHECK);
                                // mProgressBar.setVisibility(View.VISIBLE);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure: eligibility entered  FAILED" + e.toString());
                            }
                        });


                        finish();
                        Intent navigate_to_eligibility_two = new Intent(EligibilityTestFirstActivity.this, EligibilityTestSecondActivity.class);
                        startActivity(navigate_to_eligibility_two);
                    }else{
                        mProgressBar.setVisibility(View.GONE);
                        Toast.makeText(EligibilityTestFirstActivity.this, "Error, please try again!", Toast.LENGTH_SHORT).show();
                    }


                }
            }
        }).start();
    }


    private void GenderSelector() {
        mSP_newUser_register_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    mLinearLayout_display_error_personal_details.setVisibility(View.VISIBLE);
                    mGender_error_id.setVisibility(View.VISIBLE);
                    mNavigate_TestPage_Two.setEnabled(false);
                } else if (position == 1 || position == 2) {
                    mLinearLayout_display_error_personal_details.setVisibility(View.GONE);
                    mGender_error_id.setVisibility(View.GONE);
                    mNavigate_TestPage_Two.setEnabled(true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void displayDateOfBirth() {
        //display Date of Birth method
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        mNew_User_DateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog DatePickerDialog = new DatePickerDialog(EligibilityTestFirstActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog, setListener, year, month, day);
                DatePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

                DatePickerDialog.show();
            }
        });

        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                String date = day + "-" + month + "-" + year;
                mNew_User_DateOfBirth.setText(date);

            }
        };
    }


}