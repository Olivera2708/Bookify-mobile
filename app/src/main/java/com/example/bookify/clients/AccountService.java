package com.example.bookify.clients;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;

public interface AccountService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("users/image/{imageId}")
    Call<ResponseBody> getImage(@Path("imageId") Long imageId);
}
