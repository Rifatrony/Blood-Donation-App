package com.example.blooddonationapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.blooddonationapp.Adapter.AcceptRequestAdapter;
import com.example.blooddonationapp.Adapter.RequestAdapter;
import com.example.blooddonationapp.Model.AcceptRequestModel;
import com.example.blooddonationapp.Model.RequestModel;
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

public class AcceptRequestActivity extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    AppCompatImageView imageBack;
    TextView noAcceptRequestTextView;
    ProgressBar progressBar;

    AcceptRequestAdapter adapter;
    List<AcceptRequestModel> acceptRequestModelList;

    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference userRef;

    String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accept_request);

        initialization();
        setListener();

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        user_id = user.getUid();

        System.out.println("\n\n\n\nUser is " + user_id);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        acceptRequestModelList = new ArrayList<>();
        adapter = new AcceptRequestAdapter(this, acceptRequestModelList);
        recyclerView.setAdapter(adapter);

        userRef = FirebaseDatabase.getInstance().getReference().child("Accept Request").child(user_id);

        userRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                acceptRequestModelList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    AcceptRequestModel acceptRequestModel = dataSnapshot.getValue(AcceptRequestModel.class);

                    if (acceptRequestModel.getMy_uid().equals(user.getUid())){
                        acceptRequestModelList.add(acceptRequestModel);
                    }

                }
                adapter.notifyDataSetChanged();

                if (acceptRequestModelList.isEmpty()){
                    noAcceptRequestTextView.setVisibility(View.VISIBLE);
                    noAcceptRequestTextView.setText("No one Accept Your Request");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void initialization() {
        recyclerView = findViewById(R.id.recyclerView);
        imageBack = findViewById(R.id.imageBack);
        noAcceptRequestTextView = findViewById(R.id.noAcceptRequestTextView);
        progressBar = findViewById(R.id.progressBar);
    }

    private void setListener() {
        imageBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()== R.id.imageBack){
            onBackPressed();
        }
    }
}