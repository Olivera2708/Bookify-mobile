package com.example.bookify.fragments.feedback;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.bookify.R;
import com.example.bookify.adapters.data.CreatedReviewsListAdapter;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.model.review.ReviewAdminViewDTO;
import com.example.bookify.utils.JWTUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NewReviewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewReviewsFragment extends Fragment {
    private ListView createdReviewsListView;
    private List<ReviewAdminViewDTO> createdReviews;
    private CreatedReviewsListAdapter adapter;
    private Activity activity;


    public NewReviewsFragment(Activity activity) {
        // Required empty public constructor
        this.activity = activity;
    }

    public static NewReviewsFragment newInstance(Activity activity) {
        NewReviewsFragment fragment = new NewReviewsFragment(activity);
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_new_reviews, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.createdReviewsListView = view.findViewById(R.id.created_reviews);
        getCreatedReviews();
    }

    private void getCreatedReviews(){
        Call<List<ReviewAdminViewDTO>> getCreatedReviewsCall = ClientUtils.reviewService.getAllCreatedReviews();
        getCreatedReviewsCall.enqueue(new Callback<List<ReviewAdminViewDTO>>() {
            @Override
            public void onResponse(Call<List<ReviewAdminViewDTO>> call, Response<List<ReviewAdminViewDTO>> response) {
                if(response.body() != null && response.isSuccessful()){
                    createdReviews = response.body();
                    adapter = new CreatedReviewsListAdapter(requireActivity(), createdReviews);
                    createdReviewsListView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<ReviewAdminViewDTO>> call, Throwable t) {
                JWTUtils.autoLogout((AppCompatActivity) activity, t);
            }
        });
    }
}