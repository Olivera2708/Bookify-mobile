package com.example.bookify.model;

import com.example.bookify.enumerations.AccommodationType;
import com.example.bookify.enumerations.Filter;
import com.google.gson.annotations.Expose;

import java.util.List;

public class FilterDTO {
    @Expose
    private List<Filter> filters;
    @Expose
    private List<AccommodationType> types;
    @Expose
    private float minPrice;
    @Expose
    private float maxPrice;

    public FilterDTO() {
    }

    public FilterDTO(List<Filter> filters, List<AccommodationType> types, float minPrice, float maxPrice) {
        this.filters = filters;
        this.types = types;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }

    public List<AccommodationType> getTypes() {
        return types;
    }

    public void setTypes(List<AccommodationType> types) {
        this.types = types;
    }

    public float getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(float minPrice) {
        this.minPrice = minPrice;
    }

    public float getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(float maxPrice) {
        this.maxPrice = maxPrice;
    }
}
