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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bookify.R;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.model.user.UserDTO;
import com.example.bookify.utils.JWTUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllUsersAdapter extends ArrayAdapter<UserDTO> {
    private List<UserDTO> users;
    private List<UserDTO> filteredUsers;
    private Activity activity;
    private Map<Long, Bitmap> accountImages;
    public AllUsersAdapter(@NonNull Activity context, List<UserDTO> resource) {
        super(context, R.layout.user, resource);
        this.users = resource;
        filteredUsers = (ArrayList<UserDTO>) ((ArrayList<UserDTO>) this.users).clone();
        this.activity = context;
        this.accountImages = new HashMap<>();
    }

    @Override
    public int getCount() {return this.filteredUsers.size(); }

    @Nullable
    @Override
    public UserDTO getItem(int position) {
        return this.filteredUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        UserDTO user = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.user, parent, false);
        }
        TextView firstAndLastName = convertView.findViewById(R.id.first_and_last_name);
        TextView email = convertView.findViewById(R.id.email);
        Button btnBlock = convertView.findViewById(R.id.button_block);
        ImageView accImage = convertView.findViewById(R.id.account_icon);
        if(user != null){
            firstAndLastName.setText(user.getFirstName() + " " + user.getLastName());
            email.setText(user.getEmail());
        }

        if(accountImages.containsKey(user.getImageId())) {
            accImage.setImageBitmap(accountImages.get(user.getImageId()));
        } else {
            Call<ResponseBody> imageCall = ClientUtils.accountService.getImage(user.getImageId());
            imageCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.isSuccessful() && response.body() != null){
                        Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                        accountImages.put(user.getImageId(), bitmap);
                        accImage.setImageBitmap(bitmap);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("OOPS", "Oops something went wrong");
                }
            });
        }
        String blockText = user.isBlocked() ? "Unblock" : "Block";
        btnBlock.setText(blockText);
        btnBlock.setOnClickListener(v -> {
            blockUser(position, btnBlock);
        });

        return convertView;
    }

    private void blockUser(int position, Button button){
        UserDTO user = getItem(position);
        Call<UserDTO> call = user.isBlocked() ? ClientUtils.accountService.unblockUser(user.getId()) : ClientUtils.accountService.blockUser(user.getId());
        call.enqueue(new Callback<UserDTO>() {
            @Override
            public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                if(response.isSuccessful() && response.code() == 200){
                    UserDTO u = response.body();
                    user.setBlocked(u.isBlocked());
//                    filteredUsers.set(position, response.body());
                    String blockText = u.isBlocked() ? "Unblock" : "Block";
                    button.setText(blockText);
                }
            }

            @Override
            public void onFailure(Call<UserDTO> call, Throwable t) {
                JWTUtils.autoLogout((AppCompatActivity) activity, t);
            }
        });
    }
    public void filterUsers(String searchPara){
        this.filteredUsers.clear();
        this.users.forEach(u -> {
            if((u.getFirstName() + " " + u.getLastName() + " " + u.getEmail()).contains(searchPara)) this.filteredUsers.add(u);
        });

        notifyDataSetChanged();
    }
}
