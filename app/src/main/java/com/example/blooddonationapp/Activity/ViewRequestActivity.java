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

public class ViewRequestActivity extends AppCompatActivity implements View.OnClickListener{

    TextView viewYourRequestTextView;

    AppCompatImageView imageBack;
    FirebaseAuth mAuth;
    RecyclerView recyclerView;
    List<RequestModel> userList;
    RequestAdapter adapter;

    DatabaseReference userRef;
    FirebaseUser firebaseUser;

    RequestModel requestModel;
    String test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_request);

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

        //test = requestModel.getRequest_uid().toString();
        System.out.println("Test is " + test);

        userRef = FirebaseDatabase.getInstance().getReference().child("Request").child(firebaseUser.getUid());

        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                try {
                    readUser();

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
        final FirebaseUser firebaseUser = mAuth.getCurrentUser();
        //progressBar.setVisibility(View.VISIBLE);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Request")
                .child(firebaseUser.getUid());
        //Query query = reference.orderByChild("type").equalTo("donor");

        reference.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    RequestModel user = dataSnapshot.getValue(RequestModel.class);
                    if (user.getUid().equals(firebaseUser.getUid())){

                        System.out.println("Get uid is : " + firebaseUser.getUid());
                        //progressBar.setVisibility(View.INVISIBLE);
                        userList.add(user);
                    }

                }
                adapter.notifyDataSetChanged();

                if (userList.isEmpty()){
                    //progressBar.setVisibility(View.INVISIBLE);
                    //noDataFoundTextView.setVisibility(View.VISIBLE);
                    Toast.makeText(ViewRequestActivity.this, "No Request Found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setListener() {
        imageBack.setOnClickListener(this);
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
                startActivity(new Intent(getApplicationContext(), ViewRequestActivity.class));
                break;
        }
    }
}