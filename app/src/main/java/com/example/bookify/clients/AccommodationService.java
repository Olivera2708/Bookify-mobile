package com.example.bookify.clients;

import android.database.Observable;

import com.example.bookify.model.Accommodation;
import com.example.bookify.model.AccommodationDetailDTO;
import com.example.bookify.model.AccommodationInsertDTO;
import com.example.bookify.model.FilterDTO;
import com.example.bookify.model.SearchResponseDTO;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("accommodations/filter")
    Call<SearchResponseDTO> getForFilter(@Query("location") String location,
                                         @Query("begin") String dateBegin,
                                         @Query("end") String dateEnd,
                                         @Query("persons") int persons,
                                         @Query("page") int page,
                                         @Query("size") int size,
                                         @Query("sort") String sort,
                                         @Body FilterDTO filter);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accommodations/details/{id}")
    Call<AccommodationDetailDTO> getAccommodationDetails(@Path("id") Long id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accommodations/images/{accommodationId}")
    Call<String[]> getImages(@Path("accommodationId") Long accommodationId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("accommodations")
    Call<Accommodation> insert(@Query("ownerId") Long ownerId, @Body AccommodationInsertDTO accommodationDTO);

    @Multipart
    @POST("accommodations/{accommodationId}")
    Call<Long> uploadImages(@Path("accommodationId") Long accommodationId, @Part List<MultipartBody.Part> images);

}
