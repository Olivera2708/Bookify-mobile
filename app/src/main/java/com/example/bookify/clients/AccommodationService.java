package com.example.bookify.clients;

import com.example.bookify.enumerations.PricePer;
import com.example.bookify.model.accommodation.AccommodationDetailDTO;
import com.example.bookify.model.FilterDTO;
import com.example.bookify.model.accommodation.AccommodationOwnerDTO;
import com.example.bookify.model.accommodation.SearchResponseDTO;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
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
    @GET("accommodations/price")
    Call<Double> getTotalPrice(@Query("id") Long id,
                             @Query("begin") String dateBegin,
                             @Query("end") String dateEnd,
                             @Query("pricePer") PricePer pricePer,
                             @Query("persons") int persons);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accommodations/{ownerId}")
    Call<List<AccommodationOwnerDTO>> getOwnerAccommodations(@Path("ownerId") Long id);
}
