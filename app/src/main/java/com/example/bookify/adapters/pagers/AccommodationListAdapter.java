package com.example.bookify.adapters.pagers;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationListAdapter extends ArrayAdapter<AccommodationBasicDTO> {
    private List<AccommodationBasicDTO> accommodations;
    private Activity activity;
    private Map<Long, Bitmap> imageMap;

    public AccommodationListAdapter(Activity context, List<AccommodationBasicDTO> accommodations){
        super(context, R.layout.accomodation_view, accommodations);
        this.accommodations = accommodations;
        this.activity = context;
        imageMap = new HashMap<>();
    }

    @Override
    public int getCount() {
        return accommodations.size();
    }

    @Nullable
    @Override
    public AccommodationBasicDTO getItem(int position) {
        return accommodations.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AccommodationBasicDTO accommodation = getItem(position);
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.accomodation_view,
                parent, false);

        TextView name = convertView.findViewById(R.id.apartment_name);
        TextView address = convertView.findViewById(R.id.apartment_address);
        TextView type = convertView.findViewById(R.id.acc_type);
        RatingBar start = convertView.findViewById(R.id.stars);
        TextView price = convertView.findViewById(R.id.price);
        TextView pricePer = convertView.findViewById(R.id.priceperday);
        Button details = convertView.findViewById(R.id.details);
        ImageView image = convertView.findViewById(R.id.acc_image);

        if(accommodation != null){
            name.setText(accommodation.getName());
            address.setText(accommodation.getAddress().toString());
            price.setText(String.valueOf(accommodation.getTotalPrice()));
            pricePer.setText(accommodation.getPriceOne() + " per " + accommodation.getPricePer().toString().toLowerCase());
            start.setRating(accommodation.getAvgRating());
            type.setText(accommodation.getType().toString().substring(0, 1) + accommodation.getType().toString().substring(1, accommodation.getType().toString().length()).toLowerCase());

            details.setOnClickListener(v -> {
                Log.i("Test", "Otvori aaccommodation broj " + accommodation.getId());
                Intent intent = new Intent(activity, AccommodationDetailsActivity.class);
                intent.putExtra("id", accommodation.getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                activity.startActivity(intent);
                activity.overridePendingTransition(0, 0);
            });

            if (imageMap.containsKey(accommodation.getImageId())){
                image.setImageBitmap(imageMap.get(accommodation.getImageId()));
            }
            else {
                Call<ResponseBody> call = ClientUtils.accommodationService.getImage(accommodation.getImageId());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                            imageMap.put(accommodation.getImageId(), bitmap);
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
        return convertView;
    }

    public void addData(List<AccommodationBasicDTO> newData) {
        accommodations.addAll(newData);
        notifyDataSetChanged();
    }
}
