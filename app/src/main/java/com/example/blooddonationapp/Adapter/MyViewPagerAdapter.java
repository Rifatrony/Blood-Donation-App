package com.example.blooddonationapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.blooddonationapp.Fragment.BloodRequestFragment;
import com.example.blooddonationapp.Fragment.CoordinatorFragment;
import com.example.blooddonationapp.Fragment.OrganizationFragment;
import com.example.blooddonationapp.Fragment.TodayReadyFragment;

public class MyViewPagerAdapter extends FragmentStateAdapter {

    public MyViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {

        switch (position){
            case 0:
                return new BloodRequestFragment();

            case 1:
                return new TodayReadyFragment();

            case 2:
            return new CoordinatorFragment();

            case 3:
                return new OrganizationFragment();

            default:
                return new BloodRequestFragment();
        }

    }

    @Override
    public int getItemCount() {

        /*This is the number of tab*/
        return 4;
    }
}
