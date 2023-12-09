package com.example.bookify.navigation;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookify.R;
import com.example.bookify.navigation.AdminNavigation;
import com.example.bookify.navigation.GuestNavigation;
import com.example.bookify.navigation.OwnerNavigation;
import com.example.bookify.navigation.UserNavigation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public abstract class NavigationBar {
    public static void setNavigationBar(BottomNavigationView navigationView, AppCompatActivity context, int selectedItem) {
        navigationView.getMenu().clear();
        navigationView.setOnItemSelectedListener(null);
        switch (getUserType(context)) {
            case "owner":
                navigationView.inflateMenu(R.menu.bottom_nav_menu_owner);
                navigationView.setSelectedItemId(selectedItem);
                navigationView.setOnItemSelectedListener(new OwnerNavigation(context));
                break;
            case "guest":
                navigationView.inflateMenu(R.menu.bottom_nav_menu_guest);
                navigationView.setSelectedItemId(selectedItem);
                navigationView.setOnItemSelectedListener(new GuestNavigation(context));
                break;
            case "admin":
                navigationView.inflateMenu(R.menu.bottom_nav_menu_admin);
                navigationView.setSelectedItemId(selectedItem);
                navigationView.setOnItemSelectedListener(new AdminNavigation(context));
                break;
            default:
                navigationView.inflateMenu(R.menu.bottom_nav_menu_user);
                navigationView.setSelectedItemId(selectedItem);
                navigationView.setOnItemSelectedListener(new UserNavigation(context));
                break;
        }

    }

    private static String getUserType(Context c) {
        SharedPreferences sharedPreferences = c.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        return sharedPreferences.getString("userType", "none");
    }
}
