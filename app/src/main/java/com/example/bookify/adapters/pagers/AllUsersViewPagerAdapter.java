package com.example.bookify.adapters.pagers;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.bookify.fragments.user.AllUsersFragment;
import com.example.bookify.fragments.feedback.ReportedUsersFragment;

public class AllUsersViewPagerAdapter extends FragmentStateAdapter {

    Activity activity;
    public AllUsersViewPagerAdapter(@NonNull FragmentActivity fragmentActivity){
        super(fragmentActivity);
        activity = fragmentActivity;
    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new ReportedUsersFragment();
            default:
                return AllUsersFragment.newInstance(activity);
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
