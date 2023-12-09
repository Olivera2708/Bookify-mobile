package com.example.bookify.adapters.pagers;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.bookify.fragments.accommodation.CreatedAccommodationsFragment;
import com.example.bookify.fragments.accommodation.EditedAccommodationsFragment;


public class AccommodationRequestsViewPagerAdapter extends FragmentStateAdapter {
    public AccommodationRequestsViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new EditedAccommodationsFragment();
            default:
                return new CreatedAccommodationsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
