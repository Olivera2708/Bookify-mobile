package com.example.bookify.fragments.feedback;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.chart.common.listener.Event;
import com.anychart.chart.common.listener.ListenersInterface;
import com.anychart.charts.Pie;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Align;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.LegendLayout;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.bookify.R;
import com.example.bookify.activities.accommodation.AccommodationDetailsActivity;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.model.accommodation.ChartDTO;
import com.example.bookify.model.accommodation.SearchResponseDTO;
import com.example.bookify.utils.JWTUtils;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReportOverallFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReportOverallFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    AnyChartView incomeYearChart;
    Cartesian cartesianIncome;
    AnyChartView reservationChart;
    Pie pie;

    public ReportOverallFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReportOverallFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReportOverallFragment newInstance(String param1, String param2) {
        ReportOverallFragment fragment = new ReportOverallFragment();
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
        View view = inflater.inflate(R.layout.fragment_report_overall, container, false);
        charts(view);

        incomeYearChart = view.findViewById(R.id.incomeChart);
        APIlib.getInstance().setActiveAnyChartView(incomeYearChart);
        cartesianIncome = AnyChart.column();
        setIncomeYearChart(view);
        incomeYearChart.setChart(cartesianIncome);

        reservationChart = view.findViewById(R.id.reservationChart);
        APIlib.getInstance().setActiveAnyChartView(reservationChart);
        pie = AnyChart.pie();
        setReservationChart(view);
        reservationChart.setChart(pie);

        return view;
    }

    private void charts(View view){
        Button editDate = view.findViewById(R.id.editDate);
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker<Pair<Long, Long>> materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(new Pair<>(
                        MaterialDatePicker.thisMonthInUtcMilliseconds(),
                        MaterialDatePicker.todayInUtcMilliseconds()
                )).build();

                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long, Long> selection) {
                        String startDate = new SimpleDateFormat("dd.MM.yyyy.", Locale.getDefault()).format(new Date(selection.first));
                        String endDate = new SimpleDateFormat("dd.MM.yyyy.", Locale.getDefault()).format(new Date(selection.second));

                        editDate.setText(startDate + " - " + endDate);

                        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
                        Long ownerId = sharedPreferences.getLong(JWTUtils.USER_ID, -1);
                        Call<List<ChartDTO>> call = ClientUtils.accommodationService.getOverallCharts(ownerId, startDate, endDate);
                        call.enqueue(new Callback<List<ChartDTO>>() {
                            @Override
                            public void onResponse(Call<List<ChartDTO>> call, Response<List<ChartDTO>> response) {
                                if (response.code() == 200 && response.body() != null) {
                                    showResults(response.body());
                                }
                            }

                            @Override
                            public void onFailure(Call<List<ChartDTO>> call, Throwable t) {
                                JWTUtils.autoLogout((AppCompatActivity) getActivity(), t);
                            }
                        });
                    }
                });
                materialDatePicker.show(getActivity().getSupportFragmentManager(), "tag");
            }
        });
    }

    private void showResults(List<ChartDTO> charts){
        if (charts.size() == 0)
            Toast.makeText(getContext(), "No data for selected period!", Toast.LENGTH_LONG).show();
        else {
            updateIncomeYearChart(charts);
            updateReservationChart(charts);
        }
    }

    private void updateIncomeYearChart(List<ChartDTO> chart) {
        List<DataEntry> data = new ArrayList<>();
        for (ChartDTO c : chart) {
            data.add(new ValueDataEntry(c.getName(), c.getProfitOfAccommodation()));
        }

        APIlib.getInstance().setActiveAnyChartView(incomeYearChart);
        cartesianIncome.data(data);
    }

    private void updateReservationChart(List<ChartDTO> chart) {
        List<DataEntry> data = new ArrayList<>();
        for (ChartDTO c : chart) {
            data.add(new ValueDataEntry(c.getName(), c.getNumberOfReservations()));
        }

        APIlib.getInstance().setActiveAnyChartView(reservationChart);
        pie.data(data);
    }


    private void setIncomeYearChart(View view){
        ColorDrawable colorDrawable = (ColorDrawable) getActivity().getWindow().getDecorView().getBackground();
        String hexColor = String.format("#%06X", (0xFFFFFF & colorDrawable.getColor()));

        cartesianIncome.background().enabled(true);
        cartesianIncome.background().fill(hexColor);

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("", 0));

        Column columnIncome = cartesianIncome.column(data);
        columnIncome.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{decimalCount: 0} EUR");

        cartesianIncome.animation(true);
        cartesianIncome.title("Revenue for selected period");
        cartesianIncome.yScale().minimum(0d);
        cartesianIncome.yAxis(0).labels().format("{%Value}{decimalCount: 0} EUR");
        cartesianIncome.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesianIncome.interactivity().hoverMode(HoverMode.BY_X);
        cartesianIncome.xAxis(0).title("Accommodation");
        cartesianIncome.yAxis(0).title("Revenue");
    }

    private void setReservationChart(View view){
        ColorDrawable colorDrawable = (ColorDrawable) getActivity().getWindow().getDecorView().getBackground();
        String hexColor = String.format("#%06X", (0xFFFFFF & colorDrawable.getColor()));
        pie.background().enabled(true);
        pie.background().fill(hexColor);

        pie.setOnClickListener(new ListenersInterface.OnClickListener(new String[]{"x", "value"}) {
            @Override
            public void onClick(Event event) {
            }
        });

        List<DataEntry> dataPie = new ArrayList<>();
        dataPie.add(new ValueDataEntry("", 0));

        pie.data(dataPie);
        pie.title("Days reserved for selected period");
        pie.labels().position("outside");
        pie.legend().title().enabled(true);
        pie.legend().title()
                .text("Accommodations")
                .padding(0d, 0d, 10d, 0d);
        pie.legend()
                .position("center-bottom")
                .itemsLayout(LegendLayout.HORIZONTAL)
                .align(Align.CENTER);
    }
}