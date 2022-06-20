package com.example.blooddonationapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.blooddonationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class UpdateProfileActivity extends AppCompatActivity implements View.OnClickListener {

    AppCompatImageView imageBack;
    EditText nameEditText, phoneNumberEditText, lastDonateEditText, addressEditText;

    AppCompatButton updateButton;
    ProgressBar progressBar;

    String name, number, last_donate, next_donate, address;

    String update_name, update_number, update_address, update_last_donate;

    DatabaseReference userRef;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        next_donate = getIntent().getStringExtra("next_donate");
        System.out.println(next_donate);

        initialization();
        setListener();


        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        lastDonateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                        lastDonateEditText.setText(date);
                    }
                },year,month,day);

                datePickerDialog.show();
            }
        });

        userRef = FirebaseDatabase.getInstance().getReference().child("User")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    try {
                        name = snapshot.child("name").getValue().toString();
                        nameEditText.setText(name);

                        number = snapshot.child("number").getValue().toString();
                        phoneNumberEditText.setText(number);

                        last_donate = snapshot.child("last_donate").getValue().toString();
                        lastDonateEditText.setText(last_donate);

                        address = snapshot.child("address").getValue().toString();
                        addressEditText.setText(address);
                    }catch (Exception e){
                        Toast.makeText(UpdateProfileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initialization() {

        mAuth = FirebaseAuth.getInstance();

        imageBack = findViewById(R.id.imageBack);
        nameEditText = findViewById(R.id.nameEditText);
        phoneNumberEditText = findViewById(R.id.phoneNumberEditText);
        lastDonateEditText = findViewById(R.id.lastDonateEditText);
        addressEditText = findViewById(R.id.addressEditText);

        updateButton = findViewById(R.id.updateButton);

        progressBar = findViewById(R.id.progressBar);

    }

    private void setListener() {
        imageBack.setOnClickListener(this);
        updateButton.setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageBack:
                onBackPressed();
                break;
            case R.id.updateButton:
                updateProfile();
                break;

        }
    }

    private void updateProfile() {
        update_name = nameEditText.getText().toString().trim();
        update_number = phoneNumberEditText.getText().toString().trim();
        update_last_donate = lastDonateEditText.getText().toString().trim();
        update_address = addressEditText.getText().toString().trim();

        String currentUserId = mAuth.getCurrentUser().getUid();
        DatabaseReference dbUserInfo = FirebaseDatabase.getInstance().getReference().child("User").child(currentUserId);

        HashMap updateMap = new HashMap();
        updateMap.put("name", update_name);
        updateMap.put("number", update_number);
        updateMap.put("last_donate", update_last_donate);
        updateMap.put("address", update_address);
        updateMap.put("next_donate", next_donate);
        //updateMap.put("name", update_name);


        dbUserInfo.updateChildren(updateMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    showToast("Updated Profile");
                    finish();
                }

                else {
                    showToast(task.getException().toString());
                }
            }
        });


    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}