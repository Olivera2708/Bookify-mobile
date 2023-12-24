package com.example.bookify.model.reservation;

import com.example.bookify.enumerations.Status;
import com.google.gson.annotations.Expose;

import java.time.LocalDate;
import java.util.Date;

public class ReservationDTO {
    @Expose
    private Long id;
    @Expose
    private Date created;
    @Expose
    private Date start;
    @Expose
    private Date end;
    @Expose
    private int guestNumber;
    @Expose
    private Status status;

    public ReservationDTO() {
    }

    public ReservationDTO(Long id, Date created, Date start, Date end, int guestNumber, Status status) {
        this.id = id;
        this.created = created;
        this.start = start;
        this.end = end;
        this.guestNumber = guestNumber;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public int getGuestNumber() {
        return guestNumber;
    }

    public void setGuestNumber(int guestNumber) {
        this.guestNumber = guestNumber;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
