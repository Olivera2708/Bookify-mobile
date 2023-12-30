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

//
//    getAllRequestsForOwner(userId: number): Observable<ReservationDTO[]> {
//        return this.httpClient.get<ReservationDTO[]>(environment.apiHost + "reservations/owner" + "?userId=" + userId);
//    }
//
//    getAccommodationMapForOwner(userId: number): Observable<any[]> {
//        return this.httpClient.get<any[]>(environment.apiHost + "reservations/accommodations/owner" + "?userId=" + userId);
//    }
//
//    deleteRequest(reservationId: number): Observable<string> {
//        return this.httpClient.put(environment.apiHost + "reservations/delete/" + reservationId, {}, {responseType:"text"});
//    }
//
//    getFilteredRequestsForOwner(userId: number, accommodationId: number, dateBegin: Date, dateEnd: Date, statuses: string[]): Observable<ReservationDTO[]> {
//        return this.httpClient.get<ReservationDTO[]>(environment.apiHost + "reservations/owner/filter" +
//                "?userId=" + userId +
//                "&accommodationId=" + accommodationId +
//                "&startDate=" + (moment(dateBegin)).format('DD.MM.YYYY') +
//                "&endDate=" + (moment(dateEnd)).format('DD.MM.YYYY') +
//                "&statuses=" + statuses);
//    }
}
