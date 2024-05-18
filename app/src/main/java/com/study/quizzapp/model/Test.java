package com.study.quizzapp.model;

import java.io.Serializable;
import java.util.List;

public class Test implements Serializable {
    private long id;
    private String name;
    private List<Question> questionList;
    private List<ResultTest> resultList;
    private int time;

    public Test() {
    }

    public Test(long id, String name, List<Question> questionList, List<ResultTest> resultList, int time) {
        this.id = id;
        this.name = name;
        this.questionList = questionList;
        this.resultList = resultList;
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Question> getQuestionList() {
        return questionList;
    }

    public void setQuestionList(List<Question> questionList) {
        this.questionList = questionList;
    }

    public List<ResultTest> getResultList() {
        return resultList;
    }

    public void setResultList(List<ResultTest> resultList) {
        this.resultList = resultList;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}
