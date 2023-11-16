package com.example.bookify;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class AllUsersViewPagerAdapter extends FragmentStateAdapter {

    public AllUsersViewPagerAdapter(@NonNull FragmentActivity fragmentActivity){
        super(fragmentActivity);

    }
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 1:
                return new ReportedUsersFragment();
            default:
                return new AllUsersFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
