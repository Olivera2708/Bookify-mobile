package com.example.bookify.model;

import com.google.gson.annotations.Expose;

import java.util.List;

public class AccommodationInsertDTO {
    @Expose
    String name;
    @Expose
    String description;
    @Expose
    int minGuest;
    @Expose
    int maxGuest;
    @Expose
    int cancellationDeadline;
    @Expose
    boolean manual;
    @Expose
    List<String> filters;
    @Expose
    String accommodationType;
    @Expose
    String pricePer;
    @Expose
    Address address;

    public AccommodationInsertDTO() {    }

    public AccommodationInsertDTO(String name, String description, int minGuest, int maxGuest, int cancellationDeadline, boolean manual, List<String> filters, String accommodationType, String pricePer, Address address) {
        this.name = name;
        this.description = description;
        this.minGuest = minGuest;
        this.maxGuest = maxGuest;
        this.cancellationDeadline = cancellationDeadline;
        this.manual = manual;
        this.filters = filters;
        this.accommodationType = accommodationType;
        this.pricePer = pricePer;
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMinGuest(int minGuest) {
        this.minGuest = minGuest;
    }

    public void setMaxGuest(int maxGuest) {
        this.maxGuest = maxGuest;
    }

    public void setCancellationDeadline(int cancellationDeadline) {
        this.cancellationDeadline = cancellationDeadline;
    }

    public void setManual(boolean manual) {
        this.manual = manual;
    }

    public void setFilters(List<String> filters) {
        this.filters = filters;
    }

    public void setAccommodationType(String accommodationType) {
        this.accommodationType = accommodationType;
    }

    public void setPricePer(String pricePer) {
        this.pricePer = pricePer;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getMinGuest() {
        return minGuest;
    }

    public int getMaxGuest() {
        return maxGuest;
    }

    public int getCancellationDeadline() {
        return cancellationDeadline;
    }

    public boolean isManual() {
        return manual;
    }

    public List<String> getFilters() {
        return filters;
    }

    public String getAccommodationType() {
        return accommodationType;
    }

    public String getPricePer() {
        return pricePer;
    }

    public Address getAddress() {
        return address;
    }
}
