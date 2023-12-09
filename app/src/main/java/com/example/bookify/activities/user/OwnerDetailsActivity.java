package com.example.bookify.activities.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.bookify.navigation.NavigationBar;
import com.example.bookify.R;

public class OwnerDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_details);
        NavigationBar.setNavigationBar(findViewById(R.id.bottom_navigaiton), this, R.id.navigation_home);
    }
}