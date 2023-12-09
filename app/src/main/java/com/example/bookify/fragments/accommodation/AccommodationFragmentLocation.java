package com.example.bookify.fragments.accommodation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.bookify.R;
import com.example.bookify.fragments.MyFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccommodationFragmentLocation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccommodationFragmentLocation extends MyFragment {

    private MapView mapView;
    private GoogleMap gMap;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccommodationFragmentLocation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccommodationFragmentLocation.
     */
    // TODO: Rename and change types and number of parameters
    public static AccommodationFragmentLocation newInstance(String param1, String param2) {
        AccommodationFragmentLocation fragment = new AccommodationFragmentLocation();
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
        view = inflater.inflate(R.layout.fragment_accommodation_location, container, false);

        String[] sort =  Locale.getISOCountries();
        String[] countries = new String[sort.length];
        for(int i = 0; i<sort.length; i++){
            Locale locale = new Locale("", sort[i]);
            countries[i] = locale.getDisplayCountry();
        }
        Arrays.sort(countries);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_item, countries);
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.typeDropDown);
        autoCompleteTextView.setAdapter(adapter);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //code when something is selected
            }
        });

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(googleMap -> {
            // You can customize the map here
            // For example, add a marker
            LatLng markerLatLng = new LatLng(45.267136, 19.833549);
            googleMap.addMarker(new MarkerOptions().position(markerLatLng).title("Marker Title"));

            // Move the camera to the marker
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng, 12));
        });
        return view;
    }

    @Override
    public int isValid() {
        TextInputEditText city = view.findViewById(R.id.cityInput);
        TextInputEditText address = view.findViewById(R.id.streetAddressInput);
        TextInputEditText zipCode = view.findViewById(R.id.zipCodeInput);
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.typeDropDown);

        if (city.getText().toString().equals("") || autoCompleteTextView.getText().toString().trim().length() <= 0 ||
                address.getText().toString().equals("") || zipCode.getText().toString().equals("")) {
            return 1;
        }
        return 0;
    }
}