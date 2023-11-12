package com.example.bookify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class AccountDetailsActivity extends AppCompatActivity {

    private static final int[] USER_FIELDS = {R.id.first_name, R.id.last_name, R.id.address, R.id.phone_number};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);
        setEditButtonAction();
        setSaveButtonAction();
        setBottomNavigation();
        setAccountPictureChange();
        setChangePasswordAction();
    }

    private void setAccountPictureChange() {
        ImageView accImage = (ImageView) findViewById(R.id.account_image);
        accImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                CharSequence charSequence = "To-do";
                Toast toast = Toast.makeText(context, charSequence, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private void setEditButtonAction() {
        findViewById(R.id.btn_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int id : USER_FIELDS) {
                    findViewById(id).setEnabled(true);
                }
            }
        });
    }

    private void setSaveButtonAction() {
        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int id : USER_FIELDS) {
                    findViewById(id).setEnabled(false);
                }
            }
        });
    }
    private void setBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigaiton);
        bottomNavigationView.setSelectedItemId(R.id.navigation_account);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_home) {
                    Intent intent = new Intent(AccountDetailsActivity.this, LandingActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.navigation_account) {
                    return false;
                } else if (item.getItemId() == R.id.navigation_reservations) {
                    Intent intent = new Intent(AccountDetailsActivity.this, RequestsActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.navigation_favorites) {
                    Intent intent = new Intent(AccountDetailsActivity.this, FavoritesActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.navigation_notifications) {
                   return true;
                }
                return false;
            }
        });
    }

    private void setChangePasswordAction() {
        findViewById(R.id.btnEditPassword).setOnClickListener(v -> {
            ShowDialog(R.layout.change_password);
        });
    }

    private void ShowDialog(int id){
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(id);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);
    }
}