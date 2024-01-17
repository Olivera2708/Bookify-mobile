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
import com.example.bookify.adapters.data.ReportedReviewsListAdapter;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.model.review.ReviewAdminViewDTO;
import com.example.bookify.utils.JWTUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportedReviewsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportedReviewsFragment extends Fragment {

    private ListView reportedReviewsListView;
    private ReportedReviewsListAdapter adapter;
    private List<ReviewAdminViewDTO> reviews;
    private Activity activity;

    public ReportedReviewsFragment(Activity activity) {
        // Required empty public constructor
        this.activity = activity;
    }

    public static ReportedReviewsFragment newInstance(Activity activity) {
        ReportedReviewsFragment fragment = new ReportedReviewsFragment(activity);
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
        return inflater.inflate(R.layout.fragment_reported_reviews, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reportedReviewsListView = view.findViewById(R.id.reported_reviews);
        getReportedReviews();
    }

    private void getReportedReviews(){
        Call<List<ReviewAdminViewDTO>> call = ClientUtils.reviewService.getAllReportedReviews();
        call.enqueue(new Callback<List<ReviewAdminViewDTO>>() {
            @Override
            public void onResponse(Call<List<ReviewAdminViewDTO>> call, Response<List<ReviewAdminViewDTO>> response) {
                if(response.body()!=null && response.isSuccessful()){
                    reviews = response.body();
                    adapter = new ReportedReviewsListAdapter(requireActivity(), reviews);
                    reportedReviewsListView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<ReviewAdminViewDTO>> call, Throwable t) {
                JWTUtils.autoLogout((AppCompatActivity) activity, t);
            }
        });
    }
}