package com.example.bookify.model.user;

import com.google.gson.annotations.Expose;

public class UserJWT {
    @Expose
    private String accessToken;
    @Expose
    private Long expiresIn;

    public UserJWT(String accessToken, Long expiresIn) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }

    public UserJWT() {
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }
    
}
