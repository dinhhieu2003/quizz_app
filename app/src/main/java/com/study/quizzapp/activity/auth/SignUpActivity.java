package com.study.quizzapp.activity.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.study.quizzapp.R;
import com.study.quizzapp.api.AuthApi;
import com.study.quizzapp.dto.response.SignupResponse;
import com.study.quizzapp.retrofit.RetrofitService;


import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SignUpActivity extends AppCompatActivity {
    private EditText inputEmail, inputPassword, inputName, inputAge;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private AuthApi restMethods;
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
                signup();
            }
        });
    }

    private void signup() {
        final String fname = inputName.getText().toString();
        final String password = inputPassword.getText().toString();
        final String email = inputEmail.getText().toString();
        final String age = inputAge.getText().toString();

        if(TextUtils.isEmpty(fname)) {
            inputName.setError("Please enter your username");
            inputName.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(password)) {
            inputPassword.setError("Please enter your password");
            inputPassword.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(email)) {
            inputEmail.setError("Please enter your email");
            inputEmail.requestFocus();
        }

        if(TextUtils.isEmpty(age)) {
            inputAge.setError("Please enter your age");
            inputAge.requestFocus();
        }

        // Tạo RequestBody cho username
        RequestBody emailRequestBody = RequestBody.create(MediaType.parse("text/plain"), email);
        // Tạo RequestBody cho password
        RequestBody passwordRequestBody = RequestBody.create(MediaType.parse("text/plain"), password);
        RequestBody fnameRequestBody = RequestBody.create(MediaType.parse("text/plain"), fname);
        RequestBody ageRequestBody = RequestBody.create(MediaType.parse("text/plain"), age);

        Log.i("fname", "Response: " + fnameRequestBody);
        restMethods = RetrofitService.getRetrofit().create(AuthApi.class);
        restMethods.signup(emailRequestBody, passwordRequestBody, fnameRequestBody, ageRequestBody).enqueue(new Callback<SignupResponse>() {
            @Override
            public void onResponse(@NonNull Call<SignupResponse> call, @NonNull Response<SignupResponse> response) {

                //Response was successfull
                Log.i("signup success", "Response: " + response.body());

                finish();
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(@NonNull Call<SignupResponse> call, @NonNull Throwable t) {
                t.printStackTrace();

                //Response failed
                Log.e("signup failed", "Response: " + t.getMessage());

                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        btnSignIn =  findViewById(R.id.sign_in_button);
        btnSignUp =  findViewById(R.id.sign_up_button);
        inputEmail =  findViewById(R.id.email);
        inputPassword =  findViewById(R.id.password);
        inputName = findViewById(R.id.name);
        inputAge = findViewById(R.id.age);
        btnResetPassword =  findViewById(R.id.btn_reset_password);
    }
}