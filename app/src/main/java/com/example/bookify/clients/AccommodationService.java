package com.example.bookify.clients;

import com.example.bookify.model.SearchResponseDTO;

import java.sql.Blob;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AccommodationService {
    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accommodations/search")
    Call<SearchResponseDTO> getForSearch(@Query("location") String location,
                                                    @Query("begin") String dateBegin,
                                                    @Query("end") String dateEnd,
                                                    @Query("persons") int persons,
                                                    @Query("page") int page,
                                                    @Query("size") int size);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accommodations/image/{imageId}")
    Call<ResponseBody> getImage(@Path("imageId") Long imageId);

}
