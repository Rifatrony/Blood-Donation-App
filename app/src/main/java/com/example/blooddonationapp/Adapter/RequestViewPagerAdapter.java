package com.example.blooddonationapp.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.blooddonationapp.Fragment.AcceptRequestFragment;
import com.example.blooddonationapp.Fragment.MyRequestFragment;
import com.example.blooddonationapp.Fragment.ViewSentRequestFragment;

public class RequestViewPagerAdapter extends FragmentStateAdapter {


    public RequestViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new MyRequestFragment();

            case 1:
                return new ViewSentRequestFragment();

            case 2:
                return new AcceptRequestFragment();

            default:
                return new MyRequestFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
