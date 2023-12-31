package com.example.bookify.model;

import com.google.gson.annotations.Expose;

public class UserBasicDTO {
    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private String phone;
    @Expose
    private Long imageId;

    public UserBasicDTO() {}
    public UserBasicDTO(String firstName, String lastName, String phone, Long imageId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.imageId = imageId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }
}
