package com.example.healthappdemo;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.rey.material.widget.CheckBox;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ShippingAddressActivity extends AppCompatActivity {

    private String TotalAmount = "";

    String USER_ID;
    String CART_PRODUCT;
    String CART_SHIPPING_DETAIL;
    String saveCurrentTime, saveCurrentDate;
    LinearLayout mBt_navigatePayment, error_Info, mLayout_dialogTitle_holder;
    CollectionReference collectionRef;
    CheckBox mCheckBoxShipCard;
    EditText shippingName, shippingPhoneNum, shippingHomeAddress, shippingCity;
    String Not_yet_shipped = "Not Shipped";
    Dialog  dialogSecond, dialogThird;
    TextView DialogTitle, DialogMessage, DialogMessagePolicyNum;
    Button CancelDialog_btn, ProceedDialog_btn, CancelDialogLoad_btn, ProceedDialogLoad_btn;
    LottieAnimationView animateLogo_demo_pay, lottieLoading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_address);

        TotalAmount = getIntent().getStringExtra("Total Price");

        shippingName = findViewById(R.id.TV_ShippingName);
        shippingPhoneNum = findViewById(R.id.TV_ShippingPhoneNum);
        shippingHomeAddress = findViewById(R.id.TV_ShippingAddress);
        shippingCity = findViewById(R.id.TV_ShippingCity);
        mBt_navigatePayment = findViewById(R.id.LinearLayout_Cart_Next_Address);
        error_Info = findViewById(R.id.LinearLayout_Error_Msg_Shopping_details);
        mCheckBoxShipCard = findViewById(R.id.Remember_Me_CheckBox_ShipCard);

        mBt_navigatePayment.setOnClickListener(view -> {

            String userShip_Name= shippingName.getText().toString().trim();
            String userShip_phone = shippingPhoneNum.getText().toString().trim();
            String userShip_Address = shippingHomeAddress.getText().toString().trim();
            String userShip_City = shippingCity.getText().toString().trim();

            if (TextUtils.isEmpty(userShip_Name) || TextUtils.isEmpty(userShip_phone) ||
                    TextUtils.isEmpty(userShip_Address) || TextUtils.isEmpty(userShip_City) || !mCheckBoxShipCard.isChecked()) {
                error_Info.setVisibility(View.VISIBLE);
                return;

            }
            if (!TextUtils.isEmpty(userShip_Name) && !TextUtils.isEmpty(userShip_phone) &&
                    !TextUtils.isEmpty(userShip_Address) && !TextUtils.isEmpty(userShip_City) && mCheckBoxShipCard.isChecked()) {
                error_Info.setVisibility(View.GONE);
                saveShippingData(userShip_Name, userShip_phone, userShip_Address, userShip_City,TotalAmount);

            }
        });


    }

    private void saveShippingData(String userShip_name, String userShip_phone, String userShip_address,
                                  String userShip_city, String totalAmount) {

        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("EEE MMM dd yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());



        FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
        if (AppUser != null){
            USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            //CART_PRODUCT = FirebaseAuth.getInstance().getCurrentUser().getUid();
            CART_SHIPPING_DETAIL = FirebaseAuth.getInstance().getCurrentUser().getUid();

            collectionRef = FirebaseFirestore.getInstance()
                    .collection("REGISTERED USERS").document(USER_ID)
                    //.collection("CART PRODUCT").document(CART_PRODUCT)
                    .collection("SHIPPING DETAILS");

            ShippingAddress_model shippingDetails = new ShippingAddress_model(userShip_name,userShip_phone, userShip_address,
                    userShip_city, totalAmount, saveCurrentDate, saveCurrentTime,  Not_yet_shipped );


            collectionRef.add(shippingDetails).addOnCompleteListener(task -> {
                if(task.isSuccessful()){

                    displayPaymentLoadingDialog();

                        CollectionReference colRef = FirebaseFirestore.getInstance()
                                    .collection("REGISTERED USERS").document(USER_ID)
                                    .collection("CART PRODUCT");

                            colRef.get().addOnSuccessListener(queryDocumentSnapshots -> {



                                for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                    PharmacyCart_DataModel PharmacyObject = documentSnapshot.toObject(PharmacyCart_DataModel.class);
                                    PharmacyObject.setProductID(documentSnapshot.getId());

                                    String documentID = PharmacyObject.getProductID();
                                    colRef.document(documentID).delete();


                                }
                            });
                }
            }).addOnFailureListener(e -> Toast.makeText(ShippingAddressActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show());
        }
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    private void displayPaymentLoadingDialog() {
        dialogSecond = new Dialog(ShippingAddressActivity.this);
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
        lottieLoading = dialogSecond.findViewById(R.id.Spinner_loading_lottie);
        lottieLoading.setVisibility(View.VISIBLE);
        ProceedDialogLoad_btn.setText("Payment processing ...");
        dialogSecond.show();

        LottieTransformation();
    }

    private void LottieTransformation() {
        lottieLoading.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                lottieLoading.setRepeatCount(2);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                displaySecondDialog();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    private void displaySecondDialog() {
        dialogSecond.dismiss();
        dialogThird = new Dialog(ShippingAddressActivity.this);
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
        DialogMessagePolicyNum.setText("PAYMENT SUCCESSFUL" + "\n"  + "\n" + "Your final order has been placed Successfully!");
        dialogThird.show();

        _navigate_to_activity_feed();
    }

    private void _navigate_to_activity_feed() {
        ProceedDialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent navigate_to_news_feed = new Intent(ShippingAddressActivity.this, NewsFeedActivity.class);
                navigate_to_news_feed.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                startActivity(navigate_to_news_feed);
                finish();

            }
        });
    }

}