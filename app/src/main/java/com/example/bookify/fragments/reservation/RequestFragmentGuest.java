package com.example.bookify.fragments.reservation;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;

import com.example.bookify.R;
import com.example.bookify.activities.accommodation.AccommodationDetailsActivity;
import com.example.bookify.adapters.data.GuestRequestsListAdapter;
import com.example.bookify.adapters.pagers.AccommodationListAdapter;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.model.accommodation.AccommodationBasicDTO;
import com.example.bookify.model.accommodation.SearchResponseDTO;
import com.example.bookify.model.reservation.ReservationDTO;
import com.example.bookify.model.reservation.ReservationRequestDTO;
import com.example.bookify.utils.JWTUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RequestFragmentGuest#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RequestFragmentGuest extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    FloatingActionButton filterButton;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private GuestRequestsListAdapter adapter;
    private ListView listView;

    public RequestFragmentGuest() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RequestFragmentGuest.
     */
    // TODO: Rename and change types and number of parameters
    public static RequestFragmentGuest newInstance(String param1, String param2) {
        RequestFragmentGuest fragment = new RequestFragmentGuest();
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
        getData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_request_guest, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        filterButton = (FloatingActionButton) getView().findViewById(R.id.filterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog();
            }
        });

    }

    private void getData(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        Long guestId = sharedPreferences.getLong(JWTUtils.USER_ID, -1);
        Call<List<ReservationDTO>> call = ClientUtils.reservationService.getAllRequestsForGuest(guestId);
        call.enqueue(new Callback<List<ReservationDTO>>() {
            @Override
            public void onResponse(Call<List<ReservationDTO>> call, Response<List<ReservationDTO>> response) {
                if (response.code() == 200 && response.body() != null) {
                    List<ReservationDTO> result = response.body();
                    showResults(result);
                }
            }

            @Override
            public void onFailure(Call<List<ReservationDTO>> call, Throwable t) {
                Log.d("Error", "Reservation error");
            }
        });
    }

    private void showResults(List<ReservationDTO> reservations){
        adapter = new GuestRequestsListAdapter(getActivity(), reservations);
        listView = getView().findViewById(R.id.resultList);
        listView.setAdapter(adapter);
    }

    private void showBottomDialog(){
        final BottomSheetDialog dialog = new BottomSheetDialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filter_requests);

        String[] sort = new String[] {"Test Apartment", "Cool hotel", "Babo's house"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_item, sort);
        AutoCompleteTextView autoCompleteTextView = dialog.findViewById(R.id.filled_exposed);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //code when something is selected
            }
        });

        Button editDate = dialog.findViewById(R.id.editButton);
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker<Pair<Long, Long>> materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(new Pair<>(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                )).build();

                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long, Long> selection) {
                        String startDate = new SimpleDateFormat("dd.MM.yyyy.", Locale.getDefault()).format(new Date(selection.first));
                        String endDate = new SimpleDateFormat("dd.MM.yyyy.", Locale.getDefault()).format(new Date(selection.second));

                        editDate.setText(startDate + " - " + endDate);
                    }
                });

                materialDatePicker.show(getParentFragmentManager(), "tag");
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}