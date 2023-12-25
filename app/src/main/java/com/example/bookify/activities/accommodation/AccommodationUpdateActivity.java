package com.example.bookify.activities.accommodation;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.bookify.activities.LoginActivity;
import com.example.bookify.clients.ClientUtils;
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
import com.example.bookify.fragments.accommodation.AccommodationUpdateViewModel;
import com.example.bookify.fragments.user.RegistrationViewModel;
import com.example.bookify.model.Accommodation;
import com.example.bookify.model.AccommodationInsertDTO;
import com.example.bookify.model.ImageMobileDTO;
import com.example.bookify.model.MessageDTO;
import com.example.bookify.utils.JWTUtils;

import java.util.Collection;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationUpdateActivity extends AppCompatActivity {

    ActivityAccommodationUpdateBinding binding;

    AccommodationUpdateViewModel viewModel;

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
    private Long accommodationId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accommodation_update);

        viewModel = new ViewModelProvider(this).get(AccommodationUpdateViewModel.class);

        Intent intentParams = getIntent();
        boolean isEditMode = intentParams.getBooleanExtra("isEditMode", false);
        Long accommodationIdParam = intentParams.getLongExtra("accommodationId", 0L);
        boolean price = intentParams.getBooleanExtra("price", false);

        viewModel.setAccommodationId(accommodationIdParam);
        viewModel.setIsEditMode(isEditMode);


        if (isEditMode) {
            Call<Accommodation> call = ClientUtils.accommodationService.getAccommodation(accommodationIdParam);

            call.enqueue(new Callback<Accommodation>() {
                @Override
                public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
                    if (response.code() == 200) {
                        Accommodation accommodation = response.body();
                        viewModel.setPropertyName(accommodation.getName());
                        viewModel.setDescription(accommodation.getDescription());
                        viewModel.setAddress(accommodation.getAddress());
                        viewModel.setAmenities(accommodation.getFilters());
                        viewModel.setMinGuests(accommodation.getMinGuest());
                        viewModel.setMaxGuests(accommodation.getMaxGuest());
                        viewModel.setCancellationDeadline(accommodation.getCancellationDeadline());
                        viewModel.setManual(accommodation.isManual());
                        viewModel.setType(accommodation.getAccommodationType());
                        viewModel.setPricePer(accommodation.getPricePer());
                        FragmentTransition.to(fragments[counter], AccommodationUpdateActivity.this, true,
                                R.id.accommodationFragment);

                        Call<List<ImageMobileDTO>> callStoredImages = ClientUtils.accommodationService.getImagesFiles(accommodationIdParam);

                        callStoredImages.enqueue(new Callback<List<ImageMobileDTO>>() {
                            @Override
                            public void onResponse(Call<List<ImageMobileDTO>> call, Response<List<ImageMobileDTO>> response) {
                                if (response.isSuccessful()) {
                                    viewModel.setStoredImages(response.body());
                                }
                            }

                            @Override
                            public void onFailure(Call<List<ImageMobileDTO>> call, Throwable t) {
                                Log.d("BOOKIFY", "onFailure: " + t.getMessage());
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<Accommodation> call, Throwable t) {

                }
            });
        } else {
            FragmentTransition.to(fragments[counter], AccommodationUpdateActivity.this, true,
                    R.id.accommodationFragment);
        }


        binding = ActivityAccommodationUpdateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (price) {
            counter = fragments.length - 1;
            binding.next.setText("Submit");
        }

        binding.next.setOnClickListener(v -> {
            if (counter == fragments.length - 1 && binding.next.getText().toString().equals("Submit")) {
                Intent intent = new Intent(AccommodationUpdateActivity.this, OwnerAccommodationsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                return;
            }
            if (counter < fragments.length - 1) {
                if (fragments[counter].isValid() == 1) {
                    Toast.makeText(this, "You must fill in all field", Toast.LENGTH_SHORT).show();
                    return;
                } else if (fragments[counter].isValid() == 2) {
                    Toast.makeText(this, "Max must be greater than min", Toast.LENGTH_SHORT).show();
                    return;
                } else if (fragments[counter].isValid() == 3) {
                    Toast.makeText(this, "Select at least one photo", Toast.LENGTH_SHORT).show();
                    return;
                }
                binding.previous.setVisibility(View.VISIBLE);
                FragmentTransition.to(fragments[++counter], AccommodationUpdateActivity.this, false,
                        R.id.accommodationFragment);
            }
            if (counter == fragments.length - 1) {
                binding.previous.setVisibility(View.INVISIBLE);

                if (viewModel.getIsEditMode().getValue()) {
                    Accommodation accommodation = new Accommodation();
                    accommodation.setId(accommodationIdParam);
                    accommodation.setName(viewModel.getPropertyName().getValue());
                    accommodation.setDescription(viewModel.getDescription().getValue());
                    accommodation.setAddress(viewModel.getAddress().getValue());
                    accommodation.setMinGuest(viewModel.getMinGuests().getValue());
                    accommodation.setMaxGuest(viewModel.getMaxGuests().getValue());
                    accommodation.setAccommodationType(viewModel.getType().getValue());
                    accommodation.setManual(viewModel.getManual().getValue());
                    accommodation.setFilters(viewModel.getAmenities().getValue());
                    accommodation.setCancellationDeadline(viewModel.getCancellationDeadline().getValue());
                    accommodation.setPricePer(viewModel.getPricePer().getValue());
                    Call<Long> call = ClientUtils.accommodationService.modify(accommodation);

                    call.enqueue(new Callback<Long>() {
                        @Override
                        public void onResponse(Call<Long> call, Response<Long> response) {
                            if (response.isSuccessful()) {
                                Call<Long> callImages = ClientUtils.accommodationService.uploadImages(response.body(), viewModel.getImages().getValue());
                                accommodationId = response.body();
                                viewModel.setAccommodationId(accommodationId);
                                callImages.enqueue(new Callback<Long>() {
                                    @Override
                                    public void onResponse(Call<Long> call, Response<Long> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<Long> call, Throwable t) {

                                    }
                                });
                            }
                            if (response.code() == 400 && response.errorBody() != null) {
                                Toast.makeText(AccommodationUpdateActivity.this, "Cannot change data", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AccommodationUpdateActivity.this, OwnerAccommodationsActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<Long> call, Throwable t) {

                        }
                    });
                } else {

                    AccommodationInsertDTO accommodation = new AccommodationInsertDTO();
                    accommodation.setName(viewModel.getPropertyName().getValue());
                    accommodation.setDescription(viewModel.getDescription().getValue());
                    accommodation.setAddress(viewModel.getAddress().getValue());
                    accommodation.setMinGuest(viewModel.getMinGuests().getValue());
                    accommodation.setMaxGuest(viewModel.getMaxGuests().getValue());
                    accommodation.setAccommodationType(viewModel.getType().getValue());
                    accommodation.setManual(viewModel.getManual().getValue());
                    accommodation.setFilters(viewModel.getAmenities().getValue());
                    accommodation.setCancellationDeadline(viewModel.getCancellationDeadline().getValue());
                    accommodation.setPricePer(viewModel.getPricePer().getValue());

                    SharedPreferences sharedPreferences = AccommodationUpdateActivity.this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
                    Long ownerId = sharedPreferences.getLong(JWTUtils.USER_ID, -1);
                    Call<Accommodation> call = ClientUtils.accommodationService.insert(ownerId, accommodation);

                    call.enqueue(new Callback<Accommodation>() {
                        @Override
                        public void onResponse(Call<Accommodation> call, Response<Accommodation> response) {
                            if (response.code() == 201) {
                                Call<Long> callImages = ClientUtils.accommodationService.uploadImages(response.body().getId(), viewModel.getImages().getValue());
                                accommodationId = response.body().getId();
                                viewModel.setAccommodationId(accommodationId);
                                callImages.enqueue(new Callback<Long>() {
                                    @Override
                                    public void onResponse(Call<Long> call, Response<Long> response) {

                                    }

                                    @Override
                                    public void onFailure(Call<Long> call, Throwable t) {

                                    }
                                });
                            }
                            if (response.code() == 400 && response.errorBody() != null) {
                                Toast.makeText(AccommodationUpdateActivity.this, "Cannot change data", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(AccommodationUpdateActivity.this, OwnerAccommodationsActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                startActivity(intent);
                                overridePendingTransition(0, 0);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(Call<Accommodation> call, Throwable t) {
                            JWTUtils.autoLogout(AccommodationUpdateActivity.this, t);
                        }
                    });
                }

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