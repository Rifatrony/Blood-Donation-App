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
import android.widget.Spinner;
import android.widget.Toast;

import com.example.blooddonationapp.MainActivity;
import com.example.blooddonationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {


    Spinner bloodGroupSpinner;
    ArrayAdapter<String> adapter;
    String[] bloodGroups = {"A+", "B+", "AB+", "O+", "A-", "B-", "AB-", "O-"};

    AppCompatButton updateButton;
    EditText nameEditText, stateEditText, dobEditText, districtEditText;

    ProgressBar progressBar;

    FirebaseUser user;
    DatabaseReference dbUpdateUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initialization();
        setListener();

    }

    private void initialization() {

        nameEditText = findViewById(R.id.nameEditText);
        stateEditText = findViewById(R.id.stateEditText);
        dobEditText = findViewById(R.id.dobEditText);
        districtEditText = findViewById(R.id.districtEditText);

        updateButton = findViewById(R.id.updateButton);

        progressBar = findViewById(R.id.progressBar);

        user = FirebaseAuth.getInstance().getCurrentUser();
        dbUpdateUser = FirebaseDatabase.getInstance().getReference().child("Donor List").child("Donor Details");


        bloodGroupSpinner = findViewById(R.id.bloodGroupSpinner);
        adapter = new ArrayAdapter<String>(EditProfileActivity.this, android.R.layout.simple_spinner_dropdown_item, bloodGroups);
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

    private void setListener() {
        updateButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.updateButton){
            updateProfile();
        }
    }

    private void updateProfile(){
        Map<String, Object> updatemap = new HashMap<>();
        updatemap.put("name", nameEditText.getText().toString().trim());
        /*updatemap.put("blood_group", uId);
        updatemap.put("gender", uId);*/
        updatemap.put("dob",dobEditText.getText().toString().trim());
        updatemap.put("state", stateEditText.getText().toString().trim());
        updatemap.put("district", districtEditText.getText().toString().trim());

        dbUpdateUser.updateChildren(updatemap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                Toast.makeText(EditProfileActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
            }
        });
    }
}