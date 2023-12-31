package com.example.bookify.clients;

import com.example.bookify.model.Accommodation;
import com.example.bookify.model.AccommodationInsertDTO;
import com.example.bookify.model.CommentDTO;
import com.example.bookify.model.RatingDTO;
import com.example.bookify.model.ReviewDTO;
import com.example.bookify.model.UserBasicDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
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

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("reviews/owner/{ownerId}")
    Call<List<CommentDTO>> getOwnerComments(@Path("ownerId") Long ownerId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("reviews/accommodation/{accommodationId}")
    Call<List<CommentDTO>> getAccommodationComments(@Path("accommodationId") Long accommodationId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("reviews/report/{reviewId}")
    Call<Long> reportComment(@Path("reviewId") Long reviewId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("reviews/owner/{ownerId}/rating")
    Call<RatingDTO> getOwnerRating(@Path("ownerId") Long ownerId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("reviews/accommodation/{accommodationId}/rating")
    Call<RatingDTO> getAccommodationRating(@Path("accommodationId") Long accommodationId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @DELETE("reviews/accommodation-delete/{accommodationId}/{reviewId}")
    Call<Void> deleteAccommodationReview(@Path("accommodationId") Long accommodationId, @Path("reviewId") Long reviewId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @DELETE("reviews/owner-delete/{ownerId}/{reviewId}")
    Call<Void> deleteOwnerReview(@Path("ownerId") Long ownerId, @Path("reviewId") Long reviewId);


    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("users/user/{userId}")
    Call<UserBasicDTO> getUserBasic(@Path("userId") Long userId);

}
