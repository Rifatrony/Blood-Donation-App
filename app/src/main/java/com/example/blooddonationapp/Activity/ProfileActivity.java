package com.example.blooddonationapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.ButtonBarLayout;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.blooddonationapp.Model.User;
import com.example.blooddonationapp.Model.UserRegisterModel;
import com.example.blooddonationapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    TextView userNameTextView, userNumberTextView, userBloodGroupTextView,
            userLastDonateTextView, userNextDonateTextView, userAddressTextView;

    AppCompatButton beReadyDonorButton;

    String name, number, blood_group, last_donate, next_donate, address;

    DatabaseReference userRef, dbReadyDonor, dbUser, reference;
    FirebaseUser user;

    Toolbar toolbar;

    Date date;
    //String afterFourMonthsDate;

    String dialog_address, dialog_time;

    EditText addressEditText, timeEditText;
    Button btn_cancel, btn_confirm;
    String uid, today_date;

    String today_Date_string, next_Donate_Date_string;

    Date Today, Next_Donate;

    SimpleDateFormat sdf;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initialization();
        setListener();

        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        Calendar c = Calendar.getInstance();
        sdf = new SimpleDateFormat("dd/MM/yyyy");
        today_Date_string = sdf.format(c.getTime());
        System.out.println("String " + today_Date_string);


        toolbar.setNavigationOnClickListener(view -> onBackPressed());

        user = FirebaseAuth.getInstance().getCurrentUser();
        userRef = FirebaseDatabase.getInstance().getReference().child("User")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

        reference = FirebaseDatabase.getInstance().getReference().child("User");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    if (user.getId().equals(FirebaseAuth.getInstance().getUid())){

                        if (!user.getNext_donate().isEmpty()){
                            next_Donate_Date_string = user.getNext_donate();

                            try {
                                Today = sdf.parse(today_Date_string);
                                Next_Donate = sdf.parse(next_Donate_Date_string);

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                            if (!Today.after(Next_Donate)){
                                beReadyDonorButton.setVisibility(View.INVISIBLE);
                            }

                            else {
                                beReadyDonorButton.setVisibility(View.VISIBLE);
                            }
                        }
                        else {
                            beReadyDonorButton.setVisibility(View.VISIBLE);
                        }


                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        dbUser = FirebaseDatabase.getInstance().getReference().child("User");

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
                        if (last_donate.isEmpty()){
                            userLastDonateTextView.setText("You never Donate");
                        }
                        else {
                            userLastDonateTextView.setText(last_donate);
                        }


                        @SuppressLint("SimpleDateFormat")
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

                        try {
                            date = dateFormat.parse(last_donate);
                        }
                        catch (ParseException e) {
                            e.printStackTrace();
                        }

                        /*Calendar calendar = Calendar.getInstance();
                        calendar.setTime(date);
                        calendar.add(Calendar.MONTH, +4);
                        @SuppressLint("SimpleDateFormat") SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
                        afterFourMonthsDate = date.format(calendar.getTime());
                        System.out.println("After 4 months date is---------->"+afterFourMonthsDate);*/

                        blood_group = snapshot.child("blood_group").getValue().toString();
                        userBloodGroupTextView.setText(blood_group);

                        next_donate = snapshot.child("next_donate").getValue().toString();
                        userNextDonateTextView.setText(next_donate);

                        address = snapshot.child("address").getValue().toString();
                        userAddressTextView.setText(address);

                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initialization() {

        beReadyDonorButton = findViewById(R.id.beReadyDonorButton);

        userNameTextView = findViewById(R.id.userNameTextView);
        userNumberTextView = findViewById(R.id.userNumberTextView);
        userBloodGroupTextView = findViewById(R.id.userBloodGroupTextView);
        userLastDonateTextView = findViewById(R.id.userLastDonateTextView);
        userNextDonateTextView = findViewById(R.id.userNextDonateTextView);
        userAddressTextView = findViewById(R.id.userAddressTextView);
    }

    private void setListener(){
        beReadyDonorButton.setOnClickListener(this);
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
                //intent.putExtra("next_donate", afterFourMonthsDate);
                startActivity(intent);
                Animatoo.animateSwipeLeft(ProfileActivity.this);
                break;

            /*case R.id.beReadyDonorButton:
                ReadyDonor();*/
        }

        return super.onOptionsItemSelected(item);
    }

    private void ReadyDonor() {

        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.profile_layout);
        dialog.setCancelable(false);

        addressEditText = dialog.findViewById(R.id.addressEditText);
        timeEditText = dialog.findViewById(R.id.timeEditText);

        btn_confirm = dialog.findViewById(R.id.btn_confirm);
        btn_cancel = dialog.findViewById(R.id.btn_cancel);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dbReadyDonor = FirebaseDatabase.getInstance().getReference().child("Today Ready");
                uid = dbReadyDonor.push().getKey();

                dialog_address = addressEditText.getText().toString().trim();
                dialog_time = timeEditText.getText().toString().trim();

                if (dialog_address.isEmpty()){
                    Toast.makeText(ProfileActivity.this, "Enter Your Available Address", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dialog_time.isEmpty()){
                    Toast.makeText(ProfileActivity.this, "Enter Your Available Time", Toast.LENGTH_SHORT).show();
                    return;
                }
                else {
                    System.out.println("Address "+dialog_address);
                    System.out.println(dialog_time);

                    dbUser.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                User user = dataSnapshot.getValue(User.class);

                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                                today_date = sdf.format(c.getTime());

                                if (user.getId().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){

                                    HashMap hashMap = new HashMap();
                                    hashMap.put("name", user.getName());
                                    hashMap.put("number", user.getNumber());
                                    hashMap.put("address", user.getAddress());
                                    hashMap.put("location", dialog_address);
                                    hashMap.put("time", dialog_time);
                                    hashMap.put("blood_group", user.getBlood_group());
                                    hashMap.put("id", user.getId());
                                    hashMap.put("uid", uid);
                                    hashMap.put("date", today_date);

                                    dbReadyDonor.child(uid).updateChildren(hashMap).addOnCompleteListener(new OnCompleteListener() {
                                        @Override
                                        public void onComplete(@NonNull Task task) {
                                            if (task.isSuccessful()){
                                                dialog.dismiss();
                                                addressEditText.setText("");
                                                timeEditText.setText("");
                                                Toast.makeText(ProfileActivity.this, "You can donate blood Today", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });

        dialog.show();


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSwipeRight(ProfileActivity.this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.beReadyDonorButton:
                ReadyDonor();
                break;

        }
    }
}