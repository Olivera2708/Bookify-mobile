package com.example.bookify.adapters.data;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.example.bookify.R;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.model.user.ReportedUserDetailsDTO;
import com.example.bookify.model.user.UserDTO;
import com.example.bookify.utils.JWTUtils;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportedUsersListAdapter extends ArrayAdapter<ReportedUserDetailsDTO> {
    private Activity activity;
    private List<ReportedUserDetailsDTO> reportedUsers;

    private Map<Long, Bitmap> accountImages;


    public ReportedUsersListAdapter(@NonNull Activity context, List<ReportedUserDetailsDTO> resource) {
        super(context, R.layout.reported_user, resource);
        this.activity = context;
        this.reportedUsers = resource;
        accountImages = new HashMap<>();
    }

    @Nullable
    @Override
    public ReportedUserDetailsDTO getItem(int position) {
        return reportedUsers.get(position);
    }

    @Override
    public int getCount() {
        return this.reportedUsers.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ReportedUserDetailsDTO reportedUser = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.reported_user, parent, false);
        }
        ImageView accountImage = convertView.findViewById(R.id.account_icon);
        TextView firstAndLastName = convertView.findViewById(R.id.first_and_last_name);
        TextView email = convertView.findViewById(R.id.email);
        TextView reason = convertView.findViewById(R.id.report_reason);
        TextView reportedByEmail = convertView.findViewById(R.id.reported_by_email);
        TextView reportedByFirstAndLastName = convertView.findViewById(R.id.reported_by_first_and_last_name);
        Button btnBlock = convertView.findViewById(R.id.button_block);
        if (reportedUser != null) {
            firstAndLastName.setText(reportedUser.getReportedUser().getFirstName() + " " + reportedUser.getReportedUser().getLastName());
            email.setText(reportedUser.getReportedUser().getEmail());
            reason.setText(reportedUser.getReason());
            reportedByFirstAndLastName.setText(reportedUser.getCreatedBy().getFirstName() + " " + reportedUser.getCreatedBy().getLastName());
            reportedByEmail.setText(reportedUser.getCreatedBy().getEmail());
            if (accountImages.containsKey(reportedUser.getReportedUser().getId())) {
                accountImage.setImageBitmap(accountImages.get(reportedUser.getReportedUser().getId()));
            } else {
                if (reportedUser.getReportedUser().getImageId() != null) {
                    Call<ResponseBody> call = ClientUtils.accountService.getImage(reportedUser.getReportedUser().getImageId());
                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.body() != null && response.isSuccessful()){
                                Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                                accountImage.setImageBitmap(bitmap);
                                accountImages.put(reportedUser.getReportedUser().getId(), bitmap);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            JWTUtils.autoLogout((AppCompatActivity) activity, t);
                        }
                    });
                }
            }
            btnBlock.setOnClickListener(v -> {
                Call<UserDTO> blockCall = ClientUtils.accountService.blockUser(reportedUser.getReportedUser().getId());
                blockCall.enqueue(new Callback<UserDTO>() {
                    @Override
                    public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                        if(response.isSuccessful() && response.code() == 200) {
                            reportedUsers.removeIf(r -> r.getReportedUser().getId().equals(response.body().getId()));
                            notifyDataSetChanged();
                        }
                        if(response.code() == 400){
                            Snackbar.make(parent, "User is already blocked, refresh page", Snackbar.LENGTH_LONG).setAnchorView(R.id.bottom_navigaiton).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<UserDTO> call, Throwable t) {
                        JWTUtils.autoLogout((AppCompatActivity) activity, t);
                    }
                });
            });
        }

        return convertView;
    }
}
