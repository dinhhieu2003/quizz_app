package com.study.quizzapp.dto.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.study.quizzapp.dto.UserDTO;

import java.io.Serializable;

public class SignupResponse implements Serializable {

    @SerializedName("error")
    @Expose
    private Boolean error;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("user")
    @Expose
    private UserDTO user;


    public SignupResponse(Boolean error, String message, UserDTO user) {
        this.error = error;
        this.message = message;
        this.user = user;
    }

    public SignupResponse(){

    }

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }
}
