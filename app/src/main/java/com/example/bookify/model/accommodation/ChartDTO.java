package com.example.bookify.model.accommodation;

public class ChartDTO {
    String name;
    int numberOfReservations;
    double profitOfAccommodation;

    public ChartDTO() {
    }

    public ChartDTO(String name, int numberOfReservations, double profitOfAccommodation) {
        this.name = name;
        this.numberOfReservations = numberOfReservations;
        this.profitOfAccommodation = profitOfAccommodation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberOfReservations() {
        return numberOfReservations;
    }

    public void setNumberOfReservations(int numberOfReservations) {
        this.numberOfReservations = numberOfReservations;
    }

    public double getProfitOfAccommodation() {
        return profitOfAccommodation;
    }

    public void setProfitOfAccommodation(double profitOfAccommodation) {
        this.profitOfAccommodation = profitOfAccommodation;
    }
}
