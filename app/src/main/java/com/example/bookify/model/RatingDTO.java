package com.example.bookify.model;

import com.google.gson.annotations.Expose;

public class RatingDTO {
    @Expose
    private double avgRating;
    @Expose
    private int fiveStars;
    @Expose
    private int fourStars;
    @Expose
    private int threeStars;
    @Expose
    private int twoStars;
    @Expose
    private int oneStars;

    public RatingDTO() {}
    public RatingDTO(double avgRating, int fiveStars, int fourStars, int threeStars, int twoStars, int oneStars) {
        this.avgRating = avgRating;
        this.fiveStars = fiveStars;
        this.fourStars = fourStars;
        this.threeStars = threeStars;
        this.twoStars = twoStars;
        this.oneStars = oneStars;
    }

    public double getAvgRating() {
        return avgRating;
    }

    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }

    public int getFiveStars() {
        return fiveStars;
    }

    public void setFiveStars(int fiveStars) {
        this.fiveStars = fiveStars;
    }

    public int getFourStars() {
        return fourStars;
    }

    public void setFourStars(int fourStars) {
        this.fourStars = fourStars;
    }

    public int getThreeStars() {
        return threeStars;
    }

    public void setThreeStars(int threeStars) {
        this.threeStars = threeStars;
    }

    public int getTwoStars() {
        return twoStars;
    }

    public void setTwoStars(int twoStars) {
        this.twoStars = twoStars;
    }

    public int getOneStars() {
        return oneStars;
    }

    public void setOneStars(int oneStars) {
        this.oneStars = oneStars;
    }
}
