package com.study.quizzapp.dto.request;

import com.study.quizzapp.model.Question;
import com.study.quizzapp.model.Test;

import java.io.Serializable;
import java.util.List;

public class ListQuestionTestDTO implements Serializable {
    private List<Question> listQuestion;
    private Test test;

    public ListQuestionTestDTO(List<Question> listQuestion, Test test) {
        this.listQuestion = listQuestion;
        this.test = test;
    }

    public ListQuestionTestDTO() {
    }

    public List<Question> getListQuestion() {
        return listQuestion;
    }

    public void setListQuestion(List<Question> listQuestion) {
        this.listQuestion = listQuestion;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }
}
