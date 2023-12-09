package com.example.bookify.navigation;

import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookify.R;
import com.example.bookify.activities.user.AccountDetailsActivity;
import com.example.bookify.activities.user.AllUsersActivity;
import com.example.bookify.activities.FeedbackAdminActivity;
import com.example.bookify.activities.LandingActivity;
import com.example.bookify.activities.accommodation.AccommodationRequestsActivity;
import com.google.android.material.navigation.NavigationBarView;

public class AdminNavigation implements NavigationBarView.OnItemSelectedListener {
    private final AppCompatActivity currentActivity;
    public AdminNavigation(AppCompatActivity currentActivity){
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
        } else if (item.getItemId() == R.id.navigation_all_users) {
            Intent intent = new Intent(currentActivity, AllUsersActivity.class);
            currentActivity.startActivity(intent);
            currentActivity.overridePendingTransition(0,0);
            currentActivity.finish();
        } else if (item.getItemId() == R.id.navigation_accommodation_requests) {
            Intent intent = new Intent(currentActivity, AccommodationRequestsActivity.class);
            currentActivity.startActivity(intent);
            currentActivity.overridePendingTransition(0,0);
            currentActivity.finish();
        } else if (item.getItemId() == R.id.navigation_feedback) {
            Intent intent = new Intent(currentActivity, FeedbackAdminActivity.class);
            currentActivity.startActivity(intent);
            currentActivity.overridePendingTransition(0,0);
            currentActivity.finish();
        }
        return false;
    }
}
