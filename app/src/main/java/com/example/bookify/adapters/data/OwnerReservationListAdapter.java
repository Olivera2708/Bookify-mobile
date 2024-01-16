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
import com.example.bookify.model.reservation.ReservationDTO;
import com.example.bookify.utils.JWTUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwnerReservationListAdapter extends ArrayAdapter<ReservationDTO> {
    private List<ReservationDTO> reservations;
    private Map<Long, Bitmap> imageMap;
    public OwnerReservationListAdapter(@NonNull Context context, List<ReservationDTO> resource) {
        super(context, R.layout.reservations_guest, resource);
        this.reservations = resource;
        imageMap = new HashMap<>();
    }
    @Nullable
    @Override
    public ReservationDTO getItem(int position) {
        return this.reservations.get(position);
    }

    @Override
    public int getCount() {
        return this.reservations.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ReservationDTO reservation = getItem(position);
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.reservations_guest, parent, false);

        TextView name = convertView.findViewById(R.id.apartment_name);
        TextView guest = convertView.findViewById(R.id.guest);
        TextView date = convertView.findViewById(R.id.date);
        TextView persons = convertView.findViewById(R.id.persons);
        TextView price = convertView.findViewById(R.id.price);
        TextView status = convertView.findViewById(R.id.status);
        RatingBar stars = convertView.findViewById(R.id.starsRating);
        ImageView image = convertView.findViewById(R.id.image);
        Button report = convertView.findViewById(R.id.btnReport);

        if(reservation != null) {
            name.setText(reservation.getAccommodationName());
            date.setText("Date:" + reservation.getStart() + " - " + reservation.getEnd());
            persons.setText("Persons: " + reservation.getGuestNumber());
            price.setText("Cost: " + reservation.getPrice());
            stars.setRating((float) reservation.getAvgRating());
            status.setText("Status: " + reservation.getStatus().name());
            guest.setText("Guest: " + reservation.getUser().getFirstName() + " " + reservation.getUser().getLastName());
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
            report.setOnClickListener(v -> {
                // Add code here
            });
        }

        return convertView;
    }
}
