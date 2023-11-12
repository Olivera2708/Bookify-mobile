package com.example.bookify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class FavoritesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        setBottomNavigation();
    }
    private void setBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigaiton);
        bottomNavigationView.setSelectedItemId(R.id.navigation_favorites);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_home) {
                    Intent intent = new Intent(FavoritesActivity.this, LandingActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.navigation_account) {
                    Intent intent = new Intent(FavoritesActivity.this, AccountDetailsActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.navigation_reservations) {
                    Intent intent = new Intent(FavoritesActivity.this, RequestsActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.navigation_favorites) {
                    return false;
                } else if (item.getItemId() == R.id.navigation_notifications) {
                    return true;
                }
                return false;
            }
        });
    }
}