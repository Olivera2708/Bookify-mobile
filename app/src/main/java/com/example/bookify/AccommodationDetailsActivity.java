package com.example.bookify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.os.Bundle;
import android.transition.Slide;
import android.view.View;
import android.widget.Button;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AccommodationDetailsActivity extends AppCompatActivity {

    Button reservationDate;
    ImageSlider imageSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodation_details);

        View reservationTile = findViewById(R.id.reservation);
        reservationDate = reservationTile.findViewById(R.id.editDate);
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

        imageSlider = findViewById(R.id.imageSlider);
        ArrayList<SlideModel> imageList = new ArrayList<>();

        imageList.add(new SlideModel(R.drawable.apartman1, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.apartman2, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.apartman3, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.apartman4, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.apartman5, ScaleTypes.FIT));
        imageList.add(new SlideModel(R.drawable.apartman6, ScaleTypes.FIT));

        imageSlider.setImageList(imageList, ScaleTypes.FIT);
    }
}