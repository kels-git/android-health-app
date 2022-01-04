package com.example.healthappdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    Button btnRegister, btnLogin;
    public static final String KEY_USER_EMAIL = "userLogin_email";
    public static final String KEY_USER_PASSWORD = "userLogin_password";
    Dialog dialogSecond;
    LottieAnimationView lottieLoaing;
    Button  CancelDialogLoad_btn, ProceedDialogLoad_btn;
    TextView   DialogMessage;
    LinearLayout mLayout_dialogTitle_holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = findViewById(R.id.Btn_go_to_login_page);
        btnRegister = findViewById(R.id.Btn_go_to_register_page);

        Paper.init(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navigate_login = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(navigate_login);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navigate_register = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(navigate_register);
            }
        });

        String userLogin_email = Paper.book().read(KEY_USER_EMAIL);
        String userLogin_password = Paper.book().read(KEY_USER_PASSWORD);

        if(userLogin_email != "" && userLogin_password != ""){
            if(!TextUtils.isEmpty(userLogin_email) && !TextUtils.isEmpty(userLogin_password) ){

                AllowAccess(userLogin_email,userLogin_password );
            }
        }

    }

    private void AllowAccess(String userLogin_email, String userLogin_password) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(userLogin_email, userLogin_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    FirebaseUser AppUser = FirebaseAuth.getInstance().getCurrentUser();
                    if (AppUser != null) {
                        FirebaseAuth.getInstance().getCurrentUser().getUid();
                        displayShowDialog();

                    }
                }

            }
        });
    }

    @SuppressLint({"SetTextI18n", "UseCompatLoadingForDrawables"})
    private void displayShowDialog() {

        dialogSecond = new Dialog(MainActivity.this);
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
        ProceedDialogLoad_btn.setHeight(60);

        ProceedDialogLoad_btn.setText("Logged-In,Please hold .....");
        dialogSecond.show();

        LottieTransformation();
    }

    private void LottieTransformation() {

        lottieLoaing.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                lottieLoaing.setRepeatCount(1);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

                dialogSecond.dismiss();
                Intent intent = new Intent(MainActivity.this, NewsFeedActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }
}