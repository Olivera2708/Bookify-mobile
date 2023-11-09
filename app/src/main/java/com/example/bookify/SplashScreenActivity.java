package com.example.bookify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {

    private ConnectivityManager connectivityManager;
    private ConnectivityManager.NetworkCallback networkCallback;
    private HandlerThread handlerThread;
    private Handler handler;
    private MediaPlayer player;
    private Runnable navigateTo, playPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

//        getSupportActionBar().hide();
        int SPLASH_TIME_OUT = 5000;

        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        handlerThread = new HandlerThread("HandlerThread");
        handlerThread.start();
        handler = new Handler(handlerThread.getLooper());

        playPlayer = () -> play();
        handler.postDelayed(playPlayer, 1700);

        if (isConnectedToInternet()) {
            navigateTo = () -> {
                Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            };
            handler.postDelayed(navigateTo, SPLASH_TIME_OUT);
        } else {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                    "Niste povezani na internet",
                    Snackbar.LENGTH_INDEFINITE).setAction("Povezi se", v -> {
                Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                startActivity(intent);
            });

            snackbar.show();

            handler.postDelayed(() -> {
                if (!isConnectedToInternet()) {
                    Toast.makeText(SplashScreenActivity.this, "Niste povezani na internet. Zatvaranje aplikacije.",
                            Toast.LENGTH_SHORT).show();
                    handler.postDelayed(() -> finish(), 2000);
                }
            }, 10000);
        }

        networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                Snackbar.make(findViewById(android.R.id.content), "Povezani ste na internet",
                        Snackbar.LENGTH_SHORT).show();

                handler.postDelayed(() -> {
                    Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }, 5000);
            }

            @Override
            public void onLost(Network network) {
                Snackbar.make(findViewById(android.R.id.content), "Izgubili ste vezu.", Snackbar.LENGTH_SHORT).show();
                handler.postDelayed(() -> finish(), 1000);
            }
        };

        NetworkRequest networkRequest = new NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build();
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback);
    }

    private void play() {
        if (player == null) {
            player = MediaPlayer.create(this, R.raw.sound);
            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

                @Override
                public void onCompletion(MediaPlayer mp) {

                }
            });
        }
        player.start();
    }

    private void stopPlayer() {
        if (player != null) {
            player.release();
            player = null;
        }
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            Network network = connectivityManager.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities capabilities = connectivityManager.getNetworkCapabilities(network);
                return capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            }
        }
        return false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (connectivityManager != null && networkCallback != null) {
            connectivityManager.unregisterNetworkCallback(networkCallback);
        }
        if (handlerThread != null) {
            handlerThread.quit();
        }
        handler.removeCallbacks(navigateTo);
    }

    @Override
    protected void onStop() {
        super.onStop();
        handler.removeCallbacks(playPlayer);
        handler.removeCallbacks(navigateTo);
        stopPlayer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacks(navigateTo);
    }
}