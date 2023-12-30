package com.example.bookify.clients;

import com.example.bookify.model.accommodation.SearchResponseDTO;
import com.example.bookify.model.reservation.ReservationDTO;
import com.example.bookify.model.reservation.ReservationRequestDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
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

    @Headers({
            "User-Agent: Mobile-Android",
            "Content-Type:application/json"
    })
    @GET("reservations/guest")
    Call<List<ReservationDTO>> getAllRequestsForGuest(@Query("userId") Long guestId);


//    getAllRequestsForGuest(userId: number): Observable<ReservationDTO[]> {
//        return this.httpClient.get<ReservationDTO[]>(environment.apiHost + "reservations/guest" + "?userId=" + userId);
//    }
//
//    getAccommodationMapForGuest(userId: number): Observable<any[]> {
//        return this.httpClient.get<any[]>(environment.apiHost + "reservations/accommodations/guest" + "?userId=" + userId);
//    }
//
//    getFilteredRequestsForGuest(userId: number, accommodationId: number, dateBegin: Date, dateEnd: Date, statuses: string[]): Observable<ReservationDTO[]> {
//        return this.httpClient.get<ReservationDTO[]>(environment.apiHost + "reservations/guest/filter" +
//                "?userId=" + userId +
//                "&accommodationId=" + accommodationId +
//                "&startDate=" + (moment(dateBegin)).format('DD.MM.YYYY') +
//                "&endDate=" + (moment(dateEnd)).format('DD.MM.YYYY') +
//                "&statuses=" + statuses);
//    }
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
