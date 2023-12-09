package com.example.bookify.adapters.pagers;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.bookify.fragments.feedback.ReportForAccommodationFragment;
import com.example.bookify.fragments.feedback.ReportOverallFragment;

public class ReportsViewPagerAdapter extends FragmentStateAdapter {
    public ReportsViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new ReportOverallFragment();
            case 1:
                return new ReportForAccommodationFragment();
            default:
                return new ReportOverallFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
