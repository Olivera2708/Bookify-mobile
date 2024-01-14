package com.example.bookify.activities.accommodation;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.denzcoskun.imageslider.models.SlideModel;
import com.example.bookify.activities.user.AccountDetailsActivity;
import com.example.bookify.activities.user.OwnerDetailsActivity;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.enumerations.Filter;
import com.example.bookify.model.CommentDTO;
import com.example.bookify.model.RatingDTO;
import com.example.bookify.model.accommodation.AccommodationDetailDTO;
import com.example.bookify.model.reservation.ReservationDTO;
import com.example.bookify.model.reservation.ReservationRequestDTO;
import com.example.bookify.navigation.NavigationBar;
import com.example.bookify.R;
import com.example.bookify.utils.JWTUtils;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.ParseException;
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
    private MapView mapView;
    AccommodationDetailDTO accommodation;
    Bundle savedInstanceState;
    double price = -1;
    private SharedPreferences sharedPreferences;
    Long userId;

    Map<String, Integer> amenitiesIcons = new HashMap<String, Integer>() {{
        put("Free wifi", R.drawable.wifi);
        put("Air conditioning", R.drawable.air);
        put("Terrace", R.drawable.terrace);
        put("Swimming pool", R.drawable.pool);
        put("Bar", R.drawable.bar);
        put("Sauna", R.drawable.sauna);
        put("Luggage storage", R.drawable.luggage);
        put("Lunch", R.drawable.lunch_dining);
        put("Airport shuttle", R.drawable.airport_shuttle);
        put("Wheelchair", R.drawable.wheelchair);
        put("Non smoking", R.drawable.smoking);
        put("Free parking", R.drawable.parking);
        put("Family rooms", R.drawable.family_room);
        put("Garden", R.drawable.yard);
        put("Front desk", R.drawable.bell);
        put("Jacuzzi", R.drawable.hot_tub);
        put("Heating", R.drawable.heating);
        put("Breakfast", R.drawable.breakfast);
        put("Diner", R.drawable.dinner);
        put("Private bathroom", R.drawable.bathroom);
        put("Deposit box", R.drawable.local_atm);
        put("City center", R.drawable.city);
    }};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.activity_accommodation_details);
        NavigationBar.setNavigationBar(findViewById(R.id.bottom_navigaiton), this, R.id.navigation_home);

        this.sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE);

        Intent intent = getIntent();
        Long id = intent.getLongExtra("id", 0);

        getData(id);

        Button favorite = findViewById(R.id.favorite);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favorite.getTag().equals("empty"))
                    addToFavorites(favorite);
            }
        });

        ImageView ownerDetailsInfo = findViewById(R.id.ownerPicture);
        ownerDetailsInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AccommodationDetailsActivity.this, OwnerDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("ownerId", accommodation.getOwner().getId());
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }

    private void setReviews(Long id) {
        userId = sharedPreferences.getLong("id", 0L);

        Call<List<CommentDTO>> call = ClientUtils.reviewService.getAccommodationComments(id);
        call.enqueue(new Callback<List<CommentDTO>>() {
            @Override
            public void onResponse(Call<List<CommentDTO>> call, Response<List<CommentDTO>> response) {
                if (response.code() == 200 && response.body() != null) {
                    List<CommentDTO> comments = response.body();
                    setComments(comments, id);
                }
            }

            @Override
            public void onFailure(Call<List<CommentDTO>> call, Throwable t) {

            }
        });

        Call<RatingDTO> callRating = ClientUtils.reviewService.getAccommodationRating(id);
        callRating.enqueue(new Callback<RatingDTO>() {
            @Override
            public void onResponse(Call<RatingDTO> call, Response<RatingDTO> response) {
                if (response.code() == 200 && response.body() != null) {
                    RatingDTO ratingDTO = response.body();
                    setRating(ratingDTO);
                }
            }

            @Override
            public void onFailure(Call<RatingDTO> call, Throwable t) {

            }
        });
    }

    private void setRating(RatingDTO ratingDTO) {
        String avgRating = String.format("%.2f", ratingDTO.getAvgRating());

        RatingBar rating = findViewById(R.id.rating);
        rating.setRating((float) ratingDTO.getAvgRating());

        ProgressBar progres5 = findViewById(R.id.progress_5);
        ProgressBar progres4 = findViewById(R.id.progress_4);
        ProgressBar progres3 = findViewById(R.id.progress_3);
        ProgressBar progres2 = findViewById(R.id.progress_2);
        ProgressBar progres1 = findViewById(R.id.progress_1);

        TextView total_5 = findViewById(R.id.total_5);
        TextView total_4 = findViewById(R.id.total_4);
        TextView total_3 = findViewById(R.id.total_3);
        TextView total_2 = findViewById(R.id.total_2);
        TextView total_1 = findViewById(R.id.total_1);

        TextView total = findViewById(R.id.total);
        TextView total1 = findViewById(R.id.total1);

        int count = ratingDTO.getFiveStars() + ratingDTO.getFourStars() + ratingDTO.getThreeStars() +
                ratingDTO.getTwoStars() + ratingDTO.getOneStars();

        int sum = ratingDTO.getFiveStars() * 5 + ratingDTO.getFourStars() * 4 + ratingDTO.getThreeStars() * 3 +
                ratingDTO.getTwoStars() * 2 + ratingDTO.getOneStars();

        total.setText("(" + count + ")");
        total1.setText(avgRating);

        if (count > 0) {
            progres5.setProgress(ratingDTO.getFiveStars() * 100 / count);
            progres4.setProgress(ratingDTO.getFourStars() * 100 / count);
            progres3.setProgress(ratingDTO.getThreeStars() * 100 / count);
            progres2.setProgress(ratingDTO.getTwoStars() * 100 / count);
            progres1.setProgress(ratingDTO.getOneStars() * 100 / count);
        }

        total_1.setText("(" + ratingDTO.getOneStars() + ")");
        total_2.setText("(" + ratingDTO.getTwoStars() + ")");
        total_3.setText("(" + ratingDTO.getThreeStars() + ")");
        total_4.setText("(" + ratingDTO.getFourStars() + ")");
        total_5.setText("(" + ratingDTO.getFiveStars() + ")");
    }

    private void setComments(List<CommentDTO> comments, Long id) {
        LinearLayout commentSection = findViewById(R.id.comment_section);
        commentSection.removeAllViews();
        for (CommentDTO comment : comments) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View commentView = inflater.inflate(R.layout.comment, commentSection, false);

            setComment(comment, commentView, id);
            commentSection.addView(commentView);
        }
    }

    private void setComment(CommentDTO comment, View commentView, Long id) {
        TextView name = commentView.findViewById(R.id.userName);
        TextView date = commentView.findViewById(R.id.date);
        ImageView image = commentView.findViewById(R.id.userPicture);
        RatingBar stars = commentView.findViewById(R.id.stars);
        TextView commentText = commentView.findViewById(R.id.commentText);

        Button report = commentView.findViewById(R.id.btnReport);
        ImageView delete = commentView.findViewById(R.id.deleteComment);

        Call<ResponseBody> callImage = ClientUtils.accommodationService.getImage(comment.getImageId());
        callImage.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                    image.setImageBitmap(getRoundedBitmap(bitmap));
                } else {
                    image.setImageResource(R.drawable.account_round);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Image", "Basic accommodation image");
            }
        });

        stars.setRating((float) comment.getRate());
        name.setText(comment.getName());
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd.MM.yyyy.", Locale.getDefault());
        date.setText(dateFormat.format(comment.getDate()));
        commentText.setText(comment.getComment());

        if (accommodation.getOwner().getId() == userId) {
            report.setVisibility(View.VISIBLE);
            report.setOnClickListener(v -> {
                Call<Long> call = ClientUtils.reviewService.reportComment(comment.getId());
                call.enqueue(new Callback<Long>() {
                    @Override
                    public void onResponse(Call<Long> call, Response<Long> response) {
                        Toast.makeText(AccommodationDetailsActivity.this, "Review reported", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Long> call, Throwable t) {

                    }
                });
            });
        }

        if (comment.getGuestId() == userId) {
            delete.setVisibility(View.VISIBLE);
            delete.setOnClickListener(v -> {
                Call<Void> callDelete = ClientUtils.reviewService.deleteAccommodationReview(id, comment.getId());
                callDelete.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        Toast.makeText(AccommodationDetailsActivity.this, "Successfully deleted review", Toast.LENGTH_SHORT).show();
                        setReviews(id);
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                    }
                });
            });
        }
    }

    private Bitmap getRoundedBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int radius = Math.min(width, height) / 2;

        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        Bitmap roundedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roundedBitmap);
        canvas.drawCircle(width / 2, height / 2, radius, paint);

        return roundedBitmap;
    }

    private void setMap() {
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(googleMap -> {
            LatLng address = getLocationFromAddress(accommodation.getAddress().toString());
            googleMap.addMarker(new MarkerOptions().position(address));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(address, 12));
        });
    }

    private void getData(Long id) {
        Call<AccommodationDetailDTO> call = ClientUtils.accommodationService.getAccommodationDetails(id);
        call.enqueue(new Callback<AccommodationDetailDTO>() {
            @Override
            public void onResponse(Call<AccommodationDetailDTO> call, Response<AccommodationDetailDTO> response) {
                if (response.isSuccessful() && response.body() != null) {
                    accommodation = response.body();
                    showData();
                    setReviews(id);
                }
                Log.d("Testiranje", "TEST");
            }

            @Override
            public void onFailure(Call<AccommodationDetailDTO> call, Throwable t) {
                Log.d("Error", "Accommodation details");
            }
        });
    }

    private void showData() {
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
        Button favorite = findViewById(R.id.favorite);

        checkIfInFavorites(favorite);

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

        SharedPreferences sharedPreferences = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        if (sharedPreferences.getString("userType", "none").equals("GUEST"))
            showReservationOption();
    }

    private void setAmenities(GridLayout amenitiesLayout) {
        for (Filter f : accommodation.getFilters()) {
            View amenity = LayoutInflater.from(this).inflate(R.layout.amenity, null);
            TextView name = amenity.findViewById(R.id.amenityName);
            ImageView icon = amenity.findViewById(R.id.amenityIcon);

            name.setText(transformFilter(f));
            icon.setImageResource(amenitiesIcons.get(transformFilter(f)));

            amenitiesLayout.addView(amenity);
        }
    }

    private String transformFilter(Filter f) {
        return f.toString().substring(0, 1) + f.toString().replace("_", " ").substring(1, f.toString().length()).toLowerCase();
    }

    private void setImages(ViewFlipper slider) {
        Call<String[]> call = ClientUtils.accommodationService.getImages(accommodation.getId());
        call.enqueue(new Callback<String[]>() {
            @Override
            public void onResponse(Call<String[]> call, Response<String[]> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<SlideModel> accommodationImages = new ArrayList<>();
                    for (String imageResponse : response.body()) {
                        Bitmap bitmap = decodeBase64Image(imageResponse);
                        if (bitmap != null) {
                            ImageView image = new ImageView(getApplicationContext());
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

    private void getOwnerImage(ImageView ownerImage) {
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

        Button reservationDate = reservation.findViewById(R.id.editDate);
        EditText reservationPeople = reservation.findViewById(R.id.peopleText);

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
                        calculatePrice(reservation, startDate, endDate, Integer.parseInt(reservationPeople.getText().toString()));
                    }
                });
                materialDatePicker.show(getSupportFragmentManager(), "tag");
            }
        });

        reservationPeople.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER) && !reservationDate.getText().toString().equals("")) {
                    String[] dates = reservationDate.getText().toString().split(" - ");
                    calculatePrice(reservation, dates[0], dates[1], Integer.parseInt(reservationPeople.getText().toString()));
                    return true;
                }
                return false;
            }
        });

        Button reserve = reservation.findViewById(R.id.button2);
        reserve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView priceView = reservation.findViewById(R.id.price);
                if (!priceView.getText().equals("Calculating...") && !priceView.getText().equals("")) {
                    //send request to server
                    SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy.");
                    String[] dates = reservationDate.getText().toString().split(" - ");
                    try {
                        Date start = format.parse(dates[0]);
                        Date end = format.parse(dates[1]);
                        SharedPreferences sharedPreferences = AccommodationDetailsActivity.this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
                        Long guestId = sharedPreferences.getLong(JWTUtils.USER_ID, -1);

                        ReservationRequestDTO requestDTO = new ReservationRequestDTO((new Date()).getTime(), start.getTime(), end.getTime(), Integer.parseInt(reservationPeople.getText().toString()), price);

                        Call<ReservationDTO> call = ClientUtils.reservationService.createReservation(requestDTO, accommodation.getId(), guestId);
                        call.enqueue(new Callback<ReservationDTO>() {
                            @Override
                            public void onResponse(Call<ReservationDTO> call, Response<ReservationDTO> response) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(AccommodationDetailsActivity.this);
                                builder.setTitle("Reservation Successful")
                                        .setMessage("Your reservation has been successful.")
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //Go to reservations page
                                            }
                                        })
                                        .show();
                            }

                            @Override
                            public void onFailure(Call<ReservationDTO> call, Throwable t) {
                                Log.d("ReservationCreate", "Reservation create error");
                                JWTUtils.autoLogout(AccommodationDetailsActivity.this, t);
                            }
                        });
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    private void calculatePrice(View reservation, String begin, String end, int persons) {
        TextView priceView = reservation.findViewById(R.id.price);
        priceView.setText("Calculating...");
        Call<Double> call = ClientUtils.accommodationService.getTotalPrice(accommodation.getId(), begin, end, accommodation.getPricePer(), persons);
        call.enqueue(new Callback<Double>() {
            @Override
            public void onResponse(Call<Double> call, Response<Double> response) {
                if (response.isSuccessful() && response.body() != null) {
                    price = response.body();
                    if (price == -1)
                        priceView.setText("Not available");
                    else
                        priceView.setText(String.format("%.2f", price) + " EUR");
                }
            }

            @Override
            public void onFailure(Call<Double> call, Throwable t) {
                Log.d("ReservationPrice", "Reservation price incorrect");
                JWTUtils.autoLogout(AccommodationDetailsActivity.this, t);
            }
        });
    }

    public LatLng getLocationFromAddress(String strAddress) {
        Geocoder coder = new Geocoder(this);
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p1;
    }

    private void checkIfInFavorites(Button favorite){
        SharedPreferences sharedPreferences = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        Long guestId = sharedPreferences.getLong(JWTUtils.USER_ID, -1);
        Call<Boolean> call = ClientUtils.accommodationService.checkIfInFavorites(guestId, accommodation.getId());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Boolean isFavorite = response.body();
                    if (isFavorite) {
                        favorite.setBackgroundResource(R.drawable.favorite);
                        favorite.setTag("full");
                    }
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.d("LoadingFavorites", "Not loaded");
                JWTUtils.autoLogout(AccommodationDetailsActivity.this, t);
            }
        });
    }

    private void addToFavorites(Button favorite){
        SharedPreferences sharedPreferences = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        Long guestId = sharedPreferences.getLong(JWTUtils.USER_ID, -1);
        Call<ResponseBody> call = ClientUtils.accommodationService.addToFavorites(guestId, accommodation.getId());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    favorite.setBackgroundResource(R.drawable.favorite);
                    favorite.setTag("full");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("AddToFavorites", "Not added");
                JWTUtils.autoLogout(AccommodationDetailsActivity.this, t);
            }
        });
    }

    private int getResourceIdFromDrawable(Drawable drawable) {
        if (drawable == null) {
            return 0;
        }

        Resources resources = getResources();
        if (resources != null) {
            return resources.getIdentifier(
                    resources.getResourceEntryName(drawable.hashCode()),
                    "drawable",
                    getPackageName()
            );
        }

        return 0;
    }
}