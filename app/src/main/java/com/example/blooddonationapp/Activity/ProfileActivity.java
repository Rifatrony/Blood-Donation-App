package com.example.blooddonationapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blooddonationapp.Model.UserRegisterModel;
import com.example.blooddonationapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ProfileActivity extends AppCompatActivity {

    TextView userNameTextView, userNumberTextView, userBloodGroupTextView,
            userLastDonateTextView, userNextDonateTextView, userAddressTextView;

    String name, number, blood_group, last_donate, next_donate, address;

    DatabaseReference userRef;
    FirebaseUser user;

    Toolbar toolbar;

    Date date;
    String afterFourMonthsDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initialization();

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        user = FirebaseAuth.getInstance().getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference().child("User")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){

                    try {
                        name = snapshot.child("name").getValue().toString();
                        userNameTextView.setText(name);

                        number = snapshot.child("number").getValue().toString();
                        userNumberTextView.setText(number);

                        last_donate = snapshot.child("last_donate").getValue().toString();
                        userLastDonateTextView.setText(last_donate);


                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                        try {
                            date = dateFormat.parse(last_donate);
                        }
                        catch (ParseException e) {
                            e.printStackTrace();
                        }

                        Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        calendar.add(Calendar.MONTH, +4);
                        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
                        afterFourMonthsDate = date.format(calendar.getTime());
                        System.out.println("After 4 months date is---------->"+afterFourMonthsDate);

                        blood_group = snapshot.child("blood_group").getValue().toString();
                        userBloodGroupTextView.setText(blood_group);

                        address = snapshot.child("address").getValue().toString();
                        userAddressTextView.setText(address);

                        userNextDonateTextView.setText(afterFourMonthsDate);


                    }
                    catch (Exception e) {
                        Toast.makeText(ProfileActivity.this, "here"+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }





                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void initialization() {
        userNameTextView = findViewById(R.id.userNameTextView);
        userNumberTextView = findViewById(R.id.userNumberTextView);
        userBloodGroupTextView = findViewById(R.id.userBloodGroupTextView);
        userLastDonateTextView = findViewById(R.id.userLastDonateTextView);
        userNextDonateTextView = findViewById(R.id.userNextDonateTextView);
        userAddressTextView = findViewById(R.id.userAddressTextView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_navigation, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.nav_edit_profile:
                Intent intent = new Intent(getApplicationContext(), UpdateProfileActivity.class);
                intent.putExtra("next_donate", afterFourMonthsDate);
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}