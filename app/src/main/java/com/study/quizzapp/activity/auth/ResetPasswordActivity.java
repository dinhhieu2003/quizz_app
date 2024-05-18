package com.study.quizzapp.activity.auth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;

import com.study.quizzapp.R;
import com.study.quizzapp.api.AuthApi;
import com.study.quizzapp.dto.response.TokenResponse;
import com.study.quizzapp.retrofit.RetrofitService;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText inputEmail;
    private Button btnReset, btnBack;
    private AuthApi restMethods;
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
                resetPassword();
            }
        });

    }

    private void resetPassword() {
        final String email = inputEmail.getText().toString();
        if(TextUtils.isEmpty(email)) {
            inputEmail.setError("Please enter your email");
            inputEmail.requestFocus();
            return;
        }
        RequestBody emailRequestBody = RequestBody.create(MediaType.parse("text/plain"), email);
        restMethods = RetrofitService.getRetrofit().create(AuthApi.class);
        restMethods.forgotpassword(emailRequestBody).enqueue(new Callback<TokenResponse>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                TokenResponse tokenResponse = response.body();
                if(tokenResponse.isError()) {
                    Toasty.error(getApplicationContext(), tokenResponse.getMessage(), Toasty.LENGTH_SHORT).show();
                } else {
                    Toasty.normal(getApplicationContext(), "Đã gửi tới email",Toasty.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable throwable) {
                Toasty.error(getApplicationContext(), throwable.getMessage(), Toasty.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {
        inputEmail =  findViewById(R.id.email);
        btnReset =  findViewById(R.id.btn_reset_password);
        btnBack =  findViewById(R.id.btn_back);
    }
}