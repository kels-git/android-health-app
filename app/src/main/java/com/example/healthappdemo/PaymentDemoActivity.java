package com.example.healthappdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.vinaygaba.creditcardview.CardType;
import com.vinaygaba.creditcardview.CreditCardView;

import java.util.HashMap;
import java.util.Map;

public class PaymentDemoActivity extends AppCompatActivity implements View.OnClickListener {

    Dialog dialog, dialogSecond, dialogThird;
    ImageView _closeDialogBtn;
    CreditCardView creditCardView;
    Button _cardToolTips, CancelDialog_btn, ProceedDialog_btn, CancelDialogLoad_btn, ProceedDialogLoad_btn;
    TextView DialogTitle, DialogMessage, DialogMessagePolicyNum, mMastercard_text, mDiscovercard_text;
    String USER_ID;
    String DEMO_CARD;
    private static final String TAG = "TAG";
    int progress_Status = 4;
    ProgressBar mProgress_Bar;
    Handler mHandler = new Handler();
    LinearLayout _btnSubmit;
    LinearLayout mLayout_dialogTitle_holder, mMasterCardContainer, mVisaCardContainer, mDiscoverCardContainer;
    LottieAnimationView animateLogo_demo_pay, lottieLoaing;
    CollectionReference collectionRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_demo);


        mProgress_Bar = findViewById(R.id.progressBarID);
        _btnSubmit = findViewById(R.id.Btn_to_Payment_Register_page_);


        mDiscovercard_text = findViewById(R.id.Discovercard_text);
        mMastercard_text = findViewById(R.id.Mastercard_text);

        //button for our below cards
        mMasterCardContainer = findViewById(R.id.mastercard_demo_container);
        mVisaCardContainer = findViewById(R.id.visacard_demo_container);
        mDiscoverCardContainer = findViewById(R.id.discovercard_demo_container);


        //for tooltip
        _cardToolTips = findViewById(R.id.card_toolTips);
        _closeDialogBtn = findViewById(R.id.ImageView_close_button);

        //for our main card
        creditCardView = (CreditCardView) findViewById(R.id.card_demo);
        creditCardView.setIsEditable(true);

        _cardToolTips.setText("?");


        mDiscoverCardContainer.setOnClickListener(this);
        mMasterCardContainer.setOnClickListener(this);
        mVisaCardContainer.setOnClickListener(this);
        _cardToolTips.setOnClickListener(this);
        _btnSubmit.setOnClickListener(this);
    }

    private void submitCardDB() {
        String cardName = creditCardView.getCardName();
        String cardNumber = creditCardView.getCardNumber();
        String cardValidity = creditCardView.getExpiryDate();
        _submitCardBtn(cardName, cardNumber, cardValidity);
    }

    private void _submitCardBtn(String cardName, String cardNumber, String cardValidity) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progress_Status < 10) {
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
                if (progress_Status == 10) {
                    FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (AppUser != null) {
                        USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        DEMO_CARD = FirebaseAuth.getInstance().getCurrentUser().getUid();


                        Map<String, Object> CollectCard = new HashMap<>();
                        CollectCard.put("Card Name", cardName);
                        CollectCard.put("Card Number", cardNumber);
                        CollectCard.put("Card Validity", cardValidity);


                        collectionRef = FirebaseFirestore.getInstance()
                                .collection("REGISTERED USERS").document(USER_ID)
                                .collection("PAY-DEMO CARD");


                        collectionRef.add(CollectCard).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                if (task.isSuccessful()) {
                                    mProgress_Bar.setVisibility(View.GONE);
                                    _confirmPaymentDemo();

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PaymentDemoActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }

            }
        }).start();
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    private void _confirmPaymentDemo() {
        String cardName = creditCardView.getCardName();
        String cardNumber = creditCardView.getCardNumber();
        String cardValidity = creditCardView.getExpiryDate();
        dialog = new Dialog(PaymentDemoActivity.this);
        dialog.setContentView(R.layout.dialog_custom);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        animateLogo_demo_pay = dialog.findViewById(R.id.animateLogo_demo_pay);
        animateLogo_demo_pay.setVisibility(View.GONE);
        DialogTitle = dialog.findViewById(R.id.Dialog_title);
        DialogMessage = dialog.findViewById(R.id.Dialog_Message);
        CancelDialog_btn = dialog.findViewById(R.id.Btn_Cancel);
        ProceedDialog_btn = dialog.findViewById(R.id.Btn_Proceed);
        ProceedDialog_btn.setText("Pay Now");
        DialogTitle.setText("Card Verification ");
        DialogMessage.setText("Your Card Information " + "\n" + "\n" +
                "Card Name: " + cardName.toLowerCase() + "\n" +
                "Card N0: " + cardNumber + "\n" +
                "Valid Till: " + cardValidity);
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

                displayLoadingDialog();
            }
        });
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    private void displayLoadingDialog() {
        dialog.dismiss();
        dialogSecond = new Dialog(PaymentDemoActivity.this);
        dialogSecond.setContentView(R.layout.dialog_custom);
        dialogSecond.setCancelable(false);
        dialogSecond.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        dialogSecond.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        CancelDialogLoad_btn = dialogSecond.findViewById(R.id.Btn_Cancel);
        ProceedDialogLoad_btn = dialogSecond.findViewById(R.id.Btn_Proceed);
        DialogMessage = dialogSecond.findViewById(R.id.Dialog_Message);
        mLayout_dialogTitle_holder = dialogSecond.findViewById(R.id.Layout_dialogTitle_holder);

        DialogMessage.setVisibility(View.GONE);
        CancelDialogLoad_btn.setVisibility(View.GONE);
        mLayout_dialogTitle_holder.setBackground(getDrawable(R.drawable.grad_button_white));
        lottieLoaing = dialogSecond.findViewById(R.id.Spinner_loading_lottie);
        lottieLoaing.setVisibility(View.VISIBLE);
        ProceedDialogLoad_btn.setText("Payment processing ...");
        dialogSecond.show();

        LottieTransformation();
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
                displaySecondDialog();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    private void displaySecondDialog() {
        dialog.dismiss();
        dialogSecond.dismiss();
        dialogThird = new Dialog(PaymentDemoActivity.this);
        dialogThird.setContentView(R.layout.dialog_custom);
        dialogThird.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        dialogThird.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogThird.setCancelable(false);
        animateLogo_demo_pay = dialogThird.findViewById(R.id.animateLogo_demo_pay);
        animateLogo_demo_pay.setVisibility(View.VISIBLE);

        CancelDialog_btn = dialogThird.findViewById(R.id.Btn_Cancel);
        CancelDialog_btn.setVisibility(View.GONE);
        ProceedDialog_btn = dialogThird.findViewById(R.id.Btn_Proceed);
        ProceedDialog_btn.setText("Thank You");

        mLayout_dialogTitle_holder = dialogThird.findViewById(R.id.Layout_dialogTitle_holder);
        mLayout_dialogTitle_holder.setBackground(getDrawable(R.drawable.grad_button_white));

        DialogTitle = dialogThird.findViewById(R.id.Dialog_title);
        DialogTitle.setVisibility(View.GONE);

        DialogMessagePolicyNum = dialogThird.findViewById(R.id.Dialog_Message);
        DialogMessagePolicyNum.setText("PAYMENT WAS SUCCESSFUL!");
        dialogThird.show();


        _navigate_to_activity_feed();
    }

    private void _navigate_to_activity_feed() {
        ProceedDialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent navigate_to_news_feed = new Intent(PaymentDemoActivity.this, NewsFeedActivity.class);
                startActivity(navigate_to_news_feed);
                finish();

            }
        });
    }

    private void cancelDialogBox() {
        CancelDialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mProgress_Bar.setVisibility(View.GONE);
            }
        });
    }


    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n", "CutPasteId"})
    private void _vitalInfoCard() {
        dialog = new Dialog(PaymentDemoActivity.this);
        dialog.setContentView(R.layout.dialog_custom_one_time);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        DialogMessage = dialog.findViewById(R.id.Dialog_Message_One_Time);
        DialogMessage.setText("Please Note this card is simply for software development demonstration purpose only on this project. " +
                "Its an EDITABLE card and you can fill up ANY information and also click on the right hand icon " +
                "to flip and input CVV. There after you can submit. Thank You!");
        _closeDialogBtn = dialog.findViewById(R.id.ImageView_close_button);

        dialog.show();

        closeDialogBox();

    }

    private void closeDialogBox() {
        _closeDialogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }


    @SuppressLint({"NonConstantResourceId", "UseCompatLoadingForDrawables"})
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.discovercard_demo_container:
                mDiscoverCardContainer.setBackground(getDrawable(R.drawable.grad_gold_background));
                mMasterCardContainer.setBackground(getDrawable(R.drawable.grad_button_white));
                mVisaCardContainer.setBackground(getDrawable(R.drawable.grad_button_white));
                creditCardView.setType(CardType.DISCOVER);

                break;
            case R.id.visacard_demo_container:
                mDiscoverCardContainer.setBackground(getDrawable(R.drawable.grad_button_white));
                mMasterCardContainer.setBackground(getDrawable(R.drawable.grad_button_white));
                mVisaCardContainer.setBackground(getDrawable(R.drawable.grad_gold_background));
                creditCardView.setType(CardType.VISA);

                break;

            case R.id.mastercard_demo_container:
                mDiscoverCardContainer.setBackground(getDrawable(R.drawable.grad_button_white));
                mMasterCardContainer.setBackground(getDrawable(R.drawable.grad_gold_background));
                mVisaCardContainer.setBackground(getDrawable(R.drawable.grad_button_white));
                creditCardView.setType(CardType.MASTERCARD);
                break;

            case R.id.card_toolTips:
                _vitalInfoCard();
                break;

            case R.id.Btn_to_Payment_Register_page_:
                submitCardDB();
                break;
        }
    }

}
