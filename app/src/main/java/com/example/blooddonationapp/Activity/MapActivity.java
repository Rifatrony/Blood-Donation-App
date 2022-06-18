package com.example.blooddonationapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.blooddonationapp.JSON.JsonParser;
import com.example.blooddonationapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient fusedLocationProviderClient;
    Location currentLocation;

    AppCompatButton A_Positive_Button, B_Positive_Button;

    GoogleMap map;
    SearchView searchView;

    Spinner locationTypeSpinner;
    AppCompatButton findButton;

    public static double currentLat = 0, currentLong = 0;
    private static final int REQUEST_CODE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        initialization();

        fetchLastLocation();

        String[] placeTypeList = {"atm", "bank", "hospital", "restaurant"};
        String[] placeNameList = {"ATM", "Bank", "Hospital", "Restaurant"};

        locationTypeSpinner.setAdapter(new ArrayAdapter<>(MapActivity.this,
                android.R.layout.simple_spinner_dropdown_item, placeNameList));


        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {

                String location = searchView.getQuery().toString();
                List<Address> addressList = null;

                if (location != null && !location.equals("")) {

                    try {
                        Geocoder geocoder = new Geocoder(MapActivity.this);
                        addressList = geocoder.getFromLocationName(location, 1);
                        Toast.makeText(MapActivity.this, addressList.get(0).getLocality()+" is Address Of search", Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Address address = addressList.get(0);
                    Toast.makeText(MapActivity.this, "Latitude is " + address.getLatitude(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(MapActivity.this, "Longitude is " + address.getLongitude(), Toast.LENGTH_SHORT).show();
                    LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    map.addMarker(new MarkerOptions().position(latLng).title(location));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
                }
                else {
                    Toast.makeText(MapActivity.this, "Not Found", Toast.LENGTH_SHORT).show();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        supportMapFragment.getMapAsync(this);

        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int i = locationTypeSpinner.getSelectedItemPosition();

                String url = "https://maps.googleapis.com/maps/api/nearbysearch/json" +
                        "?location=" + currentLat + "," + currentLong +
                        "&radius=5000" +
                        "&type=" + placeTypeList[i]+
                        "&sensor=true" +
                        "&key=" +
                        getResources().getString(R.string.google_map_key);

                Toast.makeText(MapActivity.this, "Current lat" + currentLat, Toast.LENGTH_SHORT).show();

                /*new PlaceTask.execute(url);*/
                new PlaceTask().execute(url);

            }
        });

    }


    private void initialization() {
        /*A_Positive_Button = findViewById(R.id.A_Positive_Button);
        B_Positive_Button = findViewById(R.id.B_Positive_Button);*/

        locationTypeSpinner = findViewById(R.id.locationTypeSpinner);
        findButton = findViewById(R.id.findButton);

        searchView = findViewById(R.id.sv_location);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    private void fetchLastLocation() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;

        }
        Task<Location> task = fusedLocationProviderClient.getLastLocation();

        System.out.println("Task is -----------> " +task.toString());


        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null){

                    //currentLocation = location;

                    currentLat = location.getLatitude();
                    currentLong = location.getLongitude();

                    Toast.makeText(MapActivity.this, currentLat + " is Current LAT \n" + currentLong+" is current long", Toast.LENGTH_SHORT).show();

                    System.out.println("current lat : "+ currentLat + "current long : "+ currentLong);

                    supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.google_map);
                    supportMapFragment.getMapAsync(MapActivity.this);

                }

                else {
                    System.out.println("Location is null");

                   /* currentLat = location.getLatitude();
                    currentLong = location.getLongitude();

                    Toast.makeText(MapActivity.this, currentLat + "" + currentLong, Toast.LENGTH_SHORT).show();

                    System.out.println("current lat : "+ currentLat + "current long : "+ currentLong);

                    supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.google_map);
                    supportMapFragment.getMapAsync(MapActivity.this);*/
                }
            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        map = googleMap;

        LatLng latLng = new LatLng(currentLat, currentLong);

        MarkerOptions options = new MarkerOptions().position(latLng).title("My Location");
        map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 18));
        map.addMarker(options);

        //map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLat, currentLong), 18));
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_CODE:
                if (grantResults.length> 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    fetchLastLocation();
                }
                break;
        }
    }


    private class PlaceTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {

            String data = null;
            try {
                data = downloadUrl(strings[0]);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {

            new ParserTask().execute(s);

        }

    }

    private String downloadUrl(String string) throws IOException {
        URL url = new URL(string);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.connect();

        InputStream stream = connection.getInputStream();

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        StringBuilder builder = new StringBuilder();
        String line = "";

        while ((line = reader.readLine()) != null){
            builder.append(line);
        }

        String data = builder.toString();
        reader.close();


        return data;

    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {

            JsonParser jsonParser = new JsonParser();
            List<HashMap<String, String>> mapList = null;

            JSONObject object = null;
            try {
                object = new JSONObject(strings[0]);
                mapList = jsonParser.parseResult(object);

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            map.clear();

            for (int i=0; i<hashMaps.size(); i++){
                HashMap<String, String> hashMap = hashMaps.get(i);

                double lat = Double.parseDouble(hashMap.get("lat"));
                double lng = Double.parseDouble(hashMap.get("lng"));
                String name = hashMap.get("name");

                LatLng latLng = new LatLng(lat, lng);

                MarkerOptions options = new MarkerOptions();
                options.position(latLng);
                options.title(name);
                map.addMarker(options);
            }

        }
    }

}