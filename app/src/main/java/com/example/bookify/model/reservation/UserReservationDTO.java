package com.example.bookify.model.reservation;

import com.google.gson.annotations.Expose;

public class UserReservationDTO {
    @Expose
    private Long id;
    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private double avgRating;
    @Expose
    private int cancellationTimes;

    public UserReservationDTO() {
    }

    public UserReservationDTO(Long id, String firstName, String lastName, double avgRating, int cancellationTimes) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.avgRating = avgRating;
        this.cancellationTimes = cancellationTimes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public int getCancellationTimes() {
        return cancellationTimes;
    }

    public void setCancellationTimes(int cancellationTimes) {
        this.cancellationTimes = cancellationTimes;
    }
}
