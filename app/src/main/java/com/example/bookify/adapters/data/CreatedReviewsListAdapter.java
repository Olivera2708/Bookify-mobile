package com.example.bookify.adapters.data;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.example.bookify.model.review.ReviewAdminViewDTO;
import com.example.bookify.utils.JWTUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreatedReviewsListAdapter extends ArrayAdapter<ReviewAdminViewDTO> {

    private List<ReviewAdminViewDTO> createdReviews;
    private Activity activity;
    private Map<Long, Bitmap> accountImages;

    public CreatedReviewsListAdapter(@NonNull Activity context, List<ReviewAdminViewDTO> resource) {
        super(context, R.layout.review_admin,resource);
        this.activity = context;
        this.createdReviews = resource;
        this.accountImages = new HashMap<>();
    }

    @Override
    public int getCount() {
        return this.createdReviews.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Nullable
    @Override
    public ReviewAdminViewDTO getItem(int position) {
        return this.createdReviews.get(position);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ReviewAdminViewDTO review = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.review_admin, parent, false);
        }

        if (review != null) {
            ImageView accountImage = convertView.findViewById(R.id.userPicture);
            TextView name = convertView.findViewById(R.id.userName);
            TextView date = convertView.findViewById(R.id.date);
            RatingBar ratingBar = convertView.findViewById(R.id.stars);
            TextView comment = convertView.findViewById(R.id.commentText);
            Button btnReject = convertView.findViewById(R.id.button_reject);
            Button btnApprove = convertView.findViewById(R.id.button_approve);

            if (accountImages.containsKey(review.getGuest().getId())) {
                accountImage.setImageBitmap(accountImages.get(review.getGuest().getId()));
            } else {
                Call<ResponseBody> callGetImage = ClientUtils.accountService.getImage(review.getGuest().getImageId());
                callGetImage.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                            accountImage.setImageBitmap(bitmap);
                            accountImages.put(review.getGuest().getId(), bitmap);
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {

                    }
                });
            }

            name.setText(review.getGuest().getFirstName());
            date.setText(review.getDate());
            ratingBar.setRating(review.getRate());
            comment.setText(review.getComment());
            btnApprove.setOnClickListener(v -> {
                Call<ReviewAdminViewDTO> approveReviewCall = ClientUtils.reviewService.acceptReview(review.getId());
                approveReviewCall.enqueue(new Callback<ReviewAdminViewDTO>() {
                    @Override
                    public void onResponse(Call<ReviewAdminViewDTO> call, Response<ReviewAdminViewDTO> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            createdReviews.removeIf(r -> r.getId().equals(response.body().getId()));
                            Snackbar.make(parent, "Review approved", Snackbar.LENGTH_LONG).setAnchorView(R.id.bottom_navigaiton).show();
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReviewAdminViewDTO> call, Throwable t) {
                        JWTUtils.autoLogout((AppCompatActivity) activity, t);
                    }
                });

            });

            btnReject.setOnClickListener(v -> {
                Call<ReviewAdminViewDTO> declineReviewCall = ClientUtils.reviewService.declineReview(review.getId());
                declineReviewCall.enqueue(new Callback<ReviewAdminViewDTO>() {
                    @Override
                    public void onResponse(Call<ReviewAdminViewDTO> call, Response<ReviewAdminViewDTO> response) {
                        if (response.isSuccessful() && response.body() != null) {
                            createdReviews.removeIf(r -> r.getId().equals(response.body().getId()));
                            Snackbar.make(parent, "Review rejected", Snackbar.LENGTH_LONG).setAnchorView(R.id.bottom_navigaiton).show();
                            notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(Call<ReviewAdminViewDTO> call, Throwable t) {
                        JWTUtils.autoLogout((AppCompatActivity) activity, t);
                    }
                });
            });

        }

        return convertView;

    }
}