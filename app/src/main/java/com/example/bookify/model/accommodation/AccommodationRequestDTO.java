package com.example.bookify.model.accommodation;

import com.example.bookify.enumerations.AccommodationStatusRequest;
import com.example.bookify.model.user.UserDTO;
import com.google.gson.annotations.Expose;

public class AccommodationRequestDTO {
    @Expose
    private Long id;
    @Expose
    private String name;
    @Expose
    private UserDTO owner;
    @Expose
    private AccommodationStatusRequest statusRequest;
    @Expose
    private Long imageId;

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

    public UserDTO getOwner() {
        return owner;
    }

    public void setOwner(UserDTO owner) {
        this.owner = owner;
    }

    public AccommodationStatusRequest getStatusRequest() {
        return statusRequest;
    }

    public void setStatusRequest(AccommodationStatusRequest statusRequest) {
        this.statusRequest = statusRequest;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public AccommodationRequestDTO() {
    }

    public AccommodationRequestDTO(Long id, String name, UserDTO owner, AccommodationStatusRequest statusRequest, Long imageId) {
        this.id = id;
        this.name = name;
        this.owner = owner;
        this.statusRequest = statusRequest;
        this.imageId = imageId;
    }
}
