package com.example.healthappdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rey.material.widget.CheckBox;

import io.paperdb.Paper;

public class LoginActivity extends AppCompatActivity {


    EditText mEmail, mPassword;
    ProgressBar mProgressBar;
    LinearLayout mLinearLayoutErrorContainer, mbtLogin;
    TextView mEmailError, mPasswordError, mEmailAndPasswordError;
    int progress_status = 2;
    Handler mHandler = new Handler();
    CheckBox mCheckBox;
    public static final String KEY_USER_EMAIL = "userLogin_email";
    public static final String KEY_USER_PASSWORD = "userLogin_password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mEmail = findViewById(R.id.et_login_email);
        mPassword = findViewById(R.id.et_login_password);
        mbtLogin = findViewById(R.id.LinearLayout_Login);
        mProgressBar = findViewById(R.id.progressBarID_login);
        mCheckBox = findViewById(R.id.Remember_Me_CheckBox);

        mEmailError = findViewById(R.id.email_error_login_id);
        mPasswordError = findViewById(R.id.password_error_id);
        mEmailAndPasswordError = findViewById(R.id.email_password_error_id_login);
        mLinearLayoutErrorContainer = findViewById(R.id.LinearLayout_display_error_email_pass_login);

        Paper.init(this);

        mbtLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userLogin_Email = mEmail.getText().toString().trim();
                String userLogin_Password = mPassword.getText().toString().trim();

                if (TextUtils.isEmpty(userLogin_Email) && TextUtils.isEmpty(userLogin_Password)) {
                    mLinearLayoutErrorContainer.setVisibility(View.VISIBLE);
                    mEmailError.setVisibility(View.VISIBLE);
                    mPasswordError.setVisibility(View.VISIBLE);
                    mEmailAndPasswordError.setVisibility(View.GONE);
                    return;

                } else if (TextUtils.isEmpty(userLogin_Email) && !TextUtils.isEmpty(userLogin_Password)) {
                    mLinearLayoutErrorContainer.setVisibility(View.VISIBLE);
                    mEmailError.setVisibility(View.VISIBLE);
                    mPasswordError.setVisibility(View.GONE);
                    mEmailAndPasswordError.setVisibility(View.GONE);
                    return;

                } else if (!TextUtils.isEmpty(userLogin_Email) && TextUtils.isEmpty(userLogin_Password)) {
                    mLinearLayoutErrorContainer.setVisibility(View.VISIBLE);
                    mEmailError.setVisibility(View.GONE);
                    mPasswordError.setVisibility(View.VISIBLE);
                    mEmailAndPasswordError.setVisibility(View.GONE);
                    return;

                }
                Login_to_account(userLogin_Email, userLogin_Password);

            }
        });

        mEmail.addTextChangedListener(LoginTextWatcher);
        mPassword.addTextChangedListener(LoginTextWatcher);


    }

    private void Login_to_account(String userLogin_email, String userLogin_password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progress_status < 10) {
                    progress_status++;
                    android.os.SystemClock.sleep(50);
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mProgressBar.setVisibility(View.VISIBLE);
                            mProgressBar.setProgress(progress_status);
                        }
                    });
                }
                if (progress_status == 10) {

                    if(mCheckBox.isChecked()){
                        Paper.book().write(KEY_USER_EMAIL, userLogin_email );
                        Paper.book().write(KEY_USER_PASSWORD, userLogin_password );
                    }

                    FirebaseAuth.getInstance().signInWithEmailAndPassword(userLogin_email, userLogin_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
                                if (AppUser != null) {
                                    FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    Intent intent = new Intent(LoginActivity.this, NewsFeedActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            } else {
                                mLinearLayoutErrorContainer.setVisibility(View.VISIBLE);
                                mEmailAndPasswordError.setVisibility(View.VISIBLE);
                                mProgressBar.setVisibility(View.GONE);

                            }

                        }
                    });
                }

            }

        }).start();


    }

    private final TextWatcher LoginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            final String userLogin_Email = mEmail.getText().toString().trim();
            final String userLogin_Password = mPassword.getText().toString().trim();



            if (userLogin_Email.isEmpty()) {
                mLinearLayoutErrorContainer.setVisibility(View.VISIBLE);
                mEmailError.setVisibility(View.VISIBLE);

            } else {
                mLinearLayoutErrorContainer.setVisibility(View.GONE);
                mEmailError.setVisibility(View.GONE);
                mEmailAndPasswordError.setVisibility(View.GONE);
            }


            if (userLogin_Password.isEmpty()) {
                mLinearLayoutErrorContainer.setVisibility(View.VISIBLE);
                mPasswordError.setVisibility(View.VISIBLE);


            } else {
                mLinearLayoutErrorContainer.setVisibility(View.GONE);
                mPasswordError.setVisibility(View.GONE);
                mEmailAndPasswordError.setVisibility(View.GONE);
            }

            if (userLogin_Password.length() < 9) {
                mLinearLayoutErrorContainer.setVisibility(View.VISIBLE);
                mPasswordError.setVisibility(View.VISIBLE);

            } else {
                mLinearLayoutErrorContainer.setVisibility(View.GONE);
                mPasswordError.setVisibility(View.GONE);
                mEmailAndPasswordError.setVisibility(View.GONE);
            }

        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };
}