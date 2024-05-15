package com.study.quizzapp.api;

import com.study.quizzapp.model.Test;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TestApi {
    @GET("/api/v1/tests")
    Call<List<Test>> getAll();
}
