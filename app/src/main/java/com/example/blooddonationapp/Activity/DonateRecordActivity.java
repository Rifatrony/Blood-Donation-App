package com.example.blooddonationapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.example.blooddonationapp.Adapter.ConfirmBloodAdapter;
import com.example.blooddonationapp.Model.ConfirmBloodModel;
import com.example.blooddonationapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DonateRecordActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<ConfirmBloodModel> confirmBloodModelList;
    ConfirmBloodAdapter adapter;

    AppCompatImageView imageBack;

    DatabaseReference dbConfirmBlood;

    FirebaseAuth auth;
    FirebaseUser user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_record);

        initialization();

        imageBack.setOnClickListener(view -> onBackPressed());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        confirmBloodModelList = new ArrayList<>();
        adapter = new ConfirmBloodAdapter(this, confirmBloodModelList);
        recyclerView.setAdapter(adapter);

        //user = auth.getCurrentUser();

        dbConfirmBlood = FirebaseDatabase.getInstance().getReference().child("Confirm Blood");

        dbConfirmBlood.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    ConfirmBloodModel confirmBloodModel = dataSnapshot.getValue(ConfirmBloodModel.class);
                    assert confirmBloodModel != null;
                    if (confirmBloodModel.getAccepted_id().equals(FirebaseAuth.getInstance().getUid())){

                        confirmBloodModelList.add(confirmBloodModel);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initialization() {
        recyclerView = findViewById(R.id.recyclerView);
        imageBack = findViewById(R.id.imageBack);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSwipeRight(DonateRecordActivity.this);
    }
}