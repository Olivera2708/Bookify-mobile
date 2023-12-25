package com.example.bookify.adapters.pagers;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.bookify.fragments.accommodation.CreatedAccommodationsFragment;
import com.example.bookify.fragments.accommodation.EditedAccommodationsFragment;


public class AccommodationRequestsViewPagerAdapter extends FragmentStateAdapter {

    private Activity activity;
    public AccommodationRequestsViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
        this.activity = fragmentActivity;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return EditedAccommodationsFragment.newInstance(activity);
            default:
                return CreatedAccommodationsFragment.newInstance(activity);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
