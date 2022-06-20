package com.example.blooddonationapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.blooddonationapp.Adapter.CoordinatorAdapter;
import com.example.blooddonationapp.Adapter.UserAdapter;
import com.example.blooddonationapp.Model.CoordinatorModel;
import com.example.blooddonationapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CoordinatorActivity extends AppCompatActivity implements View.OnClickListener {

    FloatingActionButton fabAddCoordinator;
    AppCompatImageView imageBack;

    RecyclerView recyclerView;
    CoordinatorAdapter adapter;
    List<CoordinatorModel> coordinatorModelList;

    DatabaseReference dbCoordinator;

    FirebaseUser user;
    String added_by;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coordinator);

        initialization();
        setListener();

        user = FirebaseAuth.getInstance().getCurrentUser();

        added_by = user.getUid();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        coordinatorModelList = new ArrayList<>();
        adapter = new CoordinatorAdapter(this, coordinatorModelList);
        recyclerView.setAdapter(adapter);

        dbCoordinator = FirebaseDatabase.getInstance().getReference().child("Coordinator");
        dbCoordinator.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                coordinatorModelList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    CoordinatorModel coordinatorModel = dataSnapshot.getValue(CoordinatorModel.class);
                    coordinatorModelList.add(coordinatorModel);

                }

                adapter.notifyDataSetChanged();
                if (coordinatorModelList.isEmpty()){
                    Toast.makeText(CoordinatorActivity.this, "No Data", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void initialization() {
        recyclerView = findViewById(R.id.recyclerView);
        fabAddCoordinator = findViewById(R.id.fabAddCoordinator);
        imageBack = findViewById(R.id.imageBack);
    }

    private void setListener() {
        imageBack.setOnClickListener(this);
        fabAddCoordinator.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageBack:
                onBackPressed();
                break;

            case R.id.fabAddCoordinator:
                startActivity(new Intent(getApplicationContext(), AddNewCoordinatorActivity.class));
                break;

        }
    }
}