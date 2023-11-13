package com.example.bookify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Layout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.slider.RangeSlider;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ResultsActivity extends AppCompatActivity {

    FloatingActionButton filterButton;
    Button editDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        setBottomNavigation();
        View tile = findViewById(R.id.tile);
        Button details = tile.findViewById(R.id.details);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultsActivity.this, AccommodationDetailsActivity.class);
                startActivity(intent);
            }
        });

        tile = findViewById(R.id.tile2);
        details = tile.findViewById(R.id.details);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultsActivity.this, FavoritesActivity.class);
                startActivity(intent);
            }
        });

        tile = findViewById(R.id.tile3);
        details = tile.findViewById(R.id.details);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultsActivity.this, RequestsActivity.class);
                startActivity(intent);
            }
        });

        editDate = findViewById(R.id.editButton);
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

        filterButton = (FloatingActionButton) findViewById(R.id.filterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog();
            }
        });
    }

    private void showBottomDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filter);

        EditText minPrice = dialog.findViewById(R.id.minPrice);
        minPrice.setText("0", TextView.BufferType.EDITABLE);
        EditText maxPrice = dialog.findViewById(R.id.maxPrice);
        maxPrice.setText("100", TextView.BufferType.EDITABLE);

        RangeSlider priceSlider = dialog.findViewById(R.id.priceBar);
        priceSlider.addOnSliderTouchListener(new RangeSlider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull RangeSlider slider) {
                float valueMin = slider.getValues().get(0);
                minPrice.setText((int) valueMin);

                float valueMax = slider.getValues().get(0);
                maxPrice.setText((int) valueMax);
            }

            @Override
            public void onStopTrackingTouch(@NonNull RangeSlider slider) {

            }
        });



        String[] sort = new String[]{"Price lowest first", "Price highest first", "Name"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_item, sort);
        AutoCompleteTextView autoCompleteTextView = dialog.findViewById(R.id.filled_exposed);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //code when something is selected
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void setBottomNavigation() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigaiton);
        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navigation_home) {
                    Intent intent = new Intent(ResultsActivity.this, LandingActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.navigation_account) {
                    Intent intent = new Intent(ResultsActivity.this, AccountDetailsActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.navigation_reservations) {
                    Intent intent = new Intent(ResultsActivity.this, ResultsActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.navigation_favorites) {
                    Intent intent = new Intent(ResultsActivity.this, FavoritesActivity.class);
                    startActivity(intent);
                    finish();
                } else if (item.getItemId() == R.id.navigation_notifications) {
                    return true;
                }
                return false;
            }
        });
    }
}