package com.example.healthappdemo;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UserProfileEditActivity extends AppCompatActivity {


    EditText firstName_EditProfile, lastName_EditProfile;
    Spinner editOccupation, editVaccination_Status;
    LinearLayout bt_UpdateProfile, mErrorContainer;
    ProgressBar mProgressBar;

    TextView mFirstNameErrorEdit, mLastNameErrorEdit, mOccupationErrorEdit, mVaccinationStatus;
    RelativeLayout mUploadPic;
    ImageView mPicUpdateContainer;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri mImageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDataBaseRef;
    private StorageTask mUploadTask;
    private static final String TAG = "TAG";
    int progress_Status = 3;
    Handler mHandler = new Handler();
    String USER_ID;
    String ELIGIBILITY_CHECK;
    String ELIGIBILITY_CHECK_TWO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_edit);

        firstName_EditProfile = findViewById(R.id.user_edit_FirstName);
        lastName_EditProfile = findViewById(R.id.user_edit_LastName);
        editOccupation = findViewById(R.id.SP_User_edit_occupation);
        editVaccination_Status = findViewById(R.id.SP_User_edit_vaccination);
        bt_UpdateProfile = findViewById(R.id.LinearLayout_EditProfile_Submit);
        mProgressBar = findViewById(R.id.progressBarID_UPDATE);
        mPicUpdateContainer = findViewById(R.id.Image_User_Profile_Edit);
        mUploadPic = findViewById(R.id.Image_upload_camera);

        mFirstNameErrorEdit = findViewById(R.id.first_name_edit_error_id);
        mLastNameErrorEdit = findViewById(R.id.last_name_edit_error_id);
        mOccupationErrorEdit = findViewById(R.id.Occupation_error_Edit_id);
        mVaccinationStatus = findViewById(R.id.Vaccination_error_id);
        mErrorContainer = findViewById(R.id.LinearLayout_EditProfile_error_details);

        ArrayAdapter<CharSequence> adapterOccupation =
                ArrayAdapter.createFromResource(this, R.array.array_occupation, R.layout.spinner_custom_layout);
        adapterOccupation.setDropDownViewResource(R.layout.spinner_custom_drop_down);
        editOccupation.setAdapter(adapterOccupation);

        ArrayAdapter<CharSequence> adapterVaccination =
                ArrayAdapter.createFromResource(this, R.array.user_vaccination_status, R.layout.spinner_custom_layout);
        adapterVaccination.setDropDownViewResource(R.layout.spinner_custom_drop_down);
        editVaccination_Status.setAdapter(adapterVaccination);

        //Store the image
        mStorageRef = FirebaseStorage.getInstance().getReference("ProfileUpload");

        bt_UpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String EditUser_FirstName = firstName_EditProfile.getText().toString().trim();
                String EditUser_LastName = lastName_EditProfile.getText().toString().trim();
                String EditUser_Occupation = editOccupation.getSelectedItem().toString();
                String EditUser_Vaccination = editVaccination_Status.getSelectedItem().toString();

                if (mUploadTask != null) {
                    Toast.makeText(UserProfileEditActivity.this, "Upload in progress..", Toast.LENGTH_SHORT).show();
                    return;

                }

                if (TextUtils.isEmpty(EditUser_FirstName) && TextUtils.isEmpty(EditUser_LastName)
                        && editOccupation.getSelectedItemPosition() == 0 && editVaccination_Status.getSelectedItemPosition() == 0) {
                    mErrorContainer.setVisibility(View.VISIBLE);
                    mFirstNameErrorEdit.setVisibility(View.VISIBLE);
                    mLastNameErrorEdit.setVisibility(View.VISIBLE);
                    mOccupationErrorEdit.setVisibility(View.VISIBLE);
                    mVaccinationStatus.setVisibility(View.VISIBLE);
                    return;
                }

                if (TextUtils.isEmpty(EditUser_FirstName) && TextUtils.isEmpty(EditUser_LastName)
                        && editOccupation.getSelectedItemPosition() > 0 && editVaccination_Status.getSelectedItemPosition() > 0) {
                    mErrorContainer.setVisibility(View.VISIBLE);
                    mFirstNameErrorEdit.setVisibility(View.VISIBLE);
                    mLastNameErrorEdit.setVisibility(View.VISIBLE);
                    mOccupationErrorEdit.setVisibility(View.GONE);
                    mVaccinationStatus.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(EditUser_FirstName) && TextUtils.isEmpty(EditUser_LastName)
                        && editOccupation.getSelectedItemPosition() > 0 && editVaccination_Status.getSelectedItemPosition() == 0) {
                    mErrorContainer.setVisibility(View.VISIBLE);
                    mFirstNameErrorEdit.setVisibility(View.VISIBLE);
                    mLastNameErrorEdit.setVisibility(View.VISIBLE);
                    mOccupationErrorEdit.setVisibility(View.GONE);
                    mVaccinationStatus.setVisibility(View.VISIBLE);
                    return;
                }

                if (TextUtils.isEmpty(EditUser_FirstName) && TextUtils.isEmpty(EditUser_LastName)
                        && editOccupation.getSelectedItemPosition() == 0 && editVaccination_Status.getSelectedItemPosition() > 0) {
                    mErrorContainer.setVisibility(View.VISIBLE);
                    mFirstNameErrorEdit.setVisibility(View.VISIBLE);
                    mLastNameErrorEdit.setVisibility(View.VISIBLE);
                    mOccupationErrorEdit.setVisibility(View.VISIBLE);
                    mVaccinationStatus.setVisibility(View.GONE);
                    return;
                }

                if (TextUtils.isEmpty(EditUser_FirstName) && !TextUtils.isEmpty(EditUser_LastName)
                        && editOccupation.getSelectedItemPosition() > 0 && editVaccination_Status.getSelectedItemPosition() > 0) {
                    mErrorContainer.setVisibility(View.VISIBLE);
                    mFirstNameErrorEdit.setVisibility(View.VISIBLE);
                    mLastNameErrorEdit.setVisibility(View.GONE);
                    mOccupationErrorEdit.setVisibility(View.GONE);
                    mVaccinationStatus.setVisibility(View.GONE);
                    return;
                }

                if (!TextUtils.isEmpty(EditUser_FirstName) && TextUtils.isEmpty(EditUser_LastName)
                        && editOccupation.getSelectedItemPosition() > 0 && editVaccination_Status.getSelectedItemPosition() > 0) {
                    mErrorContainer.setVisibility(View.VISIBLE);
                    mFirstNameErrorEdit.setVisibility(View.GONE);
                    mLastNameErrorEdit.setVisibility(View.VISIBLE);
                    mOccupationErrorEdit.setVisibility(View.GONE);
                    mVaccinationStatus.setVisibility(View.GONE);
                    return;
                }


                if (!TextUtils.isEmpty(EditUser_FirstName) && TextUtils.isEmpty(EditUser_LastName)
                        && editOccupation.getSelectedItemPosition() == 0 && editVaccination_Status.getSelectedItemPosition() == 0) {
                    mErrorContainer.setVisibility(View.VISIBLE);
                    mFirstNameErrorEdit.setVisibility(View.GONE);
                    mLastNameErrorEdit.setVisibility(View.VISIBLE);
                    mOccupationErrorEdit.setVisibility(View.VISIBLE);
                    mVaccinationStatus.setVisibility(View.VISIBLE);
                    return;
                }

                if (TextUtils.isEmpty(EditUser_FirstName) && !TextUtils.isEmpty(EditUser_LastName)
                        && editOccupation.getSelectedItemPosition() == 0 && editVaccination_Status.getSelectedItemPosition() == 0) {
                    mErrorContainer.setVisibility(View.VISIBLE);
                    mFirstNameErrorEdit.setVisibility(View.VISIBLE);
                    mLastNameErrorEdit.setVisibility(View.GONE);
                    mOccupationErrorEdit.setVisibility(View.VISIBLE);
                    mVaccinationStatus.setVisibility(View.VISIBLE);
                    return;
                }
                if (TextUtils.isEmpty(EditUser_FirstName) && !TextUtils.isEmpty(EditUser_LastName)
                        && editOccupation.getSelectedItemPosition() == 0 && editVaccination_Status.getSelectedItemPosition() > 0) {
                    mErrorContainer.setVisibility(View.VISIBLE);
                    mFirstNameErrorEdit.setVisibility(View.VISIBLE);
                    mLastNameErrorEdit.setVisibility(View.GONE);
                    mOccupationErrorEdit.setVisibility(View.VISIBLE);
                    mVaccinationStatus.setVisibility(View.GONE);
                    return;
                }

                if (!TextUtils.isEmpty(EditUser_FirstName) && TextUtils.isEmpty(EditUser_LastName)
                        && editOccupation.getSelectedItemPosition() == 0 && editVaccination_Status.getSelectedItemPosition() > 0) {
                    mErrorContainer.setVisibility(View.VISIBLE);
                    mFirstNameErrorEdit.setVisibility(View.GONE);
                    mLastNameErrorEdit.setVisibility(View.VISIBLE);
                    mOccupationErrorEdit.setVisibility(View.VISIBLE);
                    mVaccinationStatus.setVisibility(View.GONE);
                    return;
                }


                if (!TextUtils.isEmpty(EditUser_FirstName) && !TextUtils.isEmpty(EditUser_LastName)
                        && editOccupation.getSelectedItemPosition() == 0 && editVaccination_Status.getSelectedItemPosition() > 0) {
                    mErrorContainer.setVisibility(View.VISIBLE);
                    mFirstNameErrorEdit.setVisibility(View.GONE);
                    mLastNameErrorEdit.setVisibility(View.GONE);
                    mOccupationErrorEdit.setVisibility(View.VISIBLE);
                    mVaccinationStatus.setVisibility(View.GONE);
                    return;
                }

                if (!TextUtils.isEmpty(EditUser_FirstName) && !TextUtils.isEmpty(EditUser_LastName)
                        && editOccupation.getSelectedItemPosition() == 0 && editVaccination_Status.getSelectedItemPosition() == 0) {
                    mErrorContainer.setVisibility(View.VISIBLE);
                    mFirstNameErrorEdit.setVisibility(View.GONE);
                    mLastNameErrorEdit.setVisibility(View.GONE);
                    mOccupationErrorEdit.setVisibility(View.VISIBLE);
                    mVaccinationStatus.setVisibility(View.VISIBLE);
                    return;
                }

                if (!TextUtils.isEmpty(EditUser_FirstName) && !TextUtils.isEmpty(EditUser_LastName)
                        && editOccupation.getSelectedItemPosition() > 0 && editVaccination_Status.getSelectedItemPosition() == 0) {
                    mErrorContainer.setVisibility(View.VISIBLE);
                    mFirstNameErrorEdit.setVisibility(View.GONE);
                    mLastNameErrorEdit.setVisibility(View.GONE);
                    mOccupationErrorEdit.setVisibility(View.GONE);
                    mVaccinationStatus.setVisibility(View.VISIBLE);
                    return;
                }


                if (!TextUtils.isEmpty(EditUser_FirstName) && !TextUtils.isEmpty(EditUser_LastName)
                        && editOccupation.getSelectedItemPosition() > 0 && editVaccination_Status.getSelectedItemPosition() > 0
                        && mImageUri != null) {
                    mErrorContainer.setVisibility(View.GONE);
                    mFirstNameErrorEdit.setVisibility(View.GONE);
                    mLastNameErrorEdit.setVisibility(View.GONE);
                    mOccupationErrorEdit.setVisibility(View.GONE);
                    mVaccinationStatus.setVisibility(View.GONE);

                    new Thread(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            while (progress_Status < 15) {
                                progress_Status++;
                                android.os.SystemClock.sleep(100);
                                mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        mProgressBar.setVisibility(View.VISIBLE);
                                        mProgressBar.setProgress(progress_Status);
                                    }
                                });
                            }
                            if (progress_Status == 15) {
                                updateCurrentUserProfile(EditUser_FirstName, EditUser_LastName);
                                updateCurrentUserJob_AND_Vaccination(EditUser_Occupation, EditUser_Vaccination);
                            }

                            Intent intent = new Intent(UserProfileEditActivity.this, UserProfileActivity.class);
                            startActivity(intent);

                        }
                    }).start();
                }
            }
        });


        _upLoadProfilePic();


    }



    private void _upLoadProfilePic() {
        mUploadPic.setOnClickListener(new View.OnClickListener() {
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
                    .into(mPicUpdateContainer);


        }
    }


    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));


    }

    public void updateCurrentUserJob_AND_Vaccination(String editUser_occupation, String editUser_vaccination) {
        FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
        if (AppUser != null && mImageUri != null) {
            USER_ID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            ELIGIBILITY_CHECK = FirebaseAuth.getInstance().getCurrentUser().getUid();
            ELIGIBILITY_CHECK_TWO = FirebaseAuth.getInstance().getCurrentUser().getUid();


            DocumentReference documentReference = FirebaseFirestore.getInstance()
                    .collection("REGISTERED USERS").document(USER_ID)
                    .collection("ELIGIBILITY CHECK ONE").document(ELIGIBILITY_CHECK)
                    .collection("ELIGIBILITY CHECK TWO").document(ELIGIBILITY_CHECK_TWO);

            documentReference.update("Occupation", editUser_occupation, "Covid Status", editUser_vaccination);
           // documentReference.update("Covid Status", editUser_vaccination);
        }

    }

    public void updateCurrentUserProfile(String editUser_firstName, String editUser_lastName) {
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

                                    DocumentReference documentReference = FirebaseFirestore.getInstance()
                                            .collection("REGISTERED USERS").document(USER_ID);


                                    documentReference.update("mFirstName", editUser_firstName);
                                    documentReference.update("mLastName", editUser_lastName);
                                    documentReference.update("mPhotoUrl", photoProfileLink);


                                }
                            }
                        });
                    }
                }


            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserProfileEditActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


}