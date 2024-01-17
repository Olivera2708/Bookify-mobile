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
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookify.R;
import com.example.bookify.adapters.data.OwnerReservationListAdapter;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.enumerations.Status;
import com.example.bookify.model.ReportedUserDTO;
import com.example.bookify.model.reservation.ReservationDTO;
import com.example.bookify.utils.JWTUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReservationsFragmentOwner#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservationsFragmentOwner extends Fragment {

    private List<ReservationDTO> reservations;
    private OwnerReservationListAdapter adapter;
    private ListView reservationsListView;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public ReservationsFragmentOwner() {
        // Required empty public constructor
    }

    public static ReservationsFragmentOwner newInstance(String param1, String param2) {
        ReservationsFragmentOwner fragment = new ReservationsFragmentOwner();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reservations_owner, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reservationsListView = view.findViewById(R.id.reservations_list_view);
        getOwnerReservations();
    }

    private void getOwnerReservations(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        Long ownerId = sharedPreferences.getLong(JWTUtils.USER_ID, -1);
        Call<List<ReservationDTO>> call = ClientUtils.reservationService.getAllRequestsForOwner(ownerId);
        call.enqueue(new Callback<List<ReservationDTO>>() {
            @Override
            public void onResponse(Call<List<ReservationDTO>> call, Response<List<ReservationDTO>> response) {
                if(response.isSuccessful() && response.body() != null){
                    reservations = response.body();
                    reservations.removeIf(r -> !r.getStatus().equals(Status.ACCEPTED));
                    adapter = new OwnerReservationListAdapter(getActivity(), reservations);
                    reservationsListView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<ReservationDTO>> call, Throwable t) {
                JWTUtils.autoLogout((AppCompatActivity) getActivity(), t);
            }
        });
    }
}