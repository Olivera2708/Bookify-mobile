package com.example.bookify.activities.user;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;

import com.example.bookify.activities.LandingActivity;
import com.example.bookify.adapters.data.NotificationListAdapter;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.model.NotificationDTO;
import com.example.bookify.navigation.NavigationBar;
import com.example.bookify.R;
import com.example.bookify.utils.JWTUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private List<NotificationDTO> notifications;
    private ListView notificationsListView;
    private NotificationListAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        NavigationBar.setNavigationBar(findViewById(R.id.bottom_navigaiton), this, R.id.navigation_notifications);
        this.sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE);
        View buttonSettings = findViewById(R.id.button_settings);
        notificationsListView = findViewById(R.id.notification_scroller);
        buttonSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(NotificationActivity.this, LandingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
        getNotifications();
    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.settings);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);
    }
    private void getNotifications(){
        Long userId = sharedPreferences.getLong(JWTUtils.USER_ID, -1);
        Call<List<NotificationDTO>> getNotificationsCall = ClientUtils.notificationService.getUserNotifications(userId);
        getNotificationsCall.enqueue(new Callback<List<NotificationDTO>>() {
            @Override
            public void onResponse(Call<List<NotificationDTO>> call, Response<List<NotificationDTO>> response) {
                if(response.isSuccessful() && response.body() != null){
                    notifications = response.body();
                    adapter = new NotificationListAdapter(NotificationActivity.this, notifications);
                    notificationsListView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<NotificationDTO>> call, Throwable t) {
                JWTUtils.autoLogout(NotificationActivity.this, t);
            }
        });
    }

}