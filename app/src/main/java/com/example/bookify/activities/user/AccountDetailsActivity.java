package com.example.bookify.activities.user;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bookify.activities.LandingActivity;
import com.example.bookify.activities.LoginActivity;
import com.example.bookify.navigation.NavigationBar;
import com.example.bookify.R;

public class AccountDetailsActivity extends AppCompatActivity {

    private static final int[] USER_FIELDS = {R.id.first_name, R.id.last_name, R.id.address, R.id.phone_number};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_details);
        setEditButtonAction();
        setSaveButtonAction();
        NavigationBar.setNavigationBar(findViewById(R.id.bottom_navigaiton), this, R.id.navigation_account);
        isCommentSectionVisible();
        setAccountPictureChange();
        setChangePasswordAction();

        View view1 = findViewById(R.id.include1);
        View view2 = findViewById(R.id.include2);
        View view3 = findViewById(R.id.include3);

        SharedPreferences sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE);
        if (sharedPreferences.getString("userType", "none").equals("owner")) {
            setReportButton(view1);
            setReportButton(view2);
            setReportButton(view3);
        }

        Button logout = findViewById(R.id.btnLogout);
        logout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("userType", "none");
            editor.commit();
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(AccountDetailsActivity.this, LandingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void setReportButton(View view1) {

        Button report = view1.findViewById(R.id.btnReport);
        report.setVisibility(View.VISIBLE);
        report.setOnClickListener(v -> {
            Toast.makeText(AccountDetailsActivity.this, "Your report will be processed", Toast.LENGTH_SHORT).show();
        });
    }

    private void isCommentSectionVisible() {
        String role = getSharedPreferences("sharedPref", Context.MODE_PRIVATE).getString("userType", "none");
        if(!role.equals("owner")){
            findViewById(R.id.comment_section_account).setVisibility(View.GONE);
        }
    }

    private void setAccountPictureChange() {
        ImageView accImage = (ImageView) findViewById(R.id.account_image);
        accImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getApplicationContext();
                CharSequence charSequence = "To-do";
                Toast toast = Toast.makeText(context, charSequence, Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private void setEditButtonAction() {
        findViewById(R.id.btn_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int id : USER_FIELDS) {
                    findViewById(id).setEnabled(true);
                }
            }
        });
    }

    private void setSaveButtonAction() {
        findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int id : USER_FIELDS) {
                    findViewById(id).setEnabled(false);
                }
            }
        });
    }

    private void setChangePasswordAction() {
        findViewById(R.id.btnEditPassword).setOnClickListener(v -> {
            ShowDialog(R.layout.change_password);
        });
    }

    private void ShowDialog(int id) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(id);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);
    }
}