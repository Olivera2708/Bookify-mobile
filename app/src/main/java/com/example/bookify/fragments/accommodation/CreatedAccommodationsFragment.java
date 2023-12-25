package com.example.bookify.fragments.accommodation;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.bookify.R;
import com.example.bookify.adapters.pagers.data.AccommodationRequestsAdapter;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.enumerations.AccommodationStatusRequest;
import com.example.bookify.model.accommodation.AccommodationRequestDTO;
import com.example.bookify.utils.JWTUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreatedAccommodationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreatedAccommodationsFragment extends Fragment {

    private List<AccommodationRequestDTO> accommodationRequestList;
    private AccommodationRequestsAdapter adapter;
    private ListView createdAccommodation;
    private Activity activity;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CreatedAccommodationsFragment() {
        // Required empty public constructor

    }
    public CreatedAccommodationsFragment(Activity activity) {
        // Required empty public constructor
        this.activity = activity;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CreatedAccommodationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CreatedAccommodationsFragment newInstance(String param1, String param2) {
        CreatedAccommodationsFragment fragment = new CreatedAccommodationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    public static CreatedAccommodationsFragment newInstance(Activity activity){
        CreatedAccommodationsFragment fragment = new CreatedAccommodationsFragment(activity);
        Bundle args = new Bundle();
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
        return inflater.inflate(R.layout.fragment_created_accommodations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        createdAccommodation = view.findViewById(R.id.createdAccommodationList);
        getRequests();
    }

    private void getRequests(){
        Call<List<AccommodationRequestDTO>> call = ClientUtils.accommodationService.getRequests();
        call.enqueue(new Callback<List<AccommodationRequestDTO>>() {
            @Override
            public void onResponse(Call<List<AccommodationRequestDTO>> call, Response<List<AccommodationRequestDTO>> response) {
                if(response.body() != null && response.code() == 200){
                    accommodationRequestList = response.body();
                    accommodationRequestList.removeIf(s -> !s.getStatusRequest().equals(AccommodationStatusRequest.CREATED));
                    adapter = new AccommodationRequestsAdapter(requireActivity(), accommodationRequestList);
                    createdAccommodation.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<AccommodationRequestDTO>> call, Throwable t) {
                JWTUtils.autoLogout((AppCompatActivity) activity, t);
            }
        });
    }
}