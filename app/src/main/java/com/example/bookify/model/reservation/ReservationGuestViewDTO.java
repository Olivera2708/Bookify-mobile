package com.example.bookify.model.reservation;

import com.example.bookify.enumerations.Status;
import com.google.gson.annotations.Expose;

import java.time.LocalDate;

public class ReservationGuestViewDTO {
    @Expose
    private Long id;
    @Expose
    private String created;
    @Expose
    private String start;
    @Expose
    private String end;
    @Expose
    private String cancellationDate;
    @Expose
    private int guestNumber;
    @Expose
    private double price;
    @Expose
    private Status status;
    @Expose
    private UserReservationDTO user;
    @Expose
    private Long accommodationId;
    @Expose
    private String accommodationName;
    @Expose
    private double avgRating;
    @Expose
    private Long imageId;

    public ReservationGuestViewDTO() {
    }

    public ReservationGuestViewDTO(Long id, String created, String start, String end, String cancellationDate, int guestNumber, double price, Status status, UserReservationDTO user, Long accommodationId, String accommodationName, double avgRating, Long imageId) {
        this.id = id;
        this.created = created;
        this.start = start;
        this.end = end;
        this.cancellationDate = cancellationDate;
        this.guestNumber = guestNumber;
        this.price = price;
        this.status = status;
        this.user = user;
        this.accommodationId = accommodationId;
        this.accommodationName = accommodationName;
        this.avgRating = avgRating;
        this.imageId = imageId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getCancellationDate() {
        return cancellationDate;
    }

    public void setCancellationDate(String cancellationDate) {
        this.cancellationDate = cancellationDate;
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public UserReservationDTO getUser() {
        return user;
    }

    public void setUser(UserReservationDTO user) {
        this.user = user;
    }

    public Long getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(Long accommodationId) {
        this.accommodationId = accommodationId;
    }

    public String getAccommodationName() {
        return accommodationName;
    }

    public void setAccommodationName(String accommodationName) {
        this.accommodationName = accommodationName;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }
}
