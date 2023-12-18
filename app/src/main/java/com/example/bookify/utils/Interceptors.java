package com.example.bookify.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.HttpException;

public class Interceptors implements Interceptor {
    private final SharedPreferences sharedPreferences;

    public Interceptors(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if(chain.request().header("skip") == null){
            String token = this.sharedPreferences.getString(JWTUtils.JWT_TOKEN, "");
            request = request.newBuilder()
                    .addHeader("Authorization", "Bearer " + token)
                    .build();
        }
        Response response = chain.proceed(request);
        if(response.code()==401) {
            throw new IOException("401");
        }
        return response;
    }
}
