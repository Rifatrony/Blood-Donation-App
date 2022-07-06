package com.binaryit.blooddonationapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.binaryit.blooddonationapp.Adapter.DonorViewPagerAdapter;
import com.binaryit.blooddonationapp.R;
import com.google.android.material.tabs.TabLayout;

public class DonorListActivity extends AppCompatActivity implements View.OnClickListener {

    AppCompatImageView imageBack;

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    DonorViewPagerAdapter donorViewPagerAdapter;

    String blood_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_list);

        initialization();
        setListener();

        if (getIntent().getExtras() != null){
            blood_group = getIntent().getStringExtra("blood_group");
            System.out.println("blood_group " + blood_group);
        }

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

    private void initialization() {

        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager);

        donorViewPagerAdapter = new DonorViewPagerAdapter(this);
        viewPager2.setAdapter(donorViewPagerAdapter);

        imageBack = findViewById(R.id.imageBack);

    }

    private void setListener(){

        imageBack.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id. sendRequestButton:
                startActivity(new Intent(getApplicationContext(), RequestActivity.class));
                break;
            case R.id. imageBack:
                onBackPressed();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSwipeRight(DonorListActivity.this);
    }

}