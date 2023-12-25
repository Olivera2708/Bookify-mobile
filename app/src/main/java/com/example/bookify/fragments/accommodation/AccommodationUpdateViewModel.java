package com.example.bookify.fragments.accommodation;

import android.graphics.Bitmap;
import android.media.Image;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookify.model.Address;
import com.example.bookify.model.ImageMobileDTO;

import java.util.Collection;
import java.util.List;

import okhttp3.MultipartBody;

public class AccommodationUpdateViewModel extends ViewModel {
    private final MutableLiveData<Boolean> isEditMode = new MutableLiveData<>();
    private final MutableLiveData<Long> accommodationId = new MutableLiveData<>();
    private final MutableLiveData<String> propertyName = new MutableLiveData<>();
    private final MutableLiveData<String> description = new MutableLiveData<>();
    private final MutableLiveData<Address> address = new MutableLiveData<>();
    private final MutableLiveData<List<String>> amenities = new MutableLiveData<>();
    private final MutableLiveData<List<MultipartBody.Part>> images = new MutableLiveData<>();
    private final MutableLiveData<Collection<ImageMobileDTO>> storedImages = new MutableLiveData<>();
    private final MutableLiveData<Integer> minGuests = new MutableLiveData<>();
    private final MutableLiveData<Integer> maxGuests = new MutableLiveData<>();
    private final MutableLiveData<String> type = new MutableLiveData<>();
    private final MutableLiveData<Boolean> manual = new MutableLiveData<>();
    private final MutableLiveData<Integer> cancellationDeadline = new MutableLiveData<>();
    private final MutableLiveData<String> pricePer = new MutableLiveData<>();

    public MutableLiveData<Long> getAccommodationId() {
        return accommodationId;
    }

    public MutableLiveData<Boolean> getIsEditMode() {
        return isEditMode;
    }

    public void setIsEditMode(Boolean value) {
        isEditMode.setValue(value);
    }

    public void setAccommodationId(Long value) {
        accommodationId.setValue(value);
    }

    public MutableLiveData<String> getPropertyName() {
        return propertyName;
    }

    public void setPropertyName(String value) {
        propertyName.setValue(value);
    }

    public MutableLiveData<String> getDescription() {
        return description;
    }

    public void setDescription(String value) {
        description.setValue(value);
    }

    public MutableLiveData<Address> getAddress() {
        return address;
    }

    public void setAddress(Address value) {
        address.setValue(value);
    }

    public MutableLiveData<List<String>> getAmenities() {
        return amenities;
    }

    public void setAmenities(List<String> value) {
        amenities.setValue(value);
    }

    public MutableLiveData<List<MultipartBody.Part>> getImages() {
        return images;
    }

    public void setImages(List<MultipartBody.Part> value) {
        images.setValue(value);
    }

    public MutableLiveData<Collection<ImageMobileDTO>> getStoredImages() {
        return storedImages;
    }

    public void setStoredImages(Collection<ImageMobileDTO> value) {
        storedImages.setValue(value);
    }

    public MutableLiveData<Integer> getMinGuests() {
        return minGuests;
    }

    public void setMinGuests(Integer value) {
        minGuests.setValue(value);
    }

    public MutableLiveData<Integer> getMaxGuests() {
        return maxGuests;
    }

    public void setMaxGuests(Integer value) {
        maxGuests.setValue(value);
    }

    public MutableLiveData<String> getType() {
        return type;
    }

    public void setType(String value) {
        type.setValue(value);
    }

    public MutableLiveData<Boolean> getManual() {
        return manual;
    }

    public void setManual(Boolean value) {
        manual.setValue(value);
    }

    public MutableLiveData<Integer> getCancellationDeadline() {
        return cancellationDeadline;
    }

    public void setCancellationDeadline(Integer value) {
        cancellationDeadline.setValue(value);
    }

    public MutableLiveData<String> getPricePer() {
        return pricePer;
    }

    public void setPricePer(String value) {
        pricePer.setValue(value);
    }

}
