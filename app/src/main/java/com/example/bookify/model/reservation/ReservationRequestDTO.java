package com.example.bookify.model.reservation;

import com.google.gson.annotations.Expose;

public class ReservationRequestDTO {
    @Expose
    private long created;
    @Expose
    private long start;
    @Expose
    private long end;
    @Expose
    private int guestNumber;
    @Expose
    private double price;

    public ReservationRequestDTO() {
    }

    public ReservationRequestDTO(long created, long start, long end, int guestNumber, double price) {
        this.created = created;
        this.start = start;
        this.end = end;
        this.guestNumber = guestNumber;
        this.price = price;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public int getGuestNumber() {
        return guestNumber;
    }

    public void setGuestNumber(int guestNumber) {
        this.guestNumber = guestNumber;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
