package com.example.bookify.activities.accommodation;

import static java.util.Map.entry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.denzcoskun.imageslider.models.SlideModel;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.enumerations.Filter;
import com.example.bookify.model.AccommodationDetailDTO;
import com.example.bookify.navigation.NavigationBar;
import com.example.bookify.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationDetailsActivity extends AppCompatActivity {

    Button reservationDate;
    private MapView mapView;
    AccommodationDetailDTO accommodation;
    Bundle savedInstanceState;
    Map<String, Integer> amenitiesIcons = new HashMap<String, Integer>() {{
            put("Free wifi", R.drawable.wifi); put("Air conditioning", R.drawable.air);
            put("Terrace", R.drawable.terrace); put("Swimming pool", R.drawable.pool);
            put("Bar", R.drawable.bar); put("Sauna", R.drawable.sauna);
            put("Luggage storage", R.drawable.luggage); put("Lunch", R.drawable.lunch_dining);
            put("Airport shuttle", R.drawable.airport_shuttle); put("Wheelchair", R.drawable.wheelchair);
            put("Non smoking", R.drawable.smoking); put("Free parking", R.drawable.parking);
            put("Family rooms", R.drawable.family_room); put("Garden", R.drawable.yard);
            put("Front desk", R.drawable.bell); put("Jacuzzi", R.drawable.hot_tub);
            put("Heating", R.drawable.heating); put("Breakfast", R.drawable.breakfast);
            put("Diner", R.drawable.dinner); put("Private bathroom", R.drawable.bathroom);
            put("Deposit box", R.drawable.local_atm); put("City center", R.drawable.city); }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_accommodation_details);
        NavigationBar.setNavigationBar(findViewById(R.id.bottom_navigaiton), this, R.id.navigation_home);

        Intent intent = getIntent();
        Long id = intent.getLongExtra("id", 0);

        getData(id);


//        View view1 = findViewById(R.id.include1);
//        View view2 = findViewById(R.id.include2);
//        View view3 = findViewById(R.id.include3);
//        Button ownerDetailsInfo = findViewById(R.id.ownerPicture);
//        ownerDetailsInfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(AccommodationDetailsActivity.this, OwnerDetailsActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                startActivity(intent);
//                overridePendingTransition(0, 0);
//            }
//        });

//        SharedPreferences sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE);
//        if (sharedPreferences.getString("userType", "none").equals("guest")) {
//            showReservationOption();
//        }
//        if (sharedPreferences.getString("userType", "none").equals("owner")) {
//            setReportButton(view1);
//            setReportButton(view2);
//            setReportButton(view3);
//        }
//        if (sharedPreferences.getString("userType", "none").equals("guest")) {
//            setDeleteIcon(view1);
//            setDeleteIcon(view2);
//            setDeleteIcon(view3);
//        }
    }

    private void setMap(){
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(googleMap -> {
            LatLng address = getLocationFromAddress(accommodation.getAddress().toString());
            googleMap.addMarker(new MarkerOptions().position(address));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(address, 12));
        });
    }

    private void getData(Long id){
        Call<AccommodationDetailDTO> call = ClientUtils.accommodationService.getAccommodationDetails(id);
        call.enqueue(new Callback<AccommodationDetailDTO>() {
            @Override
            public void onResponse(Call<AccommodationDetailDTO> call, Response<AccommodationDetailDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    accommodation = response.body();
                    showData();
                }
                Log.d("Testiranje", "TEST");
            }
            @Override
            public void onFailure(Call<AccommodationDetailDTO> call, Throwable t) {
                Log.d("Error", "Accommodation details");
            }
        });
    }

    private void showData(){
        RatingBar stars = findViewById(R.id.starRating);
        TextView name = findViewById(R.id.name);
        TextView desc = findViewById(R.id.description);
        TextView address = findViewById(R.id.addressText);
        TextView ownerName = findViewById(R.id.ownerName);
        TextView ownerPhone = findViewById(R.id.ownerPhone);
        RatingBar ownerRating = findViewById(R.id.ownerRating);
        ImageView ownerImage = findViewById(R.id.ownerPicture);
        ViewFlipper slider = findViewById(R.id.imageSlider);
        GridLayout amenitiesLayout = findViewById(R.id.amenitiesLayout);

        //amenities

        name.setText(accommodation.getName());
        desc.setText(accommodation.getDescription());
        address.setText(accommodation.getAddress().toString());
        ownerName.setText(accommodation.getOwner().getFirstName() + " " + accommodation.getOwner().getLastName());
        ownerPhone.setText(accommodation.getOwner().getPhone());
        stars.setRating(accommodation.getAvgRating());
        ownerRating.setRating(accommodation.getOwner().getAvgRating());

        if (accommodation.getOwner().getImageId() != 0)
            getOwnerImage(ownerImage);

        setMap();
        setImages(slider);
        slider.setFlipInterval(3000);
        slider.startFlipping();
        setAmenities(amenitiesLayout);
    }

    private void setAmenities(GridLayout amenitiesLayout){
        for (Filter f : accommodation.getFilters()){
            View amenity = LayoutInflater.from(this).inflate(R.layout.amenity, null);
            TextView name = amenity.findViewById(R.id.amenityName);
            ImageView icon = amenity.findViewById(R.id.amenityIcon);

            name.setText(transformFilter(f));
            icon.setImageResource(amenitiesIcons.get(transformFilter(f)));

            amenitiesLayout.addView(amenity);
        }
    }

    private String transformFilter(Filter f){
        return f.toString().substring(0, 1) + f.toString().replace("_", " ").substring(1, f.toString().length()).toLowerCase();
    }

    private void setImages(ViewFlipper slider){
        Call<String[]> call = ClientUtils.accommodationService.getImages(accommodation.getId());
        call.enqueue(new Callback<String[]>() {
            @Override
            public void onResponse(Call<String[]> call, Response<String[]> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<SlideModel> accommodationImages = new ArrayList<>();
                    for (String imageResponse : response.body()) {
                        Bitmap bitmap = decodeBase64Image(imageResponse);
                        if (bitmap != null) {
                            ImageView image = new ImageView (getApplicationContext());
                            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
                            image.setImageBitmap(bitmap);
                            slider.addView(image);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<String[]> call, Throwable t) {
                Log.d("Image", "Basic accommodation image");
            }
        });
    }

    private Bitmap decodeBase64Image(String base64Image) {
        byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }

    private void getOwnerImage(ImageView ownerImage){
        Call<ResponseBody> call = ClientUtils.accountService.getImage(accommodation.getOwner().getImageId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Bitmap image = BitmapFactory.decodeStream(response.body().byteStream());
                    ownerImage.setImageBitmap(image);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Image", "Basic accommodation image");
            }
        });
    }

    private static void setDeleteIcon(View view1) {
        ImageView delete = view1.findViewById(R.id.deleteComment);
        delete.setVisibility(View.VISIBLE);
        delete.setOnClickListener(v -> {
            //brisanje komentara
        });
    }

    private void setReportButton(View view1) {

        Button report = view1.findViewById(R.id.btnReport);
        report.setVisibility(View.VISIBLE);
        report.setOnClickListener(v -> {
            Toast.makeText(AccommodationDetailsActivity.this, "Your report will be processed", Toast.LENGTH_SHORT).show();
        });
    }

    private void showReservationOption() {
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

    public LatLng getLocationFromAddress(String strAddress)
    {
        Geocoder coder= new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try
        {
            address = coder.getFromLocationName(strAddress, 5);
            if(address==null)
            {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return p1;
    }
}