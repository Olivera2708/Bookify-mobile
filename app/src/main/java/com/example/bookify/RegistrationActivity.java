package com.example.bookify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        FragmentTransition.to(RegistrationFragmentAccountInfo.newInstance("AccInfo", "Account informations"),
                RegistrationActivity.this, false, R.id.registration);

    }
}