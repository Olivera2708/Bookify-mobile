package com.example.bookify.model.user;

import com.google.gson.annotations.Expose;

public class UserCredentialsDTO {

    @Expose
    private String email;

    @Expose
    private String password;

    public UserCredentialsDTO(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserCredentialsDTO() {
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
