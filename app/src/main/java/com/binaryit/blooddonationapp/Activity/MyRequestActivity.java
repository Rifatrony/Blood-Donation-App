package com.binaryit.blooddonationapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.binaryit.blooddonationapp.Adapter.RequestViewPagerAdapter;
import com.binaryit.blooddonationapp.R;
import com.google.android.material.tabs.TabLayout;

public class MyRequestActivity extends AppCompatActivity implements View.OnClickListener {

    TabLayout tabLayout;
    ViewPager2 viewPager2;

    AppCompatImageView imageBack;

    RequestViewPagerAdapter requestViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_request);

        initialization();
        setListener();

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

    }

    private void setListener() {
        imageBack.setOnClickListener(this);
    }

    private void initialization() {

        imageBack = findViewById(R.id.imageBack);
        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager);

        requestViewPagerAdapter = new RequestViewPagerAdapter(this);
        viewPager2.setAdapter(requestViewPagerAdapter);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageBack:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSwipeRight(MyRequestActivity.this);
    }
}