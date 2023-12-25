package com.example.bookify.model;

import com.google.gson.annotations.Expose;

import java.util.Date;

public class PricelistItemDTO {
    @Expose
    private Date startDate;
    @Expose
    private Date endDate;
    @Expose
    private double price;

    public PricelistItemDTO() {    }

    public PricelistItemDTO(Date startDate, Date endDate, double price) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.price = price;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public double getPrice() {
        return price;
    }
}
