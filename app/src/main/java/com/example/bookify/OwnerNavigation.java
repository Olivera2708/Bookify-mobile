package com.example.bookify;

import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class OwnerNavigation implements NavigationBarView.OnItemSelectedListener {
    private final AppCompatActivity currentActivity;
    public OwnerNavigation(AppCompatActivity currentActivity){
        this.currentActivity = currentActivity;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.navigation_home) {
            Intent intent = new Intent(currentActivity, LandingActivity.class);
            currentActivity.startActivity(intent);
            currentActivity.overridePendingTransition(0,0);
            currentActivity.finish();
        } else if (item.getItemId() == R.id.navigation_account) {
            Intent intent = new Intent(currentActivity, AccountDetailsActivity.class);
            currentActivity.startActivity(intent);
            currentActivity.overridePendingTransition(0,0);
            currentActivity.finish();
        } else if (item.getItemId() == R.id.navigation_accommodation) {
            Intent intent = new Intent(currentActivity, OwnerAccommodationsActivity.class);
            currentActivity.startActivity(intent);
            currentActivity.overridePendingTransition(0,0);
            currentActivity.finish();
        } else if (item.getItemId() == R.id.navigation_reservations) {
            Intent intent = new Intent(currentActivity, RequestsActivity.class);
            currentActivity.startActivity(intent);
            currentActivity.overridePendingTransition(0,0);
            currentActivity.finish();
        } else if (item.getItemId() == R.id.navigation_notifications) {
            Intent intent = new Intent(currentActivity, NotificationActivity.class);
            currentActivity.startActivity(intent);
            currentActivity.overridePendingTransition(0,0);
            currentActivity.finish();
        }
        return false;
    }
}
