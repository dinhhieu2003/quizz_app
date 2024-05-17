package com.study.quizzapp.api;

import com.study.quizzapp.model.ResultTest;
import com.study.quizzapp.model.Test;
import com.study.quizzapp.model.User;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ResultApi {
    @POST("/api/v1/results")
    Call<ResultTest> addResultTest(@Body ResultTest resultTest);

    @GET("/api/v1/results")
    Call<List<ResultTest>> getAllResult();

    @GET("/api/v1/results/{id}")
    Call<List<ResultTest>> getAllResultByUserId(@Path("id") long id);

    @GET("/api/v1/results/test/{id}")
    Call<List<ResultTest>> getAllResultByTestId(@Path("id") long id);
}
