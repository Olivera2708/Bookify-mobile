package com.example.bookify.model;

import com.example.bookify.enumerations.AccommodationType;
import com.example.bookify.enumerations.PricePer;
import com.google.gson.annotations.Expose;

public class AccommodationBasicDTO {
    @Expose
    private Long id;
    @Expose
    private String name;
    @Expose
    private Address address;
    @Expose
    private float avgRating;
    @Expose
    private float totalPrice;
    @Expose
    public PricePer pricePer;
    @Expose
    public float priceOne;
    @Expose
    private Long imageId;
    @Expose
    private AccommodationType type;
    @Expose
    private String description;

    public AccommodationBasicDTO(Long id, String name, Address address, float avgRating, float totalPrice, PricePer pricePer, float priceOne, Long imageId, AccommodationType type, String description) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.avgRating = avgRating;
        this.totalPrice = totalPrice;
        this.pricePer = pricePer;
        this.priceOne = priceOne;
        this.imageId = imageId;
        this.type = type;
        this.description = description;
    }

    public AccommodationBasicDTO() {
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

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public float getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(float avgRating) {
        this.avgRating = avgRating;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public PricePer getPricePer() {
        return pricePer;
    }

    public void setPricePer(PricePer pricePer) {
        this.pricePer = pricePer;
    }

    public float getPriceOne() {
        return priceOne;
    }

    public void setPriceOne(float priceOne) {
        this.priceOne = priceOne;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public AccommodationType getType() {
        return type;
    }

    public void setType(AccommodationType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
