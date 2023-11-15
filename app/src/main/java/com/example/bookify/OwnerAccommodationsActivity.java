package com.example.bookify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class OwnerAccommodationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_accommodations);

        FloatingActionButton addAccommodation = findViewById(R.id.addAccommodationButton);

        addAccommodation.setOnClickListener(v -> {
            Intent intent = new Intent(OwnerAccommodationsActivity.this, AccommodationUpdateActivity.class);
            startActivity(intent);
        });
    }
}