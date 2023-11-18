package com.example.bookify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.Slide;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.navigation.NavigationBarView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class AccommodationDetailsActivity extends AppCompatActivity {

    Button reservationDate;
    ImageSlider imageSlider;
    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodation_details);
        NavigationBar.setNavigationBar(findViewById(R.id.bottom_navigaiton), this, R.id.navigation_home);

        Button ownerDetailsInfo = findViewById(R.id.ownerPicture);
        ownerDetailsInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccommodationDetailsActivity.this, OwnerDetailsActivity.class);
                startActivity(intent);
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE);
        if (sharedPreferences.getString("userType", "none").equals("guest")) {
            showReservationOption();
        }

        imageSlider = findViewById(R.id.imageSlider);
        ArrayList<SlideModel> imageList = new ArrayList<>();

        imageList.add(new SlideModel(R.drawable.apartman1, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.apartman2, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.apartman3, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.apartman4, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.apartman5, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.apartman6, ScaleTypes.FIT));

        imageSlider.setImageList(imageList, ScaleTypes.FIT);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(googleMap -> {
            LatLng markerLatLng = new LatLng(45.2453834, 19.7917393);
            googleMap.addMarker(new MarkerOptions().position(markerLatLng).title("Marker Title"));

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng, 12));
        });
    }

    private void showReservationOption(){
        LinearLayout layout = findViewById(R.id.reservationLayout);
        View reservation = getLayoutInflater().inflate(R.layout.reservation, layout, false);
        layout.addView(reservation);

        reservationDate = reservation.findViewById(R.id.editDate);
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