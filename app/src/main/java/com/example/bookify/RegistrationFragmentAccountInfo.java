package com.example.bookify;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.bookify.databinding.FragmentRegistrationAccountInfoBinding;
import com.google.android.material.textfield.TextInputEditText;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistrationFragmentAccountInfo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationFragmentAccountInfo extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RegistrationFragmentAccountInfo() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrationFragmentAccountInfo.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistrationFragmentAccountInfo newInstance(String param1, String param2) {
        RegistrationFragmentAccountInfo fragment = new RegistrationFragmentAccountInfo();
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
        View view = inflater.inflate(R.layout.fragment_registration_account_info, container, false);
        Button button = view.findViewById(R.id.btnNext);
        TextInputEditText email = view.findViewById(R.id.editTextTextEmailAddress);
        TextInputEditText password = view.findViewById(R.id.editTextTextPassword);
        TextInputEditText repeatedPassword = view.findViewById(R.id.editTextTextRepeatPassword);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace the current fragment with the SecondFragment
                if(email.getText().toString().equals("") || password.getText().toString().equals("") || repeatedPassword.getText().toString().equals("")){
                    Toast.makeText(getActivity(), "You must fill in all field", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!password.getText().toString().equals(repeatedPassword.getText().toString())){
                    Toast.makeText(getActivity(), "Password and repeated password must be the same", Toast.LENGTH_SHORT).show();
                    return;
                }
                FragmentTransition.to(RegistrationFragmentPersonalInfo.newInstance("AccInfo", "Account informations"),
                        getActivity(), false, R.id.registration);
            }
        });
        return view;
    }
}