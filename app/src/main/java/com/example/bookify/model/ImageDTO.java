package com.example.bookify.model;

import android.graphics.Bitmap;
import android.provider.ContactsContract;

import com.google.gson.annotations.Expose;

import okhttp3.MultipartBody;

public class ImageDTO {
    @Expose
    private Bitmap bitmap;
    @Expose
    private MultipartBody.Part imagePart;

    public ImageDTO() { }

    public ImageDTO(Bitmap bitmap, MultipartBody.Part imagePart) {
        this.bitmap = bitmap;
        this.imagePart = imagePart;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public MultipartBody.Part getImagePart() {
        return imagePart;
    }

    public void setImagePart(MultipartBody.Part imagePart) {
        this.imagePart = imagePart;
    }
}
