package com.example.healthappdemo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class CartActivity extends AppCompatActivity {

    RecyclerView mShopping_RecyclerView;
    TextView mCartTotal;
    LinearLayout wrapperShopping_Total, wrapperFinalTotalAndButton,mLinearLayout_CheckEmptyCart, mCardView_Cart_Next_Address;
    String USER_ID;
    String CART_PRODUCT;
    String CART_SHIPPING_DETAIL;
    int overAllTotalPrice = 0;
    TextView mCartInfo_Wrapper, mTV_Total_Text;
    FirestoreRecyclerOptions<PharmacyCart_DataModel> options;
    FirestoreRecyclerAdapter<PharmacyCart_DataModel, myViewHolderCart> myAdapter;
    LinearLayoutManager mLayoutManager;

    public static final String EXTRA_PTITLE = "com.example.healthappdemo.EXTRA_PTITLE";
    public static final String EXTRA_PQUANTITY = "com.example.healthappdemo.EXTRA_PQUANTITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);


        mCartTotal = findViewById(R.id.TV_ShoppingCart_Total);

        mShopping_RecyclerView = findViewById(R.id.recycler_view_shopping_cart);
        mCartInfo_Wrapper = findViewById(R.id.TV_Cart_Info_Message);
        mCardView_Cart_Next_Address = findViewById(R.id.CardView_Cart_Next_Address);
        mTV_Total_Text = findViewById(R.id.TV_Total_Text);
        mLinearLayout_CheckEmptyCart= findViewById(R.id.LinearLayout_CheckEmptyCart);
        wrapperShopping_Total = findViewById(R.id.LinearLayout_wrapperShopping_Total);
        wrapperFinalTotalAndButton = findViewById(R.id.LinearLayout_Address_);


        mLayoutManager  = new LinearLayoutManager(CartActivity.this);
        mShopping_RecyclerView.setLayoutManager(mLayoutManager);
        mShopping_RecyclerView.setHasFixedSize(false);


        readDataCart();

        checkShippingItems();
    }

    private void checkItems() {
        FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
        if (AppUser != null ) {

            USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            CART_PRODUCT = FirebaseAuth.getInstance().getCurrentUser().getUid();
            CollectionReference colRef = FirebaseFirestore.getInstance()
                    .collection("REGISTERED USERS").document(USER_ID)
                    .collection("CART PRODUCT");

            colRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                     if(queryDocumentSnapshots.isEmpty()){
                         mLinearLayout_CheckEmptyCart.setVisibility(View.VISIBLE);
                         mCartInfo_Wrapper.setVisibility(View.VISIBLE);
                         mCartInfo_Wrapper.setText("Cart is Empty" + "\n" + "Add Product to view items!");
                         mShopping_RecyclerView.setVisibility(View.GONE);
                         mTV_Total_Text.setVisibility(View.GONE);
                         mCartTotal.setVisibility(View.GONE);
                         wrapperShopping_Total.setVisibility(View.GONE);
                         mCardView_Cart_Next_Address.setVisibility(View.GONE);
                         wrapperFinalTotalAndButton.setVisibility(View.GONE);

                     }else {
                         mLinearLayout_CheckEmptyCart.setVisibility(View.GONE);
                         mCartInfo_Wrapper.setVisibility(View.GONE);
                         mShopping_RecyclerView.setVisibility(View.VISIBLE);
                         wrapperFinalTotalAndButton.setVisibility(View.VISIBLE);
                         mTV_Total_Text.setVisibility(View.VISIBLE);
                         mCartTotal.setVisibility(View.VISIBLE);
                         wrapperShopping_Total.setVisibility(View.VISIBLE);
                         mCardView_Cart_Next_Address.setVisibility(View.VISIBLE);

                     }
                }
            });
        }
    }


    private void checkShippingItems() {
        FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
        if (AppUser != null ) {

            USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            CART_SHIPPING_DETAIL = FirebaseAuth.getInstance().getCurrentUser().getUid();
            CollectionReference colRef =FirebaseFirestore.getInstance()
                    .collection("REGISTERED USERS").document(USER_ID)
                    //.collection("CART PRODUCT").document(CART_PRODUCT)
                    .collection("SHIPPING DETAILS");

            colRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                  if(!queryDocumentSnapshots.isEmpty()){
                      for(QueryDocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                         // PharmacyCart_DataModel PharmacyObject = documentSnapshot.toObject(PharmacyCart_DataModel.class);
                          String shippingState = documentSnapshot.getString("orderStatus");
                          if(shippingState.equals("Not Shipped")){
                              mLinearLayout_CheckEmptyCart.setVisibility(View.VISIBLE);
                              mCartInfo_Wrapper.setVisibility(View.VISIBLE);
                              mCartInfo_Wrapper.setText("Congratulations, your final order have been placed successfully. " +
                                      "Soon it will be verified." +
                                      "\n" + "\n"+ "Thank you");
                              mShopping_RecyclerView.setVisibility(View.GONE);
                              mTV_Total_Text.setVisibility(View.GONE);
                              mCartTotal.setVisibility(View.GONE);
                              wrapperShopping_Total.setVisibility(View.GONE);
                              mCardView_Cart_Next_Address.setVisibility(View.GONE);
                              wrapperFinalTotalAndButton.setVisibility(View.GONE);

                          }
                      }


                  }else{
                      checkItems();
                  }
                }
            });
        }
    }

    private void readDataCart() {
        FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
        if (AppUser != null ) {

                USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                CART_PRODUCT = FirebaseAuth.getInstance().getCurrentUser().getUid();


                Query query = FirebaseFirestore.getInstance()
                        .collection("REGISTERED USERS").document(USER_ID)
                        .collection("CART PRODUCT").orderBy("productTime", Query.Direction.ASCENDING);


                options = new FirestoreRecyclerOptions.Builder<PharmacyCart_DataModel>()
                        .setQuery(query, PharmacyCart_DataModel.class)
                        .build();

                myAdapter = new FirestoreRecyclerAdapter<PharmacyCart_DataModel, myViewHolderCart>(options) {
                    @SuppressLint("SetTextI18n")
                    @Override
                    protected void onBindViewHolder(@NonNull myViewHolderCart holder, int position, @NonNull PharmacyCart_DataModel model) {

                        holder.mCartTitle.setText(model.getProductCartName());
                        holder.mCartPrice.setText("RM" + model.getProductCartPrice());
                        holder.mCartQuantity.setText(model.getProductCartQuantity());

                        int oneTypeProductPrice = Integer.valueOf(model.getProductCartPrice()) * Integer.valueOf(model.getProductCartQuantity());
                        overAllTotalPrice = overAllTotalPrice + oneTypeProductPrice;
                        mCartTotal.setText("RM " + String.valueOf(overAllTotalPrice));

                        holder.mDeleteProductCart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                _deleteCartItemNow(holder);

                                int oneTypeProductPrice = Integer.valueOf(model.getProductCartPrice()) * Integer.valueOf(model.getProductCartQuantity());
                                overAllTotalPrice = overAllTotalPrice - oneTypeProductPrice;
                                mCartTotal.setText("RM " + String.valueOf(overAllTotalPrice));
                                checkItems();
                            }
                        });

                    }

                    @NonNull
                    @Override
                    public myViewHolderCart onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_product, parent, false);
                        return new myViewHolderCart(v);
                    }

                    @Override
                    public int getItemCount() {
                        return myAdapter.getSnapshots().toArray().length;

                    }
                };
                mShopping_RecyclerView.setAdapter(myAdapter);
            _pushDataForward();
        }
    }

    private void _pushDataForward() {
        mCardView_Cart_Next_Address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent navigateShippingScreen = new Intent(CartActivity.this, ShippingAddressActivity.class);
                navigateShippingScreen.putExtra("Total Price", String.valueOf(overAllTotalPrice));
                startActivity(navigateShippingScreen);
                finish();
            }
        });
    }

    private void _deleteCartItemNow(myViewHolderCart holder) {
        _deleteCartItem(holder.getAdapterPosition());
    }

    private void _deleteCartItem(int position) {
        myAdapter.getSnapshots().getSnapshot(position).getReference().delete();
    }


    @Override
    protected void onStart() {
        super.onStart();
        myAdapter.startListening();
        checkShippingItems();
    }

    @Override
    protected void onStop() {
        super.onStop();
        myAdapter.stopListening();
    }


}