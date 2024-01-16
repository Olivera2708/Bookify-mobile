package com.example.bookify.adapters.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookify.R;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.model.OwnerDTO;
import com.example.bookify.model.accommodation.AccommodationDetailDTO;
import com.example.bookify.model.reservation.ReservationGuestViewDTO;
import com.example.bookify.utils.JWTUtils;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
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

        if(reservation != null) {
            name.setText(reservation.getAccommodationName());
            date.setText("Date:" + reservation.getStart() + " - " + reservation.getEnd());
            persons.setText("Persons: " + reservation.getGuestNumber());
            price.setText("Cost: " + reservation.getPrice());
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

        return convertView;
    }
}
