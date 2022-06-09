package com.example.blooddonationapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import com.example.blooddonationapp.MainActivity;
import com.example.blooddonationapp.Model.User;
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
    SearchView searchView;
    GoogleMap map;
    SupportMapFragment supportMapFragment;

    DatabaseReference dbDonorList;

    UserAdapter adapter;

    double currentLat;
    double currentLong;

    double brng = 360;
    double initialLat = 23.8641225;
    double initialLng = 90.3990683;
    double dist = 5.0/6371.0;

    DatabaseReference userRef;
    List<User> userList = new ArrayList<>();

    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donor_list);

        initialization();

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

                        //Longitude = addresses.get(0).getLongitude();
                        currentLat = addresses.get(0).getLatitude();
                        currentLong = addresses.get(0).getLongitude();
                        //Toast.makeText(DonorListActivity.this, "Current Lat == > " + currentLat +"\nCurrent Long == > " + currentLong, Toast.LENGTH_SHORT).show();
                        System.out.println("Current Lat == > " + currentLat +"\nCurrent Long == > " + currentLong);

                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        double currentLong =90.3988889, currentLat =  23.8698458;
        double Ra = 6371;  // earth radius in km
        double radius = 5; // km
        double x1 = currentLong - Math.toDegrees(radius/Ra/Math.cos(Math.toRadians(currentLat)));
        double x2 = currentLong + Math.toDegrees(radius/Ra/Math.cos(Math.toRadians(currentLat)));
        double y1 = currentLat + Math.toDegrees(radius/Ra);
        double y2 = currentLat - Math.toDegrees(radius/Ra);


        System.out.println("X1 is " + x1 +"\nX2 is " + x2+"\nY1 is " + y1+"\nY2 is " + y2);

        /*Toast.makeText(this, "X1 is " + x1, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "X2 is " + x2, Toast.LENGTH_SHORT).show();

        Toast.makeText(this, "Y1 is " + y1, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Y2 is " + y2, Toast.LENGTH_SHORT).show();*/


        donorListRecyclerView = findViewById(R.id.donorListRecyclerView);
        donorListRecyclerView.setHasFixedSize(true);
        donorListRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new UserAdapter(DonorListActivity.this, userList);
        donorListRecyclerView.setAdapter(adapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User");
        Query query = reference.orderByChild("type").equalTo("donor");

        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    User user = dataSnapshot.getValue(User.class);

                    String latitude1 = user.getLatitude();
                    double value = Double.parseDouble(latitude1);

                    if (value < y1 && value >= y2) {
                        System.out.println("Latitudes Are : " + user.getName());
                        userList.add(user);
                    }
                }
                adapter.notifyDataSetChanged();

                if (userList.isEmpty()) {

                    Toast.makeText(DonorListActivity.this, "No Donor Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        
        /*double lat2 = Math.asin( Math.sin(initialLat)*Math.cos(dist) + Math.cos(initialLat)*Math.sin(dist)*Math.cos(brng) );
        double a = Math.atan2(Math.sin(brng)*Math.sin(dist)*Math.cos(initialLat), Math.cos(dist)-Math.sin(initialLat)*Math.sin(lat2));
        System.out.println("a = " +  a);
        Toast.makeText(this, "a = "+ a, Toast.LENGTH_SHORT).show();
        double lon2 = initialLng + a;

        lon2 = (lon2+ 3*Math.PI) % (2*Math.PI) - Math.PI;

        System.out.println("Latitude = "+Math.toDegrees(lat2)+"\nLongitude = "+lon2);

        double finalLat = Math.toDegrees(lat2);
        double finalLng = Math.toDegrees(lon2);



        Toast.makeText(this, "Latitude = "+finalLat, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "Longitude = "+finalLng, Toast.LENGTH_SHORT).show();*/


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
        searchBloodGroupEditText = findViewById(R.id.searchBloodGroupEditText);
        searchView = findViewById(R.id.sv_location);
    }

    private void filter(String text) {
        ArrayList<User> filteredList = new ArrayList<>();

        for (User item : userList) {
            if (item.getBlood_group().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        adapter.filterList(filteredList);
    }

}