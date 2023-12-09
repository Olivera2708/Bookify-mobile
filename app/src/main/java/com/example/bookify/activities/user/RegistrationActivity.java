package com.example.bookify.activities.user;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.bookify.activities.LoginActivity;
import com.example.bookify.fragments.FragmentTransition;
import com.example.bookify.R;
import com.example.bookify.fragments.user.RegistrationFragmentAccountInfo;

public class RegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        FragmentTransition.to(RegistrationFragmentAccountInfo.newInstance("AccInfo", "Account informations"),
                RegistrationActivity.this, false, R.id.registration);
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }
}