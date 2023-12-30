package com.example.bookify.activities.user;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookify.activities.LandingActivity;
import com.example.bookify.activities.LoginActivity;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.databinding.AccountDeletionDialogBinding;
import com.example.bookify.databinding.ActivityAccountDetailsBinding;
import com.example.bookify.databinding.ChangePasswordBinding;
import com.example.bookify.model.user.UserDetailsDTO;
import com.example.bookify.navigation.NavigationBar;
import com.example.bookify.R;
import com.example.bookify.utils.GenericFileProvider;
import com.example.bookify.utils.JWTUtils;
import com.example.bookify.utils.TextValidator;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
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

public class AccountDetailsActivity extends AppCompatActivity {

    private static final String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private static final int[] USER_FIELDS = {R.id.first_name, R.id.last_name, R.id.country, R.id.zip_code, R.id.street_address, R.id.city, R.id.phone_number};
    private SharedPreferences sharedPreferences;
    private UserDetailsDTO userDetails;
    private ActivityAccountDetailsBinding binding;
    private ChangePasswordBinding changePasswordBinding;
    private AccountDeletionDialogBinding accountDeletionDialogBinding;
    private boolean takeAPicture = false;
    private Uri file;
    private final ActivityResultLauncher<String[]> permissionsResult = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
        ArrayList<Boolean> list = new ArrayList<>(result.values());
        if (list.get(0) && list.get(1)) takeAPicture = true;
    });
    private final ActivityResultLauncher<Intent> startForAccountImage = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    String filePath = "";
                    Uri imageData;
                    if(result.getData() != null) {
                        Intent intent = result.getData();
                        imageData = intent.getData();
                    }else{
                        imageData = file;
                    }
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
        setDeleteAccountAction();

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
        processPermission();
        loadUserData();
        setTextFieldsValidators();
    }

    private void loadUserData() {
        Call<UserDetailsDTO> call = ClientUtils.accountService.getUserDetails(this.sharedPreferences.getLong(JWTUtils.USER_ID, -1));
        call.enqueue(new Callback<UserDetailsDTO>() {
            @Override
            public void onResponse(Call<UserDetailsDTO> call, Response<UserDetailsDTO> response) {
                userDetails = response.body();
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
    }

    private void processPermission() {
        boolean permissionsGiven = true;
        for (String permission : permissions) {
            int perm = ContextCompat.checkSelfPermission(AccountDetailsActivity.this, permission);
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
        binding.btnLogout.setOnClickListener(v -> {
            Call<ResponseBody> logoutCall = ClientUtils.accountService.logout();
            logoutCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    JWTUtils.clearCurrentLoginUserData(sharedPreferences);
                    Intent intent = new Intent(AccountDetailsActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    finish();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("D", "onFailure: ");
                }
            });
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
        binding.accountImage.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
            galleryIntent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
            galleryIntent.setType("image/*");
            Intent chooser = Intent.createChooser(new Intent(), "Chose an action");
            if (takeAPicture) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                file = FileProvider.getUriForFile(getApplicationContext(), GenericFileProvider.MY_PROVIDER, Objects.requireNonNull(getOutputMediaFile()));
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, file);
                chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{galleryIntent, cameraIntent});
            }
            startForAccountImage.launch(chooser);
        });
    }

    private void setEditButtonAction() {
        binding.userInformation.btnEdit.setOnClickListener(v -> setFieldsEnabledStatus(true));
    }

    private void setSaveButtonAction() {
        binding.userInformation.btnSave.setOnClickListener(v -> {
            if (!isUserInformationFormValid()) {
                Snackbar.make(binding.getRoot(), "Please make sure all fields are valid", Snackbar.LENGTH_LONG).setAnchorView(binding.bottomNavigaiton).show();
                return;
            }
            prepareUserDataForSending();
            Call<UserDetailsDTO> updateUserCall = ClientUtils.accountService.updateUser(userDetails);
            updateUserCall.enqueue(new Callback<UserDetailsDTO>() {
                @Override
                public void onResponse(Call<UserDetailsDTO> call, Response<UserDetailsDTO> response) {
                    if (response.body() != null) {
                        Snackbar.make(binding.getRoot(), "User information successfully updated!", Snackbar.LENGTH_LONG)
                                .setAnchorView(binding.bottomNavigaiton).show();
                        setFieldsEnabledStatus(false);
                    }
                    Snackbar.make(binding.getRoot(), "Something went wrong!", Snackbar.LENGTH_LONG)
                            .setAnchorView(binding.bottomNavigaiton).show();
                }

                @Override
                public void onFailure(Call<UserDetailsDTO> call, Throwable t) {
                    JWTUtils.autoLogout(AccountDetailsActivity.this, t);
                }
            });
        });
    }

    private void prepareUserDataForSending() {
        userDetails.setFirstName(binding.userInformation.firstName.getText().toString());
        userDetails.setLastName(binding.userInformation.lastName.getText().toString());
        userDetails.setPhone(binding.userInformation.phoneNumber.getText().toString());
        userDetails.getAddress().setCountry(binding.userInformation.country.getText().toString());
        userDetails.getAddress().setCity(binding.userInformation.city.getText().toString());
        userDetails.getAddress().setZipCode(binding.userInformation.zipCode.getText().toString());
        userDetails.getAddress().setAddress(binding.userInformation.streetAddress.getText().toString());
    }


    private void setChangePasswordAction() {
        binding.accountInformation.btnEditPassword.setOnClickListener(v -> {
            ShowDialog(R.layout.change_password);
        });
    }

    private void ShowDialog(int id) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        changePasswordBinding = ChangePasswordBinding.inflate(getLayoutInflater());
        dialog.setContentView(changePasswordBinding.getRoot());
        setPasswordFieldsValidation();
        changePasswordBinding.btnChangePassword.setOnClickListener(v -> {
            String errorMessage = isPasswordValid();
            if (!errorMessage.isEmpty()) {
                Snackbar.make(dialog.getWindow().getDecorView(), errorMessage, Snackbar.LENGTH_LONG).show();
                return;
            }
            String password = changePasswordBinding.password.getText().toString();
            RequestBody requestBodyPassword = RequestBody.create(MediaType.parse("text/plain"), password);
            Call<ResponseBody> changePasswordCall = ClientUtils.accountService.changePassword(sharedPreferences.getLong(JWTUtils.USER_ID, -1), requestBodyPassword);
            changePasswordCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    String message = null;
                    try {
                        message = response.body().string();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Snackbar.make(binding.getRoot(), message, Snackbar.LENGTH_LONG).setAnchorView(binding.bottomNavigaiton).show();
                    dialog.dismiss();
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    JWTUtils.autoLogout(AccountDetailsActivity.this, t);
                    Log.d("N", t.getMessage());
                }
            });
        });
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
        return new File(mediaStoreDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");

    }

    @NonNull
    private String getFilePath(Uri imageData) {
        String filePath;
        try (InputStream in = getContentResolver().openInputStream(imageData);) {
            File tempfile = File.createTempFile("test", ".jpeg");
            OutputStream out = Files.newOutputStream(tempfile.toPath());
            byte[] buffer = new byte[1024];
            int lenghtRead;
            while ((lenghtRead = in.read(buffer)) > 0) {
                out.write(buffer);
                out.flush();
            }
            filePath = tempfile.getAbsolutePath();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return filePath;
    }

    private void setFieldsEnabledStatus(boolean isEnabled) {
        for (int id : USER_FIELDS) {
            findViewById(id).setEnabled(isEnabled);
        }
    }

    private void setTextFieldsValidators() {
        binding.userInformation.firstName.addTextChangedListener(new TextValidator(binding.userInformation.firstName) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) textView.setError("Field must not be empty");
            }
        });
        binding.userInformation.lastName.addTextChangedListener(new TextValidator(binding.userInformation.lastName) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) textView.setError("Field must not be empty");
            }
        });
        binding.userInformation.streetAddress.addTextChangedListener(new TextValidator(binding.userInformation.streetAddress) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) textView.setError("Field must not be empty");
            }
        });
        binding.userInformation.city.addTextChangedListener(new TextValidator(binding.userInformation.city) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) textView.setError("Field must not be empty");
            }
        });

        binding.userInformation.zipCode.addTextChangedListener(new TextValidator(binding.userInformation.zipCode) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) textView.setError("Field must not be empty");
            }
        });
        binding.userInformation.country.addTextChangedListener(new TextValidator(binding.userInformation.country) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) textView.setError("Field must not be empty");
            }
        });
        binding.userInformation.phoneNumber.addTextChangedListener(new TextValidator(binding.userInformation.phoneNumber) {
            @Override
            public void validate(TextView textView, String text) {
                if (text.isEmpty()) textView.setError("Field must not be empty");
            }
        });
    }

    private boolean isUserInformationFormValid() {
        for (int field_id : USER_FIELDS) {
            if (((EditText) findViewById(field_id)).getError() != null) {
                return false;
            }
        }
        return true;
    }

    private void setPasswordFieldsValidation() {
        changePasswordBinding.password.addTextChangedListener(new TextValidator(changePasswordBinding.password) {
            @Override
            public void validate(TextView textView, String text) {
                if (!text.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
                    textView.setError("Password must be 8 characters long and must contain uppercase letter, number, special character", null);
                }
                if (text.isEmpty()) {
                    textView.setError("Password must not be empty", null);
                }
            }
        });
        changePasswordBinding.repeatedPassword.addTextChangedListener(new TextValidator(changePasswordBinding.repeatedPassword) {
            @Override
            public void validate(TextView textView, String text) {
                if (!text.equals(changePasswordBinding.password.getText().toString()))
                    textView.setError("Please check your repeated password", null);
                if (text.isEmpty()) textView.setError("This field can't be empty", null);
            }
        });
        changePasswordBinding.repeatedPassword.setText("");
        changePasswordBinding.password.setText("");
    }

    private String isPasswordValid() {
        if (changePasswordBinding.password.getError() != null) {
            return changePasswordBinding.password.getError().toString();
        }
        if (changePasswordBinding.repeatedPassword.getError() != null) {
            return  changePasswordBinding.repeatedPassword.getError().toString();
        }
        return "";
    }

    private void setDeleteAccountAction(){
        binding.accountInformation.btnDeleteAccount.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            accountDeletionDialogBinding = AccountDeletionDialogBinding.inflate(getLayoutInflater());
            dialog.setContentView(accountDeletionDialogBinding.getRoot());

            accountDeletionDialogBinding.btnAcceptDelete.setOnClickListener(v1->{
                deleteAccount();
                dialog.dismiss();
            });

            accountDeletionDialogBinding.btnCancelDelete.setOnClickListener(v1 -> {
                dialog.dismiss();
            });
            dialog.show();
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);
        });
    }

    private void deleteAccount() {
        Call<ResponseBody> deleteAccountCall = ClientUtils.accountService.deleteUser(sharedPreferences.getLong(JWTUtils.USER_ID, -1));
        deleteAccountCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.code() == 400){
                    try {
                        Snackbar.make(binding.getRoot(), response.errorBody().string(), Snackbar.LENGTH_LONG).setAnchorView(binding.bottomNavigaiton).show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                JWTUtils.clearCurrentLoginUserData(sharedPreferences);
                Intent intent = new Intent(AccountDetailsActivity.this, LoginActivity.class);
                intent.putExtra(JWTUtils.AUTO_LOGOUT,true);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d("", t.getMessage());
                Log.d("NOT YIPI", "We are here");

            }
        });
    }
}