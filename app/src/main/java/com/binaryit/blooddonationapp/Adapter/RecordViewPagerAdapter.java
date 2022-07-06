package com.binaryit.blooddonationapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.binaryit.blooddonationapp.Fragment.DonateRecordFragment;
import com.binaryit.blooddonationapp.Fragment.TakeBloodRecordFragment;

public class RecordViewPagerAdapter extends FragmentStateAdapter {

    public RecordViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new DonateRecordFragment();

            case 1:
                return new TakeBloodRecordFragment();

            default:
                return new DonateRecordFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
