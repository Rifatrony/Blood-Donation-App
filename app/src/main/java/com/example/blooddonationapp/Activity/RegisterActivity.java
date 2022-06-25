package com.example.blooddonationapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.blooddonationapp.MainActivity;
import com.example.blooddonationapp.Model.OrganizationModel;
import com.example.blooddonationapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.hbb20.CountryCodePicker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    String donor, search_blood;

    ProgressBar progressBar;
    TextView haveAccountTextView;
    EditText nameEditText, emailEditText, phoneNumberEditText, passwordEditText,
            confirmPasswordEditText, dobEditText, addressEditText;

    Spinner organizationSpinner;

    TextView longitudeTextView, latitudeTextView;

    double Longitude, latitude;
    String address1;

    Spinner bloodGroupSpinner;

    RadioGroup radioGroup;
    RadioButton radioButton;
    String clickedItemIs;

    AppCompatButton registerButton;

    CountryCodePicker countryCodePicker;

    String ccp, name, email, number, password, confirm_password, dob, address, blood_group, token, organization;
    String uid;

    ArrayAdapter<String> adapter;
    String[] bloodGroups = {"A+", "B+", "AB+", "O+", "A-", "B-", "AB-", "O-"};


    FirebaseAuth mAuth;
    DatabaseReference dbUserInfo;
    FirebaseUser user;

    FusedLocationProviderClient fusedLocationProviderClient;

    DatabaseReference dbOrganization;
    List<String> organizationList;
    ArrayAdapter<String> organizationAdapter;
    String selectedOrganizationUid = null;
    ArrayList<String> organizationUidList;
    ArrayList<String> organizationNameList;
    ArrayList<String> organizationTotalMember;

    String totalMember, organizationName;
    int intTotal = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initialization();
        setListener();

        organizationList = new ArrayList<>();
        organizationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, organizationList);
        organizationSpinner.setAdapter(organizationAdapter);

        organizationUidList = new ArrayList<>();
        organizationNameList = new ArrayList<>();
        organizationTotalMember = new ArrayList<>();

        dbOrganization.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                organizationList.clear();
                for (DataSnapshot medicineCategory : snapshot.getChildren()){

                    OrganizationModel data = medicineCategory.getValue(OrganizationModel.class);

                    if (data != null)
                    {
                        organizationList.add(data.getName());
                        organizationUidList.add(data.getUid());
                        organizationTotalMember.add(data.getTotal_member());
                        //Toast.makeText(RegisterActivity.this, "Count is " + data.getTotal_member(), Toast.LENGTH_SHORT).show();
                    }
                }

                setOrganization(organizationList, organizationUidList, organizationTotalMember);
                organizationAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        dobEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                        String date;
                        month = month +1;

                        if (month < 10){
                            date = day+"/0"+month+"/"+year;
                            /*THIS PART*/
                            if (day < 10) {
                                date = "0"+day+"/0"+month+"/"+year;
                            }
                        }
                        else {
                            date = day+"/"+month+"/"+year;

                            /*THIS PART*/

                            if (day < 10) {
                                date = "0"+day+"/0"+month+"/"+year;
                            }
                        }
                        dobEditText.setText(date);
                    }
                },year,month,day);

                datePickerDialog.show();
            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        if (ContextCompat.checkSelfPermission(RegisterActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            if (ActivityCompat.shouldShowRequestPermissionRationale(RegisterActivity.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)){
                ActivityCompat.requestPermissions(RegisterActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }else{
                ActivityCompat.requestPermissions(RegisterActivity.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
            }
        }

        fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onComplete(@NonNull Task<Location> task) {

                try {
                    Location location = task.getResult();

                    if (location!= null){

                        double currentLat = location.getLatitude();
                        double currentLong = location.getLongitude();

                        try {
                            Geocoder geocoder = new Geocoder(RegisterActivity.this, Locale.getDefault());
                            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                            Longitude = addresses.get(0).getLongitude();
                            latitude = addresses.get(0).getLatitude();

                            System.out.println("Longitude " + Longitude +"Latitude " + latitude);
                            /*longitudeTextView.setText(String.valueOf(addresses.get(0).getLongitude()));
                            latitudeTextView.setText(String.valueOf(addresses.get(0).getLatitude()));*/

                            address1 = addresses.get(0).getSubLocality().toString();
                            addressEditText.setText(address1);

                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }catch (Exception e){
                    Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

        adapter = new ArrayAdapter<String>(RegisterActivity.this, android.R.layout.simple_spinner_dropdown_item, bloodGroups);
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

                        System.out.println("Token is : " + token);
                    }
                });

    }

    private void setOrganization(List<String> organizationList, ArrayList<String> organizationUidList, ArrayList<String> organizationTotalMember) {
        organizationAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, organizationList);
        organizationSpinner.setAdapter(organizationAdapter);

        organizationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                totalMember = organizationTotalMember.get(i);
                organizationName = organizationList.get(i);

                selectedOrganizationUid = organizationUidList.get(i);

                Toast.makeText(RegisterActivity.this, "Total member is "  + totalMember + " of " + organizationName, Toast.LENGTH_SHORT).show();

                //selectedAccountUid = parAccountBalance;
                System.out.println("Selected Account Uid----->"+selectedOrganizationUid);
                Toast.makeText(RegisterActivity.this, "Clicked is " + selectedOrganizationUid, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void initialization(){

        dbOrganization = FirebaseDatabase.getInstance().getReference().child("Organization");

        organizationSpinner = findViewById(R.id.organizationSpinner);
        radioGroup = findViewById(R.id.radioGroup);

        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        countryCodePicker = findViewById(R.id.countryCodePicker);

        haveAccountTextView = findViewById(R.id.haveAccountTextView);

        nameEditText = findViewById(R.id.nameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        bloodGroupSpinner = findViewById(R.id.bloodGroupSpinner);
        dobEditText = findViewById(R.id.dobEditText);
        addressEditText = findViewById(R.id.addressEditText);

        registerButton = findViewById(R.id.registerButton);

        user = FirebaseAuth.getInstance().getCurrentUser();

        System.out.println("\n\n\n\n"+clickedItemIs);

    }

    private void setListener(){
        haveAccountTextView.setOnClickListener(this);
        registerButton.setOnClickListener(this);
    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void checkButton(View view){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = findViewById(radioId);
        clickedItemIs = radioButton.getText().toString();
        Toast.makeText(this, "Clicked is " + clickedItemIs, Toast.LENGTH_SHORT).show();
        System.out.println("Clicked is " + clickedItemIs);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.haveAccountTextView:
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                Animatoo.animateSwipeRight(RegisterActivity.this);
                break;
            case R.id.registerButton:
                checkValidation();
        }
    }

    private void checkValidation() {
        if (organizationSpinner.getSelectedItemPosition()<0){
            organization = "";

            totalMember = "";

        }
        else {
            organization = organizationSpinner.getSelectedItem().toString();
        }

        name = nameEditText.getText().toString().trim();
        email = emailEditText.getText().toString().trim();
        number = phoneNumberEditText.getText().toString().trim();
        password = passwordEditText.getText().toString().trim();
        confirm_password = confirmPasswordEditText.getText().toString().trim();
        ccp = countryCodePicker.getSelectedCountryName().toString().trim();
        dob = dobEditText.getText().toString().trim();
        address = addressEditText.getText().toString().trim();
        blood_group = bloodGroupSpinner.getSelectedItem().toString().trim();

        System.out.println("Selected country is ======>" + ccp);

        if (email.isEmpty()){
            showToast("Email Required");
            return;
        }

        if (number.isEmpty()){
            showToast("Number Required");
            return;
        }
        if (password.isEmpty()){
            showToast("Password Required");
            return;
        }
        if (confirm_password.isEmpty()){
            showToast("Confirm Password Required");
            return;
        }
        if (!confirm_password.equals(password)){
            showToast("Password and Confirm Password Should be Same");
            return;
        }
        else {
            registerNewUser();
        }
    }

    private void registerNewUser(){

        progressBar.setVisibility(View.VISIBLE);
        registerButton.setVisibility(View.GONE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    String currentUserId = mAuth.getCurrentUser().getUid();
                    dbUserInfo = FirebaseDatabase.getInstance().getReference().child("User").child(currentUserId);
                    int convert = 0;
                    try {
                        convert = Integer.parseInt(totalMember);
                    }
                    catch (Exception e){

                    }
                    //intTotal = Integer.parseInt(totalMember);
                    intTotal = convert+1;
                    Toast.makeText(RegisterActivity.this, "Count is "+ intTotal, Toast.LENGTH_SHORT).show();

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
                    userInfo.put("longitude", String.valueOf(Longitude));
                    userInfo.put("latitude", String.valueOf(latitude));
                    userInfo.put("address", address1);
                    userInfo.put("last_donate", "");
                    userInfo.put("next_donate", "");
                    userInfo.put("total_member", String.valueOf(intTotal));
                    userInfo.put("organization", organization);
                    userInfo.put("role", "user");
                    //userInfo.put("type", "donor");
                    //userInfo.put("search", "donor" + blood_group);
                    userInfo.put("token", token);

                    dbUserInfo.updateChildren(userInfo).addOnCompleteListener(task1 -> {

                        if (task1.isSuccessful()) {
                            showToast("Registration Complete");
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            finish();
                            progressBar.setVisibility(View.GONE);
                            registerButton.setVisibility(View.VISIBLE);

                            HashMap hashMap= new HashMap();
                            hashMap.put("total_member", String.valueOf(intTotal));

                            DatabaseReference dbUpdateOrganization = FirebaseDatabase.getInstance()
                                    .getReference().child("Organization").child(selectedOrganizationUid);

                            if (selectedOrganizationUid.isEmpty()){
                                Toast.makeText(RegisterActivity.this, "No Organization found", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            else {
                                dbUpdateOrganization.updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(RegisterActivity.this, "Member Updated", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }

                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            showToast(e.getMessage());
                        }
                    });
                }

                else {
                    Toast.makeText(RegisterActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                showToast(e.getMessage());
                progressBar.setVisibility(View.GONE);
                registerButton.setVisibility(View.VISIBLE);
            }
        });
    }

}