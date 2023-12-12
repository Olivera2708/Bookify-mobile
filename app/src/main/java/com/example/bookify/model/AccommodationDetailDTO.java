package com.example.bookify.model;

import com.example.bookify.enumerations.Filter;
import com.google.gson.annotations.Expose;

import java.util.Collection;

public class AccommodationDetailDTO {
    @Expose
    private Long id;
    @Expose
    private String name;
    @Expose
    private String description;
    @Expose
    private float avgRating;
    @Expose
    private Collection<ReviewDTO> reviews;
    @Expose
    private Collection<Filter> filters;
    @Expose
    private Address address;
    @Expose
    private OwnerDTO owner;

    public AccommodationDetailDTO() {
    }

    public AccommodationDetailDTO(Long id, String name, String description, float avgRating, Collection<ReviewDTO> reviews, Collection<Filter> filters, Address address, OwnerDTO owner) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.avgRating = avgRating;
        this.reviews = reviews;
        this.filters = filters;
        this.address = address;
        this.owner = owner;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(float avgRating) {
        this.avgRating = avgRating;
    }

    public Collection<ReviewDTO> getReviews() {
        return reviews;
    }

    public void setReviews(Collection<ReviewDTO> reviews) {
        this.reviews = reviews;
    }

    public Collection<Filter> getFilters() {
        return filters;
    }

    public void setFilters(Collection<Filter> filters) {
        this.filters = filters;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public OwnerDTO getOwner() {
        return owner;
    }

    public void setOwner(OwnerDTO owner) {
        this.owner = owner;
    }
}
