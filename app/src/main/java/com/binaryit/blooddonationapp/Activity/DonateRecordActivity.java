package com.binaryit.blooddonationapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.binaryit.blooddonationapp.Adapter.ConfirmBloodAdapter;
import com.binaryit.blooddonationapp.Adapter.RecordViewPagerAdapter;
import com.binaryit.blooddonationapp.Model.ConfirmBloodModel;
import com.binaryit.blooddonationapp.R;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DatabaseReference;

import java.util.List;

public class DonateRecordActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<ConfirmBloodModel> confirmBloodModelList;
    ConfirmBloodAdapter adapter;

    AppCompatImageView imageBack;

    DatabaseReference dbConfirmBlood;

    TabLayout tabLayout;
    ViewPager2 viewPager2;

    RecordViewPagerAdapter recordViewPagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_record);

        initialization();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

                tabLayout.getTabAt(position).select();
            }
        });

        imageBack.setOnClickListener(view -> onBackPressed());

    }

    private void initialization() {

        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager);

        recordViewPagerAdapter = new RecordViewPagerAdapter(this);
        viewPager2.setAdapter(recordViewPagerAdapter);

        imageBack = findViewById(R.id.imageBack);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSwipeRight(DonateRecordActivity.this);
    }
}