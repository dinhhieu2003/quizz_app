package com.study.quizzapp.api;

import com.study.quizzapp.dto.request.ListQuestionTestDTO;
import com.study.quizzapp.model.Question;
import com.study.quizzapp.model.Test;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
public interface QuestionApi {
    @POST("/api/v1/questions")
    Call<List<Question>> addAllQuestion(@Body ListQuestionTestDTO listQuestionDTO);
}
