package com.example.bookify.activities;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.bookify.R;
import com.example.bookify.activities.user.ForgotPasswordActivity;
import com.example.bookify.activities.user.RegistrationActivity;
import com.example.bookify.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        binding.btnGuest.setOnClickListener(v -> {
            editor.putString("userType", "none");
            editor.commit();
            Intent intent = new Intent(LoginActivity.this, LandingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });

        binding.btnLogin.setOnClickListener(v -> {
            if (binding.editTextTextEmailAddress.getText().toString().equals("") ||
                    binding.editTextTextPassword.getText().toString().equals("")) {
                Toast.makeText(LoginActivity.this, "You must fill in all field", Toast.LENGTH_SHORT).show();
                return;
            }
            login();
        });

        binding.btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finishAffinity();
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void login() {
        //Simulation of login for video
        String mail = String.valueOf(binding.editTextTextEmailAddress.getText());
        String password = String.valueOf(binding.editTextTextPassword.getText());

        Intent intent = new Intent(LoginActivity.this, LandingActivity.class);
        if (mail.equals("o") && password.equals("o"))
            editor.putString("userType", "owner");
        else if (mail.equals("g") && password.equals("g"))
            editor.putString("userType", "guest");
        else if (mail.equals("a") && password.equals("a"))
            editor.putString("userType", "admin");

        editor.commit();
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    public void openForgotPasswordActivity(View view) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
}