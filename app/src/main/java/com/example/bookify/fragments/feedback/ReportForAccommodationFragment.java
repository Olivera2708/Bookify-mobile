package com.example.bookify.fragments.feedback;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.anychart.APIlib;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Cartesian;
import com.anychart.charts.Pie;
import com.anychart.core.cartesian.series.Column;
import com.anychart.enums.Anchor;
import com.anychart.enums.HoverMode;
import com.anychart.enums.Position;
import com.anychart.enums.TooltipPositionMode;
import com.example.bookify.R;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.model.DropdownItem;
import com.example.bookify.model.accommodation.ChartDTO;
import com.example.bookify.utils.JWTUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    AnyChartView incomeYearChart;
    Cartesian cartesianIncome;
    AnyChartView reservationChart;
    Cartesian cartesianReservations;


    final Long[] accommodationId = new Long[1];
    Integer selected = null;


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

        incomeYearChart = view.findViewById(R.id.incomeYearChart);
        APIlib.getInstance().setActiveAnyChartView(incomeYearChart);
        cartesianIncome = AnyChart.column();
        setIncomeYearChart(view);
        incomeYearChart.setChart(cartesianIncome);

        reservationChart = view.findViewById(R.id.reservationsYearChart);
        APIlib.getInstance().setActiveAnyChartView(reservationChart);
        cartesianReservations = AnyChart.column();
        setReservationYearChart(view);
        reservationChart.setChart(cartesianReservations);

        return view;
    }

    private void charts(View view){
        ArrayAdapter<DropdownItem> adapter = new ArrayAdapter<>(getActivity(), R.layout.dropdown_item, getAccommodations());
        AutoCompleteTextView autoCompleteTextView = view.findViewById(R.id.filled_exposed);
        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener((parent, view1, position, id) -> {
            DropdownItem selectedItem = (DropdownItem) parent.getItemAtPosition(position);
            accommodationId[0] = selectedItem.getId();
            if (selected != null)
                getData();
        });

        ArrayAdapter<Integer> adapterYear = new ArrayAdapter<>(getActivity(), R.layout.dropdown_item, getYears());
        AutoCompleteTextView autoCompleteTextViewYear = view.findViewById(R.id.filled_exposed_year);
        autoCompleteTextViewYear.setAdapter(adapterYear);

        autoCompleteTextViewYear.setOnItemClickListener((parent, view1, position, id) -> {
            selected = (Integer) parent.getItemAtPosition(position);
            if (accommodationId[0] != null)
                getData();
        });

        Button download = view.findViewById(R.id.download);
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
                Long ownerId = sharedPreferences.getLong(JWTUtils.USER_ID, -1);
                if (accommodationId[0] != null && selected != null) {
                    checkAndRequestPermissionsForDownload(ownerId, accommodationId[0], selected);
                }
            }
        });
    }

    private void checkAndRequestPermissionsForDownload(Long ownerId, Long accommodationId, int year) {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_EXTERNAL_STORAGE);
        } else {
            // Permission is already granted, proceed with PDF download
            downloadPdf(ownerId, accommodationId, year);
        }
    }

    private void downloadPdf(Long ownerId, Long accommodationId, int year) {
        Call<ResponseBody> call = ClientUtils.accommodationService.generatePdfReportForAccommodation(ownerId, accommodationId, year);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.code() == 200 && response.body() != null) {
                    savePdf(response.body());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                JWTUtils.autoLogout((AppCompatActivity) getActivity(), t);
            }
        });
    }

    private void savePdf(ResponseBody body) {
        File path = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOWNLOADS);
        String fileName = "report" + System.currentTimeMillis() + ".pdf";
        File file = new File(path, fileName);

        try {
            path.mkdirs();
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(body.bytes());
            fos.close();

            Toast.makeText(getContext(), "File saved in downloads folder!", Toast.LENGTH_LONG).show();
        } catch (Exception e){
            Log.d("DownloadActivity", e.getMessage());
        }
    }

    private void setIncomeYearChart(View view){
        ColorDrawable colorDrawable = (ColorDrawable) getActivity().getWindow().getDecorView().getBackground();
        String hexColor = String.format("#%06X", (0xFFFFFF & colorDrawable.getColor()));

        cartesianIncome.background().enabled(true);
        cartesianIncome.background().fill(hexColor);

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Jan", 0));
        data.add(new ValueDataEntry("Feb", 0));
        data.add(new ValueDataEntry("Mar", 0));
        data.add(new ValueDataEntry("Apr", 0));
        data.add(new ValueDataEntry("May", 0));
        data.add(new ValueDataEntry("Jun", 0));
        data.add(new ValueDataEntry("Jul", 0));
        data.add(new ValueDataEntry("Aug", 0));
        data.add(new ValueDataEntry("Sep", 0));
        data.add(new ValueDataEntry("Oct", 0));
        data.add(new ValueDataEntry("Nov", 0));
        data.add(new ValueDataEntry("Dec", 0));

        Column columnIncome = cartesianIncome.column(data);
        columnIncome.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{decimalCount: 0} EUR");

        cartesianIncome.animation(true);
        cartesianIncome.title("Revenue");
        cartesianIncome.yScale().minimum(0d);
        cartesianIncome.yAxis(0).labels().format("{%Value}{decimalCount: 0} EUR");
        cartesianIncome.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesianIncome.interactivity().hoverMode(HoverMode.BY_X);
        cartesianIncome.xAxis(0).title("Month");
        cartesianIncome.yAxis(0).title("Revenue");
    }

    private void setReservationYearChart(View view){
        ColorDrawable colorDrawable = (ColorDrawable) getActivity().getWindow().getDecorView().getBackground();
        String hexColor = String.format("#%06X", (0xFFFFFF & colorDrawable.getColor()));

        cartesianReservations.background().enabled(true);
        cartesianReservations.background().fill(hexColor);

        List<DataEntry> dataReservations = new ArrayList<>();
        dataReservations.add(new ValueDataEntry("Jan", 0));
        dataReservations.add(new ValueDataEntry("Feb", 0));
        dataReservations.add(new ValueDataEntry("Mar", 0));
        dataReservations.add(new ValueDataEntry("Apr", 0));
        dataReservations.add(new ValueDataEntry("May", 0));
        dataReservations.add(new ValueDataEntry("Jun", 0));
        dataReservations.add(new ValueDataEntry("Jul", 0));
        dataReservations.add(new ValueDataEntry("Aug", 0));
        dataReservations.add(new ValueDataEntry("Sep", 0));
        dataReservations.add(new ValueDataEntry("Oct", 0));
        dataReservations.add(new ValueDataEntry("Nov", 0));
        dataReservations.add(new ValueDataEntry("Dec", 0));

        Column column = cartesianReservations.column(dataReservations);
        column.tooltip()
                .titleFormat("{%X}")
                .position(Position.CENTER_BOTTOM)
                .anchor(Anchor.CENTER_BOTTOM)
                .offsetX(0d)
                .offsetY(5d)
                .format("{%Value}{decimalCount: 0}");

        cartesianReservations.animation(true);
        cartesianReservations.title("Days reserved");
        cartesianReservations.yScale().minimum(0d);
        cartesianReservations.yAxis(0).labels().format("{%Value}{decimalCount: 0}");
        cartesianReservations.tooltip().positionMode(TooltipPositionMode.POINT);
        cartesianReservations.interactivity().hoverMode(HoverMode.BY_X);
        cartesianReservations.xAxis(0).title("Month");
        cartesianReservations.yAxis(0).title("Reservations");
    }

    private void getData(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        Long ownerId = sharedPreferences.getLong(JWTUtils.USER_ID, -1);
        Call<List<ChartDTO>> call = ClientUtils.accommodationService.getAccommodationCharts(ownerId, accommodationId[0], selected);
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

    private void showResults(List<ChartDTO> charts){
        if (charts.size() == 0)
            Toast.makeText(getContext(), "No data for selected period!", Toast.LENGTH_LONG).show();
        else {
            updateIncomeYearChart(charts);
            updateReservationChart(charts);
        }
    }

    private void updateIncomeYearChart(List<ChartDTO> chart) {
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        List<DataEntry> data = new ArrayList<>();
        for (int i = 0; i < chart.size(); i++)
            data.add(new ValueDataEntry(months[i], chart.get(i).getProfitOfAccommodation()));

        APIlib.getInstance().setActiveAnyChartView(incomeYearChart);
        cartesianIncome.data(data);
    }

    private void updateReservationChart(List<ChartDTO> chart) {
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        List<DataEntry> data = new ArrayList<>();
        for (int i = 0; i < chart.size(); i++)
            data.add(new ValueDataEntry(months[i], chart.get(i).getNumberOfReservations()));

        APIlib.getInstance().setActiveAnyChartView(reservationChart);
        cartesianReservations.data(data);
    }

    private List<DropdownItem> getAccommodations(){
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        Long guestId = sharedPreferences.getLong(JWTUtils.USER_ID, -1);
        List<DropdownItem> itemList = new ArrayList<>();
        Call<Map<Long, String>> call = ClientUtils.accommodationService.getNamesForAccommodations(guestId);
        call.enqueue(new Callback<Map<Long, String>>() {
            @Override
            public void onResponse(Call<Map<Long, String>> call, Response<Map<Long, String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    for (Long o : response.body().keySet()){
                        itemList.add(new DropdownItem(response.body().get(o), o));
                    }
                }
            }
            @Override
            public void onFailure(Call<Map<Long, String>> call, Throwable t) {
                Log.d("AccommodationNames", "Accommodation names not here");
                JWTUtils.autoLogout((AppCompatActivity) getActivity(), t);
            }
        });
        return itemList;
    }

    private List<Integer> getYears() {
        List<Integer> years = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int year = 2021; year <= currentYear; year++)
            years.add(year);
        return years;
    }

}