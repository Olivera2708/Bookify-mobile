package com.example.bookify.fragments.accommodation;

import android.location.Geocoder;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.example.bookify.R;
import com.example.bookify.fragments.MyFragment;
import com.example.bookify.model.Address;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
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

    AccommodationUpdateViewModel viewModel;

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

    Boolean isEnabledCountry = true;
    Boolean isEnabledCity = true;
    Boolean isEnabledStreet = true;
    Boolean isEnabledZip = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_accommodation_location, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(AccommodationUpdateViewModel.class);

        String[] sort = Locale.getISOCountries();
        String[] countries = new String[sort.length];
        for (int i = 0; i < sort.length; i++) {
            Locale locale = new Locale("", sort[i]);
            countries[i] = locale.getDisplayCountry();
        }
        Arrays.sort(countries);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_item, countries);
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.typeDropDown);
        autoCompleteTextView.setAdapter(adapter);

        TextInputEditText cityField = view.findViewById(R.id.cityInput);
        TextInputEditText addressField = view.findViewById(R.id.streetAddressInput);
        TextInputEditText zipCodeField = view.findViewById(R.id.zipCodeInput);
        AutoCompleteTextView countryField = view.findViewById(R.id.typeDropDown);
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(googleMap -> {
            LatLng markerLatLng = new LatLng(45.267136, 19.833549);


            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng, 12));

            if (viewModel.getIsEditMode().getValue()) {
                countryField.setText(viewModel.getAddress().getValue().getCountry());
                cityField.setText(viewModel.getAddress().getValue().getCity());
                addressField.setText(viewModel.getAddress().getValue().getAddress());
                zipCodeField.setText(viewModel.getAddress().getValue().getZipCode());
                searchLocation(googleMap);
            }

            googleMap.setOnMapClickListener(latLang -> {
                googleMap.clear();
                googleMap.addMarker(new MarkerOptions().position(latLang));
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLang, 12), 1000, null);
                getAddressFromLocation(latLang.latitude, latLang.longitude);
            });

            countryField.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (isEnabledCountry) {
                        searchLocation(googleMap);
                    }
                    isEnabledCountry = true;
                }
            });

            cityField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (isEnabledCity) {
                        searchLocation(googleMap);
                    }
                    isEnabledCity = true;
                }
            });

            addressField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (isEnabledStreet) {
                        searchLocation(googleMap);
                    }
                    isEnabledStreet = true;
                }
            });

            zipCodeField.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (isEnabledZip) {
                        searchLocation(googleMap);
                    }
                    isEnabledZip = true;
                }
            });
        });
        return view;
    }

    private void searchLocation(GoogleMap googleMap) {
        TextInputEditText cityField = view.findViewById(R.id.cityInput);
        TextInputEditText addressField = view.findViewById(R.id.streetAddressInput);
        TextInputEditText zipCodeField = view.findViewById(R.id.zipCodeInput);
        AutoCompleteTextView countryField = view.findViewById(R.id.typeDropDown);
        String searchQuery = addressField.getText().toString() + ", " + cityField.getText().toString() + ", " + zipCodeField.getText().toString() + ", " + countryField.getText().toString();
        if (!searchQuery.isEmpty()) {
            Geocoder geocoder = new Geocoder(getActivity());
            try {
                List<android.location.Address> addresses = geocoder.getFromLocationName(searchQuery, 1);
                if (!addresses.isEmpty()) {
                    android.location.Address address = addresses.get(0);
                    LatLng newLocation = new LatLng(address.getLatitude(), address.getLongitude());

                    googleMap.clear();

                    googleMap.addMarker(new MarkerOptions().position(newLocation));

                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(newLocation, 12), 1000, null);
                } else {
                    googleMap.clear();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
        TextInputEditText cityField = view.findViewById(R.id.cityInput);
        TextInputEditText addressField = view.findViewById(R.id.streetAddressInput);
        TextInputEditText zipCodeField = view.findViewById(R.id.zipCodeInput);
        AutoCompleteTextView countryField = view.findViewById(R.id.typeDropDown);
        isEnabledCity = false;
        isEnabledCountry = false;
        isEnabledZip = false;
        isEnabledStreet = false;
        try {
            List<android.location.Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (!addresses.isEmpty()) {
                android.location.Address address = addresses.get(0);

                String country = address.getCountryName();
                String city = address.getLocality();
                String street = address.getThoroughfare() + " " + address.getSubThoroughfare();
                String zipCode = address.getPostalCode();

                // Display the address information

                countryField.setText(country, false);
                cityField.setText(city);
                addressField.setText(street);
                zipCodeField.setText(zipCode);
            } else {
                addressField.setText("");
                countryField.setText("");
                cityField.setText("");
                zipCodeField.setText("");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

        Address a = new Address();
        a.setCountry(autoCompleteTextView.getText().toString());
        a.setCity(city.getText().toString());
        a.setAddress(address.getText().toString());
        a.setZipCode(zipCode.getText().toString());
        viewModel.setAddress(a);

        return 0;
    }
}