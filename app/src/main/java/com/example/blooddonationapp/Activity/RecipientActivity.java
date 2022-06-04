package com.example.blooddonationapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blooddonationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

import java.util.HashMap;

public class RecipientActivity extends AppCompatActivity implements View.OnClickListener {

    ProgressBar progressBar;
    TextView haveAccountTextView;
    EditText nameEditText, emailEditText, phoneNumberEditText, passwordEditText,
            confirmPasswordEditText, dobEditText, addressEditText;

    Spinner bloodGroupSpinner;

    RadioGroup radioGroup;
    RadioButton radioButton;
    String clickedItemIs;

    AppCompatButton registerButton;

    CountryCodePicker countryCodePicker;

    String ccp, name, email, number, password, confirm_password, dob, address, blood_group;
    String uid;

    ArrayAdapter<String> adapter;
    String[] bloodGroups = {"A+", "B+", "AB+", "O+", "A-", "B-", "AB-", "O-"};


    FirebaseAuth mAuth;
    DatabaseReference dbUserInfo;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipient);

        initialization();
        setListener();

        adapter = new ArrayAdapter<String>(RecipientActivity.this, android.R.layout.simple_spinner_dropdown_item, bloodGroups);
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
    }

    private void initialization(){

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

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.haveAccountTextView:
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.registerButton:
                checkValidation();
        }
    }

    private void checkValidation() {
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
            /*Intent intent = new Intent(RegisterActivity.this, EditProfileActivity.class);
            startActivity(intent);*/
            registerNewUser();
        }
    }

    private void registerNewUser(){

        progressBar.setVisibility(View.VISIBLE);
        registerButton.setVisibility(View.GONE);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
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
                userInfo.put("type", "recipient");
                userInfo.put("search", "recipient" + blood_group);

                dbUserInfo.updateChildren(userInfo).addOnCompleteListener(task1 -> {

                    if (task1.isSuccessful()) {
                        showToast("Registration Complete");
                        progressBar.setVisibility(View.GONE);
                        registerButton.setVisibility(View.VISIBLE);
                    }

                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        showToast(e.getMessage());
                    }
                });

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