package com.example.bookify.activities.user;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultKt;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.os.ParcelFileDescriptor;
import android.os.ResultReceiver;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.bookify.activities.LandingActivity;
import com.example.bookify.activities.LoginActivity;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.databinding.ActivityAccountDetailsBinding;
import com.example.bookify.model.user.UserDetailsDTO;
import com.example.bookify.navigation.NavigationBar;
import com.example.bookify.R;
import com.example.bookify.utils.GenericFileProvider;
import com.example.bookify.utils.JWTUtils;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Multipart;

public class AccountDetailsActivity extends AppCompatActivity {

    private static final String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int[] USER_FIELDS = {R.id.first_name, R.id.last_name, R.id.country, R.id.zip_code, R.id.street_address, R.id.city, R.id.phone_number};
    private SharedPreferences sharedPreferences;
    private ActivityAccountDetailsBinding binding;
    private ActivityResultLauncher<String[]> permissionsResult;
    private boolean takeAPicture = false;

    private ActivityResultLauncher<Intent> startForAccountImage;

    @NonNull
    private String getFilePath(Uri imageData) {
        String filePath;
        try {
            InputStream in = getContentResolver().openInputStream(imageData);
            File tempfile = File.createTempFile("test", ".jpeg");
            OutputStream out = new FileOutputStream(tempfile);
            byte[] buffer = new byte[1024];
            int lenghtRead;
            while ((lenghtRead = in.read(buffer)) > 0) {
                out.write(buffer);
                out.flush();
            }
            filePath = tempfile.getAbsolutePath();


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return filePath;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setEditButtonAction();
        setSaveButtonAction();
        NavigationBar.setNavigationBar(findViewById(R.id.bottom_navigaiton), this, R.id.navigation_account);
        isCommentSectionVisible();
        setAccountPictureChange();
        setChangePasswordAction();
        setLogoutButtonAction();
        View view1 = findViewById(R.id.include1);
        View view2 = findViewById(R.id.include2);
        View view3 = findViewById(R.id.include3);

        this.sharedPreferences = getSharedPreferences("sharedPref", MODE_PRIVATE);
        if (sharedPreferences.getString("userType", "none").equals("OWNER")) {
            setReportButton(view1);
            setReportButton(view2);
            setReportButton(view3);
        }

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
        Call<UserDetailsDTO> call = ClientUtils.accountService.getUserDetails(this.sharedPreferences.getLong(JWTUtils.USER_ID, -1));
        call.enqueue(new Callback<UserDetailsDTO>() {
            @Override
            public void onResponse(Call<UserDetailsDTO> call, Response<UserDetailsDTO> response) {
                UserDetailsDTO userDetails = response.body();
                binding.accountInformation.email.setText(userDetails.getEmail());
                binding.userInformation.firstName.setText(userDetails.getFirstName());
                binding.userInformation.lastName.setText(userDetails.getLastName());
                binding.userInformation.country.setText(userDetails.getAddress().getCountry());
                binding.userInformation.city.setText(userDetails.getAddress().getCity());
                binding.userInformation.streetAddress.setText(userDetails.getAddress().getAddress());
                binding.userInformation.zipCode.setText(userDetails.getAddress().getZipCode());
                binding.userInformation.phoneNumber.setText(userDetails.getPhone());
                setAccountImage(userDetails.getImageId());
            }

            @Override
            public void onFailure(Call<UserDetailsDTO> call, Throwable t) {
                JWTUtils.autoLogout(AccountDetailsActivity.this, t);
            }
        });

        permissionsResult = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            ArrayList<Boolean> list = new ArrayList<>(result.values());
            if (list.get(0) && list.get(1)) takeAPicture = true;
        });
        processPermission();
        startForAccountImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent intent = result.getData();
                        String filePath = "";
                        Uri imageData = intent.getData();
                        filePath = getFilePath(imageData);
                        File file = new File(filePath);
                        RequestBody requestFile = RequestBody.create(MultipartBody.FORM, file);
                        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
                        Call<Long> call1 = ClientUtils.accountService.changeUserAccountImage(imagePart, sharedPreferences.getLong(JWTUtils.USER_ID, -1));
                        call1.enqueue(new Callback<Long>() {
                            @Override
                            public void onResponse(Call<Long> call, Response<Long> response) {
                                Log.d("nesto", "onResponse: nesto");
                            }

                            @Override
                            public void onFailure(Call<Long> call, Throwable t) {
                                JWTUtils.autoLogout(AccountDetailsActivity.this, t);
                            }
                        });
                    }

                });
    }

    private void processPermission() {
        boolean permissionsGiven = true;
        for (int i = 0; i < permissions.length; i++) {
            int perm = ContextCompat.checkSelfPermission(AccountDetailsActivity.this, permissions[i]);
            if (perm != PackageManager.PERMISSION_GRANTED) {
                permissionsGiven = false;
            }
        }
        if (!permissionsGiven) {
            permissionsResult.launch(permissions);
        } else {
            this.takeAPicture = true;
        }

    }

    private void setLogoutButtonAction() {
        Button logout = findViewById(R.id.btnLogout);
        logout.setOnClickListener(v -> {
            JWTUtils.clearCurrentLoginUserData(this.sharedPreferences);
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        });
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
        if (!role.equals("OWNER")) {
            findViewById(R.id.comment_section_account).setVisibility(View.GONE);
        }
    }

    private void setAccountPictureChange() {
        ImageView accImage = (ImageView) findViewById(R.id.account_image);
        accImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Setting up gallery Intent
                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                galleryIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                galleryIntent.setType("image/*");
                Intent chooser = Intent.createChooser(new Intent(), "Chose an action");
                // Setting up camera Intent if give permissions
                if (takeAPicture) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
                    cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    Uri file = FileProvider.getUriForFile(AccountDetailsActivity.this, GenericFileProvider.MY_PROVIDER, Objects.requireNonNull(getOutputMediaFile()));
//                    Uri file = FileProvider.getUriForFile(AccountDetailsActivity.this, GenericFileProvider.MY_PROVIDER, Objects.requireNonNull(getOutputMediaFile()));
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, file);
                    chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{galleryIntent, cameraIntent});
                    chooser.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    chooser.putExtra(MediaStore.EXTRA_OUTPUT, file);
                }
                startForAccountImage.launch(chooser);
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

    private void setAccountImage(Long imageId) {
        if (imageId == null) return;
        Call<ResponseBody> imageCall = ClientUtils.accountService.getImage(imageId);
        imageCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Bitmap image = BitmapFactory.decodeStream(response.body().byteStream());
                    binding.accountImage.setImageBitmap(image);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("F", "onFailure: failed to load user image" + t.getMessage());
            }
        });
    }

    private static File getOutputMediaFile() {
        File mediaStoreDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "Bookify");
        if (!mediaStoreDir.exists()) {
            if (!mediaStoreDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyHHdd_HHmmss", Locale.UK).format(new Date());
        return new File(mediaStoreDir.getPath() + File.separator + "IMG_" + timeStamp + "jpg");

    }
}