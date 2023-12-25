package com.example.bookify.clients;

import com.example.bookify.model.accommodation.SearchResponseDTO;
import com.example.bookify.model.reservation.ReservationDTO;
import com.example.bookify.model.reservation.ReservationRequestDTO;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
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
}
