package com.example.bookify.adapters.pagers;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.bookify.fragments.reservation.RequestFragmentGuest;
import com.example.bookify.fragments.reservation.ReservationsFragmentGuest;

public class ReservationRequestsViewPagerGuestAdapter extends FragmentStateAdapter {

    public ReservationRequestsViewPagerGuestAdapter(@NonNull FragmentActivity fragmentActivity){
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new ReservationsFragmentGuest();
            case 1:
                return new RequestFragmentGuest();
        }
        return new ReservationsFragmentGuest();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
