package com.example.bookify.fragments.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.example.bookify.R;
import com.example.bookify.activities.LoginActivity;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.model.Address;
import com.example.bookify.model.MessageDTO;
//import com.example.bookify.model.SearchResponseDTO;
import com.example.bookify.model.UserRegistrationDTO;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Arrays;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegistrationFragmentLocation#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrationFragmentLocation extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RegistrationViewModel viewModel;

    public RegistrationFragmentLocation() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegistrationFragmentLocation.
     */
    // TODO: Rename and change types and number of parameters
    public static RegistrationFragmentLocation newInstance(String param1, String param2) {
        RegistrationFragmentLocation fragment = new RegistrationFragmentLocation();
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
        View view = inflater.inflate(R.layout.fragment_registration_location, container, false);

        viewModel = new ViewModelProvider(requireActivity()).get(RegistrationViewModel.class);

        Button button = view.findViewById(R.id.btnNext);

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

        TextInputEditText city = view.findViewById(R.id.inputCity);
        TextInputEditText address = view.findViewById(R.id.inputAddress);
        TextInputEditText zipCode = view.findViewById(R.id.inputZipCode);

        button.setOnClickListener(v -> {
            if (autoCompleteTextView.getText().toString().trim().length() <= 0 || city.getText().toString().equals("") ||
                    address.getText().toString().equals("") || zipCode.getText().toString().equals("")) {
                Toast.makeText(getActivity(), "You must fill in all field", Toast.LENGTH_SHORT).show();
                return;
            }

            UserRegistrationDTO user = new UserRegistrationDTO();
            user.setEmail(viewModel.getEmail().getValue());
            user.setPassword(viewModel.getPassword().getValue());
            user.setConfirmPassword(viewModel.getConfirmPassword().getValue());
            user.setFirstName(viewModel.getFirstName().getValue());
            user.setLastName(viewModel.getLastName().getValue());
            user.setPhone(viewModel.getPhone().getValue());
            user.setRole(viewModel.getRole().getValue());
            Address a = new Address();
            a.setCountry(autoCompleteTextView.getText().toString());
            a.setCity(city.getText().toString());
            a.setAddress(address.getText().toString());
            a.setZipCode(zipCode.getText().toString());
            user.setAddress(a);

            Call<MessageDTO> call = ClientUtils.accountService.register(user);
            call.enqueue(new Callback<MessageDTO>() {
                @Override
                public void onResponse(Call<MessageDTO> call, Response<MessageDTO> response) {
                    if (response.code() == 200) {
                        Toast.makeText(getActivity(), getString(R.string.check_email), Toast.LENGTH_SHORT).show();
                        MessageDTO result = response.body();
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        startActivity(intent);
                        getActivity().finish();
                    }
                    if(response.code() == 400){
                        Toast.makeText(getActivity(), "Already have account", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<MessageDTO> call, Throwable t) {
                    Toast.makeText(getActivity(), "Fail", Toast.LENGTH_SHORT).show();
                }
            });

        });
        return view;
    }
}