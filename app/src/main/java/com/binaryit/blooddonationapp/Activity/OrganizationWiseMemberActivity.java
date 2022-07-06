package com.binaryit.blooddonationapp.Activity;

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

import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.binaryit.blooddonationapp.Adapter.OrganizationWiseAdapter;
import com.binaryit.blooddonationapp.Model.User;
import com.binaryit.blooddonationapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrganizationWiseMemberActivity extends AppCompatActivity {

    TextView titleTextView, noOrganizationFoundTextView;
    ProgressBar progressBar;
    String title;

    AppCompatImageView imageBack;

    RecyclerView recyclerView;
    List<User> userList;
    OrganizationWiseAdapter adapter;

    DatabaseReference dbUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization_wise_member);

        initialization();

        title = getIntent().getStringExtra("title");
        titleTextView.setText(title);

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userList = new ArrayList<>();
        adapter = new OrganizationWiseAdapter(this, userList);
        recyclerView.setAdapter(adapter);

        progressBar.setVisibility(View.VISIBLE);

        Query query = dbUser.orderByChild("name");

        query.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    if (user.getOrganization().equals(title)){
                        userList.add(user);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
                adapter.notifyDataSetChanged();

                if (userList.isEmpty()){
                    progressBar.setVisibility(View.INVISIBLE);
                    noOrganizationFoundTextView.setVisibility(View.VISIBLE);
                    noOrganizationFoundTextView.setText("No Member found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void initialization() {

        imageBack = findViewById(R.id.imageBack);

        dbUser = FirebaseDatabase.getInstance().getReference().child("User");

        titleTextView = findViewById(R.id.titleTextView);
        recyclerView = findViewById(R.id.recyclerView);
        noOrganizationFoundTextView = findViewById(R.id.noOrganizationFoundTextView);
        progressBar = findViewById(R.id.progressBar);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateSwipeRight(OrganizationWiseMemberActivity.this);
    }
}