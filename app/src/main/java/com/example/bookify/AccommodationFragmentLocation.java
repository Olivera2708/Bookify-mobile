package com.example.bookify;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccommodationFragmentLocation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccommodationFragmentLocation extends Fragment {

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

        new ReverseGeocodingTask().execute();
    }

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_accommodation_location, container, false);
        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(googleMap -> {
            // You can customize the map here
            // For example, add a marker
            LatLng markerLatLng = new LatLng(45.2453834, 19.7917393);
            googleMap.addMarker(new MarkerOptions().position(markerLatLng).title("Marker Title"));

            // Move the camera to the marker
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng, 12));
        });
        return view;
    }

    private double latitude = 37.7749;  // Example: San Francisco
    private double longitude = -122.4194;

    private class ReverseGeocodingTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            String result = null;

            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                if (addresses != null && addresses.size() > 0) {
                    Address address = addresses.get(0);
                    // Construct a human-readable address
                    result = address.getAddressLine(0);
                }
            } catch (IOException e) {
                Log.e("", "Error in reverse geocoding: " + e.getMessage());
            }

            return result;
        }

        @Override
        protected void onPostExecute(String address) {
            if (address != null) {
                Log.d("TAG", "Address: " + address);
                TextInputEditText city = view.findViewById(R.id.cityInput);
                city.setText(address);
                // Use the address as needed (e.g., display it in a TextView)
            } else {
                Log.d("TAG", "Unable to retrieve address");
            }
        }
    }
}