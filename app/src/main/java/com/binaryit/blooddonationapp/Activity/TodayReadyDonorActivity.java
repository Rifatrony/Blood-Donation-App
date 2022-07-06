package com.binaryit.blooddonationapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.binaryit.blooddonationapp.Adapter.TodayReadyAdapter;
import com.binaryit.blooddonationapp.Model.TodayReadyModel;
import com.binaryit.blooddonationapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TodayReadyDonorActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    AppCompatImageView imageBack;
    List<TodayReadyModel> todayReadyModelList;
    TodayReadyAdapter adapter;

    DatabaseReference dbTodayDonor;

    String today_date;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_today_ready_donor);

        initialization();

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();

            }
        });

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        today_date = sdf.format(c.getTime());

        System.out.println(today_date);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        todayReadyModelList = new ArrayList<>();
        adapter = new TodayReadyAdapter(this, todayReadyModelList);
        recyclerView.setAdapter(adapter);

        dbTodayDonor.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    TodayReadyModel todayReadyModel = dataSnapshot.getValue(TodayReadyModel.class);
                    if (!todayReadyModel.getId().equals(FirebaseAuth.getInstance().getUid()) && today_date.equals(todayReadyModel.getDate())){

                        todayReadyModelList.add(todayReadyModel);
                    }
                    if (!todayReadyModel.getDate().equals(today_date)){

                        databaseReference.child(todayReadyModel.getUid()).removeValue();
                        Toast.makeText(TodayReadyDonorActivity.this, todayReadyModel.getDate(), Toast.LENGTH_SHORT).show();
                    }



                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("Today Ready");



    }

    private void initialization() {

        dbTodayDonor = FirebaseDatabase.getInstance().getReference().child("Today Ready");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Today Ready");

        recyclerView = findViewById(R.id.recyclerView);
        imageBack = findViewById(R.id.imageBack);

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();

        Animatoo.animateSwipeRight(TodayReadyDonorActivity.this);

    }
}