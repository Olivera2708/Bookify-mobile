package com.example.bookify;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ReservationRequestsViewPagerAdapter extends FragmentStateAdapter {

    public ReservationRequestsViewPagerAdapter(@NonNull FragmentActivity fragmentActivity){
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
