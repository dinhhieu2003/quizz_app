package com.study.quizzapp.activity.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;

import com.study.quizzapp.R;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText inputEmail;
    private Button btnReset, btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        init();
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DO RESET
            }
        });

    }

    private void init() {
        inputEmail =  findViewById(R.id.email);
        btnReset =  findViewById(R.id.btn_reset_password);
        btnBack =  findViewById(R.id.btn_back);
    }
}