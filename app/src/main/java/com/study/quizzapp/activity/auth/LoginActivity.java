package com.study.quizzapp.activity.auth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.study.quizzapp.R;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private Button btnSignup, btnLogin, btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DO LOGIN
            }
        });

    }

    private void init() {
        setContentView(R.layout.activity_login);

//        Toolbar toolbar =  findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        inputEmail =  findViewById(R.id.email);
        inputPassword =  findViewById(R.id.password);
        btnSignup =  findViewById(R.id.btn_signup);
        btnLogin =  findViewById(R.id.btn_login);
        btnReset =  findViewById(R.id.btn_reset_password);
    }
}