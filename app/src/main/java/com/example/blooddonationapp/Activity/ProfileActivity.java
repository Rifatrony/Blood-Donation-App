package com.example.blooddonationapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class ProfileActivity extends AppCompatActivity {

    TextView userNameTextView, userNumberTextView, userBloodGroupTextView,
            userLastDonateTextView, userNextDonateTextView, userAddressTextView;

    String name, number, blood_group, last_donate, next_donate, address;

    DatabaseReference userRef, dbReadyDonor, dbUser;
    FirebaseUser user;

    Toolbar toolbar;

    Date date;
    //String afterFourMonthsDate;

    String dialog_address, dialog_time;

    EditText addressEditText, timeEditText;
    Button btn_cancel, btn_confirm;
    String uid, today_date;


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

                        address = snapshot.child("address").getValue().toString();
                        userAddressTextView.setText(address);

                        //userNextDonateTextView.setText(afterFourMonthsDate);


                    }
                    catch (Exception e) {
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
                //intent.putExtra("next_donate", afterFourMonthsDate);
                startActivity(intent);
                break;

            case R.id.nav_ready_donor:
                ReadyDonor();
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
        });

        dialog.show();


    }
}