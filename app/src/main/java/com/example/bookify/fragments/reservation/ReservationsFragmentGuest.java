package com.example.bookify.fragments.reservation;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookify.R;
import com.example.bookify.adapters.data.GuestReservationListAdapter;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.model.ReportedUserDTO;
import com.example.bookify.model.ReviewDTO;
import com.example.bookify.model.reservation.ReservationGuestViewDTO;
import com.example.bookify.utils.JWTUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReservationsFragmentGuest#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservationsFragmentGuest extends Fragment {

    private List<ReservationGuestViewDTO> reservations;
    private ListView reservationsListView;
    private GuestReservationListAdapter adapter;

    public ReservationsFragmentGuest() {
        // Required empty public constructor
    }

    public static ReservationsFragmentGuest newInstance() {
        ReservationsFragmentGuest fragment = new ReservationsFragmentGuest();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.reservationsListView = view.findViewById(R.id.reservations_list_view);
        getReservations();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reservations_guest, container, false);
        return view;
    }

    private void getReservations(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        Long guestId = sharedPreferences.getLong(JWTUtils.USER_ID, -1);
        Call<List<ReservationGuestViewDTO>> getReservationsGuest = ClientUtils.reservationService.getGuestReservations(guestId);
        getReservationsGuest.enqueue(new Callback<List<ReservationGuestViewDTO>>() {
            @Override
            public void onResponse(Call<List<ReservationGuestViewDTO>> call, Response<List<ReservationGuestViewDTO>> response) {
                if(response.isSuccessful() && response.body() != null){
                    reservations = response.body();
                    adapter = new GuestReservationListAdapter(getActivity(), reservations);
                    reservationsListView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<ReservationGuestViewDTO>> call, Throwable t) {
                JWTUtils.autoLogout((AppCompatActivity) getActivity(), t);
            }
        });
    }
}