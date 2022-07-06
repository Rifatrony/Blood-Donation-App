package com.binaryit.blooddonationapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.binaryit.blooddonationapp.Fragment.AllDonorFragment;
import com.binaryit.blooddonationapp.Fragment.NearestDonorFragment;

public class DonorViewPagerAdapter extends FragmentStateAdapter {

    public DonorViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new AllDonorFragment();

            case 1:
                return new NearestDonorFragment();

            default:
                return new AllDonorFragment();
        }

    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
