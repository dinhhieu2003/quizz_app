package com.study.quizzapp.activity.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.study.quizzapp.R;
import com.study.quizzapp.activity.home.MainActivity;
import com.study.quizzapp.api.AuthApi;
import com.study.quizzapp.dto.UserDTO;
import com.study.quizzapp.dto.response.LoginResponse;
import com.study.quizzapp.model.User;
import com.study.quizzapp.retrofit.RetrofitService;
import com.study.quizzapp.sharedpref.SharedPrefManager;

import es.dmoral.toasty.Toasty;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private Button btnSignup, btnLogin, btnReset;
    private ProgressBar progressBar;
    private AuthApi restMethods;
    private String TAG = LoginActivity.class.getSimpleName();

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
                login();
            }
        });

    }

    private void login() {
        final String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();

        if(TextUtils.isEmpty(email)) {
            inputEmail.setError("Please enter your email");
            inputEmail.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(password)) {
            inputPassword.setError("Please enter your password");
            inputPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        // Tạo RequestBody cho username
        RequestBody emailRequestBody = RequestBody.create(MediaType.parse("text/plain"), email);

        // Tạo RequestBody cho password
        RequestBody passwordRequestBody = RequestBody.create(MediaType.parse("text/plain"), password);

        Log.d("email", emailRequestBody.toString());
        Log.d("password", passwordRequestBody.toString());

        restMethods = RetrofitService.getRetrofit().create(AuthApi.class);
        restMethods.login(emailRequestBody,  passwordRequestBody).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                assert response.body() != null;;
                if(response.body().getError() == true) {
                    Toasty.error(getApplicationContext(), response.body().getMessage(), Toasty.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                } else {
                    UserDTO userDTO = response.body().getUser();
                    Log.d("Success login", response.body().getMessage().toString());
                    User user = new User(userDTO.getId(), userDTO.getFname(), userDTO.getAge(), userDTO.getEmail(),"",
                            userDTO.getRole());
                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                    progressBar.setVisibility(View.GONE);
                    finish();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                //Response failed
                Log.e("login failed", "Response: " + t.getMessage());
                //hideLoading();

                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_SHORT).show();
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
        progressBar = findViewById(R.id.progressBar_login);
        progressBar.setVisibility(View.GONE);
    }
}