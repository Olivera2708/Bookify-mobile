package com.example.bookify.clients;

import com.example.bookify.model.NotificationDTO;
import com.example.bookify.model.NotificationSettingsDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NotificationService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("notifications/{userId}")
    Call<List<NotificationDTO>> getUserNotifications(@Path("userId") Long userId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("notifications/{userId}/settings")
    Call<NotificationSettingsDTO> getUserNotificationSettings(@Path("userId") Long userId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("notifications/{userId}/settings")
    Call<NotificationSettingsDTO> setUserNotificationSettings(@Path("userId") Long userId, @Body NotificationSettingsDTO notificationSettingsDTO);
}
