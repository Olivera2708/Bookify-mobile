package com.example.bookify.model.review;

import com.example.bookify.enumerations.ReviewType;
import com.example.bookify.model.user.UserDTO;
import com.google.gson.annotations.Expose;

public class ReviewAdminViewDTO {
    @Expose
    private Long id;
    @Expose
    private int rate;
    @Expose
    private String comment;
    @Expose
    private String date;
    @Expose
    private boolean accepted;
    @Expose
    private boolean reported;
    @Expose
    private UserDTO guest;
    @Expose
    private ReviewType reviewType;

    public ReviewAdminViewDTO(Long id, int rate, String comment, String date, boolean accepted, boolean reported, UserDTO guest, ReviewType reviewType) {
        this.id = id;
        this.rate = rate;
        this.comment = comment;
        this.date = date;
        this.accepted = accepted;
        this.reported = reported;
        this.guest = guest;
        this.reviewType = reviewType;
    }

    public ReviewAdminViewDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

    public boolean isReported() {
        return reported;
    }

    public void setReported(boolean reported) {
        this.reported = reported;
    }

    public UserDTO getGuest() {
        return guest;
    }

    public void setGuest(UserDTO guest) {
        this.guest = guest;
    }

    public ReviewType getReviewType() {
        return reviewType;
    }

    public void setReviewType(ReviewType reviewType) {
        this.reviewType = reviewType;
    }
}
