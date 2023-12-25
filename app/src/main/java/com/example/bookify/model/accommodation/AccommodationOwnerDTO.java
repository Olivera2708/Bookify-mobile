package com.example.bookify.model.accommodation;

import com.example.bookify.enumerations.AccommodationStatusRequest;
import com.example.bookify.enumerations.AccommodationType;
import com.example.bookify.model.Address;
import com.google.gson.annotations.Expose;

public class AccommodationOwnerDTO {

    @Expose
    private Long id;
    @Expose
    private String name;
    @Expose
    private AccommodationType accommodationType;
    @Expose
    private float avgRating;
    @Expose
    private AccommodationStatusRequest statusRequest;
    @Expose
    private Address address;
    @Expose
    private Long imageId;

    public AccommodationOwnerDTO(Long id, String name, AccommodationType accommodationType, float avgRating, AccommodationStatusRequest statusRequest, Address address, Long imageId) {
        this.id = id;
        this.name = name;
        this.accommodationType = accommodationType;
        this.avgRating = avgRating;
        this.statusRequest = statusRequest;
        this.address = address;
        this.imageId = imageId;
    }

    public AccommodationOwnerDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AccommodationType getAccommodationType() {
        return accommodationType;
    }

    public void setAccommodationType(AccommodationType accommodationType) {
        this.accommodationType = accommodationType;
    }

    public float getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(float avgRating) {
        this.avgRating = avgRating;
    }

    public AccommodationStatusRequest getStatusRequest() {
        return statusRequest;
    }

    public void setStatusRequest(AccommodationStatusRequest statusRequest) {
        this.statusRequest = statusRequest;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }
}
