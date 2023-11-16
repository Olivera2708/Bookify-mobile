package com.example.bookify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

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
        editor.putString("userType", "none");

        binding.btnGuest.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, LandingActivity.class);
            startActivity(intent);
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
            startActivity(intent);
            finish();
        });
    }

    private void login(){
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
        startActivity(intent);
        finish();
    }

    public void openForgotPasswordActivity(View view) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        startActivity(intent);
    }
}