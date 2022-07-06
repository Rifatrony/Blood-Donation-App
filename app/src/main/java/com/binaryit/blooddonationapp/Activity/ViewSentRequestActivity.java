package com.binaryit.blooddonationapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.binaryit.blooddonationapp.Adapter.ViewSentRequestAdapter;
import com.binaryit.blooddonationapp.Model.RequestIdModel;
import com.binaryit.blooddonationapp.Model.RequestModel;
import com.binaryit.blooddonationapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewSentRequestActivity extends AppCompatActivity implements View.OnClickListener{

    TextView viewYourRequestTextView;
    ProgressBar progressBar;
    TextView noRequestFoundTextView;

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
                requestModelList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()){

                    RequestIdModel data = dataSnapshot.getValue(RequestIdModel.class);
                    requestSentUid = data.getUid().toString();

                    dbViewRequest = FirebaseDatabase.getInstance().getReference()
                            .child("Request").child(requestSentUid);
                    dbViewRequest.addValueEventListener(new ValueEventListener() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                                RequestModel user = dataSnapshot.getValue(RequestModel.class);
                                if (!user.getUid().equals(mAuth.getUid())){
                                    requestModelList.add(user);
                                }

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
                startActivity(new Intent(getApplicationContext(), MyRequestActivity.class));
                Animatoo.animateSwipeRight(ViewSentRequestActivity.this);
                finish();
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSwipeRight(ViewSentRequestActivity.this);
    }
}