package com.example.bookify.fragments.user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegistrationViewModel extends ViewModel {
    private final MutableLiveData<String> email = new MutableLiveData<>();
    private final MutableLiveData<String> password = new MutableLiveData<>();
    private final MutableLiveData<String> confirmPassword = new MutableLiveData<>();
    private final MutableLiveData<String> firstName = new MutableLiveData<>();
    private final MutableLiveData<String> lastName = new MutableLiveData<>();
    private final MutableLiveData<String> phone = new MutableLiveData<>();
    private final MutableLiveData<String> role = new MutableLiveData<>();

    public void setEmail(String value) {
        email.setValue(value);
    }

    public LiveData<String> getEmail(){
        return email;
    }

    public void setPassword(String value) {
        password.setValue(value);
    }

    public LiveData<String> getPassword(){
        return password;
    }

    public void setConfirmPassword(String value) {
        confirmPassword.setValue(value);
    }

    public LiveData<String> getConfirmPassword(){
        return confirmPassword;
    }

    public void setFirstName(String value) {
        firstName.setValue(value);
    }

    public LiveData<String> getFirstName(){
        return firstName;
    }

    public void setLastName(String value) {
        lastName.setValue(value);
    }

    public LiveData<String> getLastName(){
        return lastName;
    }

    public void setPhone(String value) {
        phone.setValue(value);
    }

    public LiveData<String> getPhone(){
        return phone;
    }

    public void setRole(String value) {
        role.setValue(value);
    }

    public LiveData<String> getRole(){
        return role;
    }
}
