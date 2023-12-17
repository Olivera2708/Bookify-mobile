package com.example.bookify.utils;

import android.content.SharedPreferences;

import com.auth0.android.jwt.JWT;
import com.example.bookify.model.user.UserJWT;

public abstract class JWTUtils {
    public static final String USER_ROLE = "userType";
    public static final String USER_ID = "id";
    public static final String USER_EMAIL = "subject";
    public static final String JWT_TOKEN = "token";
    public static final String EXPIRATION = "tokenExpires";

    public static void setCurrentLoginUser(SharedPreferences sharedPreferences, UserJWT token) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        JWT jwt = new JWT(token.getAccessToken());
        String role = jwt.getClaim("role").asString();
        String email = jwt.getSubject();
        Long id = jwt.getClaim("id").asLong();
        editor.putString(USER_ROLE, role);
        editor.putString(USER_EMAIL, email);
        editor.putString(USER_ID, id.toString());
        editor.putString(JWT_TOKEN, token.getAccessToken());
        editor.putString(EXPIRATION, token.getExpiresIn().toString());
        editor.commit();
    }

    public static void clearCurrentLoginUserData(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor
                .remove(USER_ROLE)
                .remove(USER_EMAIL)
                .remove(JWT_TOKEN)
                .remove(USER_ID)
                .remove(EXPIRATION)
                .commit();
    }

}
