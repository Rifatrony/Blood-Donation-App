package com.binaryit.blooddonationapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.binaryit.blooddonationapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import java.io.IOException;
import java.util.List;

public class SearchLocationActivity extends AppCompatActivity implements OnMapReadyCallback {

    SearchView searchLocation;

    String searchText;

    AppCompatImageView imageBack;

    GoogleMap map;
    FusedLocationProviderClient fusedLocationProviderClient;

    double searchLat, searchLng;
    String blood_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_location);

        searchLocation = findViewById(R.id.searchLocation);
        imageBack = findViewById(R.id.imageBack);

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        blood_group = getIntent().getStringExtra("group");
        System.out.println("Group Is " + blood_group);

        searchLocation.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchLocation.getQuery().toString();
                List<Address> addressList = null;

                try {
                    if (location != null && !location.equals("")) {

                        try {
                            Geocoder geocoder = new Geocoder(SearchLocationActivity.this);
                            addressList = geocoder.getFromLocationName(location, 1);
                            searchText = searchLocation.getQuery().toString();
                            System.out.println("Search Text is " + searchText);
                            //Toast.makeText(SearchLocationActivity.this, addressList.get(0).getLocality()+" is Address Of search", Toast.LENGTH_SHORT).show();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Address address = addressList.get(0);
                        searchLat = address.getLatitude();
                        searchLng = address.getLongitude();

                        //Toast.makeText(SearchLocationActivity.this, "Search Latitude is " + searchLat, Toast.LENGTH_SHORT).show();
                        //Toast.makeText(SearchLocationActivity.this, "Search Longitude is " + searchLng, Toast.LENGTH_SHORT).show();
                    /*LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    map.addMarker(new MarkerOptions().position(latLng).title(location));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));*/

                        Intent intent = new Intent(getApplicationContext(), RequestActivity.class);
                        intent.putExtra("lat", String.valueOf(searchLat));
                        intent.putExtra("lng", String.valueOf(searchLng));
                        intent.putExtra("group", String.valueOf(blood_group));
                        intent.putExtra("address", String.valueOf(searchText));
                        startActivity(intent);
                        Animatoo.animateSwipeLeft(SearchLocationActivity.this);

                        finish();

                    }
                    else {
                        Toast.makeText(SearchLocationActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    Toast.makeText(SearchLocationActivity.this, "No Address Found", Toast.LENGTH_SHORT).show();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });


    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSwipeRight(SearchLocationActivity.this);
    }
}