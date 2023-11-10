package com.example.bookify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AccommodationDetailsActivity extends AppCompatActivity {

    Button reservationDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodation_details);

        View reservationTile = findViewById(R.id.reservation);
        Button reservationDate = reservationTile.findViewById(R.id.editDate);
        reservationDate.setOnClickListener(new View.OnClickListener() {
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

                        reservationDate.setText(startDate + " - " + endDate);
                    }
                });

                materialDatePicker.show(getSupportFragmentManager(), "tag");
            }
        });
    }
}