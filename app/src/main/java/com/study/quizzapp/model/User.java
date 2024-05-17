package com.study.quizzapp.model;

import java.util.List;

public class User {
    private long id;
    private String fname;
    private int age;
    private String email;
    private String password;
    private Role role;
    private List<ResultTest> resultList;

    public User(long id, String fname, int age, String email, String password, Role role) {
        this.id = id;
        this.fname = fname;
        this.age = age;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(long id, String fname, int age, String email, String password, Role role, List<ResultTest> resultList) {
        this.id = id;
        this.fname = fname;
        this.age = age;
        this.email = email;
        this.password = password;
        this.role = role;
        this.resultList = resultList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public List<ResultTest> getResultList() {
        return resultList;
    }

    public void setResultList(List<ResultTest> resultList) {
        this.resultList = resultList;
    }
}
