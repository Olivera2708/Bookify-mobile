package com.example.bookify.adapters.pagers;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.bookify.fragments.reservation.RequestFragmentOwner;
import com.example.bookify.fragments.reservation.ReservationsFragmentOwner;

public class ReservationRequestsViewPagerOwnerAdapter extends FragmentStateAdapter {

    public ReservationRequestsViewPagerOwnerAdapter(@NonNull FragmentActivity fragmentActivity){
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new ReservationsFragmentOwner();
            case 1:
                return new RequestFragmentOwner();
        }
        return new ReservationsFragmentOwner();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
