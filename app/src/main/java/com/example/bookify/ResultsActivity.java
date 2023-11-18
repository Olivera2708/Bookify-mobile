package com.example.bookify;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
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
        NavigationBar.setNavigationBar(findViewById(R.id.bottom_navigaiton), this, R.id.navigation_home);

        View tile = findViewById(R.id.tile);
        Button details = tile.findViewById(R.id.details);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultsActivity.this, AccommodationDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        tile = findViewById(R.id.tile2);
        details = tile.findViewById(R.id.details);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultsActivity.this, AccommodationDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

        tile = findViewById(R.id.tile3);
        details = tile.findViewById(R.id.details);
        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ResultsActivity.this, AccommodationDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
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

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(ResultsActivity.this, LandingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void showBottomDialog() {
        final BottomSheetDialog dialog = new BottomSheetDialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filter);

        RangeSlider priceSlider = dialog.findViewById(R.id.priceBar);
        EditText minPrice = dialog.findViewById(R.id.minPrice);
        minPrice.setText("0", TextView.BufferType.EDITABLE);
        minPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    if (Integer.parseInt(editable.toString()) > 1000 || editable.toString().equals("-")){
                        minPrice.setText(editable.toString().substring(0, minPrice.getText().length() - 1));
                    }
                    else {
                        float minValue = Float.valueOf(editable.toString());
                        priceSlider.setValues(minValue, priceSlider.getValues().get(1));
                        minPrice.setSelection(minPrice.getText().length());
                    }
                }
            }
        });

        EditText maxPrice = dialog.findViewById(R.id.maxPrice);
        maxPrice.setText("1000", TextView.BufferType.EDITABLE);
        maxPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    if (Integer.parseInt(editable.toString()) > 1000 || editable.toString().equals("-")){
                        maxPrice.setText(editable.toString().substring(0, maxPrice.getText().length() - 1));
                    }
                    else {
                        float maxValue = Float.valueOf(editable.toString());
                        priceSlider.setValues(priceSlider.getValues().get(0), maxValue);
                        maxPrice.setSelection(maxPrice.getText().length());
                    }
                }
            }
        });

        priceSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                int valueMin = Math.round(slider.getValues().get(0));
                minPrice.setText(Integer.toString(valueMin));

                int valueMax = Math.round(slider.getValues().get(1));
                maxPrice.setText(Integer.toString(valueMax));
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

        CheckBox hotel = dialog.findViewById(R.id.hotel);
        CheckBox apartment = dialog.findViewById(R.id.apartment);
        CheckBox freeWiFi = dialog.findViewById(R.id.freeWiFi);
        CheckBox airConditioning = dialog.findViewById(R.id.airConditioning);
        CheckBox terrace = dialog.findViewById(R.id.terrace);
        CheckBox swimmingPool = dialog.findViewById(R.id.swimmingPool);
        CheckBox bar = dialog.findViewById(R.id.bar);
        CheckBox sauna = dialog.findViewById(R.id.sauna);
        CheckBox luggageStorage = dialog.findViewById(R.id.luggageStorage);
        CheckBox lunch = dialog.findViewById(R.id.lunch);
        CheckBox airportShuttle = dialog.findViewById(R.id.airportShuttle);
        CheckBox wheelchair = dialog.findViewById(R.id.wheelchair);
        CheckBox non_smoking = dialog.findViewById(R.id.non_smoking);
        CheckBox freeParking = dialog.findViewById(R.id.freeParking);
        CheckBox familyRooms = dialog.findViewById(R.id.familyRooms);
        CheckBox garden = dialog.findViewById(R.id.garden);
        CheckBox frontDesk = dialog.findViewById(R.id.frontDesk);
        CheckBox jacuzzi = dialog.findViewById(R.id.jacuzzi);
        CheckBox heating = dialog.findViewById(R.id.heating);
        CheckBox breakfast = dialog.findViewById(R.id.breakfast);
        CheckBox dinner = dialog.findViewById(R.id.dinner);
        CheckBox privateBathroom = dialog.findViewById(R.id.privateBathroom);
        CheckBox depositBox = dialog.findViewById(R.id.depositBox);
        CheckBox cityCenter = dialog.findViewById(R.id.cityCenter);
        CheckBox room = dialog.findViewById(R.id.room);


        Button remove = dialog.findViewById(R.id.remove);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hotel.setChecked(false);
                apartment.setChecked(false);
                freeWiFi.setChecked(false);
                airConditioning.setChecked(false);
                terrace.setChecked(false);
                swimmingPool.setChecked(false);
                bar.setChecked(false);
                sauna.setChecked(false);
                luggageStorage.setChecked(false);
                lunch.setChecked(false);
                airportShuttle.setChecked(false);
                wheelchair.setChecked(false);
                non_smoking.setChecked(false);
                freeParking.setChecked(false);
                familyRooms.setChecked(false);
                garden.setChecked(false);
                frontDesk.setChecked(false);
                jacuzzi.setChecked(false);
                heating.setChecked(false);
                breakfast.setChecked(false);
                dinner.setChecked(false);
                privateBathroom.setChecked(false);
                depositBox.setChecked(false);
                cityCenter.setChecked(false);
                room.setChecked(false);
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}