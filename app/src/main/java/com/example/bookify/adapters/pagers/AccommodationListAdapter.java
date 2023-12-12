package com.example.bookify.adapters.pagers;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.bookify.R;
import com.example.bookify.clients.ClientUtils;
import com.example.bookify.model.AccommodationBasicDTO;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationListAdapter extends ArrayAdapter<AccommodationBasicDTO> {
    private List<AccommodationBasicDTO> accommodations;
    private Activity activity;

    public AccommodationListAdapter(Activity context, List<AccommodationBasicDTO> accommodations){
        super(context, R.layout.accomodation_view, accommodations);
        this.accommodations = accommodations;
        this.activity = context;
    }

    @Override
    public int getCount() {
        return accommodations.size();
    }

    /*
     * Ova metoda vraca pojedinacan element na osnovu pozicije
     * */
    @Nullable
    @Override
    public AccommodationBasicDTO getItem(int position) {
        return accommodations.get(position);
    }

    /*
     * Ova metoda vraca jedinstveni identifikator, za adaptere koji prikazuju
     * listu ili niz, pozicija je dovoljno dobra. Naravno mozemo iskoristiti i
     * jedinstveni identifikator objekta, ako on postoji.
     * */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     * Ova metoda popunjava pojedinacan element ListView-a podacima.
     * Ako adapter cuva listu od n elemenata, adapter ce u petlji ici
     * onoliko puta koliko getCount() vrati. Prilikom svake iteracije
     * uzece java objekat sa odredjene poziciuje (model) koji cuva podatke,
     * i layout koji treba da prikaze te podatke (view) npr R.layout.product_card.
     * Kada adapter ima model i view, prosto ce uzeti podatke iz modela,
     * popuniti view podacima i poslati listview da prikaze, i nastavice
     * sledecu iteraciju.
     * */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AccommodationBasicDTO accommodation = getItem(position);
//        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.accomodation_view,
                    parent, false);
//        }

        TextView name = convertView.findViewById(R.id.apartment_name);
        TextView address = convertView.findViewById(R.id.apartment_address);
        RatingBar start = convertView.findViewById(R.id.stars);
        TextView price = convertView.findViewById(R.id.price);
        TextView pricePer = convertView.findViewById(R.id.priceperday);
        Button details = convertView.findViewById(R.id.details);
        ImageView image = convertView.findViewById(R.id.acc_image);

        if(accommodation != null){
            name.setText(accommodation.getName());
            address.setText(accommodation.getAddress().toString());
            price.setText(String.valueOf(accommodation.getTotalPrice()));
            pricePer.setText(accommodation.getPriceOne() + " per " + accommodation.getPricePer().toString().toLowerCase());
            start.setRating(accommodation.getAvgRating());

            details.setOnClickListener(v -> {
                Log.i("Test", "Otvori aaccommodation broj " + accommodation.getId());
            });

            Log.d("Image", "Moja slika je " +accommodation.getId());
            Call<ResponseBody> call = ClientUtils.accommodationService.getImage(accommodation.getImageId());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());
                        image.setImageBitmap(bitmap);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.d("Image", "Basic accommodation image");
                }
            });
        }
        return convertView;
    }

    public void addData(List<AccommodationBasicDTO> newData) {
        accommodations.addAll(newData);
        notifyDataSetChanged();
    }
}
