package com.example.bookify.adapters.data;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bookify.R;
import com.example.bookify.activities.accommodation.AccommodationDetailsActivity;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.model.accommodation.AccommodationBasicDTO;
import com.example.bookify.model.reservation.ReservationDTO;

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
            owner.setText("Owner: " + request.getUser().getFirstName() + " " + request.getUser().getLastName() + " (" + request.getUser().getAvgRating() + "/5)");
            date.setText("Date: " + request.getStart() + " - " + request.getEnd());
            persons.setText("Persons: " + request.getGuestNumber());
            price.setText("Price: " + request.getPrice() + " EUR");
            status.setText(request.getStatus().toString());

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //cancel
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
                    }
                });
            }
        }

//        if(accommodation != null){
//            name.setText(accommodation.getName());
//            address.setText(accommodation.getAddress().toString());
//            price.setText(String.valueOf(accommodation.getTotalPrice()));
//            pricePer.setText(accommodation.getPriceOne() + " per " + accommodation.getPricePer().toString().toLowerCase());
//            start.setRating(accommodation.getAvgRating());
//            type.setText(accommodation.getType().toString().substring(0, 1) + accommodation.getType().toString().substring(1, accommodation.getType().toString().length()).toLowerCase());
//
//            details.setOnClickListener(v -> {
//                Log.i("Test", "Otvori aaccommodation broj " + accommodation.getId());
//                Intent intent = new Intent(activity, AccommodationDetailsActivity.class);
//                intent.putExtra("id", accommodation.getId());
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                activity.startActivity(intent);
//                activity.overridePendingTransition(0, 0);
//            });
//
//            if (imageMap.containsKey(accommodation.getImageId())){
//                image.setImageBitmap(imageMap.get(accommodation.getImageId()));
//            }
//            else {
//                Call<ResponseBody> call = ClientUtils.accommodationService.getImage(accommodation.getImageId());
//                call.enqueue(new Callback<ResponseBody>() {
//                    @Override
//                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                        if (response.isSuccessful() && response.body() != null) {
//                            Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
//                            imageMap.put(accommodation.getImageId(), bitmap);
//                            image.setImageBitmap(bitmap);
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseBody> call, Throwable t) {
//                        Log.d("Image", "Basic accommodation image");
//                    }
//                });
//            }
//        }
        return convertView;
    }
}
