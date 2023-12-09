package com.example.bookify.adapters.pagers;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.bookify.fragments.feedback.NewReviewsFragment;
import com.example.bookify.fragments.feedback.ReportedReviewsFragment;

public class FeedbackAdminViewPagerAdapter extends FragmentStateAdapter {
    public FeedbackAdminViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new ReportedReviewsFragment();
            default:
                return new NewReviewsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
