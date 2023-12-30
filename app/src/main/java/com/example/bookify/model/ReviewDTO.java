package com.example.bookify.model;

import com.example.bookify.enumerations.ReviewType;
import com.google.gson.annotations.Expose;

import java.util.Date;

public class ReviewDTO {
    @Expose
    private String comment;
    @Expose
    private int rate;
    @Expose
    private Long guestId;

    public ReviewDTO() {}

    public ReviewDTO(String comment, int rate, Long guestId) {
        this.comment = comment;
        this.rate = rate;
        this.guestId = guestId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public Long getGuestId() {
        return guestId;
    }

    public void setGuestId(Long guestId) {
        this.guestId = guestId;
    }
}
