package com.example.bookify.adapters.data;

import android.app.Activity;
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
import com.example.bookify.model.ReportedUserDTO;
import com.example.bookify.model.reservation.ReservationDTO;
import com.example.bookify.utils.JWTUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;
import java.util.Date;
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
    private Activity activity;
    public OwnerReservationListAdapter(@NonNull Context context, List<ReservationDTO> resource) {
        super(context, R.layout.reservations_guest, resource);
        this.reservations = resource;
        imageMap = new HashMap<>();
        activity = (Activity) getContext();
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
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            price.setText("Income: " + decimalFormat.format(reservation.getPrice()) + " EUR");
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
                ShowDialog(R.layout.report, reservation);
            });
        }

        return convertView;
    }
    private void ShowDialog(int id, ReservationDTO reservation) {
        final Dialog dialog = new Dialog(getContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(id);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);

        TextView reportGuest = dialog.findViewById(R.id.reportGuest);
        reportGuest.setVisibility(View.VISIBLE);
        Button btnReport = dialog.findViewById(R.id.btnReport);

        btnReport.setOnClickListener(v -> {
            sendReportOwner(dialog, reservation.getUser().getId());
        });
    }

    private void sendReportOwner(Dialog dialog, Long guestId){
        TextInputEditText reason = dialog.findViewById(R.id.reason);

        SharedPreferences sharedPreferences = getContext().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        Long ownerId = sharedPreferences.getLong("id", 0L);
        String comment = reason.getText().toString();

        if (comment.equals("")) {
            Toast.makeText(getContext(), "Reason is required", Toast.LENGTH_SHORT).show();
            return;
        }
        ReportedUserDTO reportedUserDTO = new ReportedUserDTO(comment, new Date(), guestId, ownerId);
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
