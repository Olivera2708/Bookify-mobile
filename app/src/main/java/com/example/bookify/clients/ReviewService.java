package com.example.bookify.clients;

import com.example.bookify.model.Accommodation;
import com.example.bookify.model.AccommodationInsertDTO;
import com.example.bookify.model.ReviewDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReviewService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("reviews/new-owner/{ownerId}")
    Call<ReviewDTO> addOwnerReview(@Path("ownerId") Long ownerId, @Body ReviewDTO reviewDTO);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("reviews/new-accommodation/{accommodationId}")
    Call<ReviewDTO> addAccommodationReview(@Path("accommodationId") Long accommodationId, @Body ReviewDTO reviewDTO);
}
