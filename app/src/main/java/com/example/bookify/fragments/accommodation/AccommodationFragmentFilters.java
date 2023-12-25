package com.example.bookify.fragments.accommodation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.bookify.R;
import com.example.bookify.fragments.MyFragment;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccommodationFragmentFilters#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccommodationFragmentFilters extends MyFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    AccommodationUpdateViewModel viewModel;

    public AccommodationFragmentFilters() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccommodationFragmentFilters.
     */
    // TODO: Rename and change types and number of parameters
    public static AccommodationFragmentFilters newInstance(String param1, String param2) {
        AccommodationFragmentFilters fragment = new AccommodationFragmentFilters();
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

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_accommodation_filters, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(AccommodationUpdateViewModel.class);

        if (viewModel.getIsEditMode().getValue()) {
            setCheckBoxes(view);
        }

        return view;
    }

    List<String> amenities = new ArrayList<>();

    @Override
    public int isValid() {
        iterateThroughCheckBoxes(view);
        amenities = new ArrayList<>(new HashSet<>(amenities));
        viewModel.setAmenities(amenities);
        return 0;
    }

    private void setCheckBoxes(View view) {
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View childView = ((ViewGroup) view).getChildAt(i);
                setCheckBoxes(childView);
            }
        } else if (view instanceof CheckBox) {
            CheckBox checkBox = (CheckBox) view;
            if (viewModel.getAmenities().getValue().contains(getAmenity(checkBox.getText().toString()))) {
                checkBox.setChecked(true);
            }
        }
    }

    private void iterateThroughCheckBoxes(View view) {
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View childView = ((ViewGroup) view).getChildAt(i);
                iterateThroughCheckBoxes(childView);
            }
        } else if (view instanceof CheckBox) {
            CheckBox checkBox = (CheckBox) view;
            boolean isChecked = checkBox.isChecked();
            if (isChecked) {
                amenities.add(getAmenity(checkBox.getText().toString()));
            }
        }
    }

    private String getAmenity(String name) {
        if (name.equals("24-hour front desk"))
            return "FRONT_DESK";
        return name.toUpperCase().replaceAll("[^a-zA-Z]", "_");
    }
}