package com.rony.blooddonationapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.rony.blooddonationapp.Model.ConfirmBloodModel;
import com.rony.blooddonationapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DashBoardActivity extends AppCompatActivity implements View.OnClickListener {

    AppCompatImageView imageBack;

    CardView totalMemberCardView, totalCoordinatorCardView, totalOrganizationCardView;

    DatabaseReference dbTotalUser, dbTodayTotalDonate, dbCoordinator, dbOrganization;

    TextView totalUserTextView, totalTodayDonateTextView, totalCoordinatorTextView, totalOrganizationTextView;

    long count, total;
    int totalOrganization, totalCoordinator;

    String donateDate, todayDate;
    Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        initialization();

        setListener();

        getTotalUser();
        getTotalCoordinator();
        getTotalOrganization();

        c = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        todayDate = sdf.format(c.getTime());


    }

    private void initialization() {

        dbTotalUser = FirebaseDatabase.getInstance().getReference().child("User");
        dbTodayTotalDonate = FirebaseDatabase.getInstance().getReference().child("Confirm Blood");
        dbCoordinator = FirebaseDatabase.getInstance().getReference().child("Coordinator");
        dbOrganization = FirebaseDatabase.getInstance().getReference().child("Organization");

        imageBack = findViewById(R.id.imageBack);

        totalUserTextView = findViewById(R.id.totalUserTextView);
        totalCoordinatorTextView = findViewById(R.id.totalCoordinatorTextView);
        totalOrganizationTextView = findViewById(R.id.totalOrganizationTextView);

        totalMemberCardView = findViewById(R.id.totalMemberCardView);
        totalCoordinatorCardView = findViewById(R.id.totalCoordinatorCardView);
        totalOrganizationCardView = findViewById(R.id.totalOrganizationCardView);

    }

    private void setListener(){

        imageBack.setOnClickListener(this);
        totalMemberCardView.setOnClickListener(this);
        totalCoordinatorCardView.setOnClickListener(this);
        totalOrganizationCardView.setOnClickListener(this);

    }

    private void getTotalUser() {
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
    }


    private void getTotalCoordinator() {
        dbCoordinator.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    totalCoordinator = (int) snapshot.getChildrenCount();
                    totalCoordinatorTextView.setText(String.valueOf(totalCoordinator));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void getTotalOrganization() {
        dbOrganization.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    totalOrganization = (int) snapshot.getChildrenCount();
                    totalOrganizationTextView.setText(String.valueOf(totalOrganization));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSwipeRight(DashBoardActivity.this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageBack:
                onBackPressed();
                return;

            case R.id.totalMemberCardView:
                startActivity(new Intent(DashBoardActivity.this, AllMemberActivity.class));
                Animatoo.animateSwipeLeft(DashBoardActivity.this);
                return;


            case R.id.totalCoordinatorCardView:
                startActivity(new Intent(DashBoardActivity.this, CoordinatorActivity.class));
                Animatoo.animateSwipeLeft(DashBoardActivity.this);
                return;

            case R.id.totalOrganizationCardView:
                startActivity(new Intent(DashBoardActivity.this, OrganizationActivity.class));
                Animatoo.animateSwipeLeft(DashBoardActivity.this);
                return;
        }
    }
}