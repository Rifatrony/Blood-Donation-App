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
import android.widget.TextView;
import android.widget.Toast;

import com.example.blooddonationapp.Adapter.RequestAdapter;
import com.example.blooddonationapp.Adapter.ViewSentRequestAdapter;
import com.example.blooddonationapp.Model.RequestIdModel;
import com.example.blooddonationapp.Model.RequestModel;
import com.example.blooddonationapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewSentRequestActivity extends AppCompatActivity implements View.OnClickListener{

    TextView viewYourRequestTextView;

    AppCompatImageView imageBack;
    FirebaseAuth mAuth;
    RecyclerView recyclerView;
    List<RequestModel> requestModelList;
    ViewSentRequestAdapter adapter;

    FirebaseUser firebaseUser;
    DatabaseReference dbViewRequest, dbRequestId;

    String requestSentUid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_sent_request);

        initialization();
        setListener();

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        requestModelList = new ArrayList<>();
        adapter = new ViewSentRequestAdapter(this, requestModelList);
        recyclerView.setAdapter(adapter);

        dbRequestId = FirebaseDatabase.getInstance().getReference().child("RequestId");

        dbRequestId.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    RequestIdModel data = dataSnapshot.getValue(RequestIdModel.class);
                    requestSentUid = data.getUid().toString();
                    Toast.makeText(ViewSentRequestActivity.this, "Request send to  " + requestSentUid, Toast.LENGTH_SHORT).show();

                    dbViewRequest = FirebaseDatabase.getInstance().getReference().child("Request").child(requestSentUid);
                    dbViewRequest.addValueEventListener(new ValueEventListener() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            requestModelList.clear();
                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                RequestModel user = dataSnapshot.getValue(RequestModel.class);

                                requestModelList.add(user);

                            }
                            adapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setListener() {
        imageBack.setOnClickListener(this);
        viewYourRequestTextView.setOnClickListener(this);
    }

    private void initialization() {
        imageBack = findViewById(R.id.imageBack);
        recyclerView = findViewById(R.id.recyclerView);
        viewYourRequestTextView = findViewById(R.id.viewYourRequestTextView);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageBack:
                onBackPressed();
                break;

            case R.id.viewYourRequestTextView:
                startActivity(new Intent(getApplicationContext(), BloodRequestActivity.class));
                break;
        }
    }
}