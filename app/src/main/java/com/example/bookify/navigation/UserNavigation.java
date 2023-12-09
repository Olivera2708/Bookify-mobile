package com.example.bookify.navigation;

import android.content.Intent;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookify.R;
import com.example.bookify.activities.LandingActivity;
import com.example.bookify.activities.LoginActivity;
import com.google.android.material.navigation.NavigationBarView;

public class UserNavigation implements NavigationBarView.OnItemSelectedListener {
    private final AppCompatActivity currentActivity;
    public UserNavigation(AppCompatActivity currentActivity){
        this.currentActivity = currentActivity;
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.navigation_home) {
            Intent intent = new Intent(currentActivity, LandingActivity.class);
            currentActivity.startActivity(intent);
            currentActivity.overridePendingTransition(0,0);
            currentActivity.finish();
        } else if (item.getItemId() == R.id.navigation_login) {
            Intent intent = new Intent(currentActivity, LoginActivity.class);
            currentActivity.startActivity(intent);
            currentActivity.overridePendingTransition(0,0);
            currentActivity.finish();
        }
        return false;
    }
}
