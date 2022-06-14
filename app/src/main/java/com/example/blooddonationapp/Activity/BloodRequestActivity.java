package com.example.blooddonationapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.blooddonationapp.Adapter.RequestAdapter;
import com.example.blooddonationapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class BloodRequestActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RequestAdapter adapter;
    DatabaseReference dbRequest;
    String uid;
    String currentUserId;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_request);

        initialization();
        mAuth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        uid = mAuth.getUid();
        System.out.println("Uid is " + uid);
        System.out.println("Current Uid is " + user.getUid());

        dbRequest = FirebaseDatabase.getInstance().getReference().child("Request").child("fz4Pd53O7QfQqcke2tL9LAOFduE2").child("M3O9MS5DFAO9TiGDK48wDGIkh9j1");
        dbRequest.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    try {
                        String id = snapshot.child("M3O9MS5DFAO9TiGDK48wDGIkh9j1").getValue().toString();
                        System.out.println("Id Found =======>" + id);
                    }
                    catch (Exception e){
                        e.getMessage();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initialization() {
        recyclerView = findViewById(R.id.recyclerView);

    }
}