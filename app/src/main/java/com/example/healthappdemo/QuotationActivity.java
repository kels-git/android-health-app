package com.example.healthappdemo;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Random;

public class QuotationActivity extends AppCompatActivity {

    private static final String TAG = "TAG";
    int progress_Status = 3;
    Dialog dialog, dialogSecond;
    String USER_ID;
    int bound = 10000000;
    String USER_POLICY_CHOICE;
    Handler mHandler = new Handler();
    ProgressBar mProgress_Bar;
    Spinner mSP_CategoryParent, mSP_LifeCoverageChild, mSP_Critical_illnessChild, mSP_paymentMethodChild, mSP_MedicalHospitalization;
    TextView mConfirmPolicyDeductible, DialogTitle, DialogMessage, _mDialog_Message_Policy, _mPolicy_No_Indicator;
    LinearLayout mBtn_to_Quotation, mLinear_Display_price_payment_plan, mLayout_dialogTitle_holder, _mPolicyContainer_linearLayout;
    Button CancelDialog_btn, ProceedDialog_btn, NoDialog_btn, YesDialog_btn, CancelDialogLoad_btn, ProceedDialogLoad_btn;
    Random myPolicyRandomNum = new Random();
    LottieAnimationView lottieLoaing;
    CollectionReference collectionReference;

    ArrayList<String> Policy_Category_Parent;
    ArrayAdapter<String> CategoryAdapter;

    ArrayList<String> platinum_category_Child_lifeCoverage, gold_category_child_lifeCoverage,
            silver_category_child_lifeCoverage, platinum_illness_child, gold_illness_child, silver_illness_child,
            platinum_category_Child_lifeCoverage_0, platinum_illness_child_0, paymentMethod_Platinum_child_0, paymentMethod_yearly_monthly;

    ArrayAdapter<String> Adapter_LifeCoverage, Adapter_illness, Adapter_payment;

    int Monthly = 1, Yearly = 12, DeductiblePremium = 1200, illnessAPremium = 500, DeductibleGold = 1000,
            illnessAddGold = 400, DeductibleSilver = 800, illnessAddSilver = 200;
    int finalResult;

    Policy_DataModel userPolicyChoice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quotation);

        mSP_CategoryParent = findViewById(R.id.Spinner_Choose_Category);
        mSP_LifeCoverageChild = findViewById(R.id.Spinner_LifeCoverage);
        mSP_Critical_illnessChild = findViewById(R.id.Spinner_Quotation_Critical_illness);
        mSP_paymentMethodChild = findViewById(R.id.Spinner_Choose_Payment_Method);
        mBtn_to_Quotation = findViewById(R.id.Btn_to_Quotation_Register_page_);
        mConfirmPolicyDeductible = findViewById(R.id.TV_Quotation_deductible2);
        mLinear_Display_price_payment_plan = findViewById(R.id.Linear_Display_price_payment_plan);
        mProgress_Bar = findViewById(R.id.progressBarID);

        Policy_Category_Parent = new ArrayList<>();
        Policy_Category_Parent.add("Select Category");
        Policy_Category_Parent.add("Platinum");
        Policy_Category_Parent.add("Gold");
        Policy_Category_Parent.add("Silver");

        CategoryAdapter = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_custom_layout, Policy_Category_Parent);
        CategoryAdapter.setDropDownViewResource(R.layout.spinner_custom_layout);
        mSP_CategoryParent.setAdapter(CategoryAdapter);

        // time to prepare the child in the nested spinner Platinum//
        platinum_category_Child_lifeCoverage = new ArrayList<>();
        platinum_category_Child_lifeCoverage.add("RM500,000.00");

        platinum_illness_child = new ArrayList<>();
        platinum_illness_child.add("RM400,000.00");

        paymentMethod_yearly_monthly = new ArrayList<>();
        paymentMethod_yearly_monthly.add("Monthly Plan");
        paymentMethod_yearly_monthly.add("Yearly Plan");

        // time to prepare the child in the nested spinner Gold//
        gold_category_child_lifeCoverage = new ArrayList<>();
        gold_category_child_lifeCoverage.add("RM400,000.00");

        gold_illness_child = new ArrayList<>();
        gold_illness_child.add("RM300,000.00");

        // time to prepare the child in the nested spinner Silver//
        silver_category_child_lifeCoverage = new ArrayList<>();
        silver_category_child_lifeCoverage.add("RM200,000.00");

        silver_illness_child = new ArrayList<>();
        silver_illness_child.add("RM100,000.00");

        _displayAllNestedSpinners();
        _displayYearOrMonthly_Deductible();

        mBtn_to_Quotation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _openPositiveDialogMessage();
            }
        });
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    private void _openPositiveDialogMessage() {
        dialog = new Dialog(QuotationActivity.this);
        String mLifeCoverage = mSP_LifeCoverageChild.getSelectedItem().toString();
        String mCriticalIllness = mSP_Critical_illnessChild.getSelectedItem().toString();
        String mPaymentMethod = mSP_paymentMethodChild.getSelectedItem().toString();
        String mPaymentAmount = mConfirmPolicyDeductible.getText().toString().trim();
        String mCategory = mSP_CategoryParent.getSelectedItem().toString();
        dialog.setContentView(R.layout.dialog_custom);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        _mPolicyContainer_linearLayout = dialog.findViewById(R.id.LinearLayout_policy_container);
        _mPolicy_No_Indicator = dialog.findViewById(R.id.Dialog_Message_Policy_id);
        _mDialog_Message_Policy = dialog.findViewById(R.id.Dialog_Message_Policy);

        _mPolicyContainer_linearLayout.setVisibility(View.VISIBLE);
        _mPolicyContainer_linearLayout.setBackground(getDrawable(R.drawable.grad_error_message));


        DialogTitle = dialog.findViewById(R.id.Dialog_title);
        DialogMessage = dialog.findViewById(R.id.Dialog_Message);
        CancelDialog_btn = dialog.findViewById(R.id.Btn_Cancel);
        ProceedDialog_btn = dialog.findViewById(R.id.Btn_Proceed);
        ProceedDialog_btn.setText("Confirm");
        DialogTitle.setText("Please Verify Your Choice on " + mCategory.toUpperCase());
        DialogMessage.setText(
                "Life Coverage: " + mLifeCoverage + "\n" +
                        "illness Coverage: " + mCriticalIllness + "\n" +
                        "Payment Method: " + mPaymentMethod + "\n" +
                        "Pay Amount: " + mPaymentAmount + "\n");
        dialog.show();

        //cancel the dialog box and return to the Home page
        cancelDialogBox();

        //confirm the dialog box and navigate to the payment section
        confirmDialogBox();

        _generate_payment_policy_num();
    }

    @SuppressLint("SetTextI18n")
    private void _generate_payment_policy_num() {
        myPolicyRandomNum = new Random();
        bound = 10000000;
        _mDialog_Message_Policy.setText(String.valueOf(myPolicyRandomNum.nextInt(bound)));
    }

    private void confirmDialogBox() {
        ProceedDialog_btn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialogSecond = new Dialog(QuotationActivity.this);
                dialogSecond.setContentView(R.layout.dialog_custom);
                dialogSecond.setCancelable(true);
                dialogSecond.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
                dialogSecond.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                dialogSecond.setCancelable(false);
                CancelDialogLoad_btn = dialogSecond.findViewById(R.id.Btn_Cancel);
                ProceedDialogLoad_btn = dialogSecond.findViewById(R.id.Btn_Proceed);
                DialogMessage = dialogSecond.findViewById(R.id.Dialog_Message);
                mLayout_dialogTitle_holder = dialogSecond.findViewById(R.id.Layout_dialogTitle_holder);

                DialogMessage.setVisibility(View.GONE);
                CancelDialogLoad_btn.setVisibility(View.GONE);

                mLayout_dialogTitle_holder.setBackground(getDrawable(R.drawable.grad_button_white));
                lottieLoaing = dialogSecond.findViewById(R.id.Spinner_loading_lottie);
                lottieLoaing.setVisibility(View.VISIBLE);
                ProceedDialogLoad_btn.setText("Please hold ...");
                dialogSecond.show();

                LottieTransformation();
            }
        });
    }

    private void LottieTransformation() {

        lottieLoaing.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                lottieLoaing.setRepeatCount(2);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

                dialogSecond.dismiss();
                _navigate_to_payment_demo_page();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void _navigate_to_payment_demo_page() {
        String policyNum = _mDialog_Message_Policy.getText().toString();
        String mLifeCoverage = mSP_LifeCoverageChild.getSelectedItem().toString();
        String mCriticalIllness = mSP_Critical_illnessChild.getSelectedItem().toString();
        String mPaymentMethod = mSP_paymentMethodChild.getSelectedItem().toString();
        String mPaymentAmount = mConfirmPolicyDeductible.getText().toString().trim();
        String mCategory = mSP_CategoryParent.getSelectedItem().toString();

        _saveToDatabaseQuotation(mLifeCoverage, mCriticalIllness, mPaymentMethod, mPaymentAmount, mCategory, policyNum);

        //remember to start from this line which is very important

        Intent navigate_payment_demo = new Intent(QuotationActivity.this, PaymentDemoActivity.class);
        navigate_payment_demo.putExtra("userPolicyChoice", userPolicyChoice);
        startActivity(navigate_payment_demo);
        finish();

    }

    private void _saveToDatabaseQuotation(String mLifeCoverage, String mCriticalIllness, String mPaymentMethod, String mPaymentAmount, String mCategory, String policyNum) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progress_Status < 15) {
                    progress_Status++;
                    SystemClock.sleep(200);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgress_Bar.setVisibility(View.VISIBLE);
                            mProgress_Bar.setProgress(progress_Status);
                        }
                    });
                }

                if (progress_Status == 15) {
                    _displayAllNestedSpinners();
                    FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (AppUser != null) {
                        USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        USER_POLICY_CHOICE = FirebaseAuth.getInstance().getCurrentUser().getUid();

                        collectionReference = FirebaseFirestore.getInstance()
                                .collection("REGISTERED USERS").document(USER_ID)
                                .collection("POLICY CHOICE");

                        userPolicyChoice = new Policy_DataModel(mLifeCoverage, mCriticalIllness, mPaymentMethod,
                                mPaymentAmount, mCategory, policyNum);

                        collectionReference.add(userPolicyChoice).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {
                                    mProgress_Bar.setVisibility(View.GONE);
                                    Log.d(TAG, "onSuccess: Policy Choice Successful " + USER_POLICY_CHOICE);

                                }
                            }


                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

                    }

                }

            }
        }).start();
    }

    private void cancelDialogBox() {
        CancelDialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _dialogBoxCancelMsg();
            }
        });
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    private void _dialogBoxCancelMsg() {

        dialogSecond = new Dialog(QuotationActivity.this);
        String mCategory = mSP_CategoryParent.getSelectedItem().toString();
        dialogSecond.setContentView(R.layout.dialog_custom);
        dialogSecond.setCancelable(false);
        dialogSecond.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        dialogSecond.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        DialogTitle = dialogSecond.findViewById(R.id.Dialog_title);
        DialogMessage = dialogSecond.findViewById(R.id.Dialog_Message);

        NoDialog_btn = dialogSecond.findViewById(R.id.Btn_Cancel);
        YesDialog_btn = dialogSecond.findViewById(R.id.Btn_Proceed);
        YesDialog_btn.setText("Yes");
        NoDialog_btn.setText("No");
        DialogTitle.setText("Your Selected Choice is " + mCategory.toUpperCase());
        DialogMessage.setText("Do you wish to Cancel? Please NOTE that all your info will be lost and you will have to go through the entire check cycle again! ");
        dialogSecond.show();

        //No proceed to the payment-demo page
        No_Btn_DialogBox();

        //yes -cancel the check quotation and return to main page
        Yes_Btn_DialogBox();
    }

    private void Yes_Btn_DialogBox() {
        YesDialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent navigate_to_main_activity = new Intent(QuotationActivity.this, MainActivity.class);
                startActivity(navigate_to_main_activity);
                finish();

            }
        });
    }

    private void No_Btn_DialogBox() {
        NoDialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _openPositiveDialogMessage();
            }
        });
    }

    private void _displayYearOrMonthly_Deductible() {
        mSP_paymentMethodChild.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String mLifeCoverage = mSP_LifeCoverageChild.getSelectedItem().toString();
                String mCriticalIllness = mSP_Critical_illnessChild.getSelectedItem().toString();
                String mPaymentMethod = mSP_paymentMethodChild.getSelectedItem().toString();

                if (mLifeCoverage.contains("RM500,000.00") && mCriticalIllness.contains("RM400,000.00") && mPaymentMethod.contains("Monthly Plan")) {
                    int finalMonth = Monthly;
                    int finalDeduct = DeductiblePremium;
                    int final_illnessDeduct = illnessAPremium;
                    finalResult = finalDeduct + final_illnessDeduct * finalMonth;
                    String lastResult = String.valueOf(finalResult);
                    mLinear_Display_price_payment_plan.setVisibility(View.VISIBLE);
                    mConfirmPolicyDeductible.setText("RM " + lastResult + ".00");

                } else if (mLifeCoverage.contains("RM500,000.00") && mCriticalIllness.contains("RM400,000.00") && mPaymentMethod.contains("Yearly Plan")) {
                    int finalYearly = Yearly;
                    int finalDeduct = DeductiblePremium;
                    int final_illnessDeduct = illnessAPremium;
                    finalResult = finalDeduct * finalYearly;
                    finalResult = (finalDeduct + final_illnessDeduct) * finalYearly;
                    String lastResult = String.valueOf(finalResult);
                    mLinear_Display_price_payment_plan.setVisibility(View.VISIBLE);
                    mConfirmPolicyDeductible.setText("RM " + lastResult + ".00");

                } else if (mLifeCoverage.contains("RM400,000.00") && mCriticalIllness.contains("RM300,000.00") && mPaymentMethod.contains("Monthly Plan")) {
                    int finalMonth = Monthly;
                    int finalDeduct = DeductibleGold;
                    int final_illnessDeduct = illnessAddGold;
                    finalResult = finalDeduct + final_illnessDeduct * finalMonth;
                    String lastResult = String.valueOf(finalResult);
                    mLinear_Display_price_payment_plan.setVisibility(View.VISIBLE);
                    mConfirmPolicyDeductible.setText("RM " + lastResult + ".00");
                } else if (mLifeCoverage.contains("RM400,000.00") && mCriticalIllness.contains("RM300,000.00") && mPaymentMethod.contains("Yearly Plan")) {
                    int finalYearly = Yearly;
                    int finalDeduct = DeductibleGold;
                    int final_illnessDeduct = illnessAddGold;
                    finalResult = finalDeduct * finalYearly;
                    finalResult = (finalDeduct + final_illnessDeduct) * finalYearly;
                    String lastResult = String.valueOf(finalResult);
                    mLinear_Display_price_payment_plan.setVisibility(View.VISIBLE);
                    mConfirmPolicyDeductible.setText("RM " + lastResult + ".00");
                } else if (mLifeCoverage.contains("RM200,000.00") && mCriticalIllness.contains("RM100,000.00") && mPaymentMethod.contains("Monthly Plan")) {
                    int finalMonth = Monthly;
                    int finalDeduct = DeductibleSilver;
                    int final_illnessDeduct = illnessAddSilver;
                    finalResult = finalDeduct + final_illnessDeduct * finalMonth;
                    String lastResult = String.valueOf(finalResult);
                    mLinear_Display_price_payment_plan.setVisibility(View.VISIBLE);
                    mConfirmPolicyDeductible.setText("RM " + lastResult + ".00");
                } else if (mLifeCoverage.contains("RM200,000.00") && mCriticalIllness.contains("RM100,000.00") && mPaymentMethod.contains("Yearly Plan")) {
                    int finalYearly = Yearly;
                    int finalDeduct = DeductibleSilver;
                    int final_illnessDeduct = illnessAddSilver;
                    finalResult = (finalDeduct + final_illnessDeduct) * finalYearly;
                    String lastResult = String.valueOf(finalResult);
                    mLinear_Display_price_payment_plan.setVisibility(View.VISIBLE);
                    mConfirmPolicyDeductible.setText("RM " + lastResult + ".00");
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void _displayAllNestedSpinners() {

        mSP_CategoryParent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    mLinear_Display_price_payment_plan.setVisibility(View.GONE);
                    mSP_LifeCoverageChild.setEnabled(false);
                    mSP_Critical_illnessChild.setEnabled(false);
                    mSP_paymentMethodChild.setEnabled(false);
                    mBtn_to_Quotation.setEnabled(false);

                    platinum_category_Child_lifeCoverage_0 = new ArrayList<>();
                    platinum_category_Child_lifeCoverage_0.add("Please Click Category");
                    Adapter_LifeCoverage = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_custom_layout, platinum_category_Child_lifeCoverage_0);

                    platinum_illness_child_0 = new ArrayList<>();
                    platinum_illness_child_0.add("Please Click Category");
                    Adapter_illness = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_custom_layout, platinum_category_Child_lifeCoverage_0);

                    paymentMethod_Platinum_child_0 = new ArrayList<>();
                    paymentMethod_Platinum_child_0.add("Please Click Category");
                    Adapter_payment = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_custom_layout, paymentMethod_Platinum_child_0);

                    Adapter_LifeCoverage.setDropDownViewResource(R.layout.spinner_custom_drop_down);
                    Adapter_illness.setDropDownViewResource(R.layout.spinner_custom_drop_down);
                    Adapter_payment.setDropDownViewResource(R.layout.spinner_custom_drop_down);

                } else if (position == 1) {
                    Adapter_LifeCoverage = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_custom_platinum, platinum_category_Child_lifeCoverage);
                    Adapter_illness = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_custom_platinum, platinum_illness_child);
                    Adapter_payment = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_custom_platinum, paymentMethod_yearly_monthly);


                    Adapter_LifeCoverage.setDropDownViewResource(R.layout.spinner_custom_layout);
                    Adapter_illness.setDropDownViewResource(R.layout.spinner_custom_layout);
                    Adapter_payment.setDropDownViewResource(R.layout.spinner_custom_layout);

                    mBtn_to_Quotation.setEnabled(true);
                    mSP_paymentMethodChild.setEnabled(true);
                } else if (position == 2) {
                    Adapter_LifeCoverage = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_custom_gold_layout, gold_category_child_lifeCoverage);
                    Adapter_illness = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_custom_gold_layout, gold_illness_child);
                    Adapter_payment = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_custom_gold_layout, paymentMethod_yearly_monthly);


                    Adapter_LifeCoverage.setDropDownViewResource(R.layout.spinner_custom_layout);
                    Adapter_illness.setDropDownViewResource(R.layout.spinner_custom_layout);
                    Adapter_payment.setDropDownViewResource(R.layout.spinner_custom_layout);

                    mBtn_to_Quotation.setEnabled(true);
                    mSP_paymentMethodChild.setEnabled(true);
                } else if (position == 3) {
                    Adapter_LifeCoverage = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_custom_silver_layout, silver_category_child_lifeCoverage);
                    Adapter_illness = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_custom_silver_layout, silver_illness_child);
                    Adapter_payment = new ArrayAdapter<>(getApplicationContext(), R.layout.spinner_custom_silver_layout, paymentMethod_yearly_monthly);


                    Adapter_LifeCoverage.setDropDownViewResource(R.layout.spinner_custom_layout);
                    Adapter_illness.setDropDownViewResource(R.layout.spinner_custom_layout);
                    Adapter_payment.setDropDownViewResource(R.layout.spinner_custom_layout);

                    mBtn_to_Quotation.setEnabled(true);
                    mSP_paymentMethodChild.setEnabled(true);
                }
                mSP_LifeCoverageChild.setAdapter(Adapter_LifeCoverage);
                mSP_Critical_illnessChild.setAdapter(Adapter_illness);
                mSP_paymentMethodChild.setAdapter(Adapter_payment);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


}