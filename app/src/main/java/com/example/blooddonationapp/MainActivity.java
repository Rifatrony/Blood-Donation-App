package com.example.blooddonationapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blooddonationapp.Activity.DonorListActivity;
import com.example.blooddonationapp.Activity.LoginActivity;
import com.example.blooddonationapp.Activity.ProfileActivity;
import com.example.blooddonationapp.Adapter.UserAdapter;
import com.example.blooddonationapp.Model.User;
import com.example.blooddonationapp.Utilities.SessionManagement;
import com.example.blooddonationapp.Utilities.SessionModel;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
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

    String email, password;
    String h_name, h_number, h_blood_group, h_type;

    FirebaseAuth mAuth;

    TextView header_name, header_number, header_blood_group, header_type;

    DatabaseReference userRef;

    RecyclerView recyclerView;
    List<User> userList;
    UserAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        userList = new ArrayList<>();
        adapter = new UserAdapter(this, userList);
        recyclerView.setAdapter(adapter);

        userRef = FirebaseDatabase.getInstance().getReference().child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String type = snapshot.child("type").getValue().toString();
                if (type.equals("donor")){
                    readRecipients();
                }
                else {
                    readDonor();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        toolbar = findViewById(R.id.topAppBar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.navigation_view);
        mAuth = FirebaseAuth.getInstance();

        header_name = navigationView.getHeaderView(0).findViewById(R.id.header_name_textView);
        header_number = navigationView.getHeaderView(0).findViewById(R.id.header_number_textView);
        header_blood_group = navigationView.getHeaderView(0).findViewById(R.id.header_blood_group_textView);
        header_type = navigationView.getHeaderView(0).findViewById(R.id.header_type_textView);



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

                    h_type = snapshot.child("type").getValue().toString();
                    header_type.setText(h_type);

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

                    case R.id.nav_profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        break;

                    case R.id.nav_DonorList:
                        startActivity(new Intent(getApplicationContext(), DonorListActivity.class));
                        break;

                    case R.id.nav_logout:
                        logOut();
                        break;

                    case R.id.nav_share:
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_SUBJECT,"Check this application");
                        intent.putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=com.whatsapp");
                        startActivity(Intent.createChooser(intent,"Share Via"));
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

    private void readDonor() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User");
        Query query = reference.orderByChild("type").equalTo("donor");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
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

    private void readRecipients(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User");
        Query query = reference.orderByChild("type").equalTo("recipient");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    userList.add(user);
                }
                adapter.notifyDataSetChanged();

                if (userList.isEmpty()){
                    Toast.makeText(MainActivity.this, "No Data Found", Toast.LENGTH_SHORT).show();
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

}