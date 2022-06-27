package com.example.blooddonationapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.blooddonationapp.Fragment.AllDonorFragment;
import com.example.blooddonationapp.Fragment.CompatibleWithMeFragment;
import com.example.blooddonationapp.Fragment.NearestDonorFragment;

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

            case 2:
                return new CompatibleWithMeFragment();

            default:
                return new AllDonorFragment();
        }

    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
