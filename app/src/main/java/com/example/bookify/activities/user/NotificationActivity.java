package com.example.bookify.activities.user;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import com.example.bookify.BuildConfig;
import com.example.bookify.activities.LandingActivity;
import com.example.bookify.adapters.data.NotificationListAdapter;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.databinding.SettingsBinding;
import com.example.bookify.enumerations.NotificationType;
import com.example.bookify.model.NotificationDTO;
import com.example.bookify.model.NotificationSettingsDTO;
import com.example.bookify.navigation.NavigationBar;
import com.example.bookify.R;
import com.example.bookify.services.NotificationsForegroundService;
import com.example.bookify.utils.JWTUtils;
import com.example.bookify.utils.StompUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;
import ua.naiksoftware.stomp.dto.StompHeader;

public class NotificationActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private List<NotificationDTO> notifications;
    private ListView notificationsListView;
    private NotificationListAdapter adapter;
    private SettingsBinding settingsBinding;
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
        settingsBinding = SettingsBinding.inflate(getLayoutInflater());
        dialog.setContentView(settingsBinding.getRoot());
        setSettings(dialog);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);
    }
    private void getNotifications() {
        Long userId = sharedPreferences.getLong(JWTUtils.USER_ID, -1);
        Call<List<NotificationDTO>> getNotificationsCall = ClientUtils.notificationService.getUserNotifications(userId);
        getNotificationsCall.enqueue(new Callback<List<NotificationDTO>>() {
            @Override
            public void onResponse(Call<List<NotificationDTO>> call, Response<List<NotificationDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
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
    private void setSettings(Dialog dialog) {
        String userRole = sharedPreferences.getString(JWTUtils.USER_ROLE, "none");
        setDialogView(userRole);
        getSettings(userRole);
        settingsBinding.applyChanges.setOnClickListener(v -> {
            NotificationSettingsDTO settings = new NotificationSettingsDTO(new HashMap<>());
            if(userRole.equals("GUEST")){
                settings.getNotificationType().put(NotificationType.RESERVATION_RESPONSE,settingsBinding.notificationsSwitch.isChecked());
            }
            else {
                settings.getNotificationType().put(NotificationType.NEW_USER_RATING,settingsBinding.userRatingSwitch.isChecked());
                settings.getNotificationType().put(NotificationType.NEW_ACCOMMODATION_RATING,settingsBinding.accommodationRatingSwitch.isChecked());
                settings.getNotificationType().put(NotificationType.RESERVATION_CREATED,settingsBinding.reservationCreatedSwitch.isChecked());
                settings.getNotificationType().put(NotificationType.RESERVATION_CANCELED,settingsBinding.reservationCancelledSwitch.isChecked());

            }
            Call<NotificationSettingsDTO> setUserNotificaitonSettingsCall = ClientUtils.notificationService.setUserNotificationSettings(sharedPreferences.getLong(JWTUtils.USER_ID,-1), settings);
            setUserNotificaitonSettingsCall.enqueue(new Callback<NotificationSettingsDTO>() {
                @Override
                public void onResponse(Call<NotificationSettingsDTO> call, Response<NotificationSettingsDTO> response) {
                    if(response.code() == 200 && response.isSuccessful()) {
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<NotificationSettingsDTO> call, Throwable t) {
                    JWTUtils.autoLogout(NotificationActivity.this, t);
                }
            });
        });
    }

    private void setDialogView(String userRole) {
        if(userRole.equals("GUEST")){
            settingsBinding.accommodationRatingSwitch.setVisibility(View.GONE);
            settingsBinding.reservationCreatedSwitch.setVisibility(View.GONE);
            settingsBinding.reservationCancelledSwitch.setVisibility(View.GONE);
            settingsBinding.userRatingSwitch.setVisibility(View.GONE);

        } else {
            settingsBinding.notificationsSwitch.setVisibility(View.GONE);
        }
    }

    private void getSettings(String userRole){
        Call<NotificationSettingsDTO> getSettingsCall = ClientUtils.notificationService.getUserNotificationSettings(sharedPreferences.getLong(JWTUtils.USER_ID,-1));
        getSettingsCall.enqueue(new Callback<NotificationSettingsDTO>() {
            @Override
            public void onResponse(Call<NotificationSettingsDTO> call, Response<NotificationSettingsDTO> response) {
                if(response.code() == 200 && response.isSuccessful()){
                    NotificationSettingsDTO settings = response.body();
                    if(userRole.equals("GUEST")){
                        settingsBinding.notificationsSwitch.setChecked(settings.getNotificationType().getOrDefault(
                                NotificationType.RESERVATION_RESPONSE,false));
                    }
                    else {
                        settingsBinding.userRatingSwitch.setChecked(settings.getNotificationType().getOrDefault(
                                NotificationType.NEW_USER_RATING, false));
                        settingsBinding.accommodationRatingSwitch.setChecked(settings.getNotificationType().getOrDefault(
                                NotificationType.NEW_ACCOMMODATION_RATING, false));
                        settingsBinding.reservationCreatedSwitch.setChecked(settings.getNotificationType().getOrDefault(
                                NotificationType.RESERVATION_CREATED, false));
                        settingsBinding.reservationCancelledSwitch.setChecked(settings.getNotificationType().getOrDefault(
                                NotificationType.RESERVATION_CANCELED, false));

                    }
                }
            }

            @Override
            public void onFailure(Call<NotificationSettingsDTO> call, Throwable t) {
                JWTUtils.autoLogout(NotificationActivity.this, t);
            }
        });
    }
}