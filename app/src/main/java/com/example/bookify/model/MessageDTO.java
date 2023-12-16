package com.example.bookify.model;

import com.google.gson.annotations.Expose;

public class MessageDTO {
    @Expose
    private String token;

    public MessageDTO() {    }

    public MessageDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
