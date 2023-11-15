package com.example.bookify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.bookify.databinding.ActivityAccommodationUpdateBinding;
import com.google.android.material.button.MaterialButton;

public class AccommodationUpdateActivity extends AppCompatActivity {

    ActivityAccommodationUpdateBinding binding;

    Fragment[] fragments = new Fragment[]{
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
                startActivity(intent);
                finish();
            }
            if (counter < fragments.length - 1) {
                binding.previous.setVisibility(View.VISIBLE);
                FragmentTransition.to(fragments[++counter], AccommodationUpdateActivity.this, false,
                        R.id.accommodationFragment);
            }
            if (counter == fragments.length - 1) {
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
    }
}