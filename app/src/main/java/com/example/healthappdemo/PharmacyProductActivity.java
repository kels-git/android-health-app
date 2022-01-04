package com.example.healthappdemo;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PharmacyProductActivity extends AppCompatActivity {

    ImageView ImageProductContent, _closeDialogBtn;
    TextView mProductTitle, mProductPrice, mProductDesc, DialogMessage;
    FloatingActionButton mBt_add_product;
    ElegantNumberButton mCounterProduct;
    LinearLayout mBt_putInCart;
    PharmacyItem pharmacyItem;
    String saveCurrentTime, saveCurrentDate;
    String USER_ID;
    String CART_PRODUCT;
    CollectionReference collectionReference;
String CART_SHIPPING_DETAIL;
    StorageReference mStorageRef;
    private StorageTask mUploadTask;
    Dialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pharmacy_product);

        Intent navigateProductScreen = getIntent();
         pharmacyItem = navigateProductScreen.getParcelableExtra("PharmacyItemProduct");

        mStorageRef = FirebaseStorage.getInstance().getReference("upLoadCartImage");

        ImageProductContent = findViewById(R.id.IMG_ImageProduct);
        mProductTitle = findViewById(R.id.TV_Product_Content_Title);
        mProductPrice = findViewById(R.id.TV_Product_Content_Price);
        mProductDesc = findViewById(R.id.TV_Product_Content_Desc);
        //mBt_add_product = findViewById(R.id.add_product_cart);
        mCounterProduct = findViewById(R.id.number_product_btn);
        mBt_putInCart = findViewById(R.id.LinearLayout_add_product);

        mCounterProduct.setOnClickListener(new ElegantNumberButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                ////
            }
        });


        getProduct(pharmacyItem);

        mBt_putInCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
                if (AppUser != null ) {

                    USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    //CART_PRODUCT = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    CART_SHIPPING_DETAIL = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    CollectionReference colRef =FirebaseFirestore.getInstance()
                            .collection("REGISTERED USERS").document(USER_ID)
                            .collection("SHIPPING DETAILS");

                    colRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                            if(!queryDocumentSnapshots.isEmpty()){
                                for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                                    // PharmacyCart_DataModel PharmacyObject = documentSnapshot.toObject(PharmacyCart_DataModel.class);
                                    String shippingState = documentSnapshot.getString("orderStatus");
                                    if(shippingState.equals("Not Shipped")) {
                                        displayDialogMessage();
                                    }
                                }
                            }else{
                                int imageRes = pharmacyItem.getmNavImagePharmacyItem();
                                String Product_Title = mProductTitle.getText().toString();
                                String Product_Price = mProductPrice.getText().toString();
                                String Product_Desc = mProductDesc.getText().toString();
                                String Product_Quantity = mCounterProduct.getNumber();

                                addToCartProductList(Product_Title, Product_Price, Product_Desc, Product_Quantity, imageRes);
                            }
                        }
                    });


                }
            }
        });

    }

    @SuppressLint("SetTextI18n")
    private void displayDialogMessage() {
        dialog = new Dialog(PharmacyProductActivity.this);
        dialog.setContentView(R.layout.dialog_custom_one_time);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);

        DialogMessage = dialog.findViewById(R.id.Dialog_Message_One_Time);
        DialogMessage.setText("Dear Customer" + "\n" + "You cannot add more product until the Admin verify your previous order and send to your destination"
                        + "\n"  + "\n" +" Thank You");
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


    private void addToCartProductList(String product_title, String product_price, String product_desc, String Product_Quantity, int imagesRes ) {

        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());

        FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
        if (AppUser != null ) {
            USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            CART_PRODUCT = FirebaseAuth.getInstance().getCurrentUser().getUid();

            collectionReference = FirebaseFirestore.getInstance()
                    .collection("REGISTERED USERS").document(USER_ID)
                    .collection("CART PRODUCT");

            PharmacyCart_DataModel addPharmacy = new PharmacyCart_DataModel(product_title, product_price, Product_Quantity, saveCurrentDate,
                     saveCurrentTime, product_desc, imagesRes);

            collectionReference.add(addPharmacy).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                @Override
                public void onComplete(@NonNull Task<DocumentReference> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(PharmacyProductActivity.this, "Product added successfully", Toast.LENGTH_SHORT).show();
                        Intent navigateBack_ProductPage = new Intent(PharmacyProductActivity.this, PharmacyActivity.class);
                        startActivity(navigateBack_ProductPage);
                        finish();
                    }else{
                        Toast.makeText(PharmacyProductActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }


    private void getProduct(PharmacyItem pharmacyItem) {

       int imageRes = pharmacyItem.getmNavImagePharmacyItem();
        String ProductTitle = pharmacyItem.getmPharmacyTitle();
        String ProductPrice = pharmacyItem.getmPrice();
        String ProductQuantity = pharmacyItem.getmPharmacyQuantity();
        String ProductDesc = pharmacyItem.getmPharmacyDesc();

        ImageProductContent.setImageResource(imageRes);
        mProductTitle.setText( ProductTitle);
        mProductPrice.setText(ProductPrice);
        mCounterProduct.setNumber(ProductQuantity);
        mProductDesc.setText(ProductDesc);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent navigateBackToPharmacy_Activity = new Intent(PharmacyProductActivity.this, PharmacyActivity.class);
        startActivity(navigateBackToPharmacy_Activity);
    }
}