package com.example.bookify.services;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.bookify.BuildConfig;
import com.example.bookify.R;
import com.example.bookify.activities.SplashScreenActivity;
import com.example.bookify.enumerations.NotificationType;
import com.example.bookify.model.NotificationDTO;
import com.example.bookify.utils.JWTUtils;
import com.example.bookify.utils.StompUtils;

import org.json.JSONObject;

import ua.naiksoftware.stomp.Stomp;
import ua.naiksoftware.stomp.StompClient;

public class NotificationsForegroundService extends Service {
    private String socketAddress = "ws://" + BuildConfig.IP_ADDR + ":8080/socket/websocket";
    private StompClient stompClient;
    public static final String ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE";
    public static final String ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE";
    private NotificationManager notificationManager;
    private NotificationChannel notificationChannel;
    private NotificationCompat.Builder builder;
    private String CHANNEL_ID = "ForegroundServiceChannel";
    private Long userId;
    private int notificationID = 50;


    public NotificationsForegroundService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.builder = new NotificationCompat.Builder(this, CHANNEL_ID);
        this.userId = getSharedPreferences("sharedPref", MODE_PRIVATE).getLong(JWTUtils.USER_ID, -1);
        createNotificationChannel();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification notification = buildNotification();
        if(intent.getAction().equals(ACTION_STOP_FOREGROUND_SERVICE)){
            if(stompClient!=null && stompClient.isConnected()) {
                stompClient.disconnect();
            }
            stopForeground(true);
            stopSelf();
        } else if (intent.getAction().equals(ACTION_START_FOREGROUND_SERVICE)) {
            startForeground(1, notification);
            startForegroundService();
        }
        return START_REDELIVER_INTENT;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(stompClient != null) {
            stompClient.disconnect();
        }
        stopForeground(true);
        stopSelf();
    }

    @SuppressLint("CheckResult")
    private void startForegroundService() {
        stompClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, socketAddress);
        stompClient.connect();
        stompClient.topic("/socket-publisher/" + this.userId).subscribe(topicMessage -> {
            JSONObject jsonObject = new JSONObject(topicMessage.getPayload());
            Log.i(StompUtils.TAG, "Receive: " + topicMessage.getPayload());
            try {
                NotificationDTO notification = new NotificationDTO(jsonObject.getLong("id"), jsonObject.getString("description"), NotificationType.valueOf(jsonObject.getString("notificationType")), jsonObject.getString("created"));
                showNotification(notification);
            } catch (Exception e){

            }

        }, t -> {
            Log.d("TEST", t.getMessage());
        });
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ID, "Foreground Service Channel", NotificationManager.IMPORTANCE_HIGH);
        }
        NotificationManager manager = getSystemService(NotificationManager.class);
        manager.createNotificationChannel(notificationChannel);
    }

    private Notification buildNotification() {
        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder = new Notification.Builder(this, CHANNEL_ID);
        } else {
            builder = new Notification.Builder(this);
        }
        String userRole = getSharedPreferences("sharedPref", MODE_PRIVATE).getString(JWTUtils.USER_ROLE, "none");
        Intent notificationIntent = new Intent(this, SplashScreenActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this, notificationID, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        return builder.setContentTitle("Bookify")
                .setContentText("Your are currently logged in as: " + userRole)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.bell)
                .build();
    }

    private void showNotification(NotificationDTO notification) {
        int id = (int) System.currentTimeMillis();
        Intent notificationIntent = new Intent(this, SplashScreenActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(this, notificationID, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.setBigContentTitle(notification.getNotificationType().name());
        bigTextStyle.bigText(notification.getDescription());
        builder.setSmallIcon(getIcon(notification.getNotificationType()))
                .setStyle(bigTextStyle)
                .setContentTitle(notification.getNotificationType().name())
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setWhen(System.currentTimeMillis())
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setFullScreenIntent(contentIntent, true);
        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManagerCompat.notify(id, builder.build());
        playRingtone();
    }

    private void playRingtone() {
        Uri ringtoneURI = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), ringtoneURI);
        ringtone.play();
    }

    public static int getIcon(NotificationType notificationType){
        if(notificationType.equals(NotificationType.RESERVATION_CANCELED)){
            return R.drawable.notification_reservation_cancelled;
        } else if (notificationType.equals(NotificationType.NEW_ACCOMMODATION_RATING)){
            return R.drawable.notification_rating;
        } else if (notificationType.equals(NotificationType.NEW_USER_RATING)) {
            return R.drawable.notification_rating;
        } else if (notificationType.equals(NotificationType.RESERVATION_CREATED)) {
            return R.drawable.notification_reservation_created;
        } else if (notificationType.equals(NotificationType.RESERVATION_RESPONSE)){
            return R.drawable.notification_reservation_response;
        }
        return R.drawable.bell;

    }

}
