package com.example.bookify.model;

import com.google.gson.annotations.Expose;

public class ImageMobileDTO {
    @Expose
    private String data;
    @Expose
    private Long id;

    public ImageMobileDTO() {}

    public ImageMobileDTO(String file, Long id) {
        this.data = file;
        this.id = id;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
