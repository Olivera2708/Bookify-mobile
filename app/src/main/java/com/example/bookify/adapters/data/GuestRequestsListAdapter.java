package com.example.bookify.adapters.data;

import static android.app.PendingIntent.getActivity;

import android.app.Activity;
import android.content.Intent;
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
import com.example.bookify.activities.accommodation.AccommodationDetailsActivity;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.model.accommodation.AccommodationBasicDTO;
import com.example.bookify.model.reservation.ReservationDTO;
import com.example.bookify.utils.JWTUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuestRequestsListAdapter extends ArrayAdapter<ReservationDTO> {
    private List<ReservationDTO> requests;
    private Activity activity;
    private Map<Long, Bitmap> imageMap;

    public GuestRequestsListAdapter(Activity context, List<ReservationDTO> requests){
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
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.accommodation_request_guest,
                parent, false);

        TextView name = convertView.findViewById(R.id.apartment_name);
        TextView owner = convertView.findViewById(R.id.owner);
        TextView date = convertView.findViewById(R.id.date);
        TextView persons = convertView.findViewById(R.id.persons);
        TextView price = convertView.findViewById(R.id.price);
        TextView status = convertView.findViewById(R.id.status);
        RatingBar stars = convertView.findViewById(R.id.starsRating);
        ImageView image = convertView.findViewById(R.id.image);
        Button cancel = convertView.findViewById(R.id.cancel);

        if (request != null){
            stars.setRating((float) request.getAvgRating());
            name.setText(request.getAccommodationName());
            double avgRating = request.getUser().getAvgRating();
            String formattedAvgRating = String.format("%.2f", avgRating);
            owner.setText("Owner: " + request.getUser().getFirstName() + " " + request.getUser().getLastName() + " (" + formattedAvgRating + "/5)");
            date.setText("Date: " + request.getStart() + " - " + request.getEnd());
            persons.setText("Persons: " + request.getGuestNumber());
            price.setText("Price: " + request.getPrice() + " EUR");
            status.setText(request.getStatus().toString());

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    deleteRequest(request.getId(), position);
                }
            });

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
