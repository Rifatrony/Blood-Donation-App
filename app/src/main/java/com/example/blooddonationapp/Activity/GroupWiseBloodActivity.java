package com.example.blooddonationapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blooddonationapp.Adapter.UserAdapter;
import com.example.blooddonationapp.Model.User;
import com.example.blooddonationapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GroupWiseBloodActivity extends AppCompatActivity {

    TextView bloodGroupTextView, noDonorFoundTextView;
    AppCompatButton sendRequestButton;
    ProgressBar progressBar;
    AppCompatImageView imageBack;
    RecyclerView recyclerView;
    UserAdapter adapter;

    double currentLat;
    double currentLong;

    double x1, x2, y1, y2;
    ArrayList<User> userList = new ArrayList<>();
    FusedLocationProviderClient fusedLocationProviderClient;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    DatabaseReference dbUser;
    String blood_group;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_wise_blood);

        dbUser = FirebaseDatabase.getInstance().getReference().child("User");

        bloodGroupTextView = findViewById(R.id.bloodGroupTextView);
        imageBack = findViewById(R.id.imageBack);
        noDonorFoundTextView = findViewById(R.id.noDonorFoundTextView);
        progressBar = findViewById(R.id.progressBar);
        sendRequestButton = findViewById(R.id.sendRequestButton);

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        sendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(), RequestActivity.class));
                dbUser.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        userList.clear();

                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                            progressBar.setVisibility(View.GONE);
                            User user = dataSnapshot.getValue(User.class);

                            if (!user.getId().equals(firebaseUser.getUid())&& user.getBlood_group().equals(blood_group)){

                                System.out.println("Uid is: " + user.getId());
                                System.out.println("Name is: " + user.getName());

                            }
                        }
                        //adapter.notifyDataSetChanged();

                        /*if (userList.isEmpty()) {
                            progressBar.setVisibility(View.INVISIBLE);
                            noDonorFoundTextView.setVisibility(View.VISIBLE);
                            sendRequestButton.setVisibility(View.INVISIBLE);
                        }*/
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        blood_group = getIntent().getStringExtra("group");
        System.out.println("Blood Group is ==== > "+blood_group);
        bloodGroupTextView.setText(blood_group +" Donor List");

        mAuth = FirebaseAuth.getInstance();

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        /*Check Location permission*/

        if (ContextCompat.checkSelfPermission(GroupWiseBloodActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(GroupWiseBloodActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(GroupWiseBloodActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }else{
                ActivityCompat.requestPermissions(GroupWiseBloodActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }
        }

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                progressBar.setVisibility(View.VISIBLE);

                Location location = task.getResult();

                if (location!= null){

                    currentLat = location.getLatitude();
                    currentLong = location.getLongitude();

                    try {
                        Geocoder geocoder = new Geocoder(GroupWiseBloodActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        double Radius = 6371;  // earth radius in km
                        double radius = 5; // km
                        x1 = currentLong - Math.toDegrees(radius/Radius/Math.cos(Math.toRadians(currentLat)));
                        x2 = currentLong + Math.toDegrees(radius/Radius/Math.cos(Math.toRadians(currentLat)));
                        y1 = currentLat + Math.toDegrees(radius/Radius);
                        y2 = currentLat - Math.toDegrees(radius/Radius);

                        System.out.println("X1 is " + x1 +"\nX2 is " + x2+"\nY1 is " + y1+"\nY2 is " + y2);
                        System.out.println("Current Lat == > " + currentLat +"\nCurrent Long == > " + currentLong);

                        recyclerView = findViewById(R.id.recyclerView);
                        recyclerView.setHasFixedSize(true);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                        adapter = new UserAdapter(GroupWiseBloodActivity.this, userList);
                        recyclerView.setAdapter(adapter);

                        firebaseUser = mAuth.getCurrentUser();


                        dbUser.addValueEventListener(new ValueEventListener() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                userList.clear();

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                    progressBar.setVisibility(View.GONE);
                                    User user = dataSnapshot.getValue(User.class);

                                    if (!user.getId().equals(firebaseUser.getUid())&& user.getBlood_group().equals(blood_group)){
                                        System.out.println("Token are: " + user.getToken());
                                        System.out.println("Number of Token are: " + userList.size());

                                        String latitude1 = user.getLatitude();
                                        double value = Double.parseDouble(latitude1);

                                        if (value < y1 && value >= y2) {
                                            System.out.println("Latitudes Are : " + user.getLatitude());
                                            userList.add(user);
                                        }
                                    }
                                }
                                adapter.notifyDataSetChanged();

                                if (userList.isEmpty()) {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    noDonorFoundTextView.setVisibility(View.VISIBLE);
                                    sendRequestButton.setVisibility(View.INVISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {


                            }
                        });

                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });


    }
}