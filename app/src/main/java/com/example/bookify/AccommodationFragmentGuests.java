package com.example.bookify;

import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.google.android.material.button.MaterialButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccommodationFragmentGuests#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccommodationFragmentGuests extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_accommodation_guests, container, false);

        MaterialButton left = view.findViewById(R.id.leftButton);
        MaterialButton right = view.findViewById(R.id.rightButton);

        ColorStateList colorStateListSelected = right.getBackgroundTintList();
        ColorStateList colorStateList = left.getBackgroundTintList();

        left.setOnClickListener(v -> {
            left.setBackgroundTintList(colorStateListSelected);
            right.setBackgroundTintList(colorStateList);
        });

        right.setOnClickListener(v->{
            left.setBackgroundTintList(colorStateList);
            right.setBackgroundTintList(colorStateListSelected);
        });

        right.setChecked(true);

        String[] sort = new String[] {"studio", "room", "apartment"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_item, sort);
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.typeDropDown);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //code when something is selected
            }
        });

        return view;
    }
}