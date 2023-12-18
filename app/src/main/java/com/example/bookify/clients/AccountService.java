package com.example.bookify.clients;

import com.example.bookify.model.user.UserCredentialsDTO;
import com.example.bookify.model.user.UserJWT;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
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
            "Content-Type:application/json",
            "skip:true"
    })
    @POST("users/login")
    Call<UserJWT> login(@Body UserCredentialsDTO credentialsDTO);
}
