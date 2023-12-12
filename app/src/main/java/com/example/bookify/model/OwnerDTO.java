package com.example.bookify.model;

import com.google.gson.annotations.Expose;

public class OwnerDTO {
    @Expose
    private Long id;
    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private String phone;
    @Expose
    private float avgRating;
    @Expose
    private Long imageId;

    public OwnerDTO() {
    }

    public OwnerDTO(Long id, String firstName, String lastName, String phone, float avgRating, Long imageId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.avgRating = avgRating;
        this.imageId = imageId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public float getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(float avgRating) {
        this.avgRating = avgRating;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }
}
