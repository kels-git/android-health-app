package com.example.healthappdemo;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Objects;


public class ClaimsActivity extends AppCompatActivity {

    DrawerLayout mDrawerLayout;
    RecyclerView mRecyclerView;
    ImageView btMenu, btProfile, btSetting;
    MainNavAdapter.onItemClickListener mListener;

    EditText mAmount_Receipt, mAmount_Request, mClaimantName;
    Spinner mPolicyType, mClaimType, mDocDiagnose;
    TextView mConsultDate;
    LinearLayout mReceipt_Upload, errorContainer, btnSubmitClaim;
    ImageView mImageViewReceipt;
    String USER_ID;
    //Button btnSubmitClaim;
    Dialog dialog;
    Dialog dialog_three;
    TextView mmDialogTitle,
            mmDialogMessage, mmCancelDialog_btn, mmProceedDialog_btn;

    DatePickerDialog.OnDateSetListener setListener;


    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDataBaseRef;
    private StorageTask mUploadTask;
    private static final String TAG = "TAG";
    int progress_Status = 3;
    ProgressBar mProgress_Bar;
    Handler mHandler = new Handler();
    String USER_CLAIMS;
    TextView mClaimLink, DialogTitle, DialogMessage;
    int counter = 0;
    Button YesDialog_btn, NoDialog_btn;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claims);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        btMenu = findViewById(R.id.Menu_bar_btn);
        btSetting = findViewById(R.id.Settings_Menu_bar_btn);
        btProfile = findViewById(R.id.Profile_Menu_bar_btn);
        mRecyclerView = findViewById(R.id.recycler_view);

        //claim submission form
        mPolicyType = findViewById(R.id.sp_policy_type);
        mClaimType = findViewById(R.id.sp_claim_type);
        mDocDiagnose = findViewById(R.id.sp_diagnose_type);
        mClaimantName = findViewById(R.id.et_display_name);
        mAmount_Receipt = findViewById(R.id.et_amount_on_receipt);
        mAmount_Request = findViewById(R.id.et_amount_request);
        mConsultDate = findViewById(R.id.TV_Consult_Date);
        mReceipt_Upload = findViewById(R.id.upload_receipt);
        mImageViewReceipt = findViewById(R.id.ImageViewReceipt);
        btnSubmitClaim = findViewById(R.id.Btn_submit_Claim);
        errorContainer = findViewById(R.id.LinearLayout_display_error_eClaim);
        mProgress_Bar = findViewById(R.id.progressBarID_Claim);
        mClaimLink = findViewById(R.id.TV_claim_link);


        //Store the image
        mStorageRef = FirebaseStorage.getInstance().getReference("ClaimsUpload");
        //mDataBaseRef = FirebaseDatabase.getInstance().getReference("ClaimsUpload");


        //set layout manager
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //set the adapter
        mRecyclerView.setAdapter(new MainNavAdapter(NewsFeedActivity.drawerArrayList, mListener, this));

        //open navigation drawer
        btMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //open drawer
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //navigate to profile page
        btProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClaimsActivity.this, UserProfileActivity.class);
                startActivity(intent); // start same activity
                finish(); // destroy older activity


            }
        });

        //navigate to setting page
        btSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClaimsActivity.this, UserSettingsActivity.class);
                startActivity(intent); // start same activity
                finish(); // destroy older activity
            }
        });


        ArrayAdapter<CharSequence> PolicyTypeAdapter = ArrayAdapter.createFromResource(this, R.array.policy_type, R.layout.spinner_custom_layout_blue);
        ArrayAdapter<CharSequence> TypeOfClaimAdapter = ArrayAdapter.createFromResource(this, R.array.claim_type, R.layout.spinner_custom_layout_blue);
        ArrayAdapter<CharSequence> TypeOfDiagnose = ArrayAdapter.createFromResource(this, R.array.doctor_diagnose, R.layout.spinner_custom_layout_blue);
        PolicyTypeAdapter.setDropDownViewResource(R.layout.spinner_custom_drop_down);
        TypeOfClaimAdapter.setDropDownViewResource(R.layout.spinner_custom_drop_down);
        TypeOfDiagnose.setDropDownViewResource(R.layout.spinner_custom_drop_down);

        mPolicyType.setAdapter(PolicyTypeAdapter);
        mClaimType.setAdapter(TypeOfClaimAdapter);
        mDocDiagnose.setAdapter(TypeOfDiagnose);

        pickDate();


        //submit form here
        btnSubmitClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userClaimantName = mClaimantName.getText().toString().trim();
                String userPolicy_Type = mPolicyType.getSelectedItem().toString();
                String userClaim_Type = mClaimType.getSelectedItem().toString();
                String userDoc_Diagnose = mDocDiagnose.getSelectedItem().toString();
                String userAmount_Receipt = mAmount_Receipt.getText().toString().trim();
                String userAmount_Request = mAmount_Request.getText().toString().trim();
                String userConsultation_date = mConsultDate.getText().toString().trim();

                if (mPolicyType.getSelectedItemPosition() == 0 && mClaimType.getSelectedItemPosition() == 0 &&
                        mDocDiagnose.getSelectedItemPosition() == 0
                        && TextUtils.isEmpty(userAmount_Receipt) && TextUtils.isEmpty(userAmount_Request) &&
                        TextUtils.isEmpty(userConsultation_date) && TextUtils.isEmpty(userClaimantName)) {
                    errorContainer.setVisibility(View.VISIBLE);
                    return;
                }

                if (TextUtils.isEmpty(userConsultation_date) && mPolicyType.getSelectedItemPosition() > 0 &&
                        mClaimType.getSelectedItemPosition() > 0 && mDocDiagnose.getSelectedItemPosition() > 0 &&
                        !TextUtils.isEmpty(userClaimantName) && !TextUtils.isEmpty(userAmount_Receipt) &&
                        !TextUtils.isEmpty(userAmount_Request)) {
                    errorContainer.setVisibility(View.VISIBLE);
                    return;
                }

                if (mUploadTask != null) {
                    Toast.makeText(ClaimsActivity.this, "Upload in progress..", Toast.LENGTH_SHORT).show();
                    return;

                }

                if (mPolicyType.getSelectedItemPosition() > 0 && mClaimType.getSelectedItemPosition() > 0 &&
                        mDocDiagnose.getSelectedItemPosition() > 0 && !TextUtils.isEmpty(userAmount_Receipt) && !TextUtils.isEmpty(userAmount_Request)
                        && userConsultation_date.length() > 0 && !TextUtils.isEmpty(userClaimantName)
                        && mImageUri != null) {

                    errorContainer.setVisibility(View.GONE);


                    uploadFile(userClaimantName, userPolicy_Type, userClaim_Type,
                            userDoc_Diagnose, userAmount_Receipt, userAmount_Request, userConsultation_date);
                }
            }
        });


        //click to upload receipt
        clickUpload_Button();

        NewsFeedActivity newsFeedActivity = new NewsFeedActivity();
        newsFeedActivity.mAboutHA = findViewById(R.id.TV_menu_about_HA);
        newsFeedActivity.mAboutHA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                displayDialogAboutApp();

            }
        });

    }


    @SuppressLint("SetTextI18n")
    private void displayDialogAboutApp() {
        dialog_three = new Dialog(ClaimsActivity.this);
        dialog_three.setContentView(R.layout.dialog_custom);
        dialog_three.setCancelable(true);
        dialog_three.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        dialog_three.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        mmDialogTitle = dialog_three.findViewById(R.id.Dialog_title);
        mmDialogMessage = dialog_three.findViewById(R.id.Dialog_Message);
        mmCancelDialog_btn = dialog_three.findViewById(R.id.Btn_Cancel);
        mmProceedDialog_btn = dialog_three.findViewById(R.id.Btn_Proceed);

        mmCancelDialog_btn.setVisibility(View.GONE);

        mmProceedDialog_btn.setText("Visit Developer Page");

        mmDialogTitle.setText("About Health App");
        mmDialogMessage.setText(
                "Health App is a personal side project carried out by Osazuwa Ogiemwanye, a software developer. Health app includes " +
                        "a step counter, Bmi Calculator, covid info, news feed , and follow users section." + "Also included is extra " +
                        "for IT recruiters a basic for Insurance company on eclaim submission and many more. Basically this app is met " +
                        "for IT recruiters to display my little " + "skills in mobile app development Android native (Java) and integrated " +
                        "with flexible backend (Firebase)");
        dialog_three.show();


        //confirm the dialog box and navigate to the payment section
        proceedToDeveloperScreen();
    }

    private void proceedToDeveloperScreen() {
        mmProceedDialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog_three.dismiss();
                Intent navigate_dev_screen = new Intent(ClaimsActivity.this, ContactDeveloperActivity.class);
                startActivity(navigate_dev_screen);

            }
        });
    }


    private void clickUpload_Button() {
        mReceipt_Upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askGalleryPermission();
            }
        });
    }

    private void askGalleryPermission() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {

            mImageUri = data.getData();

            Picasso.get()
                    .load(mImageUri)
                    .into(mImageViewReceipt);

            //mImageViewReceipt.setImageURI(mImageUri);
        }
    }


    private String getFileExtension(Uri uri) {
        ContentResolver CR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(CR.getType(uri));
    }

    ///for upload


    private void uploadFile(String userClaimantName, String userPolicy_Type, String userClaim_Type,
                            String userDoc_Diagnose, String userAmount_Receipt, String userAmount_Request, String userConsultation_date) {

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
                    StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));

                    mUploadTask = fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(ClaimsActivity.this, "file uploaded successfully", Toast.LENGTH_SHORT).show();

                            if (taskSnapshot.getMetadata() != null) {
                                if (taskSnapshot.getMetadata().getReference() != null) {
                                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                        @SuppressLint("SetTextI18n")
                                        @Override
                                        public void onSuccess(Uri uri) {
                                            String photoLink = uri.toString();
                                            String ClaimantName = mClaimantName.getText().toString();

                                            USER_ID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

                                            FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
                                            if (AppUser != null && mImageUri != null) {
                                                USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                                USER_CLAIMS = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                                counter++;
                                                mClaimLink.setText(String.valueOf(counter));
                                                String ClaimSubmitCount = mClaimLink.getText().toString();

                                                CollectionReference collectionReference = FirebaseFirestore.getInstance()
                                                        .collection("REGISTERED USERS").document(USER_ID)
                                                        .collection("USER CLAIMS");

                                                ClaimsHistory_DataModel ClaimSubmitData = new ClaimsHistory_DataModel(userClaimantName, userPolicy_Type, userClaim_Type,
                                                        userDoc_Diagnose, userAmount_Receipt, userAmount_Request, userConsultation_date, photoLink, ClaimSubmitCount);


                                                collectionReference.add(ClaimSubmitData)
                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                            @SuppressLint("SetTextI18n")
                                                            @Override
                                                            public void onSuccess(DocumentReference documentReference) {
                                                                Toast.makeText(ClaimsActivity.this, "Form submit successfully", Toast.LENGTH_SHORT).show();
                                                                mProgress_Bar.setVisibility(View.GONE);
                                                                _displayDialogMsg();

                                                            }
                                                        });
                                            }
                                        }
                                    });

                                }
                            }

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ClaimsActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    Toast.makeText(ClaimsActivity.this, "No file Selected", Toast.LENGTH_SHORT).show();

                }


            }

        }).start();


    }


    //dialog-box messages starts here
    @SuppressLint({"UseCompatLoadingForDrawables", "SetTextI18n"})
    private void _displayDialogMsg() {

        dialog = new Dialog(ClaimsActivity.this);
        dialog.setContentView(R.layout.dialog_custom);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.dialog_background));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        DialogTitle = dialog.findViewById(R.id.Dialog_title);
        DialogMessage = dialog.findViewById(R.id.Dialog_Message);
        NoDialog_btn = dialog.findViewById(R.id.Btn_Cancel);
        YesDialog_btn = dialog.findViewById(R.id.Btn_Proceed);
        YesDialog_btn.setText("Yes");
        NoDialog_btn.setText("No");
        DialogTitle.setText("EClaim Form Successfully Submitted! ");
        DialogMessage.setText("Do you wish to submit another claim? If Yes Click on the Button and if No click button to Exit! ");
        dialog.show();

        //No proceed to the payment-demo page
        No_Btn_DialogBox();

        //yes -cancel the check quotation and return to main page
        Yes_Btn_DialogBox();
    }

    private void Yes_Btn_DialogBox() {
        YesDialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent navigate_to_claim_activity = new Intent(ClaimsActivity.this, ClaimsActivity.class);
                startActivity(navigate_to_claim_activity);
                finish(); // destroy older activity
                overridePendingTransition(0, 0);
            }
        });
    }

    private void No_Btn_DialogBox() {
        NoDialog_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navigate_to_NewsFeedActivity = new Intent(ClaimsActivity.this, NewsFeedActivity.class);
                startActivity(navigate_to_NewsFeedActivity);
                finish();
            }
        });
    }
    //dialog-box messages ends here


    private void pickDate() {
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        mConsultDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog DatePickerDialog = new DatePickerDialog(ClaimsActivity.this,
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
                mConsultDate.setText(date);

            }
        };


    }


    @Override
    protected void onPause() {
        super.onPause();
        NewsFeedActivity.closeDrawer(mDrawerLayout);

    }
}