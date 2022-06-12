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
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.blooddonationapp.Adapter.DonorListAdapter;
import com.example.blooddonationapp.Adapter.UserAdapter;
import com.example.blooddonationapp.Adapter.UserModelAdapter;
import com.example.blooddonationapp.MainActivity;
import com.example.blooddonationapp.Model.User;
import com.example.blooddonationapp.Model.UserModel;
import com.example.blooddonationapp.Model.UserRegisterModel;
import com.example.blooddonationapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DonorListActivity extends AppCompatActivity {

    RecyclerView donorListRecyclerView;
    EditText searchBloodGroupEditText;

    AppCompatImageView imageBack;

    AppCompatButton sendRequestButton;

    SearchView searchView;
    GoogleMap map;
    SupportMapFragment supportMapFragment;

    DatabaseReference dbDonorList;

    UserModelAdapter adapter;

    double currentLat;
    double currentLong;

    double brng = 360;
    double initialLat = 23.8641225;
    double initialLng = 90.3990683;
    double dist = 5.0/6371.0;

    double x1, x2, y1, y2;

    DatabaseReference userRef;
    List<UserModel> userModelList = new ArrayList<>();

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_list);

        initialization();

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        sendRequestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DonorListActivity.this, "Request Send", Toast.LENGTH_SHORT).show();
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(DonorListActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(DonorListActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(DonorListActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }else{
                ActivityCompat.requestPermissions(DonorListActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }
        }

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                Location location = task.getResult();

                if (location!= null){

                    currentLat = location.getLatitude();
                    currentLong = location.getLongitude();

                    try {
                        Geocoder geocoder = new Geocoder(DonorListActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        double Radius = 6371;  // earth radius in km
                        double radius = 5; // km
                        x1 = currentLong - Math.toDegrees(radius/Radius/Math.cos(Math.toRadians(currentLat)));
                        x2 = currentLong + Math.toDegrees(radius/Radius/Math.cos(Math.toRadians(currentLat)));
                        y1 = currentLat + Math.toDegrees(radius/Radius);
                        y2 = currentLat - Math.toDegrees(radius/Radius);

                        System.out.println("X1 is " + x1 +"\nX2 is " + x2+"\nY1 is " + y1+"\nY2 is " + y2);
                        //Longitude = addresses.get(0).getLongitude();
                        //currentLat = addresses.get(0).getLatitude();
                        //currentLong = addresses.get(0).getLongitude();
                        //Toast.makeText(DonorListActivity.this, "Current Lat == > " + currentLat +"\nCurrent Long == > " + currentLong, Toast.LENGTH_SHORT).show();
                        System.out.println("Current Lat == > " + currentLat +"\nCurrent Long == > " + currentLong);


                        donorListRecyclerView = findViewById(R.id.donorListRecyclerView);
                        donorListRecyclerView.setHasFixedSize(true);
                        donorListRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                        adapter = new UserModelAdapter(DonorListActivity.this, userModelList);
                        donorListRecyclerView.setAdapter(adapter);

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User");
                        //Query query = reference.orderByChild("type").equalTo("donor");

                        reference.addValueEventListener(new ValueEventListener() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                userModelList.clear();

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                    UserModel user = dataSnapshot.getValue(UserModel.class);

                                    String latitude1 = user.getLatitude();
                                    double value = Double.parseDouble(latitude1);

                                    if (value < y1 && value >= y2) {
                                        System.out.println("Latitudes Are : " + user.getLatitude());
                                        userModelList.add(user);
                                    }
                                }
                                adapter.notifyDataSetChanged();

                                if (userModelList.isEmpty()) {

                                    Toast.makeText(DonorListActivity.this, "No Donor Found", Toast.LENGTH_SHORT).show();
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

        searchBloodGroupEditText.addTextChangedListener(new TextWatcher() {
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
        });
    }

    private void initialization() {
        imageBack = findViewById(R.id.imageBack);
        sendRequestButton = findViewById(R.id.sendRequestButton);
        searchBloodGroupEditText = findViewById(R.id.searchBloodGroupEditText);
        searchView = findViewById(R.id.sv_location);
    }

    private void filter(String text) {

        ArrayList<UserModel> filteredList = new ArrayList<>();

        for (UserModel item : userModelList) {
            if (item.getBlood_group().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter.filterList(filteredList);
    }

}