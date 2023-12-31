package com.example.bookify.activities.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookify.activities.accommodation.AccommodationDetailsActivity;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.model.CommentDTO;
import com.example.bookify.model.RatingDTO;
import com.example.bookify.model.UserBasicDTO;
import com.example.bookify.navigation.NavigationBar;
import com.example.bookify.R;

import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OwnerDetailsActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private Long ownerId;
    private Long userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_details);
        NavigationBar.setNavigationBar(findViewById(R.id.bottom_navigaiton), this, R.id.navigation_home);

        this.sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE);

        Intent intent = getIntent();
        ownerId = intent.getLongExtra("ownerId", 0);
        userId = sharedPreferences.getLong("id", 0L);


        setOwner(ownerId);

        setReviews(ownerId);

    }

    private void setOwner(Long id) {
        Call<UserBasicDTO> call = ClientUtils.reviewService.getUserBasic(id);
        call.enqueue(new Callback<UserBasicDTO>() {
            @Override
            public void onResponse(Call<UserBasicDTO> call, Response<UserBasicDTO> response) {
                if (response.code() == 200 && response.body() != null) {
                    UserBasicDTO dto = response.body();
                    setData(dto);
                }
            }

            @Override
            public void onFailure(Call<UserBasicDTO> call, Throwable t) {

            }
        });
    }

    private void setData(UserBasicDTO user) {
        TextView firstName = findViewById(R.id.first_name);
        TextView lastName = findViewById(R.id.last_name);
        TextView phone = findViewById(R.id.phone);
        ImageView image = findViewById(R.id.profile_pic);

        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());
        phone.setText(user.getPhone());
        setImage(user.getImageId(), image);
    }

    private void setReviews(Long id) {
        Call<List<CommentDTO>> call = ClientUtils.reviewService.getOwnerComments(id);
        call.enqueue(new Callback<List<CommentDTO>>() {
            @Override
            public void onResponse(Call<List<CommentDTO>> call, Response<List<CommentDTO>> response) {
                if (response.code() == 200 && response.body() != null) {
                    List<CommentDTO> comments = response.body();
                    setComments(comments, id);
                }
            }

            @Override
            public void onFailure(Call<List<CommentDTO>> call, Throwable t) {

            }
        });

        Call<RatingDTO> callRating = ClientUtils.reviewService.getOwnerRating(id);
        callRating.enqueue(new Callback<RatingDTO>() {
            @Override
            public void onResponse(Call<RatingDTO> call, Response<RatingDTO> response) {
                if (response.code() == 200 && response.body() != null) {
                    RatingDTO ratingDTO = response.body();
                    setRating(ratingDTO);
                }
            }

            @Override
            public void onFailure(Call<RatingDTO> call, Throwable t) {

            }
        });
    }

    private void setRating(RatingDTO ratingDTO) {
        String avgRating = String.format("%.2f", ratingDTO.getAvgRating());

        RatingBar rating = findViewById(R.id.rating);
        rating.setRating((float) ratingDTO.getAvgRating());

        ProgressBar progres5 = findViewById(R.id.progress_5);
        ProgressBar progres4 = findViewById(R.id.progress_4);
        ProgressBar progres3 = findViewById(R.id.progress_3);
        ProgressBar progres2 = findViewById(R.id.progress_2);
        ProgressBar progres1 = findViewById(R.id.progress_1);

        TextView total_5 = findViewById(R.id.total_5);
        TextView total_4 = findViewById(R.id.total_4);
        TextView total_3 = findViewById(R.id.total_3);
        TextView total_2 = findViewById(R.id.total_2);
        TextView total_1 = findViewById(R.id.total_1);

        TextView total = findViewById(R.id.total);
        TextView total1 = findViewById(R.id.total1);

        int count = ratingDTO.getFiveStars() + ratingDTO.getFourStars() + ratingDTO.getThreeStars() +
                ratingDTO.getTwoStars() + ratingDTO.getOneStars();

        int sum = ratingDTO.getFiveStars() * 5 + ratingDTO.getFourStars() * 4 + ratingDTO.getThreeStars() * 3 +
                ratingDTO.getTwoStars() * 2 + ratingDTO.getOneStars();

        total.setText("(" + count + ")");
        total1.setText(avgRating);

        if (count > 0) {
            progres5.setProgress(ratingDTO.getFiveStars() * 100 / count);
            progres4.setProgress(ratingDTO.getFourStars() * 100 / count);
            progres3.setProgress(ratingDTO.getThreeStars() * 100 / count);
            progres2.setProgress(ratingDTO.getTwoStars() * 100 / count);
            progres1.setProgress(ratingDTO.getOneStars() * 100 / count);
        }

        total_1.setText("(" + ratingDTO.getOneStars() + ")");
        total_2.setText("(" + ratingDTO.getTwoStars() + ")");
        total_3.setText("(" + ratingDTO.getThreeStars() + ")");
        total_4.setText("(" + ratingDTO.getFourStars() + ")");
        total_5.setText("(" + ratingDTO.getFiveStars() + ")");
    }

    private void setComments(List<CommentDTO> comments, Long id) {
        LinearLayout commentSection = findViewById(R.id.comment_section);
        commentSection.removeAllViews();
        for (CommentDTO comment : comments) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View commentView = inflater.inflate(R.layout.comment, commentSection, false);

            setComment(comment, commentView, id);
            commentSection.addView(commentView);
        }
    }

    private void setComment(CommentDTO comment, View commentView, Long id) {
        TextView name = commentView.findViewById(R.id.userName);
        TextView date = commentView.findViewById(R.id.date);
        ImageView image = commentView.findViewById(R.id.userPicture);
        RatingBar stars = commentView.findViewById(R.id.stars);
        TextView commentText = commentView.findViewById(R.id.commentText);

        Button report = commentView.findViewById(R.id.btnReport);
        ImageView delete = commentView.findViewById(R.id.deleteComment);

        setImage(comment.getImageId(), image);

        stars.setRating((float) comment.getRate());
        name.setText(comment.getName());
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd.MM.yyyy.", Locale.getDefault());
        date.setText(dateFormat.format(comment.getDate()));
        commentText.setText(comment.getComment());

        if (ownerId == userId) {
            report.setVisibility(View.VISIBLE);
            report.setOnClickListener(v -> {
                Call<Long> call = ClientUtils.reviewService.reportComment(comment.getId());
                call.enqueue(new Callback<Long>() {
                    @Override
                    public void onResponse(Call<Long> call, Response<Long> response) {
                        Toast.makeText(OwnerDetailsActivity.this, "Review reported", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<Long> call, Throwable t) {

                    }
                });
            });
        }

        if (comment.getGuestId() == userId) {
            delete.setVisibility(View.VISIBLE);
            delete.setOnClickListener(v -> {
                Call<Void> callDelete = ClientUtils.reviewService.deleteOwnerReview(id, comment.getId());
                callDelete.enqueue(new Callback() {
                    @Override
                    public void onResponse(Call call, Response response) {
                        Toast.makeText(OwnerDetailsActivity.this, "Successfully deleted review", Toast.LENGTH_SHORT).show();
                        setReviews(id);
                    }

                    @Override
                    public void onFailure(Call call, Throwable t) {

                    }
                });
            });
        }
    }

    private void setImage(Long imageId, ImageView image) {
        Call<ResponseBody> callImage = ClientUtils.accommodationService.getImage(imageId);
        callImage.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                    image.setImageBitmap(getRoundedBitmap(bitmap));
                } else {
                    image.setImageResource(R.drawable.account_round);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("Image", "Basic accommodation image");
//                JWTUtils.autoLogout((AppCompatActivity) getContext(), t);
            }
        });
    }

    private Bitmap getRoundedBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int radius = Math.min(width, height) / 2;

        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setShader(shader);

        Bitmap roundedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(roundedBitmap);
        canvas.drawCircle(width / 2, height / 2, radius, paint);

        return roundedBitmap;
    }

}