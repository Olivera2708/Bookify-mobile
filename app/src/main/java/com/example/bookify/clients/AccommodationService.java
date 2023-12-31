package com.example.bookify.clients;

import android.database.Observable;

import com.example.bookify.model.Accommodation;
import com.example.bookify.model.AccommodationInsertDTO;
import com.example.bookify.model.FilterDTO;
import com.example.bookify.model.ImageMobileDTO;
import com.example.bookify.model.PricelistItemDTO;
import com.example.bookify.enumerations.PricePer;
import com.example.bookify.model.accommodation.AccommodationBasicDTO;
import com.example.bookify.model.accommodation.AccommodationDetailDTO;
import com.example.bookify.model.FilterDTO;
import com.example.bookify.model.accommodation.AccommodationRequestDTO;
import com.example.bookify.model.accommodation.SearchResponseDTO;

import com.example.bookify.model.accommodation.AccommodationOwnerDTO;
import com.example.bookify.model.accommodation.SearchResponseDTO;

import java.util.Collection;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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
    @GET("accommodations/images/files/{accommodationId}")
    Call<List<ImageMobileDTO>> getImagesFiles(@Path("accommodationId") Long accommodationId);


    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("accommodations")
    Call<Accommodation> insert(@Query("ownerId") Long ownerId, @Body AccommodationInsertDTO accommodationDTO);

    @Multipart
    @POST("accommodations/{accommodationId}")
    Call<Long> uploadImages(@Path("accommodationId") Long accommodationId, @Part List<MultipartBody.Part> images);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accommodations/{accommodationId}/getPrice")
    Call<List<PricelistItemDTO>> getPricelistItems(@Path("accommodationId") Long accommodationId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("accommodations/{accommodationId}/addPrice")
    Call<Long> addPricelistItem(@Path("accommodationId") Long accommodationId, @Body PricelistItemDTO dto);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @HTTP(method = "DELETE", path = "accommodations/price/{accommodationId}", hasBody = true)
    Call<PricelistItemDTO> deletePricelistItem(@Path("accommodationId") Long accommodationId, @Body PricelistItemDTO dto);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accommodations/edit/{accommodationId}")
    Call<Accommodation> getAccommodation(@Path("accommodationId") Long accommodationId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("accommodations")
    Call<Long> modify(@Body Accommodation accommodation);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @DELETE("accommodations/images/{imageId}")
    Call<Long> deleteImage(@Path("imageId") Long imageId);

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
    @GET("accommodations/requests")
    Call<List<AccommodationRequestDTO>> getRequests();

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("accommodations/approve/{accommodationId}")
    Call<ResponseBody> approveAccommodation(@Path("accommodationId") Long accommodationId);


    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("accommodations/reject/{accommodationId}")
    Call<ResponseBody> rejectAccommodation(@Path("accommodationId") Long accommodationId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accommodations/{ownerId}")
    Call<List<AccommodationOwnerDTO>> getOwnerAccommodations(@Path("ownerId") Long id);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("accommodations/add-to-favorites/{guestId}/{accommodationId}")
    Call<ResponseBody> addToFavorites(@Path("guestId") Long guestId, @Path("accommodationId") Long accommodationId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accommodations/favorites")
    Call<List<AccommodationBasicDTO>> getFavorites(@Query("guestId") Long guestId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("accommodations/added-to-favorites/{guestId}/{accommodationId}")
    Call<List<AccommodationBasicDTO>> checkIfInFavorites(@Path("guestId") Long guestId, @Path("accommodationId") Long accommodationId);
}
