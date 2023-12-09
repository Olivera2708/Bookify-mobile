package com.example.bookify.fragments.user;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Toast;

import com.example.bookify.R;
import com.example.bookify.fragments.FragmentTransition;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistrationFragmentPersonalInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationFragmentPersonalInfo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegistrationFragmentPersonalInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrationFragmentPersonalInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistrationFragmentPersonalInfo newInstance(String param1, String param2) {
        RegistrationFragmentPersonalInfo fragment = new RegistrationFragmentPersonalInfo();
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
        View view = inflater.inflate(R.layout.fragment_registration_personal_info, container, false);
        Button button = view.findViewById(R.id.btnNext);
        TextInputEditText firstName = view.findViewById(R.id.inputFirstName);
        TextInputEditText lastName = view.findViewById(R.id.inputLastName);
        TextInputEditText phone = view.findViewById(R.id.inputPhoneNumber);
        RadioButton guest = view.findViewById(R.id.rbtGuest);
        RadioButton owner = view.findViewById(R.id.rbtOwner);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with the SecondFragment

                if(firstName.getText().toString().equals("") || lastName.getText().toString().equals("") ||
                        phone.getText().toString().equals("") || (!guest.isChecked() && !owner.isChecked())){
                    Toast.makeText(getActivity(), "You must fill in all field", Toast.LENGTH_SHORT).show();
                    return;
                }
                FragmentTransition.to(RegistrationFragmentLocation.newInstance("Locationa", "Location"),
                        getActivity(), false, R.id.registration);
            }
        });
        return view;
    }
}