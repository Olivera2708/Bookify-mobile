package com.example.bookify.clients;

import com.example.bookify.model.MessageDTO;
import com.example.bookify.model.UserRegistrationDTO;
import android.net.Uri;

import com.example.bookify.model.user.UserCredentialsDTO;
import com.example.bookify.model.user.UserDetailsDTO;
import com.example.bookify.model.user.UserJWT;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface AccountService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("users/image/{imageId}")
    Call<ResponseBody> getImage(@Path("imageId") Long imageId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("users/mobile")
    Call<MessageDTO> register(@Body UserRegistrationDTO user);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("users/forgot-password/{email}")
    Call<ResponseBody> forgetPassword(@Path("email") String email);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json",
            "skip:true"
    })
    @POST("users/login")
    Call<UserJWT> login(@Body UserCredentialsDTO credentialsDTO);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("users/{userId}")
    Call<UserDetailsDTO> getUserDetails(@Path("userId") Long userId);

    @Multipart
    @POST("users/change-image/{userId}")
    Call<Long> changeUserAccountImage(@Part MultipartBody.Part image, @Path("userId") Long userId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("users/{userId}/change-password")
    Call<ResponseBody> changePassword(@Path("userId") Long userId, @Body RequestBody newPassword);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("users/logout")
    Call<ResponseBody> logout();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("users")
    Call<UserDetailsDTO> updateUser(@Body UserDetailsDTO updatedUser);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @DELETE("users/{userId}")
    Call<ResponseBody> deleteUser(@Path("userId") Long userId);
}
