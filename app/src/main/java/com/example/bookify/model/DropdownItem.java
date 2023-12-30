package com.example.bookify.model;

public class DropdownItem {
    private String displayString;
    private Long id;

    public DropdownItem(String displayString, Long id) {
        this.displayString = displayString;
        this.id = id;
    }

    public String getDisplayString() {
        return displayString;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return displayString; // This is what will be displayed in the dropdown
    }
}