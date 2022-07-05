package com.rony.blooddonationapp.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

import com.rony.blooddonationapp.Adapter.UserAdapter;
import com.rony.blooddonationapp.Model.User;
import com.rony.blooddonationapp.R;
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
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class NearestDonorFragment extends Fragment {

    View view;
    RecyclerView donorListRecyclerView;
    //EditText searchBloodGroupEditText;

    AppCompatButton sendRequestButton;
    AppCompatImageView imageBack;
    SearchView searchView;

    UserAdapter adapter;

    double currentLat;
    double currentLong;

    double x1, x2, y1, y2;
    ArrayList<User> userList = new ArrayList<>();
    FusedLocationProviderClient fusedLocationProviderClient;

    FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_nearest_donor, container, false);

        mAuth = FirebaseAuth.getInstance();
        //searchBloodGroupEditText = view.findViewById(R.id.searchBloodGroupEditText);
        searchView = view.findViewById(R.id.sv_location);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }else{
                ActivityCompat.requestPermissions(getActivity(),
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
                        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        double Radius = 6371;  // earth radius in km
                        double radius = 5; // km
                        x1 = currentLong - Math.toDegrees(radius/Radius/Math.cos(Math.toRadians(currentLat)));
                        x2 = currentLong + Math.toDegrees(radius/Radius/Math.cos(Math.toRadians(currentLat)));
                        y1 = currentLat + Math.toDegrees(radius/Radius);
                        y2 = currentLat - Math.toDegrees(radius/Radius);

                        System.out.println("X1 is " + x1 +"\nX2 is " + x2+"\nY1 is " + y1+"\nY2 is " + y2);
                        System.out.println("Current Lat == > " + currentLat +"\nCurrent Long == > " + currentLong);

                        donorListRecyclerView = view.findViewById(R.id.donorListRecyclerView);
                        donorListRecyclerView.setHasFixedSize(true);
                        donorListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

                        adapter = new UserAdapter(getContext(), userList);
                        donorListRecyclerView.setAdapter(adapter);

                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("User");
                        Query query = reference.orderByChild("name");
                        query.addValueEventListener(new ValueEventListener() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {

                                userList.clear();

                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                                    User user = dataSnapshot.getValue(User.class);

                                    if (!user.getId().equals(firebaseUser.getUid())){
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

                                    Toast.makeText(getContext(), "No Donor Found", Toast.LENGTH_SHORT).show();
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

        /*searchBloodGroupEditText.addTextChangedListener(new TextWatcher() {
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

        return view;
    }
}