package com.example.bookify.activities.accommodation;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.util.Pair;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
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
import com.example.bookify.model.accommodation.AccommodationBasicDTO;
import com.example.bookify.model.FilterDTO;
import com.example.bookify.model.accommodation.SearchResponseDTO;
import com.example.bookify.navigation.NavigationBar;
import com.example.bookify.R;
import com.example.bookify.utils.ScrollHandler;
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

public class ResultsActivity extends AppCompatActivity implements SensorEventListener {

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
    //    ScrollHandler handler;
    private SensorManager sensorManager;
    private static final int SHAKE_THRESHOLD = 800;
    private Sensor accelerometer;
    private long lastUpdate;
    private float last_x;
    private float last_y;
    private float last_z;

    String[] allAmenities = new String[]{
            "Free WiFi", "Air conditioning", "Terrace", "Swimming pool", "Bar", "Sauna", "Luggage storage",
            "Lunch", "Airport shuttle", "Wheelchair", "Non smoking", "Free parking",
            "Family rooms", "Garden", "24-hour front desk", "Jacuzzi", "Heating",
            "Breakfast", "Diner", "Private bathroom", "Deposit box", "City center"};
    private int[] checkboxIds = new int[]{
            R.id.freeWiFi, R.id.airConditioning, R.id.terrace, R.id.swimmingPool, R.id.bar, R.id.sauna, R.id.luggageStorage,
            R.id.lunch, R.id.airportShuttle, R.id.wheelchair, R.id.non_smoking, R.id.freeParking,
            R.id.familyRooms, R.id.garden, R.id.frontDesk, R.id.jacuzzi, R.id.heating,
            R.id.breakfast, R.id.dinner, R.id.privateBathroom, R.id.depositBox, R.id.cityCenter};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        NavigationBar.setNavigationBar(findViewById(R.id.bottom_navigaiton), this, R.id.navigation_home);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        format = new SimpleDateFormat("dd.MM.yyyy.");
        editDate = findViewById(R.id.dateButton);
        locationInput = findViewById(R.id.locationText);
        personsInput = findViewById(R.id.peopleText);

        resetFilter();
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
                        resetFilter();
                        page = 0;
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
                    resetFilter();
                    hideKeyboard();
                    page = 0;
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
                    resetFilter();
                    hideKeyboard();
                    page = 0;
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

    private void filterData() {
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

    private void searchData() {
        if (this.persons > 0 && begin.after(new Date()) && !begin.equals(end)) {
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

                }
            });
        } else
            Toast.makeText(ResultsActivity.this, "Please enter correct parameters", Toast.LENGTH_SHORT).show();
    }

    private void showResults(List<AccommodationBasicDTO> accommodations) {
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

//        handler = new ScrollHandler(this, listView);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, final int totalItemCount) {
                if (totalItemCount > 0) {
                    int lastInScreen = firstVisibleItem + visibleItemCount;
                    if (lastInScreen == totalItemCount && (page + 1) * 10 <= totalResults) {
                        page++;
                        if (!sort.equals("") || filter.getMaxPrice() != -1 || filter.getFilters().size() != 0 || filter.getTypes().size() != 3)
                            filterData();
                        else
                            searchData();
                    }
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
//        handler.unregister();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        handler.unregister();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null) {
            sensorManager.registerListener(this,
                    sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL);
        }
//        if (handler != null) {
//            handler.register();
//        }
    }

    private void getSearchData() {
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

    private void setSearchValues(String dates) {
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
        } else {
            priceSlider.setValueTo(Math.round(this.maxPrice));
            if (filter.getMaxPrice() == -1)
                priceSlider.setValues((float) Math.round(this.minPrice), (float) Math.round(this.maxPrice));
            else
                priceSlider.setValues((float) Math.round(filter.getMinPrice()), (float) Math.round(filter.getMaxPrice()));
        }

        EditText minPriceEdit = dialog.findViewById(R.id.minPrice);
        if (filter.getMinPrice() == -1)
            minPriceEdit.setText(String.valueOf(Math.round(this.minPrice)), TextView.BufferType.EDITABLE);
        else
            minPriceEdit.setText(String.valueOf(Math.round(filter.getMinPrice())), TextView.BufferType.EDITABLE);
        EditText maxPriceEdit = dialog.findViewById(R.id.maxPrice);
        if (filter.getMaxPrice() == -1)
            maxPriceEdit.setText(String.valueOf(Math.round(this.maxPrice)), TextView.BufferType.EDITABLE);
        else
            maxPriceEdit.setText(String.valueOf(Math.round(filter.getMaxPrice())), TextView.BufferType.EDITABLE);

        minPriceEdit.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    hideKeyboard();
                    if (minPriceEdit.getText().toString().equals("") || Integer.valueOf(minPriceEdit.getText().toString()) < minPrice || Integer.valueOf(minPriceEdit.getText().toString()) > maxPrice || Integer.valueOf(minPriceEdit.getText().toString()) > Integer.valueOf(maxPriceEdit.getText().toString()))
                        minPriceEdit.setText(String.valueOf(Math.round(minPrice)));
                    float minValue = Float.valueOf(minPriceEdit.getText().toString());
                    priceSlider.setValues(minValue, priceSlider.getValues().get(1));
                    minPriceEdit.setSelection(minPriceEdit.getText().length());
                    return true;
                }
                return false;
            }
        });

        maxPriceEdit.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    hideKeyboard();
                    if (maxPriceEdit.getText().toString().equals("") || Integer.valueOf(maxPriceEdit.getText().toString()) < minPrice || Integer.valueOf(maxPriceEdit.getText().toString()) > maxPrice || Integer.valueOf(minPriceEdit.getText().toString()) > Integer.valueOf(maxPriceEdit.getText().toString()))
                        maxPriceEdit.setText(String.valueOf(Math.round(maxPrice)));
                    float maxVal = Float.valueOf(maxPriceEdit.getText().toString());
                    priceSlider.setValues(priceSlider.getValues().get(0), maxVal);
                    maxPriceEdit.setSelection(maxPriceEdit.getText().length());
                    return true;
                }
                return false;
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
                    case 0:
                        sort = "Name";
                        break;
                    case 1:
                        sort = "Lowest";
                        break;
                    case 2:
                        sort = "Highest";
                        break;
                }
            }
        });

        setTypeCheckboxes();
        setAmenitiesCheckboxes();

        Button remove = dialog.findViewById(R.id.remove);
        Button save = dialog.findViewById(R.id.save);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteAllAmenities();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isChanged = true;

                if (Integer.valueOf(minPriceEdit.getText().toString()) != Math.round(minPrice) || Integer.valueOf(maxPriceEdit.getText().toString()) != Math.round(maxPrice)) {
                    filter.setMaxPrice(Integer.valueOf(maxPriceEdit.getText().toString()));
                    filter.setMinPrice(Integer.valueOf(minPriceEdit.getText().toString()));
                }

                setFilterTypes();
                setAmenities();
                page = 0;
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

    private void setFilterTypes() {
        CheckBox hotel = dialog.findViewById(R.id.hotel);
        CheckBox apartment = dialog.findViewById(R.id.apartment);
        CheckBox room = dialog.findViewById(R.id.room);

        List<AccommodationType> types = new ArrayList<>();
        if (hotel.isChecked())
            types.add(AccommodationType.HOTEL);
        if (apartment.isChecked())
            types.add(AccommodationType.APARTMENT);
        if (room.isChecked())
            types.add(AccommodationType.ROOM);

        filter.setTypes(types);
    }

    private void setAmenities() {
        List<Filter> filterAmenities = new ArrayList<>();
        for (int i = 0; i < checkboxIds.length; i++) {
            CheckBox checkbox = dialog.findViewById(checkboxIds[i]);
            if (checkbox.isChecked())
                filterAmenities.add(Filter.valueOf(transformLabel(allAmenities[i])));
        }
        filter.setFilters(filterAmenities);
    }

    private void setAmenitiesCheckboxes() {
        if (filter.getFilters().size() != 0) {
            for (int i = 0; i < checkboxIds.length; i++) {
                if (filter.getFilters().contains(Filter.valueOf(transformLabel(allAmenities[i])))) {
                    CheckBox checkbox = dialog.findViewById(checkboxIds[i]);
                    checkbox.setChecked(true);
                }
            }
        }
    }

    private void deleteAllAmenities() {
        for (int id : checkboxIds) {
            CheckBox checkBox = dialog.findViewById(id);
            checkBox.setChecked(false);
        }

        CheckBox hotel = dialog.findViewById(R.id.hotel);
        CheckBox apartment = dialog.findViewById(R.id.apartment);
        CheckBox room = dialog.findViewById(R.id.room);
        hotel.setChecked(true);
        apartment.setChecked(true);
        room.setChecked(true);
    }

    private void setTypeCheckboxes() {
        CheckBox hotel = dialog.findViewById(R.id.hotel);
        CheckBox apartment = dialog.findViewById(R.id.apartment);
        CheckBox room = dialog.findViewById(R.id.room);

        if (filter.getTypes().size() != 3) {
            if (!filter.getTypes().contains(AccommodationType.ROOM))
                room.setChecked(false);
            if (!filter.getTypes().contains(AccommodationType.HOTEL))
                hotel.setChecked(false);
            if (!filter.getTypes().contains(AccommodationType.APARTMENT))
                apartment.setChecked(false);
        }

        List<AccommodationType> types = new ArrayList<>();
        if (hotel.isChecked())
            types.add(AccommodationType.HOTEL);
        if (apartment.isChecked())
            types.add(AccommodationType.APARTMENT);
        if (room.isChecked())
            types.add(AccommodationType.ROOM);

        filter.setTypes(types);
    }

    private String getSortValue() {
        if (sort.equals("Lowest"))
            return "Price lowest first";
        else if (sort.equals("Highest"))
            return "Price highest first";
        return sort;
    }

    private String transformLabel(String label) {
        if (label.equals("24-hour front desk"))
            return "FRONT_DESK";
        return label.toUpperCase().replace(' ', '_');
    }

    private void resetFilter() {
        List<Filter> listF = new ArrayList<>();
        List<AccommodationType> type = new ArrayList<>();
        type.add(AccommodationType.HOTEL);
        type.add(AccommodationType.APARTMENT);
        type.add(AccommodationType.ROOM);
        filter = new FilterDTO(listF, type, -1, -1);
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(ResultsActivity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    String[] sorts = new String[]{"Name", "Lowest", "Highest"};
    int counter = -1;

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long curTime = System.currentTimeMillis();
            if ((curTime - lastUpdate) > 330) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float[] values = event.values;
                float x = values[0];
                float y = values[1];
                float z = values[2];

                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed > SHAKE_THRESHOLD ) {
                    if (counter >= 2) {
                        counter = 0;
                    } else {
                        counter++;
                    }
                    Log.d("REZ", "shake detected w/ speed: " + counter);
                    sort = sorts[counter];
                    isChanged = true;
                    page = 0;
                    filterData();
                }
                last_x = x;
                last_y = y;
                last_z = z;
            }

        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}