package com.example.bookify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.example.bookify.databinding.AccommodationOwnerViewBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.navigation.NavigationBarView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class LandingActivity extends AppCompatActivity {
    Button editDate;
    Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        setBottomNavigation();
        showAccommodations();

        View searchLayout = findViewById(R.id.searchLayout);
        editDate = searchLayout.findViewById(R.id.dateInput);
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker<Pair<Long, Long>> materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(new Pair<>(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                )).build();

                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long, Long> selection) {
                        String startDate = new SimpleDateFormat("dd.MM.yyyy.", Locale.getDefault()).format(new Date(selection.first));
                        String endDate = new SimpleDateFormat("dd.MM.yyyy.", Locale.getDefault()).format(new Date(selection.second));

                        editDate.setText(startDate + " - " + endDate);
                    }
                });

                materialDatePicker.show(getSupportFragmentManager(), "tag");
            }
        });

        search = searchLayout.findViewById(R.id.searchButton);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LandingActivity.this, ResultsActivity.class);
                startActivity(intent);
            }
        });

        View accoLayout = findViewById(R.id.acco1);
        Button details = accoLayout.findViewById(R.id.details);
        details.setOnClickListener(v -> {
            ShowDialog(R.layout.new_comment);
        });

        accoLayout = findViewById(R.id.acco2);
        details = accoLayout.findViewById(R.id.details);
        details.setOnClickListener(v -> {
            ShowDialog(R.layout.report);
        });

        accoLayout = findViewById(R.id.acco3);
        details = accoLayout.findViewById(R.id.details);
        details.setOnClickListener(v -> {
            Intent intent = new Intent(LandingActivity.this, AccommodationUpdateActivity.class);
            startActivity(intent);
        });

        accoLayout = findViewById(R.id.acco4);
        details = accoLayout.findViewById(R.id.details);
        details.setOnClickListener(v -> {
            Intent intent = new Intent(LandingActivity.this, ReportsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

    }

    private void setBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigaiton);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_home) {
                    return false;
                } else if (item.getItemId() == R.id.navigation_account) {
                    Intent intent = new Intent(LandingActivity.this, AccountDetailsActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.navigation_reservations) {
                    Intent intent = new Intent(LandingActivity.this, RequestsActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.navigation_favorites) {
                    Intent intent = new Intent(LandingActivity.this, FavoritesActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.navigation_notifications) {
                    return true;
                }
                return false;
            }
        });
    }

    private void ShowDialog(int id) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(id);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);
    }

    private void showAccommodations(){
        findViewById(R.id.loc1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingActivity.this, OwnerAccommodationsActivity.class);
                startActivity(intent);
            }
        });
    }
}