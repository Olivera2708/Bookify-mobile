package com.example.bookify.fragments.feedback;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.bookify.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportForAccommodationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportForAccommodationFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ReportForAccommodationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportForAccommodationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportForAccommodationFragment newInstance(String param1, String param2) {
        ReportForAccommodationFragment fragment = new ReportForAccommodationFragment();
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
        View view = inflater.inflate(R.layout.fragment_report_for_accommodation, container, false);
        charts(view);
        return view;
    }

    private void charts(View view){
        AnyChartView incomeYearChart = view.findViewById(R.id.incomeYearChart);
        APIlib.getInstance().setActiveAnyChartView(incomeYearChart);
        Cartesian cartesianIncome = AnyChart.column();

        ColorDrawable colorDrawable = (ColorDrawable) getActivity().getWindow().getDecorView().getBackground();
        String hexColor = String.format("#%06X", (0xFFFFFF & colorDrawable.getColor()));

        cartesianIncome.background().enabled(true);
        cartesianIncome.background().fill(hexColor);

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Jan", 120));
        data.add(new ValueDataEntry("Feb", 230));
        data.add(new ValueDataEntry("Mar", 670));
        data.add(new ValueDataEntry("Apr", 650));
        data.add(new ValueDataEntry("May", 780));
        data.add(new ValueDataEntry("Jun", 900));
        data.add(new ValueDataEntry("Jul", 1230));
        data.add(new ValueDataEntry("Aug", 1470));
        data.add(new ValueDataEntry("Sep", 890));
        data.add(new ValueDataEntry("Oct", 530));
        data.add(new ValueDataEntry("Nov", 440));
        data.add(new ValueDataEntry("Dec", 220));

        Column columnIncome = cartesianIncome.column(data);
        columnIncome.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{decimalCount: 0} EUR");

        cartesianIncome.animation(true);
        cartesianIncome.title("Revenue in 2022 for Test Apartment");
        cartesianIncome.yScale().minimum(0d);
        cartesianIncome.yAxis(0).labels().format("{%Value}{decimalCount: 0} EUR");
        cartesianIncome.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesianIncome.interactivity().hoverMode(HoverMode.BY_X);
        cartesianIncome.xAxis(0).title("Month");
        cartesianIncome.yAxis(0).title("Revenue");
        incomeYearChart.setChart(cartesianIncome);


        AnyChartView reservationsYearChart = view.findViewById(R.id.reservationsYearChart);
        APIlib.getInstance().setActiveAnyChartView(reservationsYearChart);
        Cartesian cartesianReservations = AnyChart.column();

        cartesianReservations.background().enabled(true);
        cartesianReservations.background().fill(hexColor);

        List<DataEntry> dataReservations = new ArrayList<>();
        dataReservations.add(new ValueDataEntry("Jan", 1));
        dataReservations.add(new ValueDataEntry("Feb", 2));
        dataReservations.add(new ValueDataEntry("Mar", 5));
        dataReservations.add(new ValueDataEntry("Apr", 4));
        dataReservations.add(new ValueDataEntry("May", 6));
        dataReservations.add(new ValueDataEntry("Jun", 8));
        dataReservations.add(new ValueDataEntry("Jul", 10));
        dataReservations.add(new ValueDataEntry("Aug", 12));
        dataReservations.add(new ValueDataEntry("Sep", 7));
        dataReservations.add(new ValueDataEntry("Oct", 3));
        dataReservations.add(new ValueDataEntry("Nov", 2));
        dataReservations.add(new ValueDataEntry("Dec", 1));

        Column column = cartesianReservations.column(dataReservations);
        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{decimalCount: 0}");

        cartesianReservations.animation(true);
        cartesianReservations.title("Reservations in 2022 for Test Apartment");
        cartesianReservations.yScale().minimum(0d);
        cartesianReservations.yAxis(0).labels().format("{%Value}{decimalCount: 0}");
        cartesianReservations.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesianReservations.interactivity().hoverMode(HoverMode.BY_X);
        cartesianReservations.xAxis(0).title("Month");
        cartesianReservations.yAxis(0).title("Reservations");
        reservationsYearChart.setChart(cartesianReservations);
    }
}