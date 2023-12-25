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
import com.example.bookify.model.accommodation.AccommodationOwnerDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwnerAccommodationListAdapter extends ArrayAdapter<AccommodationOwnerDTO> {
    private List<AccommodationOwnerDTO> accommodations;
    private Activity activity;
    private Map<Long, Bitmap> images;

    public OwnerAccommodationListAdapter(@NonNull Activity context, List<AccommodationOwnerDTO> resource) {
        super(context, R.layout.accommodation_owner_view, resource);
        this.activity = context;
        this.accommodations = resource;
        this.images = new HashMap<>();
    }

    @Override
    public int getCount() {
        return this.accommodations.size();
    }

    @Nullable
    @Override
    public AccommodationOwnerDTO getItem(int position) {
        return this.accommodations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AccommodationOwnerDTO accommodation = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.accommodation_owner_view, parent, false);
        }

        TextView name = convertView.findViewById(R.id.apartment_name);
        TextView address = convertView.findViewById(R.id.apartment_address);
        ImageView image = convertView.findViewById(R.id.accommodation_image);
        Button btnEditAccommodation = convertView.findViewById(R.id.btnEditAccommodation);
        Button btnEditPrice = convertView.findViewById(R.id.btnEditPrice);
        RatingBar ratingBar = convertView.findViewById(R.id.starsRating);
        TextView type = convertView.findViewById(R.id.apartment_type);
        TextView status = convertView.findViewById(R.id.apartment_status);
        if (accommodation != null) {
            name.setText(accommodation.getName());
            address.setText(accommodation.getAddress().toString());
            ratingBar.setRating(accommodation.getAvgRating());
            type.setText(accommodation.getAccommodationType().toString());
            status.setText(accommodation.getStatusRequest().toString());
            btnEditPrice.setOnClickListener(v -> {
                // open for price editing
            });

            btnEditAccommodation.setOnClickListener(v -> {
                // open for accommodation editing
            });

            name.setOnClickListener(v -> {
                Intent intent = new Intent(activity, AccommodationDetailsActivity.class);
                intent.putExtra("id", accommodation.getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.startActivity(intent);
                activity.overridePendingTransition(0, 0);
            });

            if (images.containsKey(accommodation.getImageId())) {
                image.setImageBitmap(images.get(accommodation.getImageId()));
            } else {
                Call<ResponseBody> getImageCall = ClientUtils.accommodationService.getImage(accommodation.getImageId());
                getImageCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                            images.put(accommodation.getImageId(), bitmap);
                            image.setImageBitmap(bitmap);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("OOPS", "Oops something went wrong");
                    }
                });
            }
        }

        return convertView;
    }
}
