package com.example.bookify.model.user;

import com.example.bookify.model.Address;
import com.google.gson.annotations.Expose;

public class UserDetailsDTO {
    @Expose
    private Long id;
    @Expose
    private String email;
    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private boolean blocked;
    @Expose
    private String phone;
    @Expose
    private Address address;
    @Expose
    private Long imageId;

    public UserDetailsDTO(Long id, String email, String firstName, String lastName, boolean blocked, String phone, Address address, Long imageId) {
        this.id = id;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.blocked = blocked;
        this.phone = phone;
        this.address = address;
        this.imageId = imageId;
    }

    public UserDetailsDTO() {
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public String getPhone() {
        return phone;
    }

    public Address getAddress() {
        return address;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

}
