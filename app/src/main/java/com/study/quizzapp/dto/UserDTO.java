package com.study.quizzapp.dto;

import com.study.quizzapp.model.Role;

public class UserDTO {
    private long id;
    private String fname;
    private int age;
    private String email;
    private Role role;

    public UserDTO() {
    }

    public UserDTO(long id, String fname, int age, String email, Role role) {
        this.id = id;
        this.fname = fname;
        this.age = age;
        this.email = email;
        this.role = role;
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

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
