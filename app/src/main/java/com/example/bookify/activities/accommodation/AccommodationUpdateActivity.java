package com.example.bookify.activities.accommodation;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.bookify.fragments.accommodation.AccommodationFragmentAvailability;
import com.example.bookify.fragments.accommodation.AccommodationFragmentBasicInformation;
import com.example.bookify.fragments.accommodation.AccommodationFragmentFilters;
import com.example.bookify.fragments.accommodation.AccommodationFragmentGuests;
import com.example.bookify.fragments.accommodation.AccommodationFragmentLocation;
import com.example.bookify.fragments.accommodation.AccommodationFragmentPaymentDetails;
import com.example.bookify.fragments.accommodation.AccommodationFragmentPhotos;
import com.example.bookify.fragments.FragmentTransition;
import com.example.bookify.fragments.MyFragment;
import com.example.bookify.R;
import com.example.bookify.databinding.ActivityAccommodationUpdateBinding;

public class AccommodationUpdateActivity extends AppCompatActivity {

    ActivityAccommodationUpdateBinding binding;

    MyFragment[] fragments = new MyFragment[]{
            AccommodationFragmentBasicInformation.newInstance("", ""),
            AccommodationFragmentLocation.newInstance("", ""),
            AccommodationFragmentFilters.newInstance("", ""),
            AccommodationFragmentPhotos.newInstance("", ""),
            AccommodationFragmentGuests.newInstance("", ""),
            AccommodationFragmentPaymentDetails.newInstance("", ""),
            AccommodationFragmentAvailability.newInstance("", "")
    };

    private int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodation_update);
        FragmentTransition.to(fragments[counter], AccommodationUpdateActivity.this, true,
                R.id.accommodationFragment);

        binding = ActivityAccommodationUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.next.setOnClickListener(v -> {
            if (counter == fragments.length - 1 && binding.next.getText().toString().equals("Submit")) {
                Intent intent = new Intent(AccommodationUpdateActivity.this, OwnerAccommodationsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
            if (counter < fragments.length - 1) {
                if (fragments[counter].isValid() == 1) {
                    Toast.makeText(this, "You must fill in all field", Toast.LENGTH_SHORT).show();
                    return;
                } else if (fragments[counter].isValid() == 2) {
                    Toast.makeText(this, "Max must be greater than min", Toast.LENGTH_SHORT).show();
                    return;
                }
                binding.previous.setVisibility(View.VISIBLE);
                FragmentTransition.to(fragments[++counter], AccommodationUpdateActivity.this, false,
                        R.id.accommodationFragment);
            }
            if (counter == fragments.length - 1) {
                binding.previous.setVisibility(View.INVISIBLE);
            }
            if (counter == fragments.length - 2) {
                binding.next.setText("Submit");
            }
        });

        binding.previous.setOnClickListener(v -> {
            if (counter > 0) {
                binding.next.setText("Next");
                FragmentTransition.to(fragments[--counter], AccommodationUpdateActivity.this, false,
                        R.id.accommodationFragment);
            }
            if (counter == 0) {
                binding.previous.setVisibility(View.INVISIBLE);
            }
        });

        Toast.makeText(AccommodationUpdateActivity.this, "" + counter, Toast.LENGTH_SHORT);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(AccommodationUpdateActivity.this, OwnerAccommodationsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }
}