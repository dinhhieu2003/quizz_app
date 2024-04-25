package com.study.quizzapp.activity.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.study.quizzapp.R;

public class SignUpActivity extends AppCompatActivity {
    private EditText inputEmail, inputPassword;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        init();
        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignUpActivity.this, ResetPasswordActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DO SIGNUP
            }
        });
    }

    private void init() {
        btnSignIn =  findViewById(R.id.sign_in_button);
        btnSignUp =  findViewById(R.id.sign_up_button);
        inputEmail =  findViewById(R.id.email);
        inputPassword =  findViewById(R.id.password);
        btnResetPassword =  findViewById(R.id.btn_reset_password);
    }
}