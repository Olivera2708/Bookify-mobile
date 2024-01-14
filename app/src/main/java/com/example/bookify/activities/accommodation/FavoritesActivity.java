package com.example.bookify.activities.accommodation;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.example.bookify.activities.LandingActivity;
import com.example.bookify.adapters.data.FavoritesListAdapter;
import com.example.bookify.adapters.pagers.AccommodationListAdapter;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.model.accommodation.AccommodationBasicDTO;
import com.example.bookify.model.accommodation.SearchResponseDTO;
import com.example.bookify.navigation.NavigationBar;
import com.example.bookify.R;
import com.example.bookify.utils.JWTUtils;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritesActivity extends AppCompatActivity {

    private FavoritesListAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        NavigationBar.setNavigationBar(findViewById(R.id.bottom_navigaiton), this, R.id.navigation_favorites);

        getData();

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent intent = new Intent(FavoritesActivity.this, LandingActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void getData(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("sharedPref", Context.MODE_PRIVATE);
        Long guestId = sharedPreferences.getLong(JWTUtils.USER_ID, -1);
        Call<List<AccommodationBasicDTO>> call = ClientUtils.accommodationService.getFavorites(guestId);
        call.enqueue(new Callback<List<AccommodationBasicDTO>>() {
            @Override
            public void onResponse(Call<List<AccommodationBasicDTO>> call, Response<List<AccommodationBasicDTO>> response) {
                if (response.code() == 200 && response.body() != null) {
                    showResults(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<AccommodationBasicDTO>> call, Throwable t) {

            }
        });
    }

    private void showResults(List<AccommodationBasicDTO> accommodations) {
        adapter = new FavoritesListAdapter(this, accommodations);
        listView = findViewById(R.id.resultList);
        listView.setAdapter(adapter);
    }
}