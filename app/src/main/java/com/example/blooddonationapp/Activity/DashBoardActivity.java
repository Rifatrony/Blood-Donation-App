package com.example.blooddonationapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.blooddonationapp.Model.ConfirmBloodModel;
import com.example.blooddonationapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DashBoardActivity extends AppCompatActivity {

    DatabaseReference dbTotalUser, dbTodayTotalDonate;

    TextView totalUserTextView, totalTodayDonateTextView;

    long count, total;

    String donateDate, todayDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        initialization();

        Calendar c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        todayDate = sdf.format(c.getTime());
        Toast.makeText(getApplicationContext(), "Today Date is " + todayDate, Toast.LENGTH_SHORT).show();

        dbTotalUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (snapshot.exists()){
                    count = snapshot.getChildrenCount();
                    totalUserTextView.setText(String.valueOf(count));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        dbTodayTotalDonate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ConfirmBloodModel confirmBloodModel = dataSnapshot.getValue(ConfirmBloodModel.class);

                    donateDate = confirmBloodModel.getDonate_date();

                    if (donateDate.equals(todayDate)){

                        //int num = confirmBloodModel.getAccepted_id()
                        total = snapshot.getChildrenCount();
                        totalTodayDonateTextView.setText(String.valueOf(total));
                    }

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void initialization() {

        dbTotalUser = FirebaseDatabase.getInstance().getReference().child("User");
        dbTodayTotalDonate = FirebaseDatabase.getInstance().getReference().child("Confirm Blood");

        totalUserTextView = findViewById(R.id.totalUserTextView);
        totalTodayDonateTextView = findViewById(R.id.totalTodayDonateTextView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSwipeRight(DashBoardActivity.this);
    }

}