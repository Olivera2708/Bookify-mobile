package com.example.bookify.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class SearchResponseDTO {
    @Expose
    List<AccommodationBasicDTO> accommodations;
    @Expose
    int results;
    @Expose
    float minPrice;

    @Expose
    float maxPrice;

    public SearchResponseDTO(List<AccommodationBasicDTO> accommodations, int results, float minPrice, float maxPrice) {
        this.accommodations = accommodations;
        this.results = results;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
    }

    public SearchResponseDTO() {
    }

    public List<AccommodationBasicDTO> getAccommodations() {
        return accommodations;
    }

    public void setAccommodations(List<AccommodationBasicDTO> accommodations) {
        this.accommodations = accommodations;
    }

    public int getResults() {
        return results;
    }

    public void setResults(int results) {
        this.results = results;
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
