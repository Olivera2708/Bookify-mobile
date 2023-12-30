package com.example.bookify.fragments.reservation;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.bookify.R;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.model.ReviewDTO;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReservationsFragmentGuest#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReservationsFragmentGuest extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReservationsFragmentGuest() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReservationsFragmentGuest.
     */
    // TODO: Rename and change types and number of parameters
    public static ReservationsFragmentGuest newInstance(String param1, String param2) {
        ReservationsFragmentGuest fragment = new ReservationsFragmentGuest();
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
        View view = inflater.inflate(R.layout.fragment_reservations_guest, container, false);

        Button report = view.findViewById(R.id.btnReport);

        report.setOnClickListener(v -> {
            ShowDialog(R.layout.report);
        });

        Button comment = view.findViewById(R.id.btnComment);

        comment.setOnClickListener(v -> {
            ShowDialog(R.layout.new_comment);
        });
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

        Button accommodationReview = dialog.findViewById(R.id.btnSendAccommodation);

        accommodationReview.setOnClickListener(v -> {
            sendAccommodationReview(dialog);
        });

        Button ownerReview = dialog.findViewById(R.id.btnSendOwner);

        ownerReview.setOnClickListener(v -> {
            sendOwnerReview(dialog);
        });

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
        Call<ReviewDTO> call = ClientUtils.reviewService.addOwnerReview(11L, reviewDTO);

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
}