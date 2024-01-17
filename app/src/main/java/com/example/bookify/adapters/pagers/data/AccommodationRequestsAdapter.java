package com.example.bookify.adapters.pagers.data;

import android.app.Activity;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.example.bookify.R;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.model.accommodation.AccommodationRequestDTO;
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

public class AccommodationRequestsAdapter extends ArrayAdapter<AccommodationRequestDTO> {
    private List<AccommodationRequestDTO> requests;
    private Map<Long, Bitmap> images;
    private Activity activity;

    public AccommodationRequestsAdapter(@NonNull FragmentActivity context, List<AccommodationRequestDTO> requests) {
        super(context, R.layout.accommodation_request_admin, requests);
        this.requests = requests;
        this.activity = context;
        images = new HashMap<>();
    }

    @Override
    public int getCount() {
        return requests.size();
    }

    @Nullable
    @Override
    public AccommodationRequestDTO getItem(int position) {
        return requests.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AccommodationRequestDTO request = getItem(position);

        convertView = LayoutInflater.from(getContext()).inflate(R.layout.accommodation_request_admin, parent, false);

        TextView ownerEmail = convertView.findViewById(R.id.owner_email);
        TextView ownerFirstAndLastName = convertView.findViewById(R.id.owner_first_and_last_name);
        TextView name = convertView.findViewById(R.id.accommodation_name);
        TextView status = convertView.findViewById(R.id.status);
        ImageView image = convertView.findViewById(R.id.acc_image);
        Button btnApprove = convertView.findViewById(R.id.button_approve);
        Button btnReject = convertView.findViewById(R.id.button_reject);

        if (request != null) {
            String firstAndLastName = request.getOwner().getFirstName() + " " + request.getOwner().getLastName();
            ownerEmail.setText(request.getOwner().getEmail());
            ownerFirstAndLastName.setText(firstAndLastName);
            name.setText(request.getName());
            status.setText(request.getStatusRequest().toString());

            btnApprove.setOnClickListener(v -> {
                Call<ResponseBody> call = ClientUtils.accommodationService.approveAccommodation(request.getId());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() == 200 && response.body() != null) {
                            requests.remove(position);
                            notifyDataSetChanged();
                            Snackbar.make(parent, "Accommodation " + request.getName() + " approved", Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        JWTUtils.autoLogout((AppCompatActivity) activity, t);
                    }
                });
            });

            btnReject.setOnClickListener(v -> {
                Call<ResponseBody> call = ClientUtils.accommodationService.rejectAccommodation(request.getId());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.code() == 200 && response.body() != null) {
                            requests.remove(position);
                            notifyDataSetChanged();
                            Snackbar.make(parent, "Accommodation " + request.getName() + " rejected", Snackbar.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        JWTUtils.autoLogout((AppCompatActivity) activity, t);
                    }
                });
            });

            if (images.containsKey(request.getImageId())) {
                image.setImageBitmap(images.get(request.getImageId()));
            } else {
                Call<ResponseBody> getImageCall = ClientUtils.accommodationService.getImage(request.getImageId());
                getImageCall.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                            images.put(request.getImageId(), bitmap);
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
