package com.binaryit.blooddonationapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.binaryit.blooddonationapp.Adapter.AllMemberViewPagerAdapter;
import com.binaryit.blooddonationapp.R;
import com.google.android.material.tabs.TabLayout;

public class AllMemberActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    AllMemberViewPagerAdapter allMemberViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_member);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager);

        allMemberViewPagerAdapter = new AllMemberViewPagerAdapter(this);
        viewPager2.setAdapter(allMemberViewPagerAdapter);

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


        //imageView.findViewById(R.id.imageView);

       /* recyclerView = findViewById(R.id.recyclerView);
        searchMemberEditText = findViewById(R.id.searchMemberEditText);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        adapter = new AdminUserDetailsAdapter(this, userList);
        recyclerView.setAdapter(adapter);

        dbUser = FirebaseDatabase.getInstance().getReference().child("User");
        query = dbUser.orderByChild("name");

        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        searchMemberEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });*/

    }

    /*private void filter(String text) {

        ArrayList<User> list = new ArrayList<>();

        for (User item : userList) {
            if (item.getName().toLowerCase().contains(text.toLowerCase())) {
                list.add(item);
            }
        }
        adapter.filterList(list);
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSwipeRight(AllMemberActivity.this);
    }
}