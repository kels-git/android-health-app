package com.example.healthappdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    String USER_ID;
    ProgressBar mProgressBar;
    EditText mEmail, mPassword;
    EditText mFirstName, mLastName,  mUserName;
    ImageView mContainerImage;
    RelativeLayout mUploadCamera;
   TextView  mFirst_name_error_id, mLast_name_error_id, mUserNameError;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;
    String ELIGIBILITY_CHECK;

    private StorageReference mStorageRef;
    private StorageTask mUploadTask;

    TextView emailError, passError, validPassError, validEmailError;
    String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-m]+\\.+[c-o]+"; // registration strictly with Gmail
    //    String RegExpPass ="/^[A-Z0-9]+$/"; // for password

    private static final String TAG = "TAG";
    LinearLayout mLinearLayout_goBack_Welcome_Screen, mLinearLayout_Register_button, mLinearLayout_display_error_email_pass;
    int progress_Status = 2;
    Handler mHandler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        // progress bar
        mProgressBar = findViewById(R.id.progressBarID);

        // all variables needed
        mEmail = findViewById(R.id.user_register_email);
        mPassword = findViewById(R.id.user_register_password);
        mFirstName = findViewById(R.id.user_register_firstName);
        mUserName = findViewById(R.id.user_Register_UserName);
        mLastName = findViewById(R.id.user_register_LastName);

        //for error messages
        mLast_name_error_id = findViewById(R.id.last_name_error_id);
        mFirst_name_error_id = findViewById(R.id.first_name_error_id);
        mUserNameError = findViewById(R.id._usernameError_id);

       //camera display
        mUploadCamera = findViewById(R.id.Image_upload_camera);
        mContainerImage = findViewById(R.id.Image_newUser_register);

        mLinearLayout_Register_button = findViewById(R.id.LinearLayout_Register);
        mLinearLayout_goBack_Welcome_Screen = findViewById(R.id.LinearLayout_goBack_Welcome_Screen);

        emailError = findViewById(R.id.email_error_id);
        passError = findViewById(R.id.password_error_id);
        validPassError = findViewById(R.id.valid_password_error_id);

        validEmailError = findViewById(R.id.email_error_valid_email_id);
        mLinearLayout_display_error_email_pass = findViewById(R.id.LinearLayout_display_error_email_pass);

        //Store the image
        mStorageRef = FirebaseStorage.getInstance().getReference("ProfileUpload");



        mLinearLayout_Register_button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {

                String newUser_Email = mEmail.getText().toString().trim();
                String newUser_Password = mPassword.getText().toString().trim();
                String newUser_FirstName = mFirstName.getText().toString().trim();
                String newUser_LastName = mLastName.getText().toString().trim();
                String newUser_userName = mUserName.getText().toString().trim();

                if (mUploadTask != null) {
                    Toast.makeText(RegisterActivity.this, "Upload in progress..", Toast.LENGTH_SHORT).show();
                    return;

                }

                if (TextUtils.isEmpty(newUser_Email) && TextUtils.isEmpty(newUser_Password) &&
                        TextUtils.isEmpty(newUser_FirstName) && TextUtils.isEmpty(newUser_LastName)&&
                        TextUtils.isEmpty(newUser_userName)) {

                    mLinearLayout_display_error_email_pass.setVisibility(View.VISIBLE);
                    emailError.setVisibility(View.VISIBLE);
                    passError.setVisibility(View.VISIBLE);
                    mFirst_name_error_id.setVisibility(View.VISIBLE);
                    mLast_name_error_id.setVisibility(View.VISIBLE);
                    mUserNameError.setVisibility(View.VISIBLE);

                    return;

                } else if (TextUtils.isEmpty(newUser_Email) && !TextUtils.isEmpty(newUser_Password) &&
                !TextUtils.isEmpty(newUser_FirstName) && !TextUtils.isEmpty(newUser_LastName)&&
                        !TextUtils.isEmpty(newUser_userName)) {

                    mLinearLayout_display_error_email_pass.setVisibility(View.VISIBLE);
                    validEmailError.setVisibility(View.VISIBLE);
                    passError.setVisibility(View.GONE);
                    mFirst_name_error_id.setVisibility(View.GONE);
                    mLast_name_error_id.setVisibility(View.GONE);
                    mUserNameError.setVisibility(View.GONE);
                    return;

                }  else if (!TextUtils.isEmpty(newUser_Email) && TextUtils.isEmpty(newUser_Password) &&
                        !TextUtils.isEmpty(newUser_FirstName) && !TextUtils.isEmpty(newUser_LastName)&&
                        !TextUtils.isEmpty(newUser_userName)) {

                    mLinearLayout_display_error_email_pass.setVisibility(View.VISIBLE);
                    validEmailError.setVisibility(View.GONE);
                    passError.setVisibility(View.VISIBLE);
                    mFirst_name_error_id.setVisibility(View.GONE);
                    mLast_name_error_id.setVisibility(View.GONE);
                    mUserNameError.setVisibility(View.GONE);
                    return;

                }  else if (!TextUtils.isEmpty(newUser_Email) && !TextUtils.isEmpty(newUser_Password) &&
                        TextUtils.isEmpty(newUser_FirstName) && !TextUtils.isEmpty(newUser_LastName)&&
                        !TextUtils.isEmpty(newUser_userName)) {

                    mLinearLayout_display_error_email_pass.setVisibility(View.VISIBLE);
                    validEmailError.setVisibility(View.GONE);
                    passError.setVisibility(View.GONE);
                    mFirst_name_error_id.setVisibility(View.VISIBLE);
                    mLast_name_error_id.setVisibility(View.GONE);
                    mUserNameError.setVisibility(View.GONE);
                    return;

                }  else if (!TextUtils.isEmpty(newUser_Email) && !TextUtils.isEmpty(newUser_Password) &&
                        !TextUtils.isEmpty(newUser_FirstName) && TextUtils.isEmpty(newUser_LastName)&&
                        !TextUtils.isEmpty(newUser_userName)) {

                    mLinearLayout_display_error_email_pass.setVisibility(View.VISIBLE);
                    validEmailError.setVisibility(View.GONE);
                    passError.setVisibility(View.GONE);
                    mFirst_name_error_id.setVisibility(View.GONE);
                    mLast_name_error_id.setVisibility(View.VISIBLE);
                    mUserNameError.setVisibility(View.GONE);
                    return;

                } else if (!TextUtils.isEmpty(newUser_Email) && !TextUtils.isEmpty(newUser_Password) &&
                        !TextUtils.isEmpty(newUser_FirstName) && !TextUtils.isEmpty(newUser_LastName)&&
                        TextUtils.isEmpty(newUser_userName)) {

                    mLinearLayout_display_error_email_pass.setVisibility(View.VISIBLE);
                    validEmailError.setVisibility(View.GONE);
                    passError.setVisibility(View.GONE);
                    mFirst_name_error_id.setVisibility(View.GONE);
                    mLast_name_error_id.setVisibility(View.GONE);
                    mUserNameError.setVisibility(View.VISIBLE);
                    return;

                } else if (TextUtils.isEmpty(newUser_Email) || TextUtils.isEmpty(newUser_Password) ||
                        TextUtils.isEmpty(newUser_FirstName) || TextUtils.isEmpty(newUser_LastName)||
                        TextUtils.isEmpty(newUser_userName)) {

                    mLinearLayout_display_error_email_pass.setVisibility(View.VISIBLE);
                    validEmailError.setVisibility(View.VISIBLE);
                    validEmailError.setText("All fields are required!");

                    return;

                } if(mImageUri == null){
                    mLinearLayout_display_error_email_pass.setVisibility(View.VISIBLE);
                    validEmailError.setVisibility(View.VISIBLE);
                    validEmailError.setText("Please Upload Photo!");
                    return;
                }

                if(mImageUri != null && !TextUtils.isEmpty(newUser_Email) && !TextUtils.isEmpty(newUser_Password) &&
                        !TextUtils.isEmpty(newUser_FirstName) && !TextUtils.isEmpty(newUser_LastName)&&
                        !TextUtils.isEmpty(newUser_userName)){

                    registerTo_RealtimeDatabase_And_FireStoreDatabase(newUser_Email, newUser_Password, newUser_FirstName,
                            newUser_LastName, newUser_userName);
                }



            }
        });

        mEmail.addTextChangedListener(RegisterTextWatcher);
        mPassword.addTextChangedListener(RegisterTextWatcher);
        mFirstName.addTextChangedListener(RegisterTextWatcher);
        mLastName.addTextChangedListener(RegisterTextWatcher);
        mUserName.addTextChangedListener(RegisterTextWatcher);

        clickUpload_ProfilePic();

    }

    private void clickUpload_ProfilePic() {

        mUploadCamera.setOnClickListener(new View.OnClickListener() {
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
                    .into(mContainerImage);
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }


    private void registerTo_RealtimeDatabase_And_FireStoreDatabase(String newUser_email, String newUser_password,
                                                                   String newUser_firstName, String newUser_lastName,
                                                                   String newUser_userName) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progress_Status < 10){
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

                if(progress_Status == 10){
                    FirebaseAuth.getInstance().createUserWithEmailAndPassword(newUser_email, newUser_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {


                                StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
                                mUploadTask = fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        if (taskSnapshot.getMetadata() != null) {
                                            if (taskSnapshot.getMetadata().getReference() != null) {
                                                Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                                result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {

                                                        String photoProfileLink = uri.toString();
                                                        FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();

                                                        if (AppUser != null && mImageUri != null) {
                                                            USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();


                                                            User_Model registeredUsers = new User_Model(USER_ID, photoProfileLink, newUser_email, newUser_password,
                                                                    newUser_firstName, newUser_lastName, newUser_userName);

                                                            DocumentReference documentReference = FirebaseFirestore.getInstance()
                                                                    .collection("REGISTERED USERS").document(USER_ID);

                                                            documentReference.set(registeredUsers).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Log.d(TAG, "onSuccess: User_Model eligibility updated " + USER_ID);
                                                                    // mProgressBar.setVisibility(View.VISIBLE);
                                                                }
                                                            }).addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.d(TAG, "onFailure: User_Model eligibility FAILED" + e.toString());
                                                                }
                                                            });


                                                            finish();
                                                            Intent navigate_to_eligibility_one = new Intent(RegisterActivity.this, EligibilityTestFirstActivity.class);
                                                            startActivity(navigate_to_eligibility_one);
                                                        }else{
                                                            mProgressBar.setVisibility(View.GONE);
                                                            Toast.makeText(RegisterActivity.this, "Error, please try again!", Toast.LENGTH_SHORT).show();
                                                        }

                                                    }
                                                });
                                            }
                                        }

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });




//                                FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
//                                if (AppUser != null) {
//                                    USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
//                                    Map<String, Object> registeredUser = new HashMap<>();
//                                    registeredUser.put("User Id", USER_ID);
//                                    registeredUser.put("Email", newUser_email);
//                                    registeredUser.put("Password", newUser_password);
//
//
//                                    //register to fireStore database
//                                    DocumentReference documentReference = FirebaseFirestore.getInstance()
//                                            .collection("REGISTERED USERS").document(USER_ID);
//                                    documentReference.set(registeredUser).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                        @Override
//                                        public void onSuccess(Void aVoid) {
//                                            Log.d(TAG, "onSuccess: User_Model profile is created for " + USER_ID);
//                                            mProgressBar.setVisibility(View.VISIBLE);
//                                            Toast.makeText(RegisterActivity.this, "Registration Successful!", Toast.LENGTH_SHORT).show();
//
//                                        }
//                                    }).addOnFailureListener(new OnFailureListener() {
//                                        @Override
//                                        public void onFailure(@NonNull Exception e) {
//                                            mProgressBar.setVisibility(View.GONE);
//                                            Log.d(TAG, "onFailure: Profile Not Created" + e.toString());
//                                        }
//                                    });
//
//
//                                    Intent navigate_to_first_eligibility = new Intent(RegisterActivity.this, EligibilityTestFirstActivity.class);
//                                    startActivity(navigate_to_first_eligibility);
//
//                                } else {
//                                    mProgressBar.setVisibility(View.GONE);
//                                    Toast.makeText(RegisterActivity.this, "Registration failed!, Try Again", Toast.LENGTH_SHORT).show();
//                                }

                            }
                        }

                    });
                }
            }




        }).start();


    }





    private final TextWatcher RegisterTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            final String newUser_Email = mEmail.getText().toString().trim();
            String newUser_Password = mPassword.getText().toString().trim();
            String newUser_FirstName = mFirstName.getText().toString().trim();
            String newUser_LastName = mLastName.getText().toString().trim();
            String newUser_userName = mUserName.getText().toString().trim();



            if (!newUser_Email.matches(EMAIL_PATTERN)) {
                mLinearLayout_display_error_email_pass.setVisibility(View.VISIBLE);
                validEmailError.setVisibility(View.VISIBLE);

            } else {
                mLinearLayout_display_error_email_pass.setVisibility(View.GONE);
                validEmailError.setVisibility(View.GONE);
            }

            if (newUser_Email.isEmpty()) {
                mLinearLayout_display_error_email_pass.setVisibility(View.VISIBLE);
                emailError.setVisibility(View.VISIBLE);

            } else {
                mLinearLayout_display_error_email_pass.setVisibility(View.GONE);
                emailError.setVisibility(View.GONE);

            }

            if (newUser_FirstName.isEmpty()) {
                mLinearLayout_display_error_email_pass.setVisibility(View.VISIBLE);
                mFirst_name_error_id.setVisibility(View.VISIBLE);

            } else {
                mLinearLayout_display_error_email_pass.setVisibility(View.GONE);
                mFirst_name_error_id.setVisibility(View.GONE);

            }

            if (newUser_LastName.isEmpty()) {
                mLinearLayout_display_error_email_pass.setVisibility(View.VISIBLE);
                mLast_name_error_id.setVisibility(View.VISIBLE);

            } else {
                mLinearLayout_display_error_email_pass.setVisibility(View.GONE);
                mLast_name_error_id.setVisibility(View.GONE);

            }

            if (newUser_userName.isEmpty()) {
                mLinearLayout_display_error_email_pass.setVisibility(View.VISIBLE);
                mUserNameError.setVisibility(View.VISIBLE);

            } else {
                mLinearLayout_display_error_email_pass.setVisibility(View.GONE);
                mUserNameError.setVisibility(View.GONE);

            }

            if (newUser_Password.isEmpty()) {
                mLinearLayout_display_error_email_pass.setVisibility(View.VISIBLE);
                passError.setVisibility(View.VISIBLE);

            } else {
                mLinearLayout_display_error_email_pass.setVisibility(View.GONE);
                passError.setVisibility(View.GONE);
            }


            if (newUser_Password.length() < 9) {
                mLinearLayout_display_error_email_pass.setVisibility(View.VISIBLE);
                validPassError.setVisibility(View.VISIBLE);

            } else {
                mLinearLayout_display_error_email_pass.setVisibility(View.GONE);
                validPassError.setVisibility(View.GONE);
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };






}