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
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookify.R;
import com.example.bookify.activities.accommodation.AccommodationDetailsActivity;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.model.accommodation.AccommodationBasicDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Context;

public class TopAccommodationsAdapter extends RecyclerView.Adapter<TopAccommodationsAdapter.ViewHolder> {
    private final Context context;
    private final List<AccommodationBasicDTO> accommodations;
    private Map<Long, Bitmap> imageMap;

    public TopAccommodationsAdapter(Context context, List<AccommodationBasicDTO> accommodations) {
        this.context = context;
        this.accommodations = accommodations;
        imageMap = new HashMap<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.popular_accommodation_guest, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AccommodationBasicDTO accommodation = accommodations.get(position);
        holder.address.setText(accommodation.getAddress().toString());
        holder.stars.setRating(accommodation.getAvgRating());
        holder.name.setText(accommodation.getName());

        holder.details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AccommodationDetailsActivity.class);
                intent.putExtra("id", accommodation.getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                context.startActivity(intent);
            }
        });

        if (imageMap.containsKey(accommodation.getImageId())){
            holder.image.setImageBitmap(imageMap.get(accommodation.getImageId()));
        }
        else {
            Call<ResponseBody> call = ClientUtils.accommodationService.getImage(accommodation.getImageId());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                        imageMap.put(accommodation.getImageId(), bitmap);
                        holder.image.setImageBitmap(bitmap);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("Image", "Basic accommodation image");
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return accommodations.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public RatingBar stars;
        public TextView name;
        public TextView address;
        public Button details;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
            stars = itemView.findViewById(R.id.stars);
            name = itemView.findViewById(R.id.apartment_name);
            address = itemView.findViewById(R.id.apartment_address);
            details = itemView.findViewById(R.id.details);
        }
    }
}
