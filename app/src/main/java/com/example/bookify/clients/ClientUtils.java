package com.example.bookify.clients;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.bookify.BuildConfig;
import com.example.bookify.utils.Interceptors;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientUtils {

    //EXAMPLE: http://192.168.43.73:8080/api/
    public static SharedPreferences sharedPreferences = null;
    public static final String SERVICE_API_PATH = "http://"+ BuildConfig.IP_ADDR +":8080/api/v1/";

    /*
     * Ovo ce nam sluziti za debug, da vidimo da li zahtevi i odgovori idu
     * odnosno dolaze i kako izgeldaju.
     * */
    public static OkHttpClient test(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new Interceptors(ClientUtils.sharedPreferences))
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .addInterceptor(interceptor).build();

        return client;
    }

    private static Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .create();

    /*
     * Prvo je potrebno da definisemo retrofit instancu preko koje ce komunikacija ici
     * */

    public static void setClientUtils(SharedPreferences sharedPreferences){
        ClientUtils.sharedPreferences = sharedPreferences;
        ClientUtils.retrofit = new Retrofit.Builder()
                .baseUrl(SERVICE_API_PATH)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(test())
                .build();
        ClientUtils.accommodationService = retrofit.create(AccommodationService.class);
        ClientUtils.accountService = retrofit.create(AccountService.class);
        ClientUtils.reservationService = retrofit.create(ReservationService.class);
        ClientUtils.reviewService = retrofit.create(ReviewService.class);
        ClientUtils.notificationService = retrofit.create(NotificationService.class);
    }
    /*
     * Prvo je potrebno da definisemo retrofit instancu preko koje ce komunikacija ici
     * */

    public static Retrofit retrofit = null;

    /*
     * Definisemo konkretnu instancu servisa na intnerntu sa kojim
     * vrsimo komunikaciju
     * */
    public static AccommodationService accommodationService = null;
    public static AccountService accountService = null;
    public static ReservationService reservationService = null;
    public static ReviewService reviewService = null;
    public static NotificationService notificationService = null;
}
