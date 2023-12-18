package com.example.bookify.fragments.accommodation;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.bookify.R;
import com.example.bookify.fragments.MyFragment;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccommodationFragmentGuests#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccommodationFragmentGuests extends MyFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    AccommodationUpdateViewModel viewModel;

    public AccommodationFragmentGuests() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccommodationFragmentGuests.
     */
    // TODO: Rename and change types and number of parameters
    public static AccommodationFragmentGuests newInstance(String param1, String param2) {
        AccommodationFragmentGuests fragment = new AccommodationFragmentGuests();
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

    boolean selected = true;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_accommodation_guests, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(AccommodationUpdateViewModel.class);

        String[] sort = new String[]{"HOTEL", "ROOM", "APARTMENT"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_item, sort);
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.typeDropDown);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //code when something is selected
            }
        });

        if (viewModel.getIsEditMode().getValue()) {
            TextInputEditText min = view.findViewById(R.id.minGuestsInput);
            TextInputEditText max = view.findViewById(R.id.maxGuestsInput);

            autoCompleteTextView.setText(viewModel.getType().getValue(), false);
            min.setText(viewModel.getMinGuests().getValue().toString());
            max.setText(viewModel.getMaxGuests().getValue().toString());

            if (viewModel.getManual().getValue()) {
                selected = true;
            } else {
                selected = false;
            }
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        MaterialButton left = view.findViewById(R.id.leftButton);
        MaterialButton right = view.findViewById(R.id.rightButton);

        ColorStateList colorStateListSelected = right.getBackgroundTintList();
        ColorStateList colorStateList = left.getBackgroundTintList();

        left.setOnClickListener(v -> {
            left.setBackgroundTintList(colorStateListSelected);
            right.setBackgroundTintList(colorStateList);
            selected = false;
            left.setChecked(true);
        });

        right.setOnClickListener(v -> {
            left.setBackgroundTintList(colorStateList);
            right.setBackgroundTintList(colorStateListSelected);
            selected = true;
            right.setChecked(true);
        });

        left.setChecked(!selected);
        right.setChecked(selected);

        if (left.isChecked()) {
            left.setBackgroundTintList(colorStateListSelected);
            right.setBackgroundTintList(colorStateList);
        }
        if (right.isChecked()) {
            right.setBackgroundTintList(colorStateListSelected);
            left.setBackgroundTintList(colorStateList);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public int isValid() {
        TextInputEditText min = view.findViewById(R.id.minGuestsInput);
        TextInputEditText max = view.findViewById(R.id.maxGuestsInput);
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.typeDropDown);
        if (min.getText().toString().equals("") || max.getText().toString().equals("") || autoCompleteTextView.getText().toString().trim().length() <= 0) {
            return 1;
        }
        if (Integer.parseInt(min.getText().toString()) > Integer.parseInt(max.getText().toString())) {
            return 2;
        }

        viewModel.setMinGuests(Integer.parseInt(min.getText().toString()));
        viewModel.setMaxGuests(Integer.parseInt(max.getText().toString()));
        viewModel.setType(autoCompleteTextView.getText().toString().toUpperCase());
        MaterialButton right = view.findViewById(R.id.rightButton);
        viewModel.setManual(right.isChecked());

        return 0;
    }
}