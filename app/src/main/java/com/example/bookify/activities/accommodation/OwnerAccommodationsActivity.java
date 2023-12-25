package com.example.bookify.activities.accommodation;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.bookify.activities.LandingActivity;
import com.example.bookify.fragments.accommodation.AccommodationUpdateViewModel;
import com.example.bookify.adapters.data.OwnerAccommodationListAdapter;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.databinding.ActivityOwnerAccommodationsBinding;
import com.example.bookify.model.accommodation.AccommodationOwnerDTO;
import com.example.bookify.navigation.NavigationBar;
import com.example.bookify.R;
import com.example.bookify.activities.ReportsActivity;
import com.example.bookify.utils.JWTUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwnerAccommodationsActivity extends AppCompatActivity {

    private ActivityOwnerAccommodationsBinding binding;
    private OwnerAccommodationListAdapter adapter;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityOwnerAccommodationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        this.sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE);
        NavigationBar.setNavigationBar(findViewById(R.id.bottom_navigaiton), this, R.id.navigation_accommodation);

        binding.addAccommodationButton.setOnClickListener(v -> {
            Intent intent = new Intent(OwnerAccommodationsActivity.this, AccommodationUpdateActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        Button reportButton = findViewById(R.id.reportButton);
        reportButton.setOnClickListener(v -> {
            Intent intent = new Intent(OwnerAccommodationsActivity.this, ReportsActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(OwnerAccommodationsActivity.this, LandingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);

        View accommodationCard = findViewById(R.id.accommodation);
        Button editButton = accommodationCard.findViewById(R.id.btnEditAccommodation);
        editButton.setOnClickListener(v -> {
            Intent intent = new Intent(OwnerAccommodationsActivity.this, AccommodationUpdateActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("isEditMode", true);
            intent.putExtra("accommodationId", 3L);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });

        Button priceButton = accommodationCard.findViewById(R.id.btnEditPrice);
        priceButton.setOnClickListener(v -> {
            Intent intent = new Intent(OwnerAccommodationsActivity.this, AccommodationUpdateActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            intent.putExtra("isEditMode", true);
            intent.putExtra("accommodationId", 3L);
            intent.putExtra("price", true);
            startActivity(intent);
            overridePendingTransition(0, 0);
        });
        getData();

    }
    private void getData(){
        Call<List<AccommodationOwnerDTO>> call = ClientUtils.accommodationService.getOwnerAccommodations(sharedPreferences.getLong(JWTUtils.USER_ID, -1));
        call.enqueue(new Callback<List<AccommodationOwnerDTO>>() {
            @Override
            public void onResponse(Call<List<AccommodationOwnerDTO>> call, Response<List<AccommodationOwnerDTO>> response) {
                if(response.body() != null && response.code() == 200){
                    displayData(response.body());
                }

            }

            @Override
            public void onFailure(Call<List<AccommodationOwnerDTO>> call, Throwable t) {

            }
        });
    }
    private void displayData(List<AccommodationOwnerDTO> accommodations) {
        adapter = new OwnerAccommodationListAdapter(this, accommodations);
        binding.ownerAccommodationsList.setAdapter(adapter);

    }
}