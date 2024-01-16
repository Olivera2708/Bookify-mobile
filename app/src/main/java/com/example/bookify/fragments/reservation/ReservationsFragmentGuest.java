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
//
//        Button report = view.findViewById(R.id.btnReport);
//
//        report.setOnClickListener(v -> {
//            ShowDialog(R.layout.report);
//        });
//
//        Button comment = view.findViewById(R.id.btnComment);
//
//        comment.setOnClickListener(v -> {
//            ShowDialog(R.layout.new_comment);
//        });
        return view;
    }

    private void ShowDialog(int id) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(id);

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER_VERTICAL);

        if (id == R.layout.new_comment) {
            Button accommodationReview = dialog.findViewById(R.id.btnSendAccommodation);

            accommodationReview.setOnClickListener(v -> {
                sendAccommodationReview(dialog);
            });

            Button ownerReview = dialog.findViewById(R.id.btnSendOwner);

            ownerReview.setOnClickListener(v -> {
                sendOwnerReview(dialog);
            });
        } else {
            TextView reportOwner = dialog.findViewById(R.id.reportOwner);
            reportOwner.setVisibility(View.VISIBLE);
            Button btnReport = dialog.findViewById(R.id.btnReport);

            btnReport.setOnClickListener(v -> {
                sendReportOwner(dialog);
            });
        }

    }

    private void sendAccommodationReview(Dialog dialog) {
        RatingBar accommodationRating = dialog.findViewById(R.id.ratingsAccommodation);
        TextInputEditText accommodationComment = dialog.findViewById(R.id.accommodationComment);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        Long guestId = sharedPreferences.getLong("id", 0L);
        String comment = accommodationComment.getText().toString();
        int rating = (int) accommodationRating.getRating();

        if (rating <= 0 || comment.equals("")) {
            Toast.makeText(getActivity(), "Comment and rating is required", Toast.LENGTH_SHORT).show();
            return;
        }
        ReviewDTO reviewDTO = new ReviewDTO(comment, rating, guestId);
        Call<ReviewDTO> call = ClientUtils.reviewService.addAccommodationReview(1L, reviewDTO);

        call.enqueue(new Callback<ReviewDTO>() {
            @Override
            public void onResponse(Call<ReviewDTO> call, Response<ReviewDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Your comment has been sent to admin for approval", Toast.LENGTH_SHORT).show();
                }
                if (response.code() == 400) {
                    Toast.makeText(getActivity(), "You need to have a reservation to be able to comment on the accommodation", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReviewDTO> call, Throwable t) {
                Toast.makeText(getActivity(), "You need to have a reservation to be able to comment on the accommodation", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendOwnerReview(Dialog dialog) {
        RatingBar accommodationRating = dialog.findViewById(R.id.ratingsOwner);
        TextInputEditText accommodationComment = dialog.findViewById(R.id.ownerComment);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        Long guestId = sharedPreferences.getLong("id", 0L);
        String comment = accommodationComment.getText().toString();
        int rating = (int) accommodationRating.getRating();

        if (rating <= 0 || comment.equals("")) {
            Toast.makeText(getActivity(), "Comment and rating is required", Toast.LENGTH_SHORT).show();
            return;
        }
        ReviewDTO reviewDTO = new ReviewDTO(comment, rating, guestId);
        Call<ReviewDTO> call = ClientUtils.reviewService.addOwnerReview(3L, reviewDTO);

        call.enqueue(new Callback<ReviewDTO>() {
            @Override
            public void onResponse(Call<ReviewDTO> call, Response<ReviewDTO> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getActivity(), "Your comment has been sent to admin for approval", Toast.LENGTH_SHORT).show();
                }
                if (response.code() == 400) {
                    Toast.makeText(getActivity(), "You need to have a reservation to be able to comment on the owner", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ReviewDTO> call, Throwable t) {
                Toast.makeText(getActivity(), "You need to have a reservation to be able to comment on the owner", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendReportOwner(Dialog dialog){
        TextInputEditText reason = dialog.findViewById(R.id.reason);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        Long guestId = sharedPreferences.getLong("id", 0L);
        String comment = reason.getText().toString();

        if (comment.equals("")) {
            Toast.makeText(getActivity(), "Reason is required", Toast.LENGTH_SHORT).show();
            return;
        }
        ReportedUserDTO reportedUserDTO = new ReportedUserDTO(comment, new Date(), 3L, guestId);
        Call<Long> call = ClientUtils.reviewService.reportUser(reportedUserDTO);

        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                if(response.isSuccessful()){
                    Toast.makeText(getActivity(), "Successfully reported user", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getActivity(), "Cannot report user", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Toast.makeText(getActivity(), "Cannot report user", Toast.LENGTH_SHORT).show();
            }
        });
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