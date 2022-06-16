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
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class GroupWiseBloodActivity extends AppCompatActivity implements View.OnClickListener {

    TextView bloodGroupTextView, noDonorFoundTextView;
    AppCompatButton sendRequestButton;
    ProgressBar progressBar;
    AppCompatImageView imageBack;
    RecyclerView recyclerView;
    UserAdapter adapter;

    String blood_group, patient_problem, blood_amount, donate_date,
            donate_time, donate_location, recipient_number, reference;

    double currentLat;
    double currentLong;

    double x1, x2, y1, y2;
    ArrayList<User> userList = new ArrayList<>();
    FusedLocationProviderClient fusedLocationProviderClient;

    String current_user_name,name, message = "Request for 1 bag AB+ blood";
    String uid;
    String currentUserId;

    DatabaseReference dbRequest;
    FirebaseAuth mAuth;
    FirebaseUser firebaseUser;
    DatabaseReference dbUser;

    String current_time;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_wise_blood);

        initialization();
        setListener();

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("hh:mm:ss");
        current_time = mdformat.format(calendar.getTime());
        System.out.println(current_time);

        blood_group = getIntent().getStringExtra("blood_group");
        patient_problem = getIntent().getStringExtra("patient_problem");
        blood_amount = getIntent().getStringExtra("blood_amount");
        donate_date = getIntent().getStringExtra("donate_date");
        donate_time = getIntent().getStringExtra("donate_time");
        donate_location = getIntent().getStringExtra("donate_location");
        recipient_number = getIntent().getStringExtra("recipient_number");
        reference = getIntent().getStringExtra("reference");

        System.out.println("Blood Group is ==== > "+blood_group +"\n");
        System.out.println("blood_amount is ==== > "+blood_amount +"\n");
        System.out.println("donate_date is ==== > "+donate_date +"\n");
        System.out.println("donate_time is ==== > "+donate_time +"\n");
        System.out.println("donate_location is ==== > "+donate_location +"\n");
        System.out.println("recipient_number is ==== > "+recipient_number +"\n");
        System.out.println("reference is ==== > "+reference +"\n");
        bloodGroupTextView.setText(blood_group +" Donor List");

        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

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

    private void initialization(){
        mAuth = FirebaseAuth.getInstance();
        dbUser = FirebaseDatabase.getInstance().getReference().child("User");

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        bloodGroupTextView = findViewById(R.id.bloodGroupTextView);
        imageBack = findViewById(R.id.imageBack);
        noDonorFoundTextView = findViewById(R.id.noDonorFoundTextView);
        progressBar = findViewById(R.id.progressBar);
        sendRequestButton = findViewById(R.id.sendRequestButton);
    }

    private void setListener(){
        imageBack.setOnClickListener(this);
        sendRequestButton.setOnClickListener(this);
    }

    private void sendRequest(){
        dbUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    progressBar.setVisibility(View.GONE);
                    User user = dataSnapshot.getValue(User.class);

                    if (!user.getId().equals(firebaseUser.getUid())&& user.getBlood_group().equals(blood_group)){
                        uid = user.getId();
                        name = user.getName();
                        /*Take blood group from here*/
                        System.out.println("Name is: " + name);
                    }

                    if (user.getId().equals(firebaseUser.getUid())){
                        current_user_name = user.getName();
                        message = current_user_name +" Request you for "+blood_amount+" "+ user.getBlood_group() + " at " + donate_location;
                        System.out.println("My name is " + current_user_name);
                        System.out.println("Message is ...====> " + message);
                    }

                    try {
                        currentUserId = mAuth.getCurrentUser().getUid();
                        dbRequest = FirebaseDatabase.getInstance().getReference().child("Request").child(uid).child(currentUserId);//

                        HashMap request = new HashMap();
                        request.put("message", message);
                        request.put("blood_group", blood_group);
                        request.put("patient_problem", patient_problem);
                        request.put("blood_amount", blood_amount);
                        request.put("donate_date", donate_date);
                        request.put("donate_time", donate_time);
                        request.put("donate_location", donate_location);
                        request.put("recipient_number", recipient_number);
                        request.put("reference", reference);
                        request.put("current_time", current_time);
                        request.put("name", name);
                        request.put("status", "pending");
                        request.put("uid", uid);
                        request.put("request_uid", currentUserId);
                        dbRequest.updateChildren(request).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()){
                                    Toast.makeText(GroupWiseBloodActivity.this, "You sent Blood Request", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }catch (Exception e){

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageBack:
                onBackPressed();
                break;
            case R.id.sendRequestButton:
                sendRequest();
        }
    }
}