package com.example.bookify.clients;

import com.example.bookify.enumerations.Status;
import com.example.bookify.model.accommodation.SearchResponseDTO;
import com.example.bookify.model.reservation.ReservationDTO;
import com.example.bookify.model.reservation.ReservationRequestDTO;

import java.util.List;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReservationService {

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @POST("reservations/create")
    Call<ReservationDTO> createReservation(@Body ReservationRequestDTO reservation,
                                           @Query("accommodationId") Long accommodationId,
                                           @Query("guestId") Long guestId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("reservations/guest")
    Call<List<ReservationDTO>> getAllRequestsForGuest(@Query("userId") Long guestId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("reservations/delete/{reservationId}")
    Call<ResponseBody> deleteRequest(@Path("reservationId") Long reservationId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("reservations/accommodations/guest")
    Call<List<Object[]>> getAccommodationNamesGuest(@Query("userId") Long userId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("reservations/guest/filter")
    Call<List<ReservationDTO>> getFilteredRequestsForGuest(@Query("userId") Long userId,
                                                           @Query("accommodationId") Long accommodationId,
                                                           @Query("startDate") String startDate,
                                                           @Query("endDate") String endDate,
                                                           @Query("statuses") Status[] statuses);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("reservations/owner")
    Call<List<ReservationDTO>> getAllRequestsForOwner(@Query("userId") Long ownerId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("reservations/accommodations/owner")
    Call<List<Object[]>> getAccommodationNamesOwner(@Query("userId") Long userId);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("reservations/owner/filter")
    Call<List<ReservationDTO>> getFilteredRequestsForOwner(@Query("userId") Long userId,
                                                           @Query("accommodationId") Long accommodationId,
                                                           @Query("startDate") String startDate,
                                                           @Query("endDate") String endDate,
                                                           @Query("statuses") Status[] statuses);

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("reservations/accept/{reservationId}")
    Call<ReservationDTO> acceptReservation(@Path("reservationId") Long reservationId);


    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @PUT("reservations/reject/{reservationId}")
    Call<ReservationDTO> rejectReservation(@Path("reservationId") Long reservationId);
}
