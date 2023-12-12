package com.example.bookify.activities;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.bookify.activities.accommodation.ResultsActivity;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.model.SearchResponseDTO;
import com.example.bookify.navigation.NavigationBar;
import com.example.bookify.R;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LandingActivity extends AppCompatActivity {
    Button editDate;
    Button search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        NavigationBar.setNavigationBar(findViewById(R.id.bottom_navigaiton), this, R.id.navigation_home);

        View searchLayout = findViewById(R.id.searchLayout);
        editDate = searchLayout.findViewById(R.id.dateInput);
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker<Pair<Long, Long>> materialDatePicker = MaterialDatePicker.Builder.dateRangePicker()
                        .setSelection(new Pair<>(
                        MaterialDatePicker.todayInUtcMilliseconds() + 24 * 60 * 60 * 1000,
                        MaterialDatePicker.todayInUtcMilliseconds() + 6 * 24 * 60 * 60 * 1000
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
                EditText location = searchLayout.findViewById(R.id.locationInput);
                Button dates = searchLayout.findViewById(R.id.dateInput);
                EditText persons = searchLayout.findViewById(R.id.personInput);

                SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy.");
                Date start = null;
                Date end = null;
                try {
                    start = format.parse(dates.getText().toString().split(" - ")[0]);
                    end = format.parse(dates.getText().toString().split(" - ")[1]);
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }

                if (String.valueOf(dates.getText()).equals("") || String.valueOf(persons.getText()).equals("") || start.before(new Date())
                        || Integer.valueOf(persons.getText().toString()) < 1 || start.equals(end))
                    Toast.makeText(LandingActivity.this, "Please enter correct parameters", Toast.LENGTH_SHORT).show();
                else {
                    Intent intent = new Intent(LandingActivity.this, ResultsActivity.class);
                    intent.putExtra("location", String.valueOf(location.getText()));
                    intent.putExtra("dates", String.valueOf(dates.getText()));
                    intent.putExtra("persons", Integer.parseInt(String.valueOf(persons.getText())));

                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                }
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finishAffinity();
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
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
}