package com.study.quizzapp.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ResultTest implements Serializable {
    @SerializedName("id")
    private long id;
    @SerializedName("score")
    private int score;
    @SerializedName("test")
    private Test test;
    @SerializedName("user")
    private User user;

    public ResultTest(long id, int score, Test test, User user) {
        this.id = id;
        this.score = score;
        this.test = test;
        this.user = user;
    }

    public ResultTest() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
