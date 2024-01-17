package com.example.bookify.adapters.data;

import android.app.Activity;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookify.R;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.enumerations.Status;
import com.example.bookify.model.reservation.ReservationDTO;
import com.example.bookify.utils.JWTUtils;
import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwnerRequestsListAdapter extends ArrayAdapter<ReservationDTO> {
    private List<ReservationDTO> requests;
    private Activity activity;
    private Map<Long, Bitmap> imageMap;

    public OwnerRequestsListAdapter(Activity context, List<ReservationDTO> requests){
        super(context, R.layout.accomodation_view);
        this.requests = requests;
        this.activity = context;
        imageMap = new HashMap<>();
    }

    @Override
    public int getCount() {
        return requests.size();
    }

    @Nullable
    @Override
    public ReservationDTO getItem(int position) {
        return requests.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ReservationDTO request = getItem(position);
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.accommodation_request_owner,
                parent, false);

        TextView name = convertView.findViewById(R.id.apartment_name);
        TextView guest = convertView.findViewById(R.id.guest);
        TextView guestCancel = convertView.findViewById(R.id.guestCancel);
        TextView date = convertView.findViewById(R.id.date);
        TextView persons = convertView.findViewById(R.id.persons);
        TextView price = convertView.findViewById(R.id.price);
        TextView status = convertView.findViewById(R.id.status);
        RatingBar stars = convertView.findViewById(R.id.starsRating);
        ImageView image = convertView.findViewById(R.id.image);
        Button accept = convertView.findViewById(R.id.accept);
        Button reject = convertView.findViewById(R.id.reject);

        if (request != null){
            stars.setRating((float) request.getAvgRating());
            name.setText(request.getAccommodationName());
            guest.setText("Guest: " + request.getUser().getFirstName() + " " + request.getUser().getLastName());
            guestCancel.setText("(Canceled " + request.getUser().getCancellationTimes() + " times" + ")");
            date.setText("Date: " + request.getStart() + " - " + request.getEnd());
            persons.setText("Persons: " + request.getGuestNumber());
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            price.setText("Income: " + decimalFormat.format(request.getPrice()) + " EUR");
            status.setText(request.getStatus().toString());

            if (imageMap.containsKey(request.getImageId())){
                image.setImageBitmap(imageMap.get(request.getImageId()));
            }
            else {
                Call<ResponseBody> call = ClientUtils.accommodationService.getImage(request.getImageId());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                            imageMap.put(request.getImageId(), bitmap);
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
            int buttonVisibility = request.getStatus().equals(Status.PENDING) ? Button.VISIBLE : Button.INVISIBLE;
            accept.setVisibility(buttonVisibility);
            reject.setVisibility(buttonVisibility);
            if(buttonVisibility == Button.VISIBLE){
                accept.setOnClickListener(v->{
                    Call<ReservationDTO> reservationAcceptCall = ClientUtils.reservationService.acceptReservation(request.getId());
                    reservationAcceptCall.enqueue(new Callback<ReservationDTO>() {
                        @Override
                        public void onResponse(Call<ReservationDTO> call, Response<ReservationDTO> response) {
                            if(response.isSuccessful() && response.body() != null){
                                ReservationDTO r = response.body();
                                requests.set(requests.indexOf(request), r);
                                Snackbar.make(parent, "Reservation request accepted", Snackbar.LENGTH_LONG).setAnchorView(R.id.bottom_navigaiton).show();
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
                        public void onFailure(Call<ReservationDTO> call, Throwable t) {
                            JWTUtils.autoLogout((AppCompatActivity) getContext(), t);
                        }
                    });
                });

                reject.setOnClickListener(v->{
                    Call<ReservationDTO> reservationRejectCall = ClientUtils.reservationService.rejectReservation(request.getId());
                    reservationRejectCall.enqueue(new Callback<ReservationDTO>() {
                        @Override
                        public void onResponse(Call<ReservationDTO> call, Response<ReservationDTO> response) {
                            if(response.isSuccessful() && response.body() != null){
                                ReservationDTO r = response.body();
                                requests.set(requests.indexOf(request), r);
                                Snackbar.make(parent, "Reservation request rejected", Snackbar.LENGTH_LONG).setAnchorView(R.id.bottom_navigaiton).show();
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
                        public void onFailure(Call<ReservationDTO> call, Throwable t) {
                            JWTUtils.autoLogout((AppCompatActivity) getContext(), t);
                        }
                    });
                });
            }
        }
        return convertView;
    }

    private void deleteRequest(Long id, int position){
        Call<ResponseBody> call = ClientUtils.reservationService.deleteRequest(id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Request deleted", Toast.LENGTH_SHORT).show();
                    requests.remove(position);
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Delete", "Error in deleting");
                JWTUtils.autoLogout((AppCompatActivity) getContext(), t);
            }
        });
    }
}
