package com.study.quizzapp.api;

import com.study.quizzapp.dto.response.LoginResponse;
import com.study.quizzapp.dto.response.SignupResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface AuthApi {
    @POST("/api/v1/auth/login")
    @Multipart
    Call<LoginResponse> login(@Part("email") RequestBody email, @Part("password") RequestBody password);

    @POST("/api/v1/auth/signup")
    @Multipart
    Call<SignupResponse> signup(@Part("email") RequestBody email, @Part("password") RequestBody password,
                                @Part("fname") RequestBody fname, @Part("age") RequestBody age);
}
