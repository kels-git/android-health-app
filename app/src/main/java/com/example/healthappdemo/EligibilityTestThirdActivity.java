package com.example.healthappdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EligibilityTestThirdActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    int progress_Status = 3;
    String USER_ID;
    String ELIGIBILITY_CHECK;
    String ELIGIBILITY_CHECK_TWO;
    String ELIGIBILITY_CHECK_THREE;
    Handler mHandler = new Handler();
    Spinner mSmoke, mDrink, mSurgery, mExercise;
    ProgressBar mProgress_Bar;
    TextView Error_Smoke, Error_Drink, Error_Surgery, Error_Exercise, Error_All_Required;
    LinearLayout mLinearLayout_display_error_User_health, mNavigate_to_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eligibility_test_third);

        mProgress_Bar = findViewById(R.id.progressBarID);
        mSmoke = findViewById(R.id.SP_newUser_register_smoke);
        mDrink = findViewById(R.id.SP_newUser_register_drink);
        mSurgery = findViewById(R.id.SP_newUser_register_surgery);
        mExercise = findViewById(R.id.SP_newUser_register_exercise);
        mNavigate_to_check = findViewById(R.id.Btn_to_eligibility_Register_page_check);


        Error_Smoke = findViewById(R.id.Smoke_error_id);
        Error_Drink = findViewById(R.id.Drink_error_id);
        Error_Surgery = findViewById(R.id.Surgery_error_id);
        Error_Exercise = findViewById(R.id.Exercise_error_id);
        Error_All_Required = findViewById(R.id.error_all_field_required_id);
        mLinearLayout_display_error_User_health = findViewById(R.id.LinearLayout_display_error_User_health);


        //display all spinner
        ArrayAdapter<CharSequence> adapterMaritalStatus =
                ArrayAdapter.createFromResource(this, R.array.array_smoke, R.layout.spinner_custom_layout);
        adapterMaritalStatus.setDropDownViewResource(R.layout.spinner_custom_drop_down);
        mSmoke.setAdapter(adapterMaritalStatus);

        ArrayAdapter<CharSequence> adapterOccupation =
                ArrayAdapter.createFromResource(this, R.array.array_drink, R.layout.spinner_custom_layout);
        adapterOccupation.setDropDownViewResource(R.layout.spinner_custom_drop_down);
        mDrink.setAdapter(adapterOccupation);

        ArrayAdapter<CharSequence> adapterCovid =
                ArrayAdapter.createFromResource(this, R.array.array_surgery, R.layout.spinner_custom_layout);
        adapterCovid.setDropDownViewResource(R.layout.spinner_custom_drop_down);
        mSurgery.setAdapter(adapterCovid);

        ArrayAdapter<CharSequence> adapterSalary =
                ArrayAdapter.createFromResource(this, R.array.array_exercise, R.layout.spinner_custom_layout);
        adapterSalary.setDropDownViewResource(R.layout.spinner_custom_drop_down);
        mExercise.setAdapter(adapterSalary);

        mNavigate_to_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUser_Smoke = mSmoke.getSelectedItem().toString();
                String newUser_Drink = mDrink.getSelectedItem().toString();
                String newUser_Exercise = mExercise.getSelectedItem().toString();
                String newUser_Surgery = mSurgery.getSelectedItem().toString();

                // if NO spinner is Selected
                if (mSmoke.getSelectedItemPosition() == 0 && mDrink.getSelectedItemPosition() == 0 && mExercise.getSelectedItemPosition() == 0
                        && mSurgery.getSelectedItemPosition() == 0) {
                    mLinearLayout_display_error_User_health.setVisibility(View.VISIBLE);
                    Error_Smoke.setVisibility(View.VISIBLE);
                    Error_Drink.setVisibility(View.VISIBLE);
                    Error_Exercise.setVisibility(View.VISIBLE);
                    Error_Surgery.setVisibility(View.VISIBLE);
                    Error_All_Required.setVisibility(View.GONE);
                } else if (mSmoke.getSelectedItemPosition() == 0 || mDrink.getSelectedItemPosition() == 0 || mExercise.getSelectedItemPosition() == 0
                        || mSurgery.getSelectedItemPosition() == 0) {
                    mLinearLayout_display_error_User_health.setVisibility(View.VISIBLE);
                    Error_All_Required.setVisibility(View.VISIBLE);
                    Error_Smoke.setVisibility(View.GONE);
                    Error_Drink.setVisibility(View.GONE);
                    Error_Exercise.setVisibility(View.GONE);
                    Error_Surgery.setVisibility(View.GONE);
                } else if (mSmoke.getSelectedItemPosition() > 0 && mDrink.getSelectedItemPosition() > 0 && mExercise.getSelectedItemPosition() > 0
                        && mSurgery.getSelectedItemPosition() > 0) {
                    mLinearLayout_display_error_User_health.setVisibility(View.GONE);
                    Error_All_Required.setVisibility(View.GONE);
                    Error_Smoke.setVisibility(View.GONE);
                    Error_Drink.setVisibility(View.GONE);
                    Error_Exercise.setVisibility(View.GONE);
                    Error_Surgery.setVisibility(View.GONE);


                    register_to_db_eligibility_three(newUser_Smoke, newUser_Drink, newUser_Exercise, newUser_Surgery);

                }
            }
        });

    }

    private void register_to_db_eligibility_three(String newUser_smoke, String newUser_drink, String newUser_exercise, String newUser_surgery) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(progress_Status < 15){
                    progress_Status++;
                    android.os.SystemClock.sleep(200);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgress_Bar.setVisibility(View.VISIBLE);
                            mProgress_Bar.setProgress(progress_Status);
                        }
                    });
                }
                if(progress_Status == 15){
                    FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
                    if(AppUser != null){
                        USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        ELIGIBILITY_CHECK = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        ELIGIBILITY_CHECK_TWO = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        ELIGIBILITY_CHECK_THREE = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        Map<String, Object> registerUser = new HashMap<>();
                        registerUser.put("Smoke", newUser_smoke);
                        registerUser.put("Drink", newUser_drink);
                        registerUser.put("Exercise", newUser_exercise);
                        registerUser.put("Surgery", newUser_surgery);

                        DocumentReference documentReference = FirebaseFirestore.getInstance()
                                .collection("REGISTERED USERS").document(USER_ID)
                                .collection("ELIGIBILITY CHECK ONE").document(ELIGIBILITY_CHECK)
                                .collection("ELIGIBILITY CHECK TWO").document(ELIGIBILITY_CHECK_TWO)
                                .collection("ELIGIBILITY CHECK THREE").document(ELIGIBILITY_CHECK_THREE);
                        documentReference.set(registerUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "onSuccess:  eligibility Stored " + ELIGIBILITY_CHECK_THREE);
                                Toast.makeText(EligibilityTestThirdActivity.this, "All data success inserted!", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mProgress_Bar.setVisibility(View.GONE);
                                Toast.makeText(EligibilityTestThirdActivity.this, "inserted data failed!", Toast.LENGTH_SHORT).show();
                                Log.d(TAG, "onFailure:  eligibility FAILED" + e.toString());
                            }
                        });




                        finish();
                        Intent navigate_to_compute_eligibility = new Intent(EligibilityTestThirdActivity.this, DeterministicProgressBarActivity.class);
                        startActivity(navigate_to_compute_eligibility);

                    }else{
                        Toast.makeText(EligibilityTestThirdActivity.this, "Error, please try again!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }).start();
    }

}