package com.example.bookify.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;

public class Address {
    @Expose
    private String country;
    @Expose
    private String city;
    @Expose
    private String address;
    @Expose
    private String zipCode;

    public Address(String country, String city, String address, String zipCode) {
        this.country = country;
        this.city = city;
        this.address = address;
        this.zipCode = zipCode;
    }

    public Address() {
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @NonNull
    @Override
    public String toString(){
        return this.address + ", " + this.city + ", " + this.country;
    }
}
