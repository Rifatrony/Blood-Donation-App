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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.blooddonationapp.Adapter.RequestAdapter;
import com.example.blooddonationapp.Adapter.UserAdapter;
import com.example.blooddonationapp.MainActivity;
import com.example.blooddonationapp.Model.RequestModel;
import com.example.blooddonationapp.Model.User;
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

public class BloodRequestActivity extends AppCompatActivity implements View.OnClickListener {

    TextView viewSentRequestTextView, noRequestFoundTextView;
    ProgressBar progressBar;

    AppCompatImageView imageBack;
    FirebaseAuth mAuth;
    RecyclerView recyclerView;
    List<RequestModel> userList;
    RequestAdapter adapter;

    DatabaseReference userRef;
    FirebaseUser firebaseUser;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_request);

        initialization();
        setListener();

        mAuth = FirebaseAuth.getInstance();
        firebaseUser = mAuth.getCurrentUser();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(layoutManager);

        userList = new ArrayList<>();
        adapter = new RequestAdapter(this, userList);
        recyclerView.setAdapter(adapter);

        userRef = FirebaseDatabase.getInstance().getReference().child("Request").child(firebaseUser.getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                try {
                    readUser();
                    progressBar.setVisibility(View.VISIBLE);

                }catch (Exception e){
                    System.out.println(e.getMessage());
                    //progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"ref"+ e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void readUser() {

        //final FirebaseUser firebaseUser = mAuth.getCurrentUser();
        //progressBar.setVisibility(View.VISIBLE);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Request")
                .child(firebaseUser.getUid());
        //Query query = reference.orderByChild("type").equalTo("donor");

        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressBar.setVisibility(View.GONE);
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    RequestModel user = dataSnapshot.getValue(RequestModel.class);
                    if (user.getUid().equals(firebaseUser.getUid())){
                        if (!user.getStatus().equals("ok")){
                            System.out.println("Get uid is : " + firebaseUser.getUid());
                            userList.add(user);
                        }
                    }
                }
                adapter.notifyDataSetChanged();

                if (userList.isEmpty()){
                    progressBar.setVisibility(View.INVISIBLE);
                    noRequestFoundTextView.setVisibility(View.VISIBLE);
                    noRequestFoundTextView.setText("No Request Available");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setListener() {
        imageBack.setOnClickListener(this);
        viewSentRequestTextView.setOnClickListener(this);
    }

    private void initialization() {

        progressBar = findViewById(R.id.progressBar);
        noRequestFoundTextView = findViewById(R.id.noRequestFoundTextView);

        imageBack = findViewById(R.id.imageBack);
        recyclerView = findViewById(R.id.recyclerView);
        viewSentRequestTextView = findViewById(R.id.viewSentRequestTextView);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.imageBack:
                onBackPressed();
                break;

            case R.id.viewSentRequestTextView:
                startActivity(new Intent(getApplicationContext(), ViewRequestActivity.class));
                break;
        }
    }
}