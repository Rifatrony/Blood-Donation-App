package com.binaryit.blooddonationapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.binaryit.blooddonationapp.Fragment.AllMemberFragment;
import com.binaryit.blooddonationapp.Fragment.BirthdayWiseFragment;

public class AllMemberViewPagerAdapter extends FragmentStateAdapter {

    public AllMemberViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new AllMemberFragment();

            case 1:
                return new BirthdayWiseFragment();

            default:
                return new AllMemberFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
