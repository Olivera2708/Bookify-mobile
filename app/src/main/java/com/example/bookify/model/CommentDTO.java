package com.example.bookify.model;

import com.google.gson.annotations.Expose;

import java.util.Date;

public class CommentDTO {
    @Expose
    private Long id;
    @Expose
    private String name;
    @Expose
    private Date date;
    @Expose
    private String comment;
    @Expose
    private int rate;
    @Expose
    private Long guestId;
    @Expose
    private Long imageId;

    public CommentDTO() {}

    public CommentDTO(Long id, String name, Date date, String comment, int rate, Long guestId, Long imageId) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.comment = comment;
        this.rate = rate;
        this.guestId = guestId;
        this.imageId = imageId;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
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

    public Long getImageId() {
        return imageId;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }
}
