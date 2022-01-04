package com.example.healthappdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class DeterministicProgressBarActivity extends AppCompatActivity {


    int counter = 10;
    Dialog dialog;
    ProgressBar mProgressBar;
    int mProgress_Status = 10;
    Handler mHandler = new Handler();
    String USER_ID;
    String ELIGIBILITY_CHECK;
    String ELIGIBILITY_CHECK_TWO;
    String ELIGIBILITY_CHECK_THREE;
    private static final String TAG = "TAG";
    TextView mProgressPercentage, DialogTitle, DialogMessage;
    ImageView CloseBtn_OneTimeDialog;
    Button CancelDialog_btn, ProceedDialog_btn, NoDialog_btn, YesDialog_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deterministic_progress_bar);

        mProgressBar = findViewById(R.id.ProgressBar_Eligible_Check);
        mProgressPercentage = findViewById(R.id.ProgressBar_Percentage);
        mProgressPercentage.setText("%");

        _displayProgressResult();
    }

    private void _displayProgressResult() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (mProgress_Status < 100) {
                    mProgress_Status++;
                    counter++;
                    android.os.SystemClock.sleep(50);
                    mHandler.post(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            mProgressBar.setProgress(mProgress_Status);
                            mProgressPercentage.setText(counter + "%");
                        }
                    });

                }

                if (mProgress_Status == 100) {
                    FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (AppUser != null) {
                        USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        ELIGIBILITY_CHECK = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        ELIGIBILITY_CHECK_TWO = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        ELIGIBILITY_CHECK_THREE = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DocumentReference docRef = FirebaseFirestore.getInstance()
                                .collection("REGISTERED USERS").document(USER_ID)
                                .collection("ELIGIBILITY CHECK ONE").document(ELIGIBILITY_CHECK)
                                .collection("ELIGIBILITY CHECK TWO").document(ELIGIBILITY_CHECK_TWO)
                                .collection("ELIGIBILITY CHECK THREE").document(ELIGIBILITY_CHECK_THREE);

                        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DocumentSnapshot document = task.getResult();
                                    assert document != null;
                                    if (document.exists()) {

                                        String getCovidStatus = document.getString("Covid Status");
                                        String getSmoke = document.getString("Smoke");
                                        String getDrink = document.getString("Drink");
                                        String getExercise = document.getString("Exercise");
                                        String getSurgery = document.getString("Surgery");
                                        String getSalary = String.valueOf(document.getString("Salary"));


                                        if (getSmoke.equals("Never Smoke")
                                                && getDrink.equals("Never drink Alcohol")
                                                && getExercise.equals("Exercise Regularly")
                                                && getSurgery.equals("No Major Surgery")
                                        ) {
                                            _openPositiveDialogMessage();
                                        } else {
                                            _openNegativeDialogMessage();
                                        }

                                    } else {
                                        Log.d(TAG, "No such document");
                                    }
                                }
                            }
                        });

                    }
                }
            }
        }).start();
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    private void _openPositiveDialogMessage() {
        dialog = new Dialog(DeterministicProgressBarActivity.this);
        dialog.setContentView(R.layout.dialog_custom);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        DialogTitle = dialog.findViewById(R.id.Dialog_title);
        DialogMessage = dialog.findViewById(R.id.Dialog_Message);
        CancelDialog_btn = dialog.findViewById(R.id.Btn_Cancel);
        ProceedDialog_btn = dialog.findViewById(R.id.Btn_Proceed);
        DialogTitle.setText("Congratulations!");
        DialogMessage.setText("You have successfully passed the Eligibility Check and can proceed to the next screen. Thank you for your patience");
        dialog.show();

        //cancel the dialog box and return to the login page
        cancelDialogBox();

        //proceed the dialog box and navigate to the quotation section
        proceedDialogBox();

    }

    private void proceedDialogBox() {
        ProceedDialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
                Intent navigate_to_quotation_activity = new Intent(DeterministicProgressBarActivity.this, QuotationActivity.class);
                startActivity(navigate_to_quotation_activity);
            }
        });
    }

    private void cancelDialogBox() {
        CancelDialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _openNegativeCancelMessage();

            }
        });
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    private void _openNegativeCancelMessage() {
        dialog = new Dialog(DeterministicProgressBarActivity.this);
        dialog.setContentView(R.layout.dialog_custom);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        DialogTitle = dialog.findViewById(R.id.Dialog_title);
        DialogMessage = dialog.findViewById(R.id.Dialog_Message);
        NoDialog_btn = dialog.findViewById(R.id.Btn_Cancel);
        YesDialog_btn = dialog.findViewById(R.id.Btn_Proceed);
        DialogTitle.setText("Do You wish to Exit?");
        DialogMessage.setText("Please Understand that your data enter will be lost if you exit, You will have to go through the Eligibility Check again");
        NoDialog_btn.setText("No");
        YesDialog_btn.setText("Yes");
        dialog.show();

        //cancel the dialog box and return to the login page
        NoDialogBox();

        //proceed the dialog box and navigate to the quotation section
        YesDialogBox();
    }

    private void YesDialogBox() {
        YesDialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
                Intent navigate_to_login_activity = new Intent(DeterministicProgressBarActivity.this, LoginActivity.class);
                startActivity(navigate_to_login_activity);
            }
        });

    }

    private void NoDialogBox() {
        NoDialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
                Intent navigate_to_quotation_activity = new Intent(DeterministicProgressBarActivity.this, QuotationActivity.class);
                startActivity(navigate_to_quotation_activity);
            }
        });
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    private void _openNegativeDialogMessage() {
        dialog = new Dialog(DeterministicProgressBarActivity.this);
        dialog.setContentView(R.layout.dialog_custom_one_time);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        DialogMessage = dialog.findViewById(R.id.Dialog_Message_One_Time);
        CloseBtn_OneTimeDialog = dialog.findViewById(R.id.ImageView_close_button);
        DialogMessage.setText("We are Sorry!  You did not pass the eligibility check Test and cannot purchase our Insurance policy. Please contact our office on Tel: 03-456-9876 for further assistance. Thank you for your patience");

        dialog.show();
        //close the dialog box and return to the login page
        CloseBox();
    }

    private void CloseBox() {
        CloseBtn_OneTimeDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
                Intent i = new Intent(DeterministicProgressBarActivity.this, LoginActivity.class);
                startActivity(i);

            }
        });
    }
}