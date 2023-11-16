package com.example.bookify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class OwnerAccommodationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_accommodations);
        NavigationBar.setNavigationBar(findViewById(R.id.bottom_navigaiton), this, R.id.navigation_accommodation);

        FloatingActionButton addAccommodation = findViewById(R.id.addAccommodationButton);

        addAccommodation.setOnClickListener(v -> {
            Intent intent = new Intent(OwnerAccommodationsActivity.this, AccommodationUpdateActivity.class);
            startActivity(intent);
        });

        Button reportButton = findViewById(R.id.reportButton);
        reportButton.setOnClickListener(v -> {
            Intent intent = new Intent(OwnerAccommodationsActivity.this, ReportsActivity.class);
            startActivity(intent);
        });
    }
}