package com.example.blooddonationapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.blooddonationapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hbb20.CountryCodePicker;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AddNewDonorActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressBar progressBar;

    AppCompatImageView imageBack;
    AppCompatButton addNewDonorButton;

    Spinner bloodGroupSpinner;
    CountryCodePicker countryCodePicker;

    EditText nameEditText, emailEditText, passwordEditText, confirmPasswordEditText,
            phoneNumberEditText, dobEditText,addressEditText;

    String ccp, name, email, number, password, confirm_password, dob, address, blood_group, token;
    String address1;

    FirebaseAuth mAuth;

    ArrayAdapter<String> adapter;
    String[] bloodGroups = {"A+", "B+", "AB+", "O+", "A-", "B-", "AB-", "O-"};

    DatabaseReference dbUserInfo;

    FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_donor);

        initialization();
        setListener();
        adapter = new ArrayAdapter<String>(AddNewDonorActivity.this, android.R.layout.simple_spinner_dropdown_item, bloodGroups);
        bloodGroupSpinner.setAdapter(adapter);

        bloodGroupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                System.out.println("Select Item Name is : " + adapterView.getSelectedItem());

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(AddNewDonorActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(AddNewDonorActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(AddNewDonorActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }else{
                ActivityCompat.requestPermissions(AddNewDonorActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }
        }

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                Location location = task.getResult();

                if (location!= null){

                    double currentLat = location.getLatitude();
                    double currentLong = location.getLongitude();

                    try {
                        Geocoder geocoder = new Geocoder(AddNewDonorActivity.this, Locale.getDefault());
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        /*Longitude = addresses.get(0).getLongitude();
                        longitudeTextView.setText(String.valueOf(addresses.get(0).getLongitude()));
                        latitudeTextView.setText(String.valueOf(addresses.get(0).getLatitude()));*/

                        address1 = addresses.get(0).getSubLocality().toString();
                        addressEditText.setText(address1);

                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            System.out.println("Fetching FCM registration token failed");
                            return;
                        }
                        // Get new FCM registration token
                        token = task.getResult();

                        Toast.makeText(AddNewDonorActivity.this, token, Toast.LENGTH_SHORT).show();
                        System.out.println("Token is : " + token);
                    }
                });
    }

    private void initialization() {

        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        imageBack = findViewById(R.id.imageBack);

        bloodGroupSpinner = findViewById(R.id.bloodGroupSpinner);

        countryCodePicker = findViewById(R.id.countryCodePicker);
        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        dobEditText = findViewById(R.id.dobEditText);
        addressEditText = findViewById(R.id.addressEditText);

        addNewDonorButton = findViewById(R.id.addNewDonorButton);

    }

    private void setListener (){
        imageBack.setOnClickListener(this);
        addNewDonorButton.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageBack:
                onBackPressed();
                break;
            case R.id.addNewDonorButton:
                addNewDonor();
                break;

        }
    }

    private void addNewDonor() {

        progressBar.setVisibility(View.VISIBLE);
        addNewDonorButton.setVisibility(View.GONE);

        name = nameEditText.getText().toString().trim();
        email = emailEditText.getText().toString().trim();
        number = phoneNumberEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();
        confirm_password = confirmPasswordEditText.getText().toString().trim();
        ccp = countryCodePicker.getSelectedCountryName().toString().trim();
        dob = dobEditText.getText().toString().trim();
        address = addressEditText.getText().toString().trim();
        blood_group = bloodGroupSpinner.getSelectedItem().toString().trim();

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    progressBar.setVisibility(View.GONE);
                    addNewDonorButton.setVisibility(View.VISIBLE);

                    Toast.makeText(AddNewDonorActivity.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();

                    String currentUserId = mAuth.getCurrentUser().getUid();
                    dbUserInfo = FirebaseDatabase.getInstance().getReference().child("User").child(currentUserId);

                    HashMap userInfo = new HashMap();
                    userInfo.put("id", currentUserId);
                    userInfo.put("country", ccp);
                    userInfo.put("name", name);
                    userInfo.put("email", email);
                    userInfo.put("number", number);
                    userInfo.put("password", password);
                    userInfo.put("confirm_password", confirm_password);
                    userInfo.put("blood_group", blood_group);
                    userInfo.put("dob", dob);
                    userInfo.put("address", address);
                    //userInfo.put("type", "donor");
                    //userInfo.put("search", "donor" + blood_group);
                    userInfo.put("token", token);

                    dbUserInfo.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            if (task.isSuccessful()){
                                Toast.makeText(AddNewDonorActivity.this, "New User Added", Toast.LENGTH_SHORT).show();

                                nameEditText.setText("");
                                emailEditText.setText("");
                                phoneNumberEditText.setText("");
                                passwordEditText.setText("");
                                confirmPasswordEditText.setText("");
                                dobEditText.setText("");
                                addressEditText.setText("");

                            }

                            else {
                                progressBar.setVisibility(View.GONE);
                                addNewDonorButton.setVisibility(View.VISIBLE);
                                Toast.makeText(AddNewDonorActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }

                else {

                    progressBar.setVisibility(View.GONE);
                    addNewDonorButton.setVisibility(View.VISIBLE);

                    Toast.makeText(AddNewDonorActivity.this, task.getException().getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void checkButton(View view) {

    }
}