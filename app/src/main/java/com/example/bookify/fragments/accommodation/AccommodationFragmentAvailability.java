package com.example.bookify.fragments.accommodation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.bookify.clients.ClientUtils;
import com.example.bookify.fragments.PriceDecorator;
import com.example.bookify.R;
import com.example.bookify.fragments.MyFragment;
import com.example.bookify.model.PricelistItemDTO;
import com.google.android.material.textfield.TextInputEditText;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccommodationFragmentAvailability#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccommodationFragmentAvailability extends MyFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    AccommodationUpdateViewModel viewModel;

    public AccommodationFragmentAvailability() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccommodationFragmentAvailability.
     */
    // TODO: Rename and change types and number of parameters
    public static AccommodationFragmentAvailability newInstance(String param1, String param2) {
        AccommodationFragmentAvailability fragment = new AccommodationFragmentAvailability();
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

    MaterialCalendarView calendarView;
    Map<CalendarDay, PriceDecorator> mapa;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_accommodation_availability, container, false);


        viewModel = new ViewModelProvider(requireActivity()).get(AccommodationUpdateViewModel.class);

        getPricelist();

        calendarView = view.findViewById(R.id.calendarView);
        calendarView.invalidateDecorators();

        TextInputEditText price = view.findViewById(R.id.priceInput);
        mapa = new HashMap<>();
        Button add = view.findViewById(R.id.btnAdd);

        add.setOnClickListener(v -> {
            String priceTxt = price.getText().toString();
            List<CalendarDay> selectedDates = calendarView.getSelectedDates();
            if (selectedDates.size() > 0 && !priceTxt.equals("")) {
                PricelistItemDTO dto = new PricelistItemDTO();
                dto.setPrice(Double.parseDouble(priceTxt));

                Calendar calendar = Calendar.getInstance();
                calendar.set(selectedDates.get(0).getYear(), selectedDates.get(0).getMonth() - 1, selectedDates.get(0).getDay());
                dto.setStartDate(calendar.getTime());

                calendar.set(selectedDates.get(selectedDates.size() - 1).getYear(), selectedDates.get(selectedDates.size() - 1).getMonth() - 1, selectedDates.get(selectedDates.size() - 1).getDay());
                dto.setEndDate(calendar.getTime());

                Call<Long> call = ClientUtils.accommodationService.addPricelistItem(viewModel.getAccommodationId().getValue(), dto);

                call.enqueue(new Callback<Long>() {
                    @Override
                    public void onResponse(Call<Long> call, Response<Long> response) {
                        if (response.code() == 200) {
                            getPricelist();
                        }
                    }

                    @Override
                    public void onFailure(Call<Long> call, Throwable t) {

                    }
                });
            } else {
                if (selectedDates.size() <= 0) {
                    Toast.makeText(getActivity(), "You must select at least one date", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "You must fill in price field", Toast.LENGTH_SHORT).show();
                }
            }
            calendarView.invalidateDecorators();

        });

        Button delete = view.findViewById(R.id.btnDelete);

        delete.setOnClickListener(v -> {
            List<CalendarDay> selectedDates = calendarView.getSelectedDates();
            if (selectedDates.size() > 0) {
                for (CalendarDay cday : selectedDates) {
                    if (mapa.containsKey(cday)) {
                        calendarView.removeDecorator(mapa.get(cday));
                        mapa.remove(cday);
                    }
                }
                PricelistItemDTO dto = new PricelistItemDTO();
                dto.setPrice(0);

                Calendar calendar = Calendar.getInstance();
                calendar.set(selectedDates.get(0).getYear(), selectedDates.get(0).getMonth() - 1, selectedDates.get(0).getDay());
                dto.setStartDate(calendar.getTime());

                calendar.set(selectedDates.get(selectedDates.size() - 1).getYear(), selectedDates.get(selectedDates.size() - 1).getMonth() - 1, selectedDates.get(selectedDates.size() - 1).getDay());
                dto.setEndDate(calendar.getTime());

                Call<PricelistItemDTO> call = ClientUtils.accommodationService.deletePricelistItem(viewModel.getAccommodationId().getValue(), dto);

                call.enqueue(new Callback<PricelistItemDTO>() {
                    @Override
                    public void onResponse(Call<PricelistItemDTO> call, Response<PricelistItemDTO> response) {
                        getPricelist();
                    }

                    @Override
                    public void onFailure(Call<PricelistItemDTO> call, Throwable t) {

                    }
                });
            } else {
                Toast.makeText(getActivity(), "You must select at least one date", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void getPricelist() {
        Call<List<PricelistItemDTO>> call = ClientUtils.accommodationService.getPricelistItems(viewModel.getAccommodationId().getValue());

        call.enqueue(new Callback<List<PricelistItemDTO>>() {
            @Override
            public void onResponse(Call<List<PricelistItemDTO>> call, Response<List<PricelistItemDTO>> response) {
                if (response.code() == 200) {
                    List<PricelistItemDTO> items = response.body();
                    for (PricelistItemDTO item : items) {
                        LocalDate current = item.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1);
                        LocalDate end = item.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().plusDays(1);
                        while (!current.isAfter(end)) {
                            CalendarDay cday = CalendarDay.from(current.getYear(), current.getMonthValue(), current.getDayOfMonth());
                            PriceDecorator pd = new PriceDecorator(new ArrayList<>(Arrays.asList(cday)), item.getPrice() + "€");

                            if (mapa.containsKey(cday)) {
                                calendarView.removeDecorator(mapa.get(cday));
                            }
                            mapa.put(cday, pd);

                            calendarView.addDecorator(pd);
                            current = current.plusDays(1);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<List<PricelistItemDTO>> call, Throwable t) {

            }
        });
    }

    @Override
    public int isValid() {
        return 0;
    }
}