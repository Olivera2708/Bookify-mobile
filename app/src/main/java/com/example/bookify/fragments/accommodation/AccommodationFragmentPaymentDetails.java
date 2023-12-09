package com.example.bookify.fragments.accommodation;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import com.example.bookify.R;
import com.example.bookify.fragments.MyFragment;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccommodationFragmentPaymentDetails#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccommodationFragmentPaymentDetails extends MyFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccommodationFragmentPaymentDetails() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccommodationFragmentPaymentDetails.
     */
    // TODO: Rename and change types and number of parameters
    public static AccommodationFragmentPaymentDetails newInstance(String param1, String param2) {
        AccommodationFragmentPaymentDetails fragment = new AccommodationFragmentPaymentDetails();
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
        view = inflater.inflate(R.layout.fragment_accommodation_payment_details, container, false);
        return view;
    }

    @Override
    public int isValid() {
        TextInputEditText deadline = view.findViewById(R.id.deadlineInput);
        RadioButton person = view.findViewById(R.id.rbtPerson);
        RadioButton room = view.findViewById(R.id.rbtRoom);

        if (deadline.getText().toString().equals("") || (!person.isChecked() && !room.isChecked())) {
            return 1;
        }
        return 0;
    }
}