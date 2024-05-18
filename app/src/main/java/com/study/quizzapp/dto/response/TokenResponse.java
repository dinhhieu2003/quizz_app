package com.study.quizzapp.dto.response;

public class TokenResponse {
    private boolean error;
    private String message;
    private String data;

    public TokenResponse(boolean error, String message, String data) {
        this.error = error;
        this.message = message;
        this.data = data;
    }

    public TokenResponse() {
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
