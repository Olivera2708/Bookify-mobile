package com.example.bookify.activities;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.auth0.android.jwt.JWT;
import com.example.bookify.R;
import com.example.bookify.activities.user.ForgotPasswordActivity;
import com.example.bookify.activities.user.RegistrationActivity;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.databinding.ActivityLoginBinding;
import com.example.bookify.model.user.UserCredentialsDTO;
import com.example.bookify.model.user.UserJWT;
import com.example.bookify.utils.JWTUtils;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        setButtonGuestAction();
        setLoginButtonAction();
        setRegisterButtonAction();

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finishAffinity();
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void setRegisterButtonAction() {
        binding.btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });
    }

    private void setLoginButtonAction() {
        binding.btnLogin.setOnClickListener(v -> {
            if (binding.editTextTextEmailAddress.getText().toString().equals("") ||
                    binding.editTextTextPassword.getText().toString().equals("")) {
                Toast.makeText(LoginActivity.this, "You must fill in all field", Toast.LENGTH_SHORT).show();
                return;
            }
            removeKeyboard();
            login();
        });
    }

    private void setButtonGuestAction() {
        binding.btnGuest.setOnClickListener(v -> {
            editor.putString("userType", "none");
            editor.commit();
            Intent intent = new Intent(LoginActivity.this, LandingActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });
    }

    private void login() {
        String mail = String.valueOf(binding.editTextTextEmailAddress.getText());
        String password = String.valueOf(binding.editTextTextPassword.getText());
        UserCredentialsDTO userCredentials = new UserCredentialsDTO(mail, password);

        Call<UserJWT> call = ClientUtils.accountService.login(userCredentials);
        call.enqueue(new Callback<UserJWT>() {
            @Override
            public void onResponse(Call<UserJWT> call, Response<UserJWT> response) {
                UserJWT token = response.body();
                if (token == null) {
                    onFailure(call, null);
                    return;
                }
                JWTUtils.setCurrentLoginUser(sharedPreferences, token);
                Intent intent = new Intent(LoginActivity.this, LandingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }

            @Override
            public void onFailure(Call<UserJWT> call, Throwable t) {
                failedLogin();
            }
        });
    }

    private void failedLogin() {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                "Invalid password or username, please try again.",
                3000);
        snackbar.show();
    }

    private void removeKeyboard(){
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
            Log.d("EXCEPTION", "removeKeyboard: " + e.getMessage());
        }
    }

    public void openForgotPasswordActivity(View view) {
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

}