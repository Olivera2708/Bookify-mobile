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
import com.example.bookify.adapters.data.AllUsersAdapter;
import com.example.bookify.adapters.data.ReportedUsersListAdapter;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.model.user.ReportedUserDetailsDTO;
import com.example.bookify.model.user.UserDTO;
import com.example.bookify.utils.JWTUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ReportedUsersFragment extends Fragment {

    private List<ReportedUserDetailsDTO> reportedUsers;
    private ReportedUsersListAdapter adapter;
    private ListView reportedUsersListView;
    private Activity activity;

    public ReportedUsersFragment(Activity activity) {
        // Required empty public constructor
        this.activity = activity;
    }

    public static ReportedUsersFragment newInstance(Activity activity) {
        ReportedUsersFragment fragment = new ReportedUsersFragment(activity);
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
        return inflater.inflate(R.layout.fragment_reported_users, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reportedUsersListView = view.findViewById(R.id.reported_users);
        getReportedUsers();
    }
    private void getReportedUsers(){
        Call<List<ReportedUserDetailsDTO>> call = ClientUtils.accountService.getAllReportedUsers();
        call.enqueue(new Callback<List<ReportedUserDetailsDTO>>() {
            @Override
            public void onResponse(Call<List<ReportedUserDetailsDTO>> call, Response<List<ReportedUserDetailsDTO>> response) {
                if(response.body() != null && response.code() == 200) {
                    reportedUsers = response.body();
                    adapter = new ReportedUsersListAdapter(requireActivity(), reportedUsers);
                    reportedUsersListView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<ReportedUserDetailsDTO>> call, Throwable t) {
                JWTUtils.autoLogout((AppCompatActivity) activity, t);
            }
        });
    }
}