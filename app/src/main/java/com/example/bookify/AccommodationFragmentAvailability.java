package com.example.bookify;

import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccommodationFragmentAvailability#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccommodationFragmentAvailability extends Fragment {

    private TableLayout tableLayout;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AccommodationFragmentAvailability() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccommodationFragmentAvailability.
     */
    // TODO: Rename and change types and number of parameters
    public static AccommodationFragmentAvailability newInstance(String param1, String param2) {
        AccommodationFragmentAvailability fragment = new AccommodationFragmentAvailability();
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
        View view = inflater.inflate(R.layout.fragment_accommodation_availability, container, false);

        Button dates = view.findViewById(R.id.datesInput);
        dates.setOnClickListener(v -> {
            MaterialDatePicker<Pair<Long, Long>> materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(new Pair<>(
                    MaterialDatePicker.thisMonthInUtcMilliseconds(),
                    MaterialDatePicker.todayInUtcMilliseconds()
            )).build();

            materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                @Override
                public void onPositiveButtonClick(Pair<Long, Long> selection) {
                    String startDate = new SimpleDateFormat("dd.MM.yyyy.", Locale.getDefault()).format(new Date(selection.first));
                    String endDate = new SimpleDateFormat("dd.MM.yyyy.", Locale.getDefault()).format(new Date(selection.second));

                    dates.setText(startDate + " - " + endDate);
                }
            });

            materialDatePicker.show(getActivity().getSupportFragmentManager(), "tag");
        });

        tableLayout = view.findViewById(R.id.tableLayout);

        TextInputEditText price = view.findViewById(R.id.priceInput);

        Button add = view.findViewById(R.id.btnAdd);
        add.setOnClickListener(v -> {
            String checkDates = dates.getText().toString();
            String priceTxt = price.getText().toString();
            addRowWithData(checkDates, priceTxt);
        });

        return view;
    }

    private void addRowWithData(String data1, String data2) {
        TableRow tableRow = new TableRow(getActivity());

        tableRow.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.border_background, null));

        // Set TableRow properties (optional)
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        tableRow.setLayoutParams(layoutParams);

        // Create TextView for Data 1
        TextView textViewData1 = createTextView(data1);
        // Create TextView for Data 2
        TextView textViewData2 = createTextView(data2);

        TextView textViewData3 = createTextView("Delete");

        textViewData3.setOnClickListener(v -> {
            tableLayout.removeView(tableRow);
        });

        // Add TextViews to TableRow
        tableRow.addView(textViewData1);
        tableRow.addView(textViewData2);
        tableRow.addView(textViewData3);

        // Add TableRow to TableLayout
        tableLayout.addView(tableRow);
    }

    // Method to create a TextView
    private TextView createTextView(String text) {
        TextView textView = new TextView(getActivity());
        textView.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.border_background, null));

        // Set TextView properties (optional)
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableRow.LayoutParams.WRAP_CONTENT
        );
        textView.setLayoutParams(layoutParams);
        textView.setPadding(16, 8, 16, 8);
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);

        return textView;
    }
}