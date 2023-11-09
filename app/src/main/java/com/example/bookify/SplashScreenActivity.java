package com.example.bookify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

public class SplashScreenActivity extends AppCompatActivity {

    private boolean isPermissions = true;
    private String [] permissions = {
            android.Manifest.permission.INTERNET,
            Manifest.permission.MANAGE_DEVICE_POLICY_MOBILE_NETWORK
    };
    private static final int REQUEST_PERMISSIONS = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

//        getSupportActionBar().hide();
        int SPLASH_TIME_OUT = 5000;
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreenActivity.this, ResultsActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case REQUEST_PERMISSIONS:
                for(int i = 0; i < permissions.length; i++) {
                    Log.i("Bookify", "permission " + permissions[i] + " " + grantResults[i]);
                    if(grantResults[i] == PackageManager.PERMISSION_DENIED){
                        isPermissions = false;
                    }
                }
                break;
        }

        if (!isPermissions) {
            Log.e("Bookigy", "Error: no permission");
            finishAndRemoveTask();
        }
    }
}