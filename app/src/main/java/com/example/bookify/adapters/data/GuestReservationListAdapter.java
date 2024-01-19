package com.example.bookify.adapters.data;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookify.R;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.model.OwnerDTO;
import com.example.bookify.model.ReportedUserDTO;
import com.example.bookify.model.ReviewDTO;
import com.example.bookify.model.accommodation.AccommodationDetailDTO;
import com.example.bookify.model.reservation.ReservationDTO;
import com.example.bookify.model.reservation.ReservationGuestViewDTO;
import com.example.bookify.utils.JWTUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuestReservationListAdapter extends ArrayAdapter<ReservationGuestViewDTO> {
    private List<ReservationGuestViewDTO> reservations;
    private HashMap<Long, OwnerDTO> owners;
    private Map<Long, Bitmap> imageMap;
    public GuestReservationListAdapter(@NonNull Context context, List<ReservationGuestViewDTO> resource) {
        super(context, R.layout.reservations_owner, resource);
        this.reservations = resource;
        imageMap = new HashMap<>();
        owners = new HashMap<>();
    }

    @Nullable
    @Override
    public ReservationGuestViewDTO getItem(int position) {
        return this.reservations.get(position);
    }

    @Override
    public int getCount() {
        return this.reservations.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ReservationGuestViewDTO reservation = getItem(position);
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.reservations_owner, parent, false);

        TextView name = convertView.findViewById(R.id.apartment_name);
        TextView owner = convertView.findViewById(R.id.guest);
        TextView date = convertView.findViewById(R.id.date);
        TextView persons = convertView.findViewById(R.id.persons);
        TextView cancellationDate = convertView.findViewById(R.id.cancellation_time);
        TextView price = convertView.findViewById(R.id.price);
        TextView status = convertView.findViewById(R.id.status);
        RatingBar stars = convertView.findViewById(R.id.starsRating);
        ImageView image = convertView.findViewById(R.id.image);
        Button cancel = convertView.findViewById(R.id.cancel);
        Button report = convertView.findViewById(R.id.btnReport);
        Button comment = convertView.findViewById(R.id.btnComment);


        if(reservation != null) {
            name.setText(reservation.getAccommodationName());
            date.setText("Date:" + reservation.getStart() + " - " + reservation.getEnd());
            persons.setText("Persons: " + reservation.getGuestNumber());
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            price.setText("Cost: " + decimalFormat.format(reservation.getPrice()) + " EUR");
            stars.setRating((float) reservation.getAvgRating());
            status.setText("Status: " + reservation.getStatus().name());
            cancellationDate.setText("Cancellation due: " + reservation.getCancellationDate());
            if(owners.containsKey(reservation.getAccommodationId())){
                OwnerDTO ownerDTO = owners.get(reservation.getAccommodationId());
                owner.setText("Owner: " + ownerDTO.getFirstName() + " " + ownerDTO.getLastName() + " (" + ownerDTO.getAvgRating()+"/5)");
            } else {
                Call<AccommodationDetailDTO> call = ClientUtils.accommodationService.getAccommodationDetails(reservation.getAccommodationId());
                call.enqueue(new Callback<AccommodationDetailDTO>() {
                    @Override
                    public void onResponse(Call<AccommodationDetailDTO> call, Response<AccommodationDetailDTO> response) {
                        if(response.body() != null && response.isSuccessful()){
                            OwnerDTO ownerDTO = response.body().getOwner();
                            owner.setText("Owner: " + ownerDTO.getFirstName() + " " + ownerDTO.getLastName() + " (" + ownerDTO.getAvgRating()+"/5)");
                            owners.put(reservation.getAccommodationId(), ownerDTO);
                        }
                    }

                    @Override
                    public void onFailure(Call<AccommodationDetailDTO> call, Throwable t) {
                        Log.d("Error", "Reservation error");
                    }
                });
            }
            if(imageMap.containsKey(reservation.getImageId())){
                image.setImageBitmap(imageMap.get(reservation.getImageId()));
            } else {
                Call<ResponseBody> call = ClientUtils.accommodationService.getImage(reservation.getImageId());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                            imageMap.put(reservation.getImageId(), bitmap);
                            image.setImageBitmap(bitmap);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("Image", "Basic accommodation image");
                        JWTUtils.autoLogout((AppCompatActivity) getContext(), t);
                    }
                });
            }
        }
        cancel.setOnClickListener(v -> {
            Call<ReservationGuestViewDTO> cancelReservationCall = ClientUtils.reservationService.cancelReservation(reservation.getId());
            cancelReservationCall.enqueue(new Callback<ReservationGuestViewDTO>() {
                @Override
                public void onResponse(Call<ReservationGuestViewDTO> call, Response<ReservationGuestViewDTO> response) {
                    if(response.isSuccessful() && response.body() != null){
                        reservations.remove(reservation);
                        Snackbar.make(parent, "Reservation cancelled", Snackbar.LENGTH_LONG).setAnchorView(R.id.bottom_navigaiton).show();
                        notifyDataSetChanged();
                    }
                    if(response.code() == 400){
                        try {
                            Snackbar.make(parent, response.errorBody().string(), Snackbar.LENGTH_LONG).setAnchorView(R.id.bottom_navigaiton).show();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }

                @Override
                public void onFailure(Call<ReservationGuestViewDTO> call, Throwable t) {
                    JWTUtils.autoLogout((AppCompatActivity) getContext(), t);
                }
            });
        });
        report.setOnClickListener(v -> {
            ShowDialog(R.layout.report, reservation);
        });

        comment.setOnClickListener(v -> {
            ShowDialog(R.layout.new_comment, reservation);
        });
        return convertView;
    }
    private void ShowDialog(int id, ReservationGuestViewDTO reservation) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(id);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);

        if (id == R.layout.new_comment) {
            Button accommodationReview = dialog.findViewById(R.id.btnSendAccommodation);

            accommodationReview.setOnClickListener(v -> {
                sendAccommodationReview(dialog, reservation.getAccommodationId());
            });

            Button ownerReview = dialog.findViewById(R.id.btnSendOwner);

            ownerReview.setOnClickListener(v -> {
                sendOwnerReview(dialog, owners.get(reservation.getAccommodationId()).getId());
            });
        } else {
            TextView reportOwner = dialog.findViewById(R.id.reportOwner);
            reportOwner.setVisibility(View.VISIBLE);
            Button btnReport = dialog.findViewById(R.id.btnReport);

            btnReport.setOnClickListener(v -> {
                sendReportOwner(dialog, owners.get(reservation.getAccommodationId()).getId());
            });
        }

    }

    private void sendAccommodationReview(Dialog dialog, Long accommodationId) {
        RatingBar accommodationRating = dialog.findViewById(R.id.ratingsAccommodation);
        TextInputEditText accommodationComment = dialog.findViewById(R.id.accommodationComment);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        Long guestId = sharedPreferences.getLong("id", 0L);
        String comment = accommodationComment.getText().toString();
        int rating = (int) accommodationRating.getRating();

        if (rating <= 0 || comment.equals("")) {
            Toast.makeText(getContext(), "Comment and rating is required", Toast.LENGTH_SHORT).show();
            return;
        }
        ReviewDTO reviewDTO = new ReviewDTO(comment, rating, guestId);
        Call<ReviewDTO> call = ClientUtils.reviewService.addAccommodationReview(accommodationId, reviewDTO);

        call.enqueue(new Callback<ReviewDTO>() {
            @Override
            public void onResponse(Call<ReviewDTO> call, Response<ReviewDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Your comment has been sent to admin for approval", Toast.LENGTH_SHORT).show();
                }
                if (response.code() == 400) {
                    Toast.makeText(getContext(), "You need to have a reservation to be able to comment on the accommodation", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReviewDTO> call, Throwable t) {
                Toast.makeText(getContext(), "You need to have a reservation to be able to comment on the accommodation", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendOwnerReview(Dialog dialog, Long ownerId) {
        RatingBar accommodationRating = dialog.findViewById(R.id.ratingsOwner);
        TextInputEditText accommodationComment = dialog.findViewById(R.id.ownerComment);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        Long guestId = sharedPreferences.getLong("id", 0L);
        String comment = accommodationComment.getText().toString();
        int rating = (int) accommodationRating.getRating();

        if (rating <= 0 || comment.equals("")) {
            Toast.makeText(getContext(), "Comment and rating is required", Toast.LENGTH_SHORT).show();
            return;
        }
        ReviewDTO reviewDTO = new ReviewDTO(comment, rating, guestId);
        Call<ReviewDTO> call = ClientUtils.reviewService.addOwnerReview(ownerId, reviewDTO);

        call.enqueue(new Callback<ReviewDTO>() {
            @Override
            public void onResponse(Call<ReviewDTO> call, Response<ReviewDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Your comment has been sent to admin for approval", Toast.LENGTH_SHORT).show();
                }
                if (response.code() == 400) {
                    Toast.makeText(getContext(), "You need to have a reservation to be able to comment on the owner", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReviewDTO> call, Throwable t) {
                Toast.makeText(getContext(), "You need to have a reservation to be able to comment on the owner", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendReportOwner(Dialog dialog, Long ownerId){
        TextInputEditText reason = dialog.findViewById(R.id.reason);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        Long guestId = sharedPreferences.getLong("id", 0L);
        String comment = reason.getText().toString();

        if (comment.equals("")) {
            Toast.makeText(getContext(), "Reason is required", Toast.LENGTH_SHORT).show();
            return;
        }
        ReportedUserDTO reportedUserDTO = new ReportedUserDTO(comment, new Date(), ownerId, guestId);
        Call<Long> call = ClientUtils.reviewService.reportUser(reportedUserDTO);

        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getContext(), "Successfully reported user", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "Cannot report user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Toast.makeText(getContext(), "Cannot report user", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
