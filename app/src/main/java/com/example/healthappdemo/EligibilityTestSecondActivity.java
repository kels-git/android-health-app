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

public class EligibilityTestSecondActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    int progress_Status = 3;
    ProgressBar mProgressBar;
    String USER_ID;
    String ELIGIBILITY_CHECK;
    String ELIGIBILITY_CHECK_TWO;
    LinearLayout mNavigate_TestPage_three, mLinearLayout_display_error_User_Work;
    Handler mHandler = new Handler();
    Spinner MaritalStatus, Occupation, Covid19, Salary;
    TextView error_MaritalStatus, error_Occupation, error_Salary, error_Covid, error_all_field;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eligibility_test_second);

        Covid19 = findViewById(R.id.SP_newUser_register_covid);
        Salary = findViewById(R.id.SP_newUser_register_salary);
        Occupation = findViewById(R.id.SP_newUser_register_occupation);
        MaritalStatus = findViewById(R.id.SP_newUser_register_marital_status);
        mProgressBar = findViewById(R.id.progressBarID);
        mNavigate_TestPage_three = findViewById(R.id.Btn_to_eligibility_Register_page_three);


        //error messages to display
        error_Covid = findViewById(R.id.Covid19_error_id);
        error_Salary = findViewById(R.id.Salary_error_id);
        error_Occupation = findViewById(R.id.Occupation_error_id);
        error_MaritalStatus = findViewById(R.id.Marital_Status_error_id);
        error_all_field = findViewById(R.id.error_all_field_required_id);
        mLinearLayout_display_error_User_Work = findViewById(R.id.LinearLayout_display_error_User_Work);

        //display all spinners
        ArrayAdapter<CharSequence> adapterMaritalStatus =
                ArrayAdapter.createFromResource(this, R.array.array_marital_status, R.layout.spinner_custom_layout);
        adapterMaritalStatus.setDropDownViewResource(R.layout.spinner_custom_drop_down);
        MaritalStatus.setAdapter(adapterMaritalStatus);

        ArrayAdapter<CharSequence> adapterOccupation =
                ArrayAdapter.createFromResource(this, R.array.array_occupation, R.layout.spinner_custom_layout);
        adapterOccupation.setDropDownViewResource(R.layout.spinner_custom_drop_down);
        Occupation.setAdapter(adapterOccupation);

        ArrayAdapter<CharSequence> adapterCovid =
                ArrayAdapter.createFromResource(this, R.array.array_Covid_19, R.layout.spinner_custom_layout);
        adapterCovid.setDropDownViewResource(R.layout.spinner_custom_drop_down);
        Covid19.setAdapter(adapterCovid);

        ArrayAdapter<CharSequence> adapterSalary =
                ArrayAdapter.createFromResource(this, R.array.array_salary, R.layout.spinner_custom_layout);
        adapterSalary.setDropDownViewResource(R.layout.spinner_custom_drop_down);
        Salary.setAdapter(adapterSalary);

        //Navigate to test page three
        mNavigate_TestPage_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String newUser_Covid = Covid19.getSelectedItem().toString();
                String newUser_MaritalStatus = MaritalStatus.getSelectedItem().toString();
                String newUser_Salary = Salary.getSelectedItem().toString();
                String newUser_Occupation = Occupation.getSelectedItem().toString();

                // if NO spinner is Selected
                if (Covid19.getSelectedItemPosition() == 0 && Salary.getSelectedItemPosition() == 0 && MaritalStatus.getSelectedItemPosition() == 0
                        && Occupation.getSelectedItemPosition() == 0) {
                    mLinearLayout_display_error_User_Work.setVisibility(View.VISIBLE);
                    error_Salary.setVisibility(View.VISIBLE);
                    error_Occupation.setVisibility(View.VISIBLE);
                    error_MaritalStatus.setVisibility(View.VISIBLE);
                    error_Covid.setVisibility(View.VISIBLE);
                    error_all_field.setVisibility(View.GONE);
                } else if (Covid19.getSelectedItemPosition() == 0 || Salary.getSelectedItemPosition() == 0 || MaritalStatus.getSelectedItemPosition() == 0
                        || Occupation.getSelectedItemPosition() == 0) {
                    mLinearLayout_display_error_User_Work.setVisibility(View.VISIBLE);
                    error_all_field.setVisibility(View.VISIBLE);
                    error_Salary.setVisibility(View.GONE);
                    error_Occupation.setVisibility(View.GONE);
                    error_MaritalStatus.setVisibility(View.GONE);
                    error_Covid.setVisibility(View.GONE);
                } else if (Covid19.getSelectedItemPosition() > 0 && Salary.getSelectedItemPosition() > 0 && MaritalStatus.getSelectedItemPosition() > 0
                        && Occupation.getSelectedItemPosition() > 0) {
                    mLinearLayout_display_error_User_Work.setVisibility(View.GONE);
                    error_Salary.setVisibility(View.GONE);
                    error_Occupation.setVisibility(View.GONE);
                    error_MaritalStatus.setVisibility(View.GONE);
                    error_Covid.setVisibility(View.GONE);
                    error_all_field.setVisibility(View.GONE);


                    register_to_db_eligibility_two(newUser_Covid, newUser_MaritalStatus, newUser_Occupation, newUser_Salary);

                }

            }
        });
    }

    private void register_to_db_eligibility_two(String newUser_covid, String newUser_maritalStatus, String newUser_occupation, String newUser_salary) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while(progress_Status < 15){
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
                    if(AppUser != null){
                        USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        ELIGIBILITY_CHECK = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        ELIGIBILITY_CHECK_TWO = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        Map<String, Object> registerUser = new HashMap<>();
                        registerUser.put("Covid Status", newUser_covid);
                        registerUser.put("Marital Status", newUser_maritalStatus);
                        registerUser.put("Occupation", newUser_occupation);
                        registerUser.put("Salary", newUser_salary);

                        DocumentReference documentReference = FirebaseFirestore.getInstance()
                                .collection("REGISTERED USERS").document(USER_ID)
                                .collection("ELIGIBILITY CHECK ONE").document(ELIGIBILITY_CHECK)
                                .collection("ELIGIBILITY CHECK TWO").document(ELIGIBILITY_CHECK_TWO);
                        documentReference.set(registerUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "onSuccess:  eligibility updated " + ELIGIBILITY_CHECK_TWO);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "onFailure:  eligibility two FAILED" + e.toString());
                            }
                        });



                        Intent navigate_to_eligibility_three = new Intent(EligibilityTestSecondActivity.this, EligibilityTestThirdActivity.class);
                        startActivity(navigate_to_eligibility_three);

                    }
                }else{
                    mProgressBar.setVisibility(View.GONE);
                    Toast.makeText(EligibilityTestSecondActivity.this, "Error, please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        }).start();

    }
}