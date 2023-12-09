package com.example.bookify.fragments.accommodation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.bookify.fragments.PriceDecorator;
import com.example.bookify.R;
import com.example.bookify.fragments.MyFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_accommodation_availability, container, false);

        MaterialCalendarView calendarView = view.findViewById(R.id.calendarView);
        calendarView.invalidateDecorators();

        TextInputEditText price = view.findViewById(R.id.priceInput);
        Map<CalendarDay, PriceDecorator> mapa = new HashMap<>();
        Button add = view.findViewById(R.id.btnAdd);

        add.setOnClickListener(v -> {
            String priceTxt = price.getText().toString();
            List<CalendarDay> selectedDates = calendarView.getSelectedDates();
            if (selectedDates.size() > 0 && !priceTxt.equals("")) {
                for (CalendarDay cday : selectedDates) {
                    PriceDecorator pd = new PriceDecorator(new ArrayList<>(Arrays.asList(cday)), priceTxt + "€");
                    if (mapa.containsKey(cday)) {
                        calendarView.removeDecorator(mapa.get(cday));
                    }
                    mapa.put(cday, pd);

                    calendarView.addDecorator(pd);
                }
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
            } else {
                Toast.makeText(getActivity(), "You must select at least one date", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public int isValid() {
        return 0;
    }
}