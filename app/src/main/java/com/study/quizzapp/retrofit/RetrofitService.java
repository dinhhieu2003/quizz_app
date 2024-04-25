package com.study.quizzapp.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
    private Retrofit retrofit;

    public RetrofitService() {
        initializeRetrofit();
    }
    private void initializeRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("http://192.168.0.103:8080")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
