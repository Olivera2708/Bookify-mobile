package com.example.bookify.activities.accommodation;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookify.activities.LandingActivity;
import com.example.bookify.adapters.pagers.AccommodationListAdapter;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.enumerations.AccommodationType;
import com.example.bookify.enumerations.Filter;
import com.example.bookify.model.AccommodationBasicDTO;
import com.example.bookify.model.FilterDTO;
import com.example.bookify.model.SearchResponseDTO;
import com.example.bookify.navigation.NavigationBar;
import com.example.bookify.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.slider.RangeSlider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResultsActivity extends AppCompatActivity {

    FloatingActionButton filterButton;
    private AccommodationListAdapter adapter;
    ListView listView;
    Button editDate;
    EditText locationInput;
    EditText personsInput;
    String search = "";
    String dates;
    Date begin;
    Date end;
    int persons;
    BottomSheetDialog dialog;
    float minPrice = 0;
    float maxPrice = 1000;
    int page = 0;
    int totalResults = 0;
    SimpleDateFormat format;
    boolean isChanged = false;
    String sort = "";
    FilterDTO filter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        NavigationBar.setNavigationBar(findViewById(R.id.bottom_navigaiton), this, R.id.navigation_home);

        format = new SimpleDateFormat("dd.MM.yyyy.");
        editDate = findViewById(R.id.dateButton);
        locationInput = findViewById(R.id.locationText);
        personsInput = findViewById(R.id.peopleText);

        List<Filter> listF = new ArrayList<>();
        List<AccommodationType> type = new ArrayList<>();
        type.add(AccommodationType.HOTEL);
        type.add(AccommodationType.APARTMENT);
        type.add(AccommodationType.ROOM);
        filter = new FilterDTO(listF, type, -1, -1);

        getSearchData();
        searchData();

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialDatePicker<Pair<Long, Long>> materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setSelection(new Pair<>(
                        begin.getTime() + 24 * 60 * 60 * 1000,
                        end.getTime() + 24 * 60 * 60 * 1000)).build();

                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Pair<Long, Long>>() {
                    @Override
                    public void onPositiveButtonClick(Pair<Long, Long> selection) {
                        String startDate = new SimpleDateFormat("dd.MM.yyyy.", Locale.getDefault()).format(new Date(selection.first));
                        String endDate = new SimpleDateFormat("dd.MM.yyyy.", Locale.getDefault()).format(new Date(selection.second));
                        dates = startDate + " - " + endDate;
                        editDate.setText(dates);

                        try {
                            begin = format.parse(dates.split(" - ")[0]);
                            end = format.parse(dates.split(" - ")[1]);
                        } catch (ParseException e) {
                        }

                        isChanged = true;
                        searchData();
                    }
                });

                materialDatePicker.show(getSupportFragmentManager(), "tag");
            }
        });

        personsInput.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    persons = Integer.valueOf(personsInput.getText().toString());
                    isChanged = true;
                    searchData();
                    return true;
                }
                return false;
            }
        });

        locationInput.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    search = locationInput.getText().toString();
                    isChanged = true;
                    searchData();
                    return true;
                }
                return false;
            }
        });

        filterButton = (FloatingActionButton) findViewById(R.id.filterButton);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog();
            }
        });

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(ResultsActivity.this, LandingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void filterData(){
        Call<SearchResponseDTO> call = ClientUtils.accommodationService.getForFilter(this.search,
                this.dates.split(" - ")[0],
                this.dates.split(" - ")[1],
                this.persons, page, 10, this.sort, this.filter);
        call.enqueue(new Callback<SearchResponseDTO>() {
            @Override
            public void onResponse(Call<SearchResponseDTO> call, Response<SearchResponseDTO> response) {
                if (response.code() == 200 && response.body() != null) {
                    SearchResponseDTO result = response.body();
                    totalResults = result.getResults();

                    showResults(result.getAccommodations());
                }
            }

            @Override
            public void onFailure(Call<SearchResponseDTO> call, Throwable t) {
                Log.d("Error", "Search");
            }
        });
    }

    private void searchData(){
        if (this.persons > 1 && begin.after(new Date()) && !begin.equals(end)) {
            Call<SearchResponseDTO> call = ClientUtils.accommodationService.getForSearch(this.search,
                    this.dates.split(" - ")[0],
                    this.dates.split(" - ")[1],
                    this.persons, page, 10);
            call.enqueue(new Callback<SearchResponseDTO>() {
                @Override
                public void onResponse(Call<SearchResponseDTO> call, Response<SearchResponseDTO> response) {
                    if (response.code() == 200 && response.body() != null) {
                        SearchResponseDTO result = response.body();
                        minPrice = result.getMinPrice();
                        maxPrice = result.getMaxPrice();
                        totalResults = result.getResults();

                        showResults(result.getAccommodations());
                    }
                }

                @Override
                public void onFailure(Call<SearchResponseDTO> call, Throwable t) {
                    Log.d("Error", "Search");
                }
            });
        }
        else
            Toast.makeText(ResultsActivity.this, "Please enter correct parameters", Toast.LENGTH_SHORT).show();
    }

    private void showResults(List<AccommodationBasicDTO> accommodations){
        if (adapter == null) {
            adapter = new AccommodationListAdapter(this, accommodations);
            listView = findViewById(R.id.resultList);
            listView.setAdapter(adapter);
        } else if (!isChanged) {
            adapter.addData(accommodations);
        } else {
            isChanged = false;
            adapter.clear();
            adapter.addData(accommodations);
            listView.invalidateViews();
        }

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, final int totalItemCount) {
                if (totalItemCount > 0) {
                    int lastInScreen = firstVisibleItem + visibleItemCount;
                    if (lastInScreen == totalItemCount && (page+1) * 10 <= totalResults) {
                        page ++;
                        if (!sort.equals("") || filter.getMaxPrice() != -1 || filter.getFilters().size() != 0 || filter.getTypes().size() != 3)
                            filterData();
                        else
                            searchData();
                    }
                }
            }
        });
    }

    private void getSearchData(){
        Intent intent = getIntent();
        search = intent.getStringExtra("location");
        persons = intent.getIntExtra("persons", 2);
        dates = intent.getStringExtra("dates");
        try {
            begin = format.parse(dates.split(" - ")[0]);
            end = format.parse(dates.split(" - ")[1]);
        } catch (ParseException e) {
        }
        setSearchValues(dates);
    }

    private void setSearchValues(String dates){
        this.locationInput.setText(this.search);
        this.personsInput.setText(String.valueOf(this.persons));
        this.editDate.setText(dates);
    }

    private void showBottomDialog() {
        dialog = new BottomSheetDialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.filter);

        RangeSlider priceSlider = dialog.findViewById(R.id.priceBar);
        priceSlider.setValueFrom(Math.round(this.minPrice));
        if (Math.round(this.minPrice) == Math.round(this.maxPrice)) {
            priceSlider.setValueTo(Math.round(this.maxPrice) + 1);
            priceSlider.setValues((float) Math.round(this.minPrice), (float) Math.round(this.maxPrice) + 1);
        }
        else {
            priceSlider.setValueTo(Math.round(this.maxPrice));
            priceSlider.setValues((float) Math.round(this.minPrice), (float) Math.round(this.maxPrice));
        }

        EditText minPriceEdit = dialog.findViewById(R.id.minPrice);
        minPriceEdit.setText(String.valueOf(Math.round(this.minPrice)), TextView.BufferType.EDITABLE);
        minPriceEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    if (Integer.parseInt(editable.toString()) > 1000 || editable.toString().equals("-")){
                        minPriceEdit.setText(editable.toString().substring(0, minPriceEdit.getText().length() - 1));
                    }
                    else {
                        float minValue = Float.valueOf(editable.toString());
                        priceSlider.setValues(minValue, priceSlider.getValues().get(1));
                        minPriceEdit.setSelection(minPriceEdit.getText().length());
                    }
                }
            }
        });

        EditText maxPriceEdit = dialog.findViewById(R.id.maxPrice);
        maxPriceEdit.setText(String.valueOf(Math.round(this.maxPrice)), TextView.BufferType.EDITABLE);
        maxPriceEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() > 0) {
                    if (Integer.parseInt(editable.toString()) > 1000 || editable.toString().equals("-")){
                        maxPriceEdit.setText(editable.toString().substring(0, maxPriceEdit.getText().length() - 1));
                    }
                    else {
                        float maxValue = Float.valueOf(editable.toString());
                        priceSlider.setValues(priceSlider.getValues().get(0), maxValue);
                        maxPriceEdit.setSelection(maxPriceEdit.getText().length());
                    }
                }
            }
        });

        priceSlider.addOnChangeListener(new RangeSlider.OnChangeListener() {
            @Override
            public void onValueChange(@NonNull RangeSlider slider, float value, boolean fromUser) {
                int valueMin = Math.round(slider.getValues().get(0));
                minPriceEdit.setText(Integer.toString(valueMin));

                int valueMax = Math.round(slider.getValues().get(1));
                maxPriceEdit.setText(Integer.toString(valueMax));
            }
        });

        String[] sortVal = new String[]{"Name", "Price lowest first", "Price highest first"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.dropdown_item, sortVal);
        AutoCompleteTextView autoCompleteTextView = dialog.findViewById(R.id.filled_exposed);
        autoCompleteTextView.setAdapter(adapter);
        if (!sort.equals(""))
            autoCompleteTextView.setText(getSortValue(), false);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0: sort = "Name"; break;
                    case 1: sort = "Lowest"; break;
                    case 2: sort = "Highest"; break;
                }
            }
        });

        CheckBox hotel = dialog.findViewById(R.id.hotel);
        CheckBox apartment = dialog.findViewById(R.id.apartment);
        CheckBox freeWiFi = dialog.findViewById(R.id.freeWiFi);
        CheckBox airConditioning = dialog.findViewById(R.id.airConditioning);
        CheckBox terrace = dialog.findViewById(R.id.terrace);
        CheckBox swimmingPool = dialog.findViewById(R.id.swimmingPool);
        CheckBox bar = dialog.findViewById(R.id.bar);
        CheckBox sauna = dialog.findViewById(R.id.sauna);
        CheckBox luggageStorage = dialog.findViewById(R.id.luggageStorage);
        CheckBox lunch = dialog.findViewById(R.id.lunch);
        CheckBox airportShuttle = dialog.findViewById(R.id.airportShuttle);
        CheckBox wheelchair = dialog.findViewById(R.id.wheelchair);
        CheckBox non_smoking = dialog.findViewById(R.id.non_smoking);
        CheckBox freeParking = dialog.findViewById(R.id.freeParking);
        CheckBox familyRooms = dialog.findViewById(R.id.familyRooms);
        CheckBox garden = dialog.findViewById(R.id.garden);
        CheckBox frontDesk = dialog.findViewById(R.id.frontDesk);
        CheckBox jacuzzi = dialog.findViewById(R.id.jacuzzi);
        CheckBox heating = dialog.findViewById(R.id.heating);
        CheckBox breakfast = dialog.findViewById(R.id.breakfast);
        CheckBox dinner = dialog.findViewById(R.id.dinner);
        CheckBox privateBathroom = dialog.findViewById(R.id.privateBathroom);
        CheckBox depositBox = dialog.findViewById(R.id.depositBox);
        CheckBox cityCenter = dialog.findViewById(R.id.cityCenter);
        CheckBox room = dialog.findViewById(R.id.room);


        Button remove = dialog.findViewById(R.id.remove);
        Button save = dialog.findViewById(R.id.save);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hotel.setChecked(false);
                apartment.setChecked(false);
                freeWiFi.setChecked(false);
                airConditioning.setChecked(false);
                terrace.setChecked(false);
                swimmingPool.setChecked(false);
                bar.setChecked(false);
                sauna.setChecked(false);
                luggageStorage.setChecked(false);
                lunch.setChecked(false);
                airportShuttle.setChecked(false);
                wheelchair.setChecked(false);
                non_smoking.setChecked(false);
                freeParking.setChecked(false);
                familyRooms.setChecked(false);
                garden.setChecked(false);
                frontDesk.setChecked(false);
                jacuzzi.setChecked(false);
                heating.setChecked(false);
                breakfast.setChecked(false);
                dinner.setChecked(false);
                privateBathroom.setChecked(false);
                depositBox.setChecked(false);
                cityCenter.setChecked(false);
                room.setChecked(false);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChanged = true;
                filterData();
                dialog.cancel();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private String getSortValue(){
        if (sort.equals("Lowest"))
            return "Price lowest first";
        else if (sort.equals("Highest"))
            return "Price highest first";
        return sort;
    }
}