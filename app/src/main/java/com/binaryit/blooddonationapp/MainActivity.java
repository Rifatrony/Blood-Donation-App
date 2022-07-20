package com.binaryit.blooddonationapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.binaryit.blooddonationapp.Model.ConfirmBloodModel;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.binaryit.blooddonationapp.Activity.AcceptRequestActivity;
import com.binaryit.blooddonationapp.Activity.CoordinatorActivity;
import com.binaryit.blooddonationapp.Activity.CoordinatorTypeActivity;
import com.binaryit.blooddonationapp.Activity.DashBoardActivity;
import com.binaryit.blooddonationapp.Activity.DonateRecordActivity;
import com.binaryit.blooddonationapp.Activity.DonorListActivity;
import com.binaryit.blooddonationapp.Activity.LoginActivity;
import com.binaryit.blooddonationapp.Activity.MyRequestActivity;
import com.binaryit.blooddonationapp.Activity.OrganizationActivity;
import com.binaryit.blooddonationapp.Activity.ProfileActivity;
import com.binaryit.blooddonationapp.Activity.SelectBloodGroupActivity;
import com.binaryit.blooddonationapp.Activity.TodayReadyDonorActivity;
import com.binaryit.blooddonationapp.Adapter.MyViewPagerAdapter;
import com.binaryit.blooddonationapp.Adapter.UserAdapter;
import com.binaryit.blooddonationapp.Model.User;

import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView profile;

    private long backPressedTime;
    private Toast backToast;

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;

    String h_name, h_number, h_blood_group, h_type;

    FirebaseAuth mAuth;

    TextView header_name, header_number, header_blood_group, header_total_donate, header_type;

    DatabaseReference userRef, dbTimes;

    RecyclerView recyclerView;
    List<User> userList;
    UserAdapter adapter;
    private static final int REQUEST_CODE = 101;

    TabLayout tabLayout;
    ViewPager2 viewPager2;
    MyViewPagerAdapter viewPagerAdapter;

    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialization();

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

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

        /*Check Location Permission start*/

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }else{
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }

        /*Check Location Permission end*/

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        userRef = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance()
                .getCurrentUser().getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               try {

               }catch (Exception e){
                   Toast.makeText(getApplicationContext(), e.getMessage().toString(), Toast.LENGTH_SHORT).show();
               }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("User");

        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);

                    if (user.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){

                        if (user.getRole().equals("user")){

                            navigationView.getMenu().clear();
                            navigationView.inflateMenu(R.menu.user_menu);

                        } else {
                            navigationView.getMenu().clear();
                            navigationView.inflateMenu(R.menu.admin_menu);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        /*Header Start*/
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    h_name = snapshot.child("name").getValue().toString();
                    header_name.setText(h_name);

                    h_number = snapshot.child("number").getValue().toString();
                    header_number.setText(h_number);

                    h_blood_group = snapshot.child("blood_group").getValue().toString();
                    header_blood_group.setText(h_blood_group);

                    if (!snapshot.child("total_donate").exists()){
                        HashMap hashMap = new HashMap();
                        hashMap.put("total_donate","0");
                        userRef.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(MainActivity.this, "New row inserted", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*Header Closed*/

        dbTimes = FirebaseDatabase.getInstance().getReference().child("Confirm Blood");

        dbTimes.addValueEventListener(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    ConfirmBloodModel confirmBloodModel = dataSnapshot.getValue(ConfirmBloodModel.class);

                    if (confirmBloodModel.getAccepted_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){

                        count = confirmBloodModel.getTimes() + count;
                        System.out.println("Count is " + count);

                        try {
                            header_total_donate.setText("Total Donate: " + count + " times");
                        }
                        catch (Exception e){

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);

                switch (id){

                    case R.id.nav_today_ready_donor:
                        startActivity(new Intent(getApplicationContext(), TodayReadyDonorActivity.class));
                        //Animatoo.animateSplit(MainActivity.this);
                        Animatoo.animateSwipeLeft(MainActivity.this);
                        //Animatoo.animateSlideDown(MainActivity.this);
                        break;

                    case R.id.nav_today_dashboard:
                        startActivity(new Intent(getApplicationContext(), DashBoardActivity.class));
                        Animatoo.animateSwipeLeft(MainActivity.this);
                        break;

                    case R.id.nav_my_request:
                        startActivity(new Intent(getApplicationContext(), MyRequestActivity.class));
                        Animatoo.animateSwipeLeft(MainActivity.this);
                        break;

                    case R.id.nav_search_location:

                        startActivity(new Intent(getApplicationContext(), SelectBloodGroupActivity.class));
                        Animatoo.animateSwipeLeft(MainActivity.this);
                        break;

                    case R.id.nav_accept_request:
                        startActivity(new Intent(getApplicationContext(), AcceptRequestActivity.class));
                        Animatoo.animateSwipeLeft(MainActivity.this);
                        break;

                    case R.id.nav_add_coordinator:
                        startActivity(new Intent(getApplicationContext(), CoordinatorActivity.class));
                        Animatoo.animateSwipeLeft(MainActivity.this);
                        break;

                    case R.id.nav_add_coordinator_type:
                        startActivity(new Intent(getApplicationContext(), CoordinatorTypeActivity.class));
                        Animatoo.animateSwipeLeft(MainActivity.this);
                        break;

                    case R.id.nav_add_organization:
                        startActivity(new Intent(getApplicationContext(), OrganizationActivity.class));
                        Animatoo.animateSwipeLeft(MainActivity.this);
                        break;

                    case R.id.nav_donate_record:
                        startActivity(new Intent(getApplicationContext(), DonateRecordActivity.class));
                        Animatoo.animateSwipeLeft(MainActivity.this);
                        break;

                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        Animatoo.animateSwipeLeft(MainActivity.this);
                        break;

                    case R.id.nav_DonorList:
                        Intent intent = new Intent(new Intent(getApplicationContext(), DonorListActivity.class));
                        intent.putExtra("blood_group", h_blood_group);
                        startActivity(intent);
                        Animatoo.animateSwipeLeft(MainActivity.this);
                        break;

                    case R.id.nav_logout:
                        logOut();
                        break;

                    case R.id.nav_share:
                        Intent intent1 = new Intent(Intent.ACTION_SEND);
                        intent1.setType("text/plain");
                        intent1.putExtra(Intent.EXTRA_SUBJECT,"Check this application");
                        intent1.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=com.binaryit.blooddonationapp");
                        startActivity(Intent.createChooser(intent1,"Share Via"));
                        break;

                    default:
                        return true;
                }
                return true;
            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }

    private void initialization() {

        tabLayout = findViewById(R.id.tab_layout);
        viewPager2 = findViewById(R.id.view_pager);

        viewPagerAdapter = new MyViewPagerAdapter(this);
        viewPager2.setAdapter(viewPagerAdapter);

        toolbar = findViewById(R.id.topAppBar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        mAuth = FirebaseAuth.getInstance();

        header_name = navigationView.getHeaderView(0).findViewById(R.id.header_name_textView);
        header_number = navigationView.getHeaderView(0).findViewById(R.id.header_number_textView);
        header_blood_group = navigationView.getHeaderView(0).findViewById(R.id.header_blood_group_textView);
        header_total_donate = navigationView.getHeaderView(0).findViewById(R.id.header_total_donate_textView);
    }


    private void readUser() {
        final FirebaseUser firebaseUser = mAuth.getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User");

        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);

                    assert user != null;
                    if (!user.getId().equals(firebaseUser.getUid())){
                        userList.add(user);
                    }
                }
                adapter.notifyDataSetChanged();

                if (userList.isEmpty()){
                    Toast.makeText(MainActivity.this, "No Donor Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void logOut(){

        mAuth.signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        Animatoo.animateSwipeRight(MainActivity.this);
        finish();

    }

    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 >System.currentTimeMillis()){
            super.onBackPressed();
            Animatoo.animateSwipeLeft(MainActivity.this);
            return;
        }
        else {
            backToast = Toast.makeText(this, "Tap again to exit...", Toast.LENGTH_SHORT);
            backToast.show();
        }

        backPressedTime = System.currentTimeMillis();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this,
                            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

}