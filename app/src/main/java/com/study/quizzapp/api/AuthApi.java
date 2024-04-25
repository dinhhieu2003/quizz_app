package com.study.quizzapp.api;

import com.study.quizzapp.dto.LoginRequest;
import com.study.quizzapp.dto.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
}
