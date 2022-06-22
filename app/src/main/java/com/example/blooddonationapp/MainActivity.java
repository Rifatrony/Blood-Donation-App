package com.example.blooddonationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

import com.example.blooddonationapp.Activity.AcceptRequestActivity;
import com.example.blooddonationapp.Activity.AddNewDonorActivity;
import com.example.blooddonationapp.Activity.BloodRequestActivity;
import com.example.blooddonationapp.Activity.CoordinatorActivity;
import com.example.blooddonationapp.Activity.DonateRecordActivity;
import com.example.blooddonationapp.Activity.DonorListActivity;
import com.example.blooddonationapp.Activity.LoginActivity;
import com.example.blooddonationapp.Activity.OrganizationActivity;
import com.example.blooddonationapp.Activity.ProfileActivity;
import com.example.blooddonationapp.Activity.SelectBloodGroupActivity;
import com.example.blooddonationapp.Activity.TodayReadyDonorActivity;
import com.example.blooddonationapp.Adapter.UserAdapter;
import com.example.blooddonationapp.Model.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
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

    TextView header_name, header_number, header_blood_group, header_type;

    DatabaseReference userRef;

    RecyclerView recyclerView;
    List<User> userList;
    UserAdapter adapter;
    private static final int REQUEST_CODE = 101;

    ProgressBar progressBar;
    TextView noDataFoundTextView;

    String current_time;
    String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialization();

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
        recyclerView.setLayoutManager(layoutManager);

        userList = new ArrayList<>();
        adapter = new UserAdapter(this, userList);
        recyclerView.setAdapter(adapter);

        userRef = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance()
                .getCurrentUser().getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               try {
                   readUser();

               }catch (Exception e){
                   progressBar.setVisibility(View.GONE);
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
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        /*Header Closed*/



        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                drawerLayout.closeDrawer(GravityCompat.START);

                switch (id){

                    case R.id.nav_today_ready_donor:
                        startActivity(new Intent(getApplicationContext(), TodayReadyDonorActivity.class));
                        break;

                    case R.id.nav_blood_request:
                        startActivity(new Intent(getApplicationContext(), BloodRequestActivity.class));
                        break;

                    case R.id.nav_search_location:

                        startActivity(new Intent(getApplicationContext(), SelectBloodGroupActivity.class));
                        break;

                    case R.id.nav_accept_request:
                        startActivity(new Intent(getApplicationContext(), AcceptRequestActivity.class));
                        break;

                    /*case R.id.nav_search_blood:
                        startActivity(new Intent(getApplicationContext(), MapActivity.class));
                        break;*/

                    case R.id.nav_add_donor:
                        startActivity(new Intent(getApplicationContext(), AddNewDonorActivity.class));
                        break;

                    case R.id.nav_add_coordinator:
                        startActivity(new Intent(getApplicationContext(), CoordinatorActivity.class));
                        break;

                    case R.id.nav_add_organization:
                        setVisible(true);
                        //setVisibility(View.VISIBLE);
                        startActivity(new Intent(getApplicationContext(), OrganizationActivity.class));
                        break;

                    case R.id.nav_donate_record:
                        startActivity(new Intent(getApplicationContext(), DonateRecordActivity.class));
                        break;

                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        break;

                    case R.id.nav_DonorList:
                        startActivity(new Intent(getApplicationContext(), DonorListActivity.class));
                        break;

                    case R.id.nav_logout:
                        logOut();
                        break;

                    /*case R.id.nav_share:
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT,"Check this application");
                        intent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=com.whatsapp");
                        startActivity(Intent.createChooser(intent,"Share Via"));
                        break;*/

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

        progressBar = findViewById(R.id.progressBar);
        noDataFoundTextView = findViewById(R.id.noDataFoundTextView);

        recyclerView = findViewById(R.id.recyclerView);

        toolbar = findViewById(R.id.topAppBar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        mAuth = FirebaseAuth.getInstance();

        header_name = navigationView.getHeaderView(0).findViewById(R.id.header_name_textView);
        header_number = navigationView.getHeaderView(0).findViewById(R.id.header_number_textView);
        header_blood_group = navigationView.getHeaderView(0).findViewById(R.id.header_blood_group_textView);
    }


    private void readUser() {
        final FirebaseUser firebaseUser = mAuth.getCurrentUser();
        progressBar.setVisibility(View.VISIBLE);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User");
        //Query query = reference.orderByChild("type").equalTo("donor");

        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);

                    /*this line add 19/6/2022*/
                    assert user != null;
                    if (!user.getId().equals(firebaseUser.getUid())){
                        progressBar.setVisibility(View.INVISIBLE);
                        userList.add(user);
                    }
                }
                adapter.notifyDataSetChanged();

                if (userList.isEmpty()){
                    progressBar.setVisibility(View.INVISIBLE);
                    noDataFoundTextView.setVisibility(View.VISIBLE);
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
        finish();

    }

    @Override
    public void onBackPressed() {

        if (backPressedTime + 2000 >System.currentTimeMillis()){
            super.onBackPressed();
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